package com.tp.service.pay;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.tp.common.vo.OrderConstant;
import com.tp.common.vo.PaymentConstant;
import com.tp.dao.pay.PaymentInfoDao;
import com.tp.dao.pay.PaymentLogDao;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.pay.AppPayData;
import com.tp.dto.pay.appdata.AliInternationalMergeAppPayData;
import com.tp.dto.pay.appdata.AliInternationalMergeAppSdkPayData;
import com.tp.dto.pay.cbdata.PayCallbackData;
import com.tp.dto.pay.cbdata.RefundCallbackData;
import com.tp.dto.pay.postdata.AliPayInternationalCustomsData;
import com.tp.dto.pay.postdata.AliPayInternationalMergePostData;
import com.tp.dto.pay.postdata.PayPostData;
import com.tp.exception.ServiceException;
import com.tp.model.ord.OrderConsignee;
import com.tp.model.ord.SubOrder;
import com.tp.model.pay.CustomsInfo;
import com.tp.model.pay.PaymentInfo;
import com.tp.model.pay.PaymentLog;
import com.tp.model.pay.RefundPayinfo;
import com.tp.result.pay.RefundResult;
import com.tp.result.pay.TradeStatusResult;
import com.tp.service.pay.ICustomsInfoService;
import com.tp.service.pay.cbdata.AliPayCallbackData;
import com.tp.service.pay.cbdata.AlipayRefundResult;
import com.tp.service.pay.cbdata.MergeAlipayRefundCallbackData;
import com.tp.service.pay.util.AlipayNotify;
import com.tp.service.pay.util.AlipaySubmit;
import com.tp.service.pay.util.HttpProtocolHandler;
import com.tp.service.pay.util.HttpRequest;
import com.tp.service.pay.util.HttpResponse;
import com.tp.service.pay.util.HttpResultType;
import com.tp.util.AlipayUtil;
import com.tp.util.DateUtil;
import com.tp.util.RSA;
import com.tp.util.StringUtil;

/**
 * 支付宝国际分账支付服务
 * 
 * @author zhouhui
 * @version $Id: AliPayInternationalPlatformServiceImpl.java, v 0.1 2015年4月14日
 *          下午6:22:37 zhouhui Exp $
 */
@Service("mergeAlipayPayPlatformService")
public class MergeAlipayPayPlatformService extends AbstractPayPlatformService {

	/**
	 * 支付宝提供给商户的服务接入网关URL(新)
	 */
	private static final String ALIPAY_GATEWAY_NEW = "https://mapi.alipay.com/gateway.do?";
	protected Logger log = LoggerFactory.getLogger(MergeAlipayPayPlatformService.class);
	@Autowired
	private PaymentInfoDao paymentInfoDao;
	@Autowired
	PaymentLogDao paymentLogDao;
	@Autowired
	ICustomsInfoService customsInfoService;

	/**
	 * MAP类型数组转换成NameValuePair类型
	 * 
	 * @param properties
	 *            MAP类型数组
	 * @return NameValuePair类型数组
	 */
	private static NameValuePair[] generatNameValuePair(Map<String, String> properties) {
		NameValuePair[] nameValuePair = new NameValuePair[properties.size()];
		int i = 0;
		for (Map.Entry<String, String> entry : properties.entrySet()) {
			nameValuePair[i++] = new NameValuePair(entry.getKey(), entry.getValue());
		}

		return nameValuePair;
	}

	@Override
	protected PayPostData constructPostData(PaymentInfo paymentInfo) {
//		if("".endsWith(arg0)){
		//支付宝合并支付
		AliPayInternationalMergePostData postData = new AliPayInternationalMergePostData(settings, paymentInfo);
		PaymentInfo query = new PaymentInfo();
		query.setPrtPaymentId(paymentInfo.getPrtPaymentId());
		List<PaymentInfo> paymentInfoDOs = paymentInfoDao.queryByObject(query);
		postData.setSplit_fund_info(getAliJson(paymentInfoDOs,settings));
//		postData.setSplit_fund_info("[{\"transIn\":\"2088711356326299\",\"amount\":\"0.1\",\"currency\":\"CNY\",\"desc\":\"test\"}]");
//		}else{
//			AliPayInternationalPostData postData = new AliPayInternationalPostData(paymentConfig, paymentInfo);
//		}
		
		return postData;
	}
	
