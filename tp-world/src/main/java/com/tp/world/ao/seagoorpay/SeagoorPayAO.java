package com.tp.world.ao.seagoorpay;

import com.tp.common.vo.PageInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.seagoorpay.SeagoorPayRefundStatus;
import com.tp.dto.seagoorpay.SeagoorPayStatus;
import com.tp.m.base.MResultVO;
import com.tp.m.enums.MResultInfo;
import com.tp.m.query.seagoorpay.*;
import com.tp.m.to.cache.TokenCacheTO;
import com.tp.m.util.DateUtil;
import com.tp.m.vo.seagoorpay.*;
import com.tp.model.pay.MerchantInfo;
import com.tp.model.pay.SeagoorPayInfo;
import com.tp.model.pay.SeagoorPayRefundInfo;
import com.tp.proxy.pay.MerchantInfoProxy;
import com.tp.proxy.pay.SeagoorPayInfoProxy;
import com.tp.proxy.pay.SeagoorPayRefundInfoProxy;
import com.tp.redis.util.JedisCacheUtil;
import com.tp.util.BarcodeUtil;
import com.tp.util.ErWeiMaUtil;
import com.tp.util.JsonUtil;
import com.tp.world.helper.SeagoorPayHelper;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import sun.misc.BASE64Encoder;

import java.io.ByteArrayOutputStream;
import java.util.*;

/**
 * Created by ldr on 2016/11/19.
 */
@Service
public class SeagoorPayAO {

    public static final String SEAGOOR_PAY_STATUS_KEY = "seagoor_pay_status_key";
    @Value("#{meta['xg.logo.image.path']}")
    public String logoImagePath;

    @Autowired
    private JedisCacheUtil util;

    @Autowired
    private SeagoorPayInfoProxy seagoorPayInfoProxy;

    @Autowired
    private SeagoorPayRefundInfoProxy seagoorPayRefundInfoProxy;

    @Autowired
    private MerchantInfoProxy merchantInfoProxy;

    private Logger logger = LoggerFactory.getLogger(SeagoorPayAO.class);


    /***
     * 生成支付碼
     * @param
     * @return
     */
    public MResultVO<SeagoorPayCodeVO> code(QuerySeagoorPayCode query, TokenCacheTO user) {
        if (user.getUid() == null) return new MResultVO<>(MResultInfo.ACCOUNT_TIMEOUT);
        if (StringUtils.isNoneBlank(query.getPrecode())) {
            // util.deleteCacheKey(query.getPrecode());
        }

        String code = getCode(user.getUid());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ErWeiMaUtil.encoderQRCode(code, outputStream, "png", 3, 22);
        //ErWeiMaUtil.encoderQRCodeWidthLogo(code, outputStream, "png", 3, 22,"/shareImg/logo.png");

        BASE64Encoder encoder = new BASE64Encoder();
        String qrCode = encoder.encode(outputStream.toByteArray());

        ByteArrayOutputStream barcodeStream = BarcodeUtil.genBarcode(code);
        String barcode = encoder.encode(barcodeStream.toByteArray());

        SeagoorPayCodeVO vo = new SeagoorPayCodeVO();
        vo.setCode(code);
        vo.setQrcode(qrCode);
        vo.setBarcode(barcode);

        return new MResultVO<>(MResultInfo.SUCCESS, vo);
    }

