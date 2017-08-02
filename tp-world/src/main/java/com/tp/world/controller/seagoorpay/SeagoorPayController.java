package com.tp.world.controller.seagoorpay;

import com.tp.dto.app.enums.SwitchEnum;
import com.tp.m.base.MResultVO;
import com.tp.m.enums.MResultInfo;
import com.tp.m.exception.MobileException;
import com.tp.m.query.seagoorpay.*;
import com.tp.m.to.cache.TokenCacheTO;
import com.tp.m.util.JsonUtil;
import com.tp.m.vo.seagoorpay.*;
import com.tp.m.vo.swt.SwitchVO;
import com.tp.world.ao.seagoorpay.SeagoorPayAO;
import com.tp.world.ao.swt.SwitchAO;
import com.tp.world.helper.AuthHelper;
import com.tp.world.helper.RequestHelper;
import com.tp.world.helper.SeagoorPayHelper;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * Created by ldr on 2016/11/19.
 */

/**
 * 西客币支付相关
 */
@Controller
@RequestMapping("/seagoorpay")
public class SeagoorPayController {

    @Autowired
    private AuthHelper authHelper;

    @Autowired
    private SeagoorPayAO seagoorPayAO;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SwitchAO switchAO;


    /**
     * 获取支付码
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/code", method = RequestMethod.POST)
    public String getPayCode(HttpServletRequest request) {
        try {
            checkSwitch();
            String jsonStr = RequestHelper.getJsonStrByIO(request);
            QuerySeagoorPayCode query = (QuerySeagoorPayCode) JsonUtil.getObjectByJsonStr(jsonStr, QuerySeagoorPayCode.class);
            if (query == null) return JsonUtil.convertObjToStr(new MResultVO<>(MResultInfo.PARAM_ERROR));
            TokenCacheTO usr = authHelper.authToken(query.getToken());

            MResultVO<SeagoorPayCodeVO> result = seagoorPayAO.code(query, usr);
            return JsonUtil.convertObjToStr(result);
        } catch (Exception e) {
            logger.error("SEAGOOR_PAY_GET_CODE_ERROR", e);
            return JsonUtil.convertObjToStr(new MResultVO<>(MResultInfo.SYSTEM_ERROR));
        }
    }

    /**
     * 获取支付码
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/querypaystatus", method = RequestMethod.POST)
    public String queryPayStatus(HttpServletRequest request) {
        String jsonStr = RequestHelper.getJsonStrByIO(request);
        QuerySeagoorPayCode query = (QuerySeagoorPayCode) JsonUtil.getObjectByJsonStr(jsonStr, QuerySeagoorPayCode.class);
        TokenCacheTO usr = authHelper.authToken(query.getToken());

        if (query == null || query.getCode() == null) {
            logger.error("SEAGOOR_PAY_QUERY_PAY_STATUS_ERROR.PARAM_ERROR.PARAM={}", JsonUtil.convertObjToStr(query));
            return JsonUtil.convertObjToStr(new MResultVO<>(MResultInfo.PARAM_ERROR));
        }
        MResultVO<SeagoorPayStatusVO> result = seagoorPayAO.queryPayStatus(query, usr);
        return JsonUtil.convertObjToStr(result);
    }

    /**
     * 支付
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/pay", method = RequestMethod.POST)
    public String pay(HttpServletRequest request) {
        try {
            checkSwitch();
            String jsonStr = RequestHelper.getJsonStrByIO(request);
            logger.info("RECEIVE_SEAGOOR_PAY_REQUEST,REQUEST_PARAM:" + jsonStr);
            SeagoorPayOrderVO order = (SeagoorPayOrderVO) JsonUtil.getObjectByJsonStr(jsonStr, SeagoorPayOrderVO.class);

            String checkResult = SeagoorPayHelper.checkParam(order);
            if (checkResult != null) {
                logger.error("RECEIVE_SEAGOOR_PAY_REQUEST,CHECK_PARAM_ERROR:" + checkResult);
                return JsonUtil.convertObjToStr(new MResultVO<>(MResultInfo.SEAGOOR_PAY_PARAM_ERROR.code, checkResult));
            }

            MResultVO<SeagoorPayResultVO> result = seagoorPayAO.pay(order);
            String resultStr = JsonUtil.convertObjToStr(result);
            return resultStr;
        } catch (Exception e) {
            logger.error("SEAGOOR_PAY_REQUEST_ERROR:", e);
            return JsonUtil.convertObjToStr(new MResultVO<>(MResultInfo.SEAGOOR_PAY_SYSTEM_ERROR));
        }
    }

    /**
     * 支付
     *
     * @param request
     * @return
     */
    @ResponseBody
   //@RequestMapping(value = "/mock", method = RequestMethod.GET)
    public String mock(HttpServletRequest request,String token,String paycode,String fee,String type) {
        try {
            checkSwitch();
            if("refund".equals(type)){
                SeagoorPayRefundVO order = new SeagoorPayRefundVO();
                order.setPayment_code(paycode);
                order.setIp("222");
                order.setMerchant_id("234234");
                order.setMer_refund_code("refund_"+String.valueOf(Math.random()));
                order.setDevice_info("2楼1号收银台");
                order.setRefund_fee(NumberUtils.isNumber(fee)? Integer.valueOf(fee): 1);
                order.setRand_str("r");
                order.setTotal_fee(1000);
                order.setOperator_id("ss");
                order.setSign(SeagoorPayHelper.sign(order,"sign"));
                String checkResult = SeagoorPayHelper.checkParam(order);
                if (checkResult != null) {
                    logger.error("RECEIVE_SEAGOOR_PAY_REQUEST,CHECK_PARAM_ERROR:" + checkResult);
                    return JsonUtil.convertObjToStr(new MResultVO<>(MResultInfo.SEAGOOR_PAY_PARAM_ERROR.code, checkResult));
                }
                MResultVO<SeagoorPayRefundResultVO>  refundResultVOMResultVO =   seagoorPayAO.refund(order);
                return  JsonUtil.convertObjToStr(refundResultVOMResultVO);
            }

            SeagoorPayOrderVO order = new SeagoorPayOrderVO();
            order.setMer_trade_code("pay_order"+String.valueOf(Math.random()));
            order.setIp("00");
            order.setMerchant_id("234234");
            order.setItem_tag("");
            order.setItem_desc("大杯奶茶");
            order.setItem_detail("asda的身份");
            order.setAttach("");
            order.setDevice_info("蜜果奶茶西客商城店");
            order.setPay_code(paycode);
            order.setRand_str("r");
            order.setTotal_fee(NumberUtils.isNumber(fee)? Integer.valueOf(fee) : 10);
            order.setSign(SeagoorPayHelper.sign(order,"sign"));
            String checkResult = SeagoorPayHelper.checkParam(order);
            if (checkResult != null) {
                logger.error("RECEIVE_SEAGOOR_PAY_REQUEST,CHECK_PARAM_ERROR:" + checkResult);
                return JsonUtil.convertObjToStr(new MResultVO<>(MResultInfo.SEAGOOR_PAY_PARAM_ERROR.code, checkResult));
            }
            MResultVO<SeagoorPayResultVO> result = seagoorPayAO.pay(order);
            String resultStr = JsonUtil.convertObjToStr(result);
            return resultStr;
        } catch (Exception e) {
            logger.error("SEAGOOR_PAY_REQUEST_ERROR:", e);
            return JsonUtil.convertObjToStr(new MResultVO<>(MResultInfo.SEAGOOR_PAY_SYSTEM_ERROR));
        }
    }