	public String getAliJson(List<PaymentInfo> paymentInfoDOs,Properties paymentConfig){
		/*if(CollectionUtils.isEmpty(paymentInfoDOs))
			return "[]";
		Gson gson = new Gson();
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		Map<String, String> map = new HashMap<String, String>();
		
		Double amount = 0d;
		for(PaymentInfo infoDO : paymentInfoDOs){
			if(PaymentConstant.BIZ_TYPE.ORDER.code.intValue() == infoDO.getBizType().intValue()){
				amount += infoDO.getAmount();
			}
		}
		
		if(amount > 0){
			map.put("transIn",paymentConfig.getProperty("ALIPAY_PARTNER")); // 入账支付宝账号 国际
			map.put("amount",String.format("%.2f", amount)); // 金额
			map.put("currency", "CNY");//分账币种
			list.add(map);
			return  gson.toJson(list);
		}
		else{
			return "[]";
		}*/
	   return "[]";
	}

	@Override
	public TradeStatusResult queryPayStatus(PaymentInfo paymentInfo) {
		try {
			String paymentTradeNo = paymentInfo.getPaymentTradeNo();
			if(PaymentConstant.BIZ_TYPE.DSS.code.equals(paymentInfo.getBizType())) {
				paymentTradeNo = paymentInfo.getSerial();
			}
			log.info("国际支付宝查询订单状态：{}", paymentTradeNo);
			TradeStatusResult result = AlipaySubmit.singleTradeQuery(settings, paymentTradeNo,true);
			log.info("国际支付宝订单查询结果：{}", result);
			return result;
		} catch (Exception e) {
			log.error("{}退款出错", paymentInfo.getPaymentId(), e);
			throw new ServiceException(e);
		}
	}

	@Override
	public TradeStatusResult queryRefundStatus(RefundPayinfo refundPayinfoDO) {
		//国际支付宝无退款接口，只能通过对账文件和商户后台得知
		return null;
	}

	@Override
	protected boolean verifyResponse(Map<String, String> parameterMap) {
		return AlipayNotify.verify(parameterMap, settings, "mergeAlipay");
	}

	@Override
	protected PayCallbackData getPayCallbackData(Map<String, String> parameterMap) {
		PayCallbackData callbackData = new AliPayCallbackData(parameterMap);
		
		Document doc_notify_data = null;
		
		if (parameterMap.containsKey("notify_data")) {
			try {
				String notifyData = RSA.decrypt(parameterMap.get("notify_data"), settings.getProperty("ALIPAY_RSA_PRIVATE_KEY"), settings.getProperty("ALIPAY_INPUT_CHARSET"));
				// XML解析notify_data数据
				doc_notify_data = DocumentHelper.parseText(notifyData);
				// 商户订单号
				String paymentTradeNo = doc_notify_data.selectSingleNode("//notify/out_trade_no").getText();
				String trade_status = doc_notify_data.selectSingleNode( "//notify/trade_status" ).getText();
				String gatewayTradeNo = doc_notify_data.selectSingleNode("//notify/trade_no").getText();
				String errorCode = null;
				if(doc_notify_data.selectSingleNode("//notify/error_code") != null)
					errorCode = doc_notify_data.selectSingleNode("//notify/error_code").getText();
				Map<String, String> params = new HashMap<String, String>();
				params.put("trade_status", trade_status);
				params.put("out_trade_no", paymentTradeNo);
				params.put("trade_no", gatewayTradeNo);
				params.put("error_code", errorCode);
				callbackData = getPayCallbackData(params);
			} catch (Exception e) {
				log.error("解析notify_data出错, params:{}",parameterMap, e);
				throw new ServiceException("解析notify_data出错" + parameterMap, e);
			}
		}
		
		return callbackData;
	}

	@Override
	protected RefundCallbackData getRefundCallbackData(
			Map<String, String> parameterMap) {
		return new MergeAlipayRefundCallbackData(parameterMap);
	}