    /**
     * 支付
     *
     * @param order
     * @return
     */
    public MResultVO<SeagoorPayResultVO> pay(SeagoorPayOrderVO order) {

        if (order == null) {
            logger.error("SEAGOOR_PAY_ERROR;PARAM_IS_NULL" + JsonUtil.convertObjToStr(order));
            return new MResultVO<>(MResultInfo.SEAGOOR_PAY_PARAM_ERROR);
        }

        String payCode = order.getPay_code();
        Long memberId = (Long) util.getCache(payCode);
        if (memberId == null) {
            logger.error("SEAGOOR_PAY_ERROR;SEAGOOR_PAY_PAY_CODE_EXPIRED" + JsonUtil.convertObjToStr(order));
            return new MResultVO<>(MResultInfo.SEAGOOR_PAY_PAY_CODE_EXPIRED);
        }


        ResultInfo<MerchantInfo> merchantInfoResult = getMerchantInfo(order.getMerchant_id());
        if (!merchantInfoResult.isSuccess() || merchantInfoResult.getData() == null) {
            logger.error("SEAGOOR_PAY_ERROR;SEAGOOR_PAY_MERCHANT_NOT_EXIT" + JsonUtil.convertObjToStr(order));
            return new MResultVO<>(MResultInfo.SEAGOOR_PAY_MERCHANT_NOT_EXIST);
        }

        String sign = SeagoorPayHelper.sign(order, merchantInfoResult.getData().getMerchantKey());
        if (!StringUtils.equals(sign, order.getSign())) {
            logger.error("SEAGOOR_PAY_ERROR;SEAGOOR_PAY_SIGN_ERROR" + JsonUtil.convertObjToStr(order));
            return new MResultVO<>(MResultInfo.SEAGOOR_PAY_SIGN_ERROR);
        }
        SeagoorPayInfo seagoorPayInfoQuery = new SeagoorPayInfo();
        seagoorPayInfoQuery.setMerchantId(order.getMerchant_id());
        seagoorPayInfoQuery.setMerTradeCode(order.getMer_trade_code());
        ResultInfo<List<SeagoorPayInfo>> listResultInfo = seagoorPayInfoProxy.queryByObject(seagoorPayInfoQuery);
        if (listResultInfo.isSuccess() && !CollectionUtils.isEmpty(listResultInfo.getData())) {
            logger.error("SEAGOOR_PAY_ERROR;SEAGOOR_PAY_ORDER_CODE_USED" + JsonUtil.convertObjToStr(order));
            return new MResultVO<>(MResultInfo.SEAGOOR_PAY_ORDER_CODE_USED);
        }

        SeagoorPayInfo info = getSeagoorPayInfo(order, memberId);
        ResultInfo<SeagoorPayInfo> result = seagoorPayInfoProxy.pay(info);
        if (result.isSuccess()) {
            SeagoorPayInfo seagoorPayInfo = result.getData();
            util.deleteCacheKey(payCode);
            SeagoorPayResultVO resultVO = getSeagoorPayResultVO(merchantInfoResult, seagoorPayInfo);
            updatePayStatus(payCode, SeagoorPayStatus.SUCCESS.getCode(), seagoorPayInfo.getMemberId(), SeagoorPayStatus.SUCCESS.getDesc(), seagoorPayInfo.getTotalFee(), seagoorPayInfo.getItemDesc(), seagoorPayInfo.getPaymentCode(),
                    DateUtil.formatDate(DateUtil.DATETIME_FORMAT, seagoorPayInfo.getCreateTime()));
            return new MResultVO<>(MResultInfo.SUCCESS, resultVO);
        } else {
            updatePayStatus(payCode, SeagoorPayStatus.PAY_ERROR.getCode(), info.getMemberId(), result.getMsg().getMessage(), info.getTotalFee(), info.getItemDesc(), info.getPaymentCode(),
                    DateUtil.formatDate(DateUtil.DATETIME_FORMAT, new Date()));
            return new MResultVO<>(result.getMsg().getCode().toString(), result.getMsg().getMessage());
        }

    }

    /**
     * 查询支付状态
     * @param query
     * @param user
     * @return
     */
    public MResultVO<SeagoorPayStatusVO> queryPayStatus(QuerySeagoorPayCode query, TokenCacheTO user) {
        SeagoorPayStatusVO vo = (SeagoorPayStatusVO) util.getCache(getKey(user.getUid(),query.getCode()));
        logger.info("SEAGOOR_PAY_CHECK_STATUS_RESULT="+JsonUtil.convertObjToStr(vo));
        if (vo == null || !vo.getMemberid().equals(user.getUid())) {
            //logger.error("SEAGOOR_PAY_QUERY_PAY_STATUS_ERROR,STATUS_IS_NULL_OR_NOT_TARGET_USER.code={},vo={}", query.getCode(), JsonUtil.convertObjToStr(vo));
            vo = new SeagoorPayStatusVO();
            vo.setStatus("-1");
            return new MResultVO<>(MResultInfo.SUCCESS, vo);
        }
        vo.setMemberid(null);
        if(vo.getStatus() != null &&!vo.getStatus().equals("1") && !vo.getStatus().equals("-1")){
            logger.info("SEAGOOR_PAY_QUERY_PAY_STATUS_STATUS_CHANGED_DEL_CACHE"+JsonUtil.convertObjToStr(vo));
            util.deleteCacheKey(getKey(user.getUid(),query.getCode()));
        }
        return new MResultVO<>(MResultInfo.SUCCESS, vo);
    }

    private String getKey(Long memberId, String code) {
        return SEAGOOR_PAY_STATUS_KEY + memberId;
    }