    /**
     * 退款
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/refund", method = RequestMethod.POST)
    public String refund(HttpServletRequest request) {
        try {
            checkSwitch();
            String jsonStr = RequestHelper.getJsonStrByIO(request);
            logger.info("RECEIVE_SEAGOOR_PAY_REFUND_REQUEST,REQUEST_PARAM:" + jsonStr);
            SeagoorPayRefundVO order = (SeagoorPayRefundVO) JsonUtil.getObjectByJsonStr(jsonStr, SeagoorPayRefundVO.class);

            String checkResult = SeagoorPayHelper.checkParam(order);
            if (checkResult != null) {
                logger.error("RECEIVE_SEAGOOR_PAY_REFUND_REQUEST,CHECK_PARAM_ERROR:" + checkResult);
                return JsonUtil.convertObjToStr(new MResultVO<>(MResultInfo.SEAGOOR_PAY_PARAM_ERROR.code, checkResult));
            }

            MResultVO<SeagoorPayRefundResultVO> result = seagoorPayAO.refund(order);
            String resultStr = JsonUtil.convertObjToStr(result);
            return resultStr;
        } catch (Exception e) {
            logger.error("SEAGOOR_PAY_REFUND_REQUEST_ERROR:", e);
            return JsonUtil.convertObjToStr(new MResultVO<>(MResultInfo.SEAGOOR_PAY_SYSTEM_ERROR));
        }
    }

    /**
     * 查询支付信息
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/querypay", method = RequestMethod.POST)
    public String queryPay(HttpServletRequest request) {
        try {
            checkSwitch();
            String jsonStr = RequestHelper.getJsonStrByIO(request);
            logger.info("RECEIVE_SEAGOOR_PAY_QUERY_REQUEST,REQUEST_PARAM:" + jsonStr);
            SeagoorPayQueryVO seagoorPayQueryVO = (SeagoorPayQueryVO) JsonUtil.getObjectByJsonStr(jsonStr, SeagoorPayQueryVO.class);

            String checkResult = SeagoorPayHelper.checkParam(seagoorPayQueryVO);
            if (checkResult != null) {
                logger.error("RECEIVE_SEAGOOR_PAY_QUERY_REQUEST,CHECK_PARAM_ERROR:" + checkResult);
                return JsonUtil.convertObjToStr(new MResultVO<>(MResultInfo.SEAGOOR_PAY_PARAM_ERROR.code, checkResult));
            }

            MResultVO<SeagoorPayResultVO> result = seagoorPayAO.queryPay(seagoorPayQueryVO);
            String resultStr = JsonUtil.convertObjToStr(result);
            return resultStr;
        } catch (Exception e) {
            logger.error("SEAGOOR_PAY_QUERY_REQUEST_ERROR:", e);
            return JsonUtil.convertObjToStr(new MResultVO<>(MResultInfo.SEAGOOR_PAY_SYSTEM_ERROR));
        }
    }

    /**
     * 查询退款信息
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryrefund", method = RequestMethod.POST)
    public String queryRefund(HttpServletRequest request) {
        try {
            checkSwitch();
            String jsonStr = RequestHelper.getJsonStrByIO(request);
            logger.info("RECEIVE_SEAGOOR_PAY_REFUND_QUERY_REQUEST,REQUEST_PARAM:" + jsonStr);
            SeagoorPayRefundQueryVO seagoorPayQueryVO = (SeagoorPayRefundQueryVO) JsonUtil.getObjectByJsonStr(jsonStr, SeagoorPayRefundQueryVO.class);

            String checkResult = SeagoorPayHelper.checkParam(seagoorPayQueryVO);
            if (checkResult != null) {
                logger.error("RECEIVE_SEAGOOR_PAY_REFUND_QUERY_REQUEST,CHECK_PARAM_ERROR:" + checkResult);
                return JsonUtil.convertObjToStr(new MResultVO<>(MResultInfo.SEAGOOR_PAY_PARAM_ERROR.code, checkResult));
            }

            MResultVO<SeagoorPayRefundQueryResultVO> result = seagoorPayAO.queryRefund(seagoorPayQueryVO);
            String resultStr = JsonUtil.convertObjToStr(result);
            return resultStr;
        } catch (Exception e) {
            logger.error("RECEIVE_SEAGOOR_PAY_REFUND_QUERY_REQUEST_ERROR:", e);
            return JsonUtil.convertObjToStr(new MResultVO<>(MResultInfo.SEAGOOR_PAY_SYSTEM_ERROR));
        }
    }


    /**
     * 分页查询退款记录
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryrefundlist", method = RequestMethod.POST)
    public String queryRefundList(HttpServletRequest request) {
        try {
            checkSwitch();
            String jsonStr = RequestHelper.getJsonStrByIO(request);
            logger.info("RECEIVE_SEAGOOR_PAY_REFUND_LIST_QUERY_REQUEST,REQUEST_PARAM:" + jsonStr);
            SeagoorPayRefundQueryListVO seagoorPayQueryVO = (SeagoorPayRefundQueryListVO) JsonUtil.getObjectByJsonStr(jsonStr, SeagoorPayRefundQueryListVO.class);

            String checkResult = SeagoorPayHelper.checkParam(seagoorPayQueryVO);
            if (checkResult != null) {
                logger.error("RECEIVE_SEAGOOR_PAY_REFUND_LIST_QUERY_REQUEST,CHECK_PARAM_ERROR:" + checkResult);
                return JsonUtil.convertObjToStr(new MResultVO<>(MResultInfo.SEAGOOR_PAY_PARAM_ERROR.code, checkResult));
            }

            MResultVO<PageForSeagoorPay<SeagoorPayRefundResultVO>> result = seagoorPayAO.queryRefundList(seagoorPayQueryVO);
            String resultStr = JsonUtil.convertObjToStr(result);
            return resultStr;
        } catch (Exception e) {
            logger.error("RECEIVE_SEAGOOR_PAY_REFUND_LIST_QUERY_REQUEST:", e);
            return JsonUtil.convertObjToStr(new MResultVO<>(MResultInfo.SEAGOOR_PAY_SYSTEM_ERROR));
        }
    }


    /**
     * 分页查询支付记录
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/querypaylist", method = RequestMethod.POST)
    public String queryPayList(HttpServletRequest request) {
        try {
            checkSwitch();
            String jsonStr = RequestHelper.getJsonStrByIO(request);
            logger.info("RECEIVE_SEAGOOR_PAY_LIST_QUERY_REQUEST,REQUEST_PARAM:" + jsonStr);
            SeagoorPayQueryListVO seagoorPayQueryVO = (SeagoorPayQueryListVO) JsonUtil.getObjectByJsonStr(jsonStr, SeagoorPayQueryListVO.class);

            String checkResult = SeagoorPayHelper.checkParam(seagoorPayQueryVO);
            if (checkResult != null) {
                logger.error("RECEIVE_SEAGOOR_PAY_LIST_QUERY_REQUEST,CHECK_PARAM_ERROR:" + checkResult);
                return JsonUtil.convertObjToStr(new MResultVO<>(MResultInfo.SEAGOOR_PAY_PARAM_ERROR.code, checkResult));
            }

            MResultVO<PageForSeagoorPay<SeagoorPayResultVO>> result = seagoorPayAO.queryPayList(seagoorPayQueryVO);
            String resultStr = JsonUtil.convertObjToStr(result);
            return resultStr;
        } catch (Exception e) {
            logger.error("RECEIVE_SEAGOOR_PAY_LIST_QUERY_REQUEST_ERROR:", e);
            return JsonUtil.convertObjToStr(new MResultVO<>(MResultInfo.SEAGOOR_PAY_SYSTEM_ERROR));
        }
    }

    private void checkSwitch() throws Exception {
        MResultVO<List<SwitchVO>> switchRes = switchAO.switchInfo();
        if(switchRes.success() && switchRes.getData()!= null){
            for(SwitchVO st : switchRes.getData()){
                if(st.getCode().equals(SwitchEnum.SEAGOOR_PAY.getCode())&& st.getValue().equals("0")){
                throw new MobileException("西客币支付暂时关闭");

                }
            }
        }
    }


}