	@Override
	protected RefundResult doRefund(RefundPayinfo refundPayinfoDO) {
		
		PaymentInfo paymentInfoDO = paymentInfoDao.queryById(refundPayinfoDO.getPaymentId());
		PaymentInfo parent = getParentPaymentInfo(paymentInfoDO.getPrtPaymentId());
		
		Double amount = refundPayinfoDO.getAmount();
		//数据原因可能会出现退款比支付金额大一分的情况，当退款金额大于支付金额时，以支付总金额来退款
		if(amount > paymentInfoDO.getAmount())
			amount = paymentInfoDO.getAmount();
		
		Map<String, String> sParam = new HashMap<String, String>();
		sParam.put("service", "forex_refund");
		sParam.put("partner", settings.getProperty("ALIPAY_MERGEALIPAY_PARTNER"));
		sParam.put("_input_charset", "UTF-8");
		sParam.put("out_return_no", refundPayinfoDO.getBizCode().toString());
		sParam.put("out_trade_no", paymentInfoDO.getPaymentTradeNo());
		if(parent != null) {
			sParam.put("out_trade_no", parent.getPaymentTradeNo());
		}
		sParam.put("return_rmb_amount", amount.toString());
		sParam.put("gmt_return", DateUtil.getLongDateString(new Date()));
		sParam.put("reason", "refund refund no:" + refundPayinfoDO.getBizCode());
		sParam.put("sign_type", "MD5");
		sParam.put("notify_url", settings.getProperty("ALIPAY_MERGEALIPAY_REFUND_RETURN_URL"));
//		sParam.put("product_code", value);//用来区分是哪种业务类型的下单。Pc 创建的交易使用NEW_OVERSEAS_SELLER，无线创建的交易使用NEW_WAP_OVERSEAS_SELLER
		if(paymentInfoDO.getTradeType() == null || paymentInfoDO.getTradeType().intValue() == 1){
			sParam.put("product_code", "NEW_OVERSEAS_SELLER");
		}
		else{
			sParam.put("product_code", "NEW_WAP_OVERSEAS_SELLER");
		}
		String refundInfo = "[]";
		// 如果是普通订单，退款从国内支付宝账号退出
		/*if(paymentInfoDO.getChannelId() == null || paymentInfoDO.getChannelId().intValue() == 0){
			refundInfo = "[{\"transOut\":\""+settings.getProperty("ALIPAY_PARTNER")+"\", \"amount\":\""+refundPayinfoDO.getAmount()+"\",\"currency\":\"CNY\", \"desc\":\"境外收单境内退分账\"}]";
		}*/
		sParam.put("currency", "USD");
		sParam.put("split_fund_info", refundInfo);
		sParam.put("sign", AlipayUtil.buildRequestPara(sParam, settings.getProperty("ALIPAY_MERGEALIPAY_KEY"), "UTF-8"));

		log.info("支付宝国际分账退款参数:{}",sParam);
		HttpProtocolHandler httpProtocolHandler = HttpProtocolHandler.getInstance();

		HttpRequest request = new HttpRequest(HttpResultType.BYTES);
		// 设置编码集
		request.setCharset("UTF-8");

		request.setParameters(generatNameValuePair(sParam));
		request.setUrl(ALIPAY_GATEWAY_NEW + "_input_charset=" + "UTF-8");

		HttpResponse response = null;
		String strResult = "";
		
		try {
			response = httpProtocolHandler.execute(request, "", "");
			if (response == null) {
				return null;
			}
			strResult = response.getStringResult("UTF-8");
			Map<String, String> map = convertXml2Map(strResult);
			log.info("支付宝国际分账退款返回参数：{}", map);
			AlipayRefundResult result = new AlipayRefundResult(map);
			log.info("支付宝国际分账{}退款结果{}",refundPayinfoDO.getBizCode(), result.isSuccess());
			return result;
		} catch (Exception e) {
			log.info("退款单号{}退款出错",e);
			throw new ServiceException(e);
		}
	}