    /**
     * 支付查询
     *
     * @param query
     * @return
     */
    public MResultVO<SeagoorPayResultVO> queryPay(SeagoorPayQueryVO query) {
        ResultInfo<MerchantInfo> merchantInfoResult = getMerchantInfo(query.getMerchant_id());
        if (!merchantInfoResult.isSuccess() || merchantInfoResult.getData() == null) {
            logger.error("SEAGOOR_PAY_QUERY_ERROR;SEAGOOR_PAY_MERCHANT_NOT_EXIT" + JsonUtil.convertObjToStr(query));
            return new MResultVO<>(MResultInfo.SEAGOOR_PAY_MERCHANT_NOT_EXIST);
        }

        String sign = SeagoorPayHelper.sign(query, merchantInfoResult.getData().getMerchantKey());
        if (!StringUtils.equals(sign, query.getSign())) {
            logger.error("SEAGOOR_PAY_QUERY_ERROR;SEAGOOR_PAY_SIGN_ERROR" + JsonUtil.convertObjToStr(query));
            return new MResultVO<>(MResultInfo.SEAGOOR_PAY_SIGN_ERROR);
        }
        SeagoorPayInfo seagoorPayInfoQuery = new SeagoorPayInfo();
        seagoorPayInfoQuery.setMerchantId(query.getMerchant_id());
        seagoorPayInfoQuery.setPaymentCode(StringUtils.isBlank(query.getPayment_code()) ? null : query.getPayment_code());
        seagoorPayInfoQuery.setMerTradeCode(StringUtils.isBlank(query.getMer_trade_code()) ? null : query.getMer_trade_code());
        ResultInfo<List<SeagoorPayInfo>> listResultInfo = seagoorPayInfoProxy.queryByObject(seagoorPayInfoQuery);
        if (!listResultInfo.isSuccess()) {
            logger.error("SEAGOOR_PAY_QUERY_ERROR;SEAGOOR_PAY_QUERY_ORDER_FIELD" + JsonUtil.convertObjToStr(query));
            return new MResultVO<>(MResultInfo.SEAGOOR_PAY_QUERY_ORDER_FIELD);
        }
        if (CollectionUtils.isEmpty(listResultInfo.getData())) {
            return new MResultVO<>(MResultInfo.SEAGOOR_PAY_ORDER_NOT_EXIST);
        }
        SeagoorPayResultVO resultVO = getSeagoorPayResultVO(merchantInfoResult, listResultInfo.getData().get(0));
        return new MResultVO<>(MResultInfo.SUCCESS, resultVO);
    }