	@Override
	protected AppPayData constructAppPostData(PaymentInfo paymentInfo, boolean forSdk) {
		
		PaymentInfo query = new PaymentInfo();
		query.setPrtPaymentId(paymentInfo.getPrtPaymentId());
		List<PaymentInfo> paymentInfoDOs = paymentInfoDao.queryByObject(query);
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MINUTE, -30);
		if(forSdk){
			AliInternationalMergeAppSdkPayData payData = new AliInternationalMergeAppSdkPayData(settings, paymentInfo);
//			if(PaymentConstant.BIZ_TYPE.DSS.code.equals(paymentInfo.getBizType())) {
//				if(paymentInfo.getUpdateTime().before(c.getTime())){
//					paymentInfo.setSerial(String.valueOf(Long.valueOf(paymentInfo.getSerial())+1));
//					paymentInfoDao.updateNotNullById(paymentInfo);
//				}
//				payData.setOutTradeNo(String.valueOf(paymentInfo.getSerial()));
//			}
			payData.setSplit_fund_info(getAliJson(paymentInfoDOs,settings));
			return payData;
		}
		else{
			AliInternationalMergeAppPayData payData = new AliInternationalMergeAppPayData(settings, paymentInfo);
			String returnUrl = settings.getProperty("ALIPAY_APP_RETURN_URL").replace("PID", paymentInfo.getPaymentId().toString()).replace("ORDERCODE", paymentInfo.getBizCode().toString());
//			if(PaymentConstant.BIZ_TYPE.DSS.code.equals(paymentInfo.getBizType())) {
//				if(paymentInfo.getUpdateTime().before(c.getTime())){
//					paymentInfo.setSerial(String.valueOf(Long.valueOf(paymentInfo.getSerial())+1));
//					paymentInfoDao.updateNotNullById(paymentInfo);
//				}
//				payData.setOutTradeNo(String.valueOf(paymentInfo.getSerial()));
//			}
			if(paymentInfo.getOrderType()!=null && OrderConstant.FAST_ORDER_TYPE.equals(paymentInfo.getOrderType().intValue())){
				returnUrl = returnUrl.replaceFirst("(.*://)?[^/]*/", settings.getProperty("fastPay.notify_url"));
			}
			payData.setSplit_fund_info(getAliJson(paymentInfoDOs,settings));
			payData.setReturnUrl(returnUrl);
			payData.setMerchantUrl(payData.getReturnUrl());
			payData.setActionUrl("https://mapi.alipay.com/gateway.do?_input_charset=utf-8");
			payData.setService("create_forex_trade_wap");
			payData.setSignType("MD5");
			return payData;
		}
	}
	
	@Override
	protected AppPayData constructAppPostDataByParams(PaymentInfo paymentInfo, boolean forSdk, Map<String, Object> params){
		PaymentInfo query = new PaymentInfo();
		query.setPrtPaymentId(paymentInfo.getPrtPaymentId());
		List<PaymentInfo> paymentInfoDOs = paymentInfoDao.queryByObject(query);
//		Calendar c = Calendar.getInstance();
//		c.add(Calendar.MINUTE, -30);
		if(forSdk){
			AliInternationalMergeAppSdkPayData payData = new AliInternationalMergeAppSdkPayData(settings, paymentInfo);
//			if(PaymentConstant.BIZ_TYPE.DSS.code.equals(paymentInfo.getBizType())) {
//				if(paymentInfo.getUpdateTime().before(c.getTime())){
//					paymentInfo.setSerial(String.valueOf(Long.valueOf(paymentInfo.getSerial())+1));
//					paymentInfoDao.updateNotNullById(paymentInfo);
//				}
//				payData.setOutTradeNo(String.valueOf(paymentInfo.getSerial()));
//			}
			payData.setSplit_fund_info(getAliJson(paymentInfoDOs,settings));
			return payData;
		}
		else{
			AliInternationalMergeAppPayData payData = new AliInternationalMergeAppPayData(settings, paymentInfo);
			String returnUrl = settings.getProperty("ALIPAY_APP_RETURN_URL").replace("PID", paymentInfo.getPaymentId().toString()).replace("ORDERCODE", paymentInfo.getBizCode().toString());
			if(PaymentConstant.BIZ_TYPE.DSS.code.equals(paymentInfo.getBizType())) {
//				if(paymentInfo.getUpdateTime().before(c.getTime())){
//					paymentInfo.setSerial(String.valueOf(Long.valueOf(paymentInfo.getSerial())+1));
//					paymentInfoDao.updateNotNullById(paymentInfo);
//				}
//				payData.setOutTradeNo(String.valueOf(paymentInfo.getSerial()));
			}
			if (params != null && params.get("channelCode") != null) { //第三方商城的支付
				returnUrl  = settings.getProperty("ALIPAY_ESHOP_RETURN_URL")
						.replace("PID", paymentInfo.getPaymentId().toString())
						.replace("ORDERCODE", paymentInfo.getBizCode().toString())
						.replace("SHOPCODE", (String)params.get("channelCode"));
			}
			if(paymentInfo.getOrderType()!=null && OrderConstant.FAST_ORDER_TYPE.equals(paymentInfo.getOrderType().intValue())){
				returnUrl = returnUrl.replaceFirst("(.*://)?[^/]*/", settings.getProperty("fastPay.notify_url"));
			}
			if(params !=null && StringUtils.equals(String.valueOf(params.get("sysSource")),"world")){
				returnUrl= settings.getProperty("ALIPAY_WORLD_RETURN_URL").replace("PID", paymentInfo.getPaymentId().toString()).replace("ORDERCODE", paymentInfo.getBizCode().toString());;
			}


			payData.setSplit_fund_info(getAliJson(paymentInfoDOs,settings));
			payData.setReturnUrl(returnUrl);
			payData.setMerchantUrl(payData.getReturnUrl());
			payData.setActionUrl("https://mapi.alipay.com/gateway.do?_input_charset=utf-8");
			payData.setService("create_forex_trade_wap");
			payData.setSignType("MD5");
			return payData;
		}
	}
	
	@Override
	public ResultInfo<Boolean> putPaymentInfoToCustoms(PaymentInfo paymentInfoDO,SubOrder subOrder, OrderConsignee consignee, CustomsInfo customsInfo){
		Integer needPushCount = 1;
		if(null == customsInfo || !customsInfo.getPush()){
			return new ResultInfo<>(new FailInfo("支付海关信息不存在,不需要申报"));
		}
		if (paymentInfoDO.getPrtPaymentId() != 0) {
			PaymentInfo paymentInfo = paymentInfoDao.queryById(paymentInfoDO.getPrtPaymentId());
			if (paymentInfo != null) {
				if(PaymentConstant.BIZ_TYPE.MERGEORDER.code.intValue() == paymentInfo.getBizType()){
					PaymentInfo query = new PaymentInfo();
					query.setPrtPaymentId(paymentInfo.getPaymentId());			
					List<PaymentInfo> paymentInfoDOs = paymentInfoDao.queryByObject(query);
					needPushCount = paymentInfoDOs.size();
				}
			}
		}
		return putCustomMessage(paymentInfoDO, needPushCount, customsInfo);
	}
	
	//子单报关
	@Override
	public ResultInfo<Boolean> putPaymentInfoToCustoms(PaymentInfo paymentInfoDO,SubOrder subOrder, OrderConsignee consignee){	
		CustomsInfo customsInfo = customsInfoService.getCustomsInfo(paymentInfoDO.getGatewayId(), paymentInfoDO.getChannelId());
		return putPaymentInfoToCustoms(paymentInfoDO, subOrder, consignee, customsInfo);
	}
	
	//父单支付回调后调用
	@Override
	public void operateAfterCallbackSuccess(PaymentInfo paymentInfoDO) {
		if(PaymentConstant.BIZ_TYPE.MERGEORDER.code.intValue() == paymentInfoDO.getBizType()){
			PaymentInfo query = new PaymentInfo();
			query.setPrtPaymentId(paymentInfoDO.getPaymentId());
			
			List<PaymentInfo> paymentInfoDOs = paymentInfoDao.queryByObject(query);
			int needPushCount = 0;
			for(PaymentInfo dto : paymentInfoDOs){
				if(customsInfoService.isNeedPush(dto.getGatewayId(), dto.getChannelId())){
					needPushCount ++ ;
				}
			}
			
			for(PaymentInfo dto : paymentInfoDOs){
				if(customsInfoService.isNeedPush(dto.getGatewayId(), dto.getChannelId())){
					putCustomMessage(dto,needPushCount, 0);
				}
			}
		}
		else{
			putCustomMessage(paymentInfoDO,1, 0);
		}
	}

	private ResultInfo<Boolean> putCustomMessage(PaymentInfo paymentInfoDO, int needPushCount, CustomsInfo customsInfo){
		paymentInfoDO.setGatewayCode("alipayInternational");
		AliPayInternationalCustomsData customsData =new AliPayInternationalCustomsData(settings, paymentInfoDO, customsInfo);
		String postDataParamters = new Gson().toJson(customsData);
		log.info("payment custom is :{}", postDataParamters);
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("service", customsData.getService());
        sParaTemp.put("partner", customsData.getPartner());
        sParaTemp.put("_input_charset", "utf-8");
		sParaTemp.put("out_request_no", customsData.getOut_request_no());
		sParaTemp.put("trade_no", customsData.getTrade_no());
		sParaTemp.put("merchant_customs_code", customsData.getMerchant_customs_code());
		sParaTemp.put("amount", customsData.getAmount());
		sParaTemp.put("customs_place", customsData.getCustoms_place());
		sParaTemp.put("merchant_customs_name", customsData.getMerchant_customs_name());
		if(needPushCount > 1){
			sParaTemp.put("is_split", "T");
			sParaTemp.put("sub_out_biz_no", paymentInfoDO.getBizCode().toString());
		}
		sParaTemp.put("sign_type", "MD5");
		sParaTemp.put("sign", AlipayUtil.buildRequestMysign(sParaTemp, customsData.getKey()));
		Integer customStatus = null;
		String sHtmlText = null;
		String payCode = null;
		String errorText = null;
		try {
			//建立请求
			log.info("支付宝合并支付参数：{}", customsData);
			sHtmlText = AlipaySubmit.buildInternaltionalRequest(sParaTemp, "utf-8");
			log.info("支付宝合并支付结果：{}", sHtmlText);
			Document doc_notify_data = DocumentHelper.parseText(sHtmlText);
			String isSuccess = doc_notify_data.selectSingleNode("//alipay/is_success").getText();			
			if("F".equals(isSuccess)){
				customStatus = PaymentConstant.CUSTOM_STATUS.FAIL.code;
				errorText = doc_notify_data.selectSingleNode("//alipay/error").getText();
			}else if("T".equals(isSuccess)){
				String result_code=doc_notify_data.selectSingleNode("//alipay//response/alipay/result_code").getText();
				payCode = doc_notify_data.selectSingleNode("//alipay//response/alipay/trade_no").getText();//支付单号(报关)				
				if("SUCCESS".equals(result_code)){
					customStatus = PaymentConstant.CUSTOM_STATUS.SUCCESS.code;
				}else if("FAIL".equals(result_code)){
					customStatus = PaymentConstant.CUSTOM_STATUS.FAIL.code;
					errorText = doc_notify_data.selectSingleNode("//alipay//response/alipay/detail_error_des").getText();
				}
			}
		} catch (Exception e) {
			customStatus = PaymentConstant.CUSTOM_STATUS.FAIL.code;
			log.error("订单号{}报关失败",paymentInfoDO.getBizCode(), e);
		}
		log.info("payment{}报关状态{}",paymentInfoDO.getBizCode(), customStatus);
		paymentInfoDO.setPaymentCustomsNo(payCode);
		updateAfterPutCustom(paymentInfoDO, customStatus, sHtmlText);
		if (!PaymentConstant.CUSTOM_STATUS.SUCCESS.code.equals(customStatus)) {
			return new ResultInfo<>(new FailInfo(errorText));
		}
		return new ResultInfo<>(Boolean.TRUE);
	}
	
	private void putCustomMessage(PaymentInfo paymentInfoDO,int needPushCount, int tryTime){
		if(tryTime >= 3)
			return ;
		log.info("payment{}报关，第{}次尝试", paymentInfoDO.getBizCode(), tryTime);
		paymentInfoDO.setGatewayCode("alipayInternational");
		AliPayInternationalCustomsData customsData =new AliPayInternationalCustomsData(settings, paymentInfoDO);
		String postDataParamters = new Gson().toJson(customsData);
		log.info("payment custom is :{}", postDataParamters);
//		Map<String, String> sParam = new HashMap<String, String>();
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("service", customsData.getService());
        sParaTemp.put("partner", customsData.getPartner());
        sParaTemp.put("_input_charset", "utf-8");
		sParaTemp.put("out_request_no", customsData.getOut_request_no());
		sParaTemp.put("trade_no", customsData.getTrade_no());
		sParaTemp.put("merchant_customs_code", customsData.getMerchant_customs_code());
		sParaTemp.put("amount", customsData.getAmount());
		sParaTemp.put("customs_place", customsData.getCustoms_place());
		sParaTemp.put("merchant_customs_name", customsData.getMerchant_customs_name());
		if(needPushCount > 1){
			sParaTemp.put("is_split", "T");
			sParaTemp.put("sub_out_biz_no", paymentInfoDO.getBizCode().toString());
		}
		sParaTemp.put("sign_type", "MD5");
		sParaTemp.put("sign", AlipayUtil.buildRequestMysign(sParaTemp, customsData.getKey()));
		Integer customStatus = null;
		String sHtmlText = null;
		try {
			//建立请求
			log.info("支付宝合并支付参数：{}", customsData);
			sHtmlText = AlipaySubmit.buildInternaltionalRequest(sParaTemp, "utf-8");
			log.info("支付宝合并支付结果：{}", sHtmlText);
			Document doc_notify_data = DocumentHelper.parseText(sHtmlText);
			String isSuccess = doc_notify_data.selectSingleNode("//alipay/is_success").getText();
			String payCode = null;
			if("F".equals(isSuccess)){
				customStatus = PaymentConstant.CUSTOM_STATUS.FAIL.code;
				Thread.sleep(2000);
				putCustomMessage(paymentInfoDO,needPushCount, tryTime ++);
			}else if("T".equals(isSuccess)){
				String result_code=doc_notify_data.selectSingleNode("//alipay//response/alipay/result_code").getText();
				payCode = doc_notify_data.selectSingleNode("//alipay//response/alipay/trade_no").getText();//支付单号(报关)
				if("SUCCESS".equals(result_code)){
					customStatus = PaymentConstant.CUSTOM_STATUS.SUCCESS.code;
				}else if("FAIL".equals(result_code)){
					customStatus = PaymentConstant.CUSTOM_STATUS.FAIL.code;
				}
			}
			paymentInfoDO.setPaymentCustomsNo(payCode);
		} catch (Exception e) {
			customStatus = PaymentConstant.CUSTOM_STATUS.FAIL.code;
			log.error("订单号{}报关失败",paymentInfoDO.getBizCode(), e);
		}
		log.info("payment{}报关状态{}",paymentInfoDO.getBizCode(), customStatus);
		if (PaymentConstant.CUSTOM_STATUS.SUCCESS.code.equals(customStatus)) {
			updateAfterPutCustom(paymentInfoDO, customStatus, sHtmlText);
		}else{
			try {
				Thread.sleep(2000);
				putCustomMessage(paymentInfoDO,needPushCount, tryTime ++);
			} catch (InterruptedException e1) {
				log.error("{}",paymentInfoDO.getBizCode(),e1);
			}	
		}	
	}
	private void updateAfterPutCustom(PaymentInfo paymentInfoDO, Integer customStatus, String logContent){
		Date now = new Date();
		PaymentInfo toBeUpdated = new PaymentInfo();
		toBeUpdated.setPaymentId(paymentInfoDO.getPaymentId());
		toBeUpdated.setPaymentCustomsType(customStatus);
		toBeUpdated.setUpdateTime(now);
		if (StringUtil.isNotEmpty(paymentInfoDO.getPaymentCustomsNo())) {
			toBeUpdated.setPaymentCustomsNo(paymentInfoDO.getPaymentCustomsNo());
		}
		try {
			int row = paymentInfoDao.updateNotNullById(toBeUpdated);
			log.info("更新bizCode{},paymentId{}行数为",paymentInfoDO.getBizCode(),paymentInfoDO.getPaymentId(), row);
			PaymentLog paymentLogDO = new PaymentLog(
					paymentInfoDO.getPaymentId(),
					PaymentConstant.OBJECT_TYPE.PAYMENT.code,
					PaymentConstant.PAY_ACTION_NAME.PUT_CUSTOM.cnName, logContent,
					paymentInfoDO.getActionIp(), now,
					paymentInfoDO.getCreateUser());
			paymentLogDO.setPartTable(DateUtil.format(now, "YYMM"));
			paymentLogDao.insert(paymentLogDO);
		} catch (Exception e) {
			log.error("更新{}出错",paymentInfoDO.getBizCode(), e);
		}
	}
}