    /**
     * 退款
     *
     * @param refundVO
     * @return
     */
    public MResultVO<SeagoorPayRefundResultVO> refund(SeagoorPayRefundVO refundVO) {
        if (refundVO == null) {
            logger.error("SEAGOOR_PAY_REFUND_ERROR;PARAM_IS_NULL" + JsonUtil.convertObjToStr(refundVO));
            return new MResultVO<>(MResultInfo.SEAGOOR_PAY_PARAM_ERROR);
        }
        ResultInfo<MerchantInfo> merchantInfoResultInfo = getMerchantInfo(refundVO.getMerchant_id());
        if (!merchantInfoResultInfo.isSuccess() || merchantInfoResultInfo.getData() == null) {
            logger.error("SEAGOOR_PAY_REFUND_ERROR;SEAGOOR_PAY_MERCHANT_NOT_EXIST" + JsonUtil.convertObjToStr(refundVO));
            return new MResultVO<>(MResultInfo.SEAGOOR_PAY_MERCHANT_NOT_EXIST);
        }

        String sign = SeagoorPayHelper.sign(refundVO, merchantInfoResultInfo.getData().getMerchantKey());
        if (!StringUtils.equals(sign, refundVO.getSign())) {
            logger.error("SEAGOOR_PAY_REFUND_ERROR;SEAGOOR_PAY_SIGN_ERROR" + JsonUtil.convertObjToStr(refundVO));
            return new MResultVO<>(MResultInfo.SEAGOOR_PAY_SIGN_ERROR);
        }
        SeagoorPayInfo payInfo = new SeagoorPayInfo();
        payInfo.setPaymentCode(StringUtils.isEmpty(refundVO.getPayment_code()) ? null : refundVO.getPayment_code());
        payInfo.setMerTradeCode(StringUtils.isEmpty(refundVO.getMer_trade_code()) ? null : refundVO.getMer_trade_code());
        payInfo.setMerchantId(refundVO.getMerchant_id());
        ResultInfo<SeagoorPayInfo> seagoorPayInfoResultInfo = seagoorPayInfoProxy.queryUniqueByObject(payInfo);
        if (!seagoorPayInfoResultInfo.isSuccess() || seagoorPayInfoResultInfo.getData() == null) {
            logger.error("SEAGOOR_PAY_REFUND_ERROR;SEAGOOR_PAY_ORDER_NOT_EXIST" + JsonUtil.convertObjToStr(refundVO));
            return new MResultVO<>(MResultInfo.SEAGOOR_PAY_ORDER_NOT_EXIST);
        }
        if( refundVO.getRefund_fee()>seagoorPayInfoResultInfo.getData().getTotalFee()){
            logger.error("SEAGOOR_PAY_REFUND:SEAGOOR_PAY_REFUND_NOT_ENOUGH.PARAM={}", JsonUtil.convertObjToStr(refundVO));
            return new MResultVO<>(MResultInfo.SEAGOOR_PAY_REFUND_NOT_ENOUGH);
        }

        SeagoorPayRefundInfo refundInfoQuery_1 = new SeagoorPayRefundInfo();
        refundInfoQuery_1.setMerRefundCode(refundVO.getMer_refund_code());
        refundInfoQuery_1.setMerchantId(refundVO.getMerchant_id());
        ResultInfo<List<SeagoorPayRefundInfo>> existRefundInfo = seagoorPayRefundInfoProxy.queryByObject(refundInfoQuery_1);
        if (existRefundInfo.isSuccess() && !CollectionUtils.isEmpty(existRefundInfo.getData())) {
            logger.error("SEAGOOR_PAY_REFUND_ERROR;SEAGOOR_PAY_REFUND_ORDER_CODE_USED" + JsonUtil.convertObjToStr(refundVO));
            return new MResultVO<>(MResultInfo.SEAGOOR_PAY_REFUND_ORDER_CODE_USED);
        }

        SeagoorPayInfo seagoorPayInfo = seagoorPayInfoResultInfo.getData();
        if (!seagoorPayInfo.getStatus().equals(SeagoorPayStatus.SUCCESS.getCode())) {
            logger.error("SEAGOOR_PAY_REFUND_ERROR;SEAGOOR_PAY_ORDER_NOT_PAID" + JsonUtil.convertObjToStr(refundVO));
            return new MResultVO<>(MResultInfo.SEAGOOR_PAY_ORDER_NOT_PAID);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        if (seagoorPayInfo.getCreateTime().before(calendar.getTime())) {
            logger.error("SEAGOOR_PAY_REFUND_ERROR;SEAGOOR_PAY_REFUND_ORDER_OVER_TIME" + JsonUtil.convertObjToStr(refundVO));
            return new MResultVO<>(MResultInfo.SEAGOOR_PAY_REFUND_ORDER_OVER_TIME);

        }
        
        SeagoorPayRefundInfo refundInfoQuery = new SeagoorPayRefundInfo();
        refundInfoQuery.setPaymentCode(seagoorPayInfo.getPaymentCode());
        refundInfoQuery.setStatus(SeagoorPayRefundStatus.SUCCESS.getCode());

        ResultInfo<List<SeagoorPayRefundInfo>> listResultInfo = seagoorPayRefundInfoProxy.queryByObject(refundInfoQuery);
        if (!listResultInfo.isSuccess()) {
            logger.error("SEAGOOR_PAY_REFUND:QUERY_EXIST_REFUND_LIST_ERROR.PARAM={},RESULT={}", JsonUtil.convertObjToStr(refundInfoQuery), JsonUtil.convertObjToStr(listResultInfo));
            return new MResultVO<>(MResultInfo.SEAGOOR_PAY_REFUND_CHECK_POINT_ERROR);
        }
        if (!CollectionUtils.isEmpty(listResultInfo.getData())) {
            int refundedFee = 0;
            for (SeagoorPayRefundInfo info : listResultInfo.getData()) {
                refundedFee += info.getRefundFee();
            }
            if ((refundedFee + refundVO.getRefund_fee()) > seagoorPayInfo.getTotalFee()) {
                logger.error("SEAGOOR_PAY_REFUND:SEAGOOR_PAY_REFUND_NOT_ENOUGH.PARAM={}", JsonUtil.convertObjToStr(refundVO));
                return new MResultVO<>(MResultInfo.SEAGOOR_PAY_REFUND_NOT_ENOUGH);
            }
        }

        SeagoorPayRefundInfo refundInfo = getSeagoorPayRefundInfo(refundVO, seagoorPayInfo);

        ResultInfo<SeagoorPayRefundInfo> refundInfoResultInfo = seagoorPayRefundInfoProxy.refund(refundInfo);
        if (refundInfoResultInfo.isSuccess()) {
            logger.info("SEAGOOR_PAY_REFUND_SUCCESS,RESULT={},PARAM={}", JsonUtil.convertObjToStr(refundInfoResultInfo), JsonUtil.convertObjToStr(refundInfo));
            SeagoorPayRefundResultVO vo = getSeagoorPayRefundResultVO(merchantInfoResultInfo.getData(), refundInfoResultInfo.getData());
            return new MResultVO<>(MResultInfo.SUCCESS, vo);
        } else {
            if (refundInfoResultInfo.getMsg() != null) {
                if (refundInfoResultInfo.getMsg().getCode() != null) {
                    if (refundInfoResultInfo.getMsg().getCode().toString().equals(MResultInfo.SEAGOOR_PAY_REFUND_NOT_ENOUGH.code)) {
                        return new MResultVO<>(MResultInfo.SEAGOOR_PAY_REFUND_NOT_ENOUGH);
                    } else {
                        return new MResultVO<>(MResultInfo.SEAGOOR_PAY_REFUND_CHECK_POINT_ERROR);
                    }
                }
            }
            return new MResultVO<>(MResultInfo.SEAGOOR_PAY_REFUND_CHECK_POINT_ERROR);
        }
    }


    /**
     * 查询退款
     *
     * @param refundVO
     * @return
     */
    public MResultVO<SeagoorPayRefundQueryResultVO> queryRefund(SeagoorPayRefundQueryVO refundVO) {

        ResultInfo<MerchantInfo> merchantInfoResultInfo = getMerchantInfo(refundVO.getMerchant_id());
        if (!merchantInfoResultInfo.isSuccess() || merchantInfoResultInfo.getData() == null) {
            logger.error("SEAGOOR_PAY_REFUND_QUERY_ERROR;SEAGOOR_PAY_MERCHANT_NOT_EXIST" + JsonUtil.convertObjToStr(refundVO));
            return new MResultVO<>(MResultInfo.SEAGOOR_PAY_MERCHANT_NOT_EXIST);
        }

        String sign = SeagoorPayHelper.sign(refundVO, merchantInfoResultInfo.getData().getMerchantKey());
        if (!StringUtils.equals(sign, refundVO.getSign())) {
            logger.error("SEAGOOR_PAY_REFUND_QUERY_ERROR;SEAGOOR_PAY_SIGN_ERROR" + JsonUtil.convertObjToStr(refundVO));
            return new MResultVO<>(MResultInfo.SEAGOOR_PAY_SIGN_ERROR);
        }

        SeagoorPayRefundInfo refundInfoQuery_1 = new SeagoorPayRefundInfo();
        refundInfoQuery_1.setMerRefundCode(StringUtils.isBlank(refundVO.getMer_refund_code()) ? null : refundVO.getMer_refund_code());
        refundInfoQuery_1.setRefundCode(StringUtils.isBlank(refundVO.getRefund_code()) ? null : refundVO.getRefund_code());
        refundInfoQuery_1.setMerTradeCode(StringUtils.isBlank(refundVO.getMer_trade_code()) ? null : refundVO.getMer_trade_code());
        refundInfoQuery_1.setPaymentCode(StringUtils.isBlank(refundVO.getPayment_code()) ? null : refundVO.getPayment_code());
        refundInfoQuery_1.setMerchantId(refundVO.getMerchant_id());

        ResultInfo<List<SeagoorPayRefundInfo>> existRefundInfo = seagoorPayRefundInfoProxy.queryByObject(refundInfoQuery_1);
        if (!existRefundInfo.isSuccess()) {
            logger.error("SEAGOOR_PAY_REFUND_QUERY_ERROR;SEAGOOR_PAY_REFUND_QUERY_FAILED" + JsonUtil.convertObjToStr(refundVO) + JsonUtil.convertObjToStr(existRefundInfo));
            return new MResultVO<>(MResultInfo.SEAGOOR_PAY_REFUND_QUERY_FAILED);
        }
        if (CollectionUtils.isEmpty(existRefundInfo.getData())) {
            return new MResultVO<>(MResultInfo.SEAGOOR_PAY_ORDER_NOT_EXIST);
        }
        SeagoorPayRefundQueryResultVO refundResultVO = new SeagoorPayRefundQueryResultVO();
        refundResultVO.setMerchant_id(merchantInfoResultInfo.getData().getMerchantId());
        refundResultVO.setRand_str(SeagoorPayHelper.getRandStr());
        refundResultVO.setSign(SeagoorPayHelper.sign(refundResultVO, merchantInfoResultInfo.getData().getMerchantKey()));

        List<SeagoorPayRefundResultVO> vos = new ArrayList<>();
        for (SeagoorPayRefundInfo info : existRefundInfo.getData()) {
            SeagoorPayRefundResultVO vo = new SeagoorPayRefundResultVO();
            vo.setPayment_code(info.getPaymentCode());
            vo.setRefund_code(info.getRefundCode());
            vo.setMer_trade_code(info.getMerTradeCode());
            vo.setMer_refund_code(info.getMerRefundCode());
            vo.setTotal_fee(info.getTotalFee());
            vo.setRefund_fee(info.getRefundFee());
            vo.setStatus(info.getStatus());
            vo.setStatus_desc(SeagoorPayRefundStatus.getDescByCode(info.getStatus()));
            vo.setRefund_time(DateUtil.formatDate(DateUtil.DATETIME_FORMAT, info.getCreateTime()));
            vos.add(vo);
        }
        refundResultVO.setRefund_list(vos);
        return new MResultVO<>(MResultInfo.SUCCESS, refundResultVO);
    }


    /**
     * 查询支付信息分页
     *
     * @param queryListVO
     * @return
     */
    public MResultVO<PageForSeagoorPay<SeagoorPayResultVO>> queryPayList(SeagoorPayQueryListVO queryListVO) {

        ResultInfo<MerchantInfo> merchantInfoResultInfo = getMerchantInfo(queryListVO.getMerchant_id());
        if (!merchantInfoResultInfo.isSuccess() || merchantInfoResultInfo.getData() == null) {
            logger.error("SEAGOOR_PAY_LIST_QUERY_ERROR;SEAGOOR_PAY_MERCHANT_NOT_EXIST" + JsonUtil.convertObjToStr(queryListVO));
            return new MResultVO<>(MResultInfo.SEAGOOR_PAY_MERCHANT_NOT_EXIST);
        }

        String sign = SeagoorPayHelper.sign(queryListVO, merchantInfoResultInfo.getData().getMerchantKey());
        if (!StringUtils.equals(sign, queryListVO.getSign())) {
            logger.error("SEAGOOR_PAY_LIST_QUERY_ERROR;SEAGOOR_PAY_SIGN_ERROR" + JsonUtil.convertObjToStr(queryListVO));
            return new MResultVO<>(MResultInfo.SEAGOOR_PAY_SIGN_ERROR);
        }

        Map<String, Object> params = new HashMap<>();
        params.put("merchantId", queryListVO.getMerchant_id());
        params.put("merTradeCode", StringUtils.isBlank(queryListVO.getMer_trade_code()) ? null : queryListVO.getMer_trade_code());
        params.put("paymentCode", StringUtils.isBlank(queryListVO.getPayment_code()) ? null : queryListVO.getPayment_code());
        params.put("beginTime", DateUtil.getDate(queryListVO.getBegin_time(), DateUtil.DATETIME_FORMAT));
        params.put("endTime", DateUtil.getDate(queryListVO.getEnd_time(), DateUtil.DATETIME_FORMAT));
        if (queryListVO.getPage_size() == null || queryListVO.getPage_size() < 1) {
            queryListVO.setPage_size(200);
        }
        if (queryListVO.getCur_page() == null || queryListVO.getCur_page() < 1) {
            queryListVO.setCur_page(1);
        }
        params.put("start", (queryListVO.getCur_page() - 1) * queryListVO.getPage_size());
        params.put("size", queryListVO.getPage_size());
        params.put("status", queryListVO.getStatus() == null ? null : queryListVO.getStatus());
        System.out.println(params);
        ResultInfo<PageInfo<SeagoorPayInfo>> resultInfo = seagoorPayInfoProxy.queryByparamForPage(params);
        if (!resultInfo.isSuccess()) {
            logger.error("SEAGOOR_PAY_LIST_QUERY_ERROR;SEAGOOR_PAY_REFUND_QUERY_FAILED" + JsonUtil.convertObjToStr(queryListVO) + JsonUtil.convertObjToStr(resultInfo));
            return new MResultVO<>(MResultInfo.SEAGOOR_PAY_QUERY_ORDER_FIELD);
        }

        PageInfo<SeagoorPayInfo> pageInfo = resultInfo.getData();
        PageForSeagoorPay<SeagoorPayResultVO> page = new PageForSeagoorPay<>();
        page.setCur_page(queryListVO.getCur_page());
        page.setPage_size(queryListVO.getPage_size());
        page.setTotal_count(pageInfo.getRecords());
        page.setRand_str(SeagoorPayHelper.getRandStr());
        page.setMerchant_id(merchantInfoResultInfo.getData().getMerchantId());
        page.setSign(SeagoorPayHelper.sign(page, merchantInfoResultInfo.getData().getMerchantKey()));

        List<SeagoorPayResultVO> seagoorPayResultVOs = new ArrayList<>();
        for (SeagoorPayInfo seagoorPayInfo : pageInfo.getRows()) {
            SeagoorPayResultVO vo = new SeagoorPayResultVO();
            vo.setPayment_code(seagoorPayInfo.getPaymentCode());
            vo.setMer_trade_code(seagoorPayInfo.getMerTradeCode());
            vo.setTotal_fee(seagoorPayInfo.getTotalFee());
            vo.setStatus(seagoorPayInfo.getStatus());
            vo.setStatus_desc(SeagoorPayStatus.getDescByCode(seagoorPayInfo.getStatus()));
            vo.setPay_time(DateUtil.formatDate(DateUtil.DATETIME_FORMAT, seagoorPayInfo.getCreateTime()));
            vo.setAttach(seagoorPayInfo.getAttach());
            vo.setItem_desc(seagoorPayInfo.getItemDesc());
            vo.setItem_tag(seagoorPayInfo.getItemTag());
            vo.setDevice_info(seagoorPayInfo.getDeviceInfo());
            seagoorPayResultVOs.add(vo);
        }
        page.setList(seagoorPayResultVOs);
        return new MResultVO<>(MResultInfo.SUCCESS, page);
    }


    /**
     * 查询退款分页
     *
     * @param refundVO
     * @return
     */
    public MResultVO<PageForSeagoorPay<SeagoorPayRefundResultVO>> queryRefundList(SeagoorPayRefundQueryListVO refundVO) {

        ResultInfo<MerchantInfo> merchantInfoResultInfo = getMerchantInfo(refundVO.getMerchant_id());
        if (!merchantInfoResultInfo.isSuccess() || merchantInfoResultInfo.getData() == null) {
            logger.error("SEAGOOR_PAY_REFUND_LIST_QUERY_ERROR;SEAGOOR_PAY_MERCHANT_NOT_EXIST" + JsonUtil.convertObjToStr(refundVO));
            return new MResultVO<>(MResultInfo.SEAGOOR_PAY_MERCHANT_NOT_EXIST);
        }

        String sign = SeagoorPayHelper.sign(refundVO, merchantInfoResultInfo.getData().getMerchantKey());
        if (!StringUtils.equals(sign, refundVO.getSign())) {
            logger.error("SEAGOOR_PAY_REFUND_LIST_QUERY_ERROR;SEAGOOR_PAY_SIGN_ERROR" + JsonUtil.convertObjToStr(refundVO));
            return new MResultVO<>(MResultInfo.SEAGOOR_PAY_SIGN_ERROR);
        }


        Map<String, Object> params = new HashMap<>();
        params.put("merchantId", refundVO.getMerchant_id());
        params.put("refundCode", StringUtils.isBlank(refundVO.getRefund_code()) ? null : refundVO.getRefund_code());
        params.put("merRefundCode", StringUtils.isBlank(refundVO.getMer_refund_code()) ? null : refundVO.getMer_refund_code());
        params.put("merTradeCode", StringUtils.isBlank(refundVO.getMer_trade_code()) ? null : refundVO.getMer_trade_code());
        params.put("paymentCode", StringUtils.isBlank(refundVO.getPayment_code()) ? null : refundVO.getPayment_code());
        params.put("beginTime", DateUtil.getDate(refundVO.getBegin_time()));
        params.put("endTime", DateUtil.getDate(refundVO.getEnd_time()));
        if (refundVO.getPage_size() == null || refundVO.getPage_size() < 1) {
            refundVO.setPage_size(200);
        }
        if (refundVO.getCur_page() == null || refundVO.getCur_page() < 1) {
            refundVO.setCur_page(1);
        }
        params.put("start", (refundVO.getCur_page() - 1) * refundVO.getPage_size());
        params.put("size", refundVO.getPage_size());
        params.put("status", refundVO.getStatus() == null ? null : refundVO.getStatus());
        System.out.println(params);
        ResultInfo<PageInfo<SeagoorPayRefundInfo>> existRefundInfo = seagoorPayRefundInfoProxy.queryRefundList(params);
        if (!existRefundInfo.isSuccess()) {
            logger.error("SEAGOOR_PAY_REFUND_LIST_QUERY_ERROR;SEAGOOR_PAY_REFUND_QUERY_FAILED" + JsonUtil.convertObjToStr(refundVO) + JsonUtil.convertObjToStr(existRefundInfo));
            return new MResultVO<>(MResultInfo.SEAGOOR_PAY_REFUND_QUERY_FAILED);
        }

        PageInfo<SeagoorPayRefundInfo> pageInfo = existRefundInfo.getData();
        PageForSeagoorPay<SeagoorPayRefundResultVO> page = new PageForSeagoorPay<>();
        page.setCur_page(refundVO.getCur_page());
        page.setPage_size(refundVO.getPage_size());
        page.setTotal_count(pageInfo.getRecords());
        page.setRand_str(SeagoorPayHelper.getRandStr());
        page.setMerchant_id(merchantInfoResultInfo.getData().getMerchantId());
        page.setSign(SeagoorPayHelper.sign(page, merchantInfoResultInfo.getData().getMerchantKey()));

        List<SeagoorPayRefundResultVO> refundResultVOs = new ArrayList<>();
        for (SeagoorPayRefundInfo seagoorPayRefundInfo : pageInfo.getRows()) {
            SeagoorPayRefundResultVO vo = new SeagoorPayRefundResultVO();
            vo.setPayment_code(seagoorPayRefundInfo.getPaymentCode());
            vo.setMer_trade_code(seagoorPayRefundInfo.getMerTradeCode());
            vo.setRefund_code(seagoorPayRefundInfo.getRefundCode());
            vo.setMer_refund_code(seagoorPayRefundInfo.getMerRefundCode());
            vo.setRefund_fee(seagoorPayRefundInfo.getRefundFee());
            vo.setDevice_info(seagoorPayRefundInfo.getDeviceInfo());
            vo.setTotal_fee(seagoorPayRefundInfo.getTotalFee());
            vo.setStatus(seagoorPayRefundInfo.getStatus());
            vo.setStatus_desc(SeagoorPayRefundStatus.getDescByCode(seagoorPayRefundInfo.getStatus()));
            vo.setOperator_id(seagoorPayRefundInfo.getOperatorId());
            vo.setRefund_time(DateUtil.formatDate(DateUtil.DATETIME_FORMAT, seagoorPayRefundInfo.getCreateTime()));
            refundResultVOs.add(vo);
        }
        page.setList(refundResultVOs);
        return new MResultVO<>(MResultInfo.SUCCESS, page);
    }


    private SeagoorPayResultVO getSeagoorPayResultVO(ResultInfo<MerchantInfo> merchantInfoResult, SeagoorPayInfo payInfo) {
        SeagoorPayResultVO resultVO = new SeagoorPayResultVO();
        // resultVO.setMember_id(payInfo.getMemberId().toString());
        resultVO.setMer_trade_code(payInfo.getMerTradeCode());
        resultVO.setMerchant_id(payInfo.getMerchantId());
        resultVO.setPayment_code(payInfo.getPaymentCode());
        resultVO.setPay_time(DateUtil.formatDate(DateUtil.DATETIME_FORMAT, payInfo.getCreateTime()));
        resultVO.setTotal_fee(payInfo.getTotalFee());
        resultVO.setItem_desc(payInfo.getItemDesc());
        // resultVO.setItem_detail(payInfo.getItemDetail());
        resultVO.setAttach(payInfo.getAttach());
        resultVO.setStatus(payInfo.getStatus());
        resultVO.setDevice_info(payInfo.getDeviceInfo());
        resultVO.setRand_str(SeagoorPayHelper.getRandStr());
        resultVO.setSign(SeagoorPayHelper.sign(resultVO, merchantInfoResult.getData().getMerchantKey()));
        return resultVO;
    }

    private SeagoorPayInfo getSeagoorPayInfo(SeagoorPayOrderVO order, Long memberId) {
        SeagoorPayInfo info = new SeagoorPayInfo();
        info.setMemberId(memberId);
        info.setCreateUser(memberId.toString());
        info.setUpdateUser(memberId.toString());
        info.setMerchantId(order.getMerchant_id());
        info.setMerTradeCode(order.getMer_trade_code());
        info.setBizType(1);
        info.setDeviceInfo(order.getDevice_info());
        info.setPayCode(order.getPay_code());
        info.setAttach(order.getAttach());
        info.setItemDesc(order.getItem_desc());
        info.setItemDetail(order.getItem_detail());
        info.setItemTag(order.getItem_tag());
        info.setTotalFee(order.getTotal_fee());
        info.setIp(order.getIp());
        info.setRandStr(order.getRand_str());
        return info;
    }

    private ResultInfo<MerchantInfo> getMerchantInfo(String merchant_id) {
        MerchantInfo merchantInfoQuery = new MerchantInfo();
        merchantInfoQuery.setMerchantId(merchant_id);
        merchantInfoQuery.setStatus(0);
        return merchantInfoProxy.queryUniqueByObject(merchantInfoQuery);
    }


    private SeagoorPayRefundResultVO getSeagoorPayRefundResultVO(MerchantInfo merchantInfo, SeagoorPayRefundInfo seagoorPayRefundInfo) {

        SeagoorPayRefundResultVO vo = new SeagoorPayRefundResultVO();
        vo.setRand_str(SeagoorPayHelper.getRandStr());
        vo.setMerchant_id(merchantInfo.getMerchantId());
        vo.setMer_refund_code(seagoorPayRefundInfo.getMerRefundCode());
        vo.setMer_trade_code(seagoorPayRefundInfo.getMerTradeCode());
        vo.setPayment_code(seagoorPayRefundInfo.getPaymentCode());
        vo.setRefund_code(seagoorPayRefundInfo.getRefundCode());
        vo.setTotal_fee(seagoorPayRefundInfo.getTotalFee());
        vo.setRefund_fee(seagoorPayRefundInfo.getRefundFee());
        vo.setStatus(seagoorPayRefundInfo.getStatus());
        vo.setStatus_desc(SeagoorPayRefundStatus.getDescByCode(seagoorPayRefundInfo.getStatus()));
        vo.setSign(SeagoorPayHelper.sign(vo, merchantInfo.getMerchantKey()));
        return vo;
    }

    private SeagoorPayRefundInfo getSeagoorPayRefundInfo(SeagoorPayRefundVO refundVO, SeagoorPayInfo seagoorPayInfo) {
        SeagoorPayRefundInfo refundInfo = new SeagoorPayRefundInfo();
        refundInfo.setMerchantId(refundVO.getMerchant_id());
        refundInfo.setPaymentCode(seagoorPayInfo.getPaymentCode());
        refundInfo.setMerTradeCode(seagoorPayInfo.getMerTradeCode());
        refundInfo.setMerRefundCode(refundVO.getMer_refund_code());
        refundInfo.setIp(refundVO.getIp());
        refundInfo.setDeviceInfo(refundVO.getDevice_info());
        refundInfo.setOperatorId(refundVO.getOperator_id());

        refundInfo.setTotalFee(seagoorPayInfo.getTotalFee());
        refundInfo.setRefundFee(refundVO.getRefund_fee());
        refundInfo.setCreateUser(refundVO.getMerchant_id());
        refundInfo.setUpdateUser(refundVO.getMerchant_id());
        refundInfo.setMemberId(seagoorPayInfo.getMemberId());
        return refundInfo;
    }


    private String getCode(Long id) {
        String code = SeagoorPayHelper.genSeagoorPayCode(id);
        Object o = util.getCache(code);
        if (o != null) {
            return getCode(id);
        } else {
            util.setCache(code, id, 300);
            SeagoorPayStatusVO seagoorPayStatusVO = new SeagoorPayStatusVO();
            seagoorPayStatusVO.setCode(code);
            seagoorPayStatusVO.setMemberid(id);
            seagoorPayStatusVO.setStatus(String.valueOf(SeagoorPayStatus.NOT_PAY.getCode()));
            seagoorPayStatusVO.setMessage(SeagoorPayStatus.NOT_PAY.getDesc());
            //util.setCache(getKey(id,code), seagoorPayStatusVO, 300);
            return code;
        }
    }

    private void updatePayStatus(String code, int status, Long memberId, String message, Integer consume, String itemDesc, String ordercode, String time) {
        SeagoorPayStatusVO vo = (SeagoorPayStatusVO) util.getCache(getKey(memberId,code));
        if (vo == null) vo = new SeagoorPayStatusVO();
        vo.setStatus(String.valueOf(status));
        vo.setMessage(message);
        vo.setMemberid(memberId);
        vo.setConsume(String.valueOf(consume));
        vo.setItemdesc(itemDesc);
        vo.setOrdercode(ordercode);
        vo.setTime(time);
        logger.info("SEAGOOR_UPDATE_PAY_STATUS_"+JsonUtil.convertObjToStr(vo));
        util.setCache(getKey(memberId,code), vo, 300);

    }

}
