package com.tp.service.pay;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.mchange.v2.codegen.bean.GeneratorExtension;
import com.tp.common.vo.OrderConstant;
import com.tp.common.vo.PaymentConstant;
import com.tp.dao.pay.PaymentInfoDao;
import com.tp.dto.pay.AppPayData;
import com.tp.dto.pay.appdata.AliAppPayData;
import com.tp.dto.pay.cbdata.PayCallbackData;
import com.tp.dto.pay.cbdata.RefundCallbackData;
import com.tp.dto.pay.postdata.AliPayPostData;
import com.tp.dto.pay.postdata.AliPayRefundPostData;
import com.tp.dto.pay.postdata.PayPostData;
import com.tp.model.pay.PaymentInfo;
import com.tp.model.pay.RefundPayinfo;
import com.tp.result.pay.RefundResult;
import com.tp.result.pay.TradeStatusResult;
import com.tp.service.pay.cbdata.AliPayCallbackData;
import com.tp.service.pay.cbdata.AliPayRefundCallbackData;
import com.tp.service.pay.cbdata.AlipayRefundResult;
import com.tp.service.pay.util.AlipayNotify;
import com.tp.service.pay.util.AlipaySubmit;
import com.tp.util.DateUtil;
import com.tp.util.RSA;


@Service("alipayDirectPayPlatformService")
public class AliPayPlatformService extends AbstractPayPlatformService {
	private Logger log = LoggerFactory.getLogger(AliPayPlatformService.class);
	
	@Override
	protected PayPostData constructPostData(PaymentInfo paymentInfo) {
		AliPayPostData postData = new AliPayPostData(settings, paymentInfo);
		return postData;
	}
	
	@Override
	protected AppPayData constructAppPostData(PaymentInfo paymentInfo, boolean forSdk) {
		AppPayData postData;
		if (forSdk) {
			postData = constructAppSdkData(paymentInfo);
		} else {
			postData=constructWapData(paymentInfo);
		}
		
		return postData;
	}
	
	@Override
	protected AppPayData constructAppPostDataByParams(PaymentInfo paymentInfo, boolean forSdk, Map<String, Object> params){
		AppPayData postData;
		if (forSdk) {
			postData = constructAppSdkData(paymentInfo);
		} else {
			postData=constructWapDataWithParams(paymentInfo, params);
		}	
		return postData;
	}
	
	private AppPayData constructWapData(PaymentInfo paymentInfo) {
		AliPayPostData postData = new AliPayPostData(settings, paymentInfo);
		
		String returnUrl = settings.getProperty("ALIPAY_APP_RETURN_URL").replace("PID", paymentInfo.getPaymentId().toString()).replace("ORDERCODE", paymentInfo.getBizCode().toString());
//		Calendar c = Calendar.getInstance();
//		c.add(Calendar.MINUTE, -30);
//		if(PaymentConstant.BIZ_TYPE.DSS.code.equals(paymentInfo.getBizType())) {
//			if(paymentInfo.getUpdateTime().before(c.getTime())) {
//				paymentInfo.setSerial(String.valueOf(Long.valueOf(paymentInfo.getSerial())+1));
//				paymentInfo.setUpdateTime(new Date());
//				paymentInfoDao.updateNotNullById(paymentInfo);
//			}
//			postData.setOutTradeNo(String.valueOf(paymentInfo.getSerial()));
//		}
		if(PaymentConstant.BIZ_TYPE.DSS.code.equals(paymentInfo.getBizType())) {
			postData.setOutTradeNo(String.valueOf(paymentInfo.getSerial()));
		}
		Map<String, String> sParaTemp1 = new HashMap<String, String>();
		sParaTemp1.put("service", "alipay.wap.create.direct.pay.by.user");
        sParaTemp1.put("partner", settings.getProperty("ALIPAY_PARTNER"));
        sParaTemp1.put("seller_id", settings.getProperty("ALIPAY_PARTNER"));
        sParaTemp1.put("_input_charset", settings.getProperty("ALIPAY_INPUT_CHARSET"));
		sParaTemp1.put("payment_type", "1");
		sParaTemp1.put("notify_url", postData.getNotifyUrl());
		sParaTemp1.put("return_url", returnUrl);
		if(paymentInfo.getOrderType()!=null && OrderConstant.FAST_ORDER_TYPE.equals(paymentInfo.getOrderType().intValue())){
			sParaTemp1.put("notify_url", sParaTemp1.get("notify_url").replaceFirst("(.*://)?[^/]*/", settings.getProperty("fastPay.notify_url")));
			sParaTemp1.put("return_url", sParaTemp1.get("return_url").replaceFirst("(.*://)?[^/]*/", settings.getProperty("fastPay.notify_url")));
			returnUrl = sParaTemp1.get("return_url");
		}
		sParaTemp1.put("out_trade_no", postData.getOutTradeNo());
		sParaTemp1.put("subject", postData.getSubject());
		sParaTemp1.put("total_fee", postData.getTotalFee());
		// 支付宝收银台页面 “返回”按钮的跳转地址
		sParaTemp1.put("show_url", returnUrl);
		
		
		try {
			
			Map<String, String> resultMap = AlipaySubmit.BuildRequestParaAdapter(sParaTemp1, settings.getProperty("ALIPAY_RSA_PRIVATE_KEY"));
//			System.out.println(AlipaySubmit.buildRequest1(sParaTemp1, "get", "确认",settings.getProperty("ALIPAY_RSA_PRIVATE_KEY")));
			
	        postData.setService(resultMap.get("service"));
	        postData.setPartner(resultMap.get("partner"));
	        postData.setSellerEmail(resultMap.get("seller_id"));
	        postData.setSignType(resultMap.get("sign_type"));
	        postData.setSign(resultMap.get("sign"));
	        postData.setWap_action_url(AlipaySubmit.ALIPAY_GATEWAY_NEW+"_input_charset=UTF-8");
	        postData.setShowUrl(returnUrl);
	        postData.setReturnUrl(returnUrl);
			
		} catch (Exception e) {
			log.error("", e);
		} 
		return postData;
	}
	
	private AppPayData constructWapDataWithParams(PaymentInfo paymentInfo, Map<String, Object> params) {
		AliPayPostData postData = new AliPayPostData(settings, paymentInfo);
		
		String returnUrl = settings.getProperty("ALIPAY_APP_RETURN_URL").replace("PID", paymentInfo.getPaymentId().toString()).replace("ORDERCODE", paymentInfo.getBizCode().toString());
//		Calendar c = Calendar.getInstance();
//		c.add(Calendar.MINUTE, -30);
//		if(PaymentConstant.BIZ_TYPE.DSS.code.equals(paymentInfo.getBizType())) {
//			if(paymentInfo.getUpdateTime().before(c.getTime())) {
//				paymentInfo.setSerial(String.valueOf(Long.valueOf(paymentInfo.getSerial())+1));
//				paymentInfo.setUpdateTime(new Date());
//				paymentInfoDao.updateNotNullById(paymentInfo);
//			}
//			postData.setOutTradeNo(String.valueOf(paymentInfo.getSerial()));
//		}
		if(PaymentConstant.BIZ_TYPE.DSS.code.equals(paymentInfo.getBizType())) {
			postData.setOutTradeNo(String.valueOf(paymentInfo.getSerial()));
		}
		Map<String, String> sParaTemp1 = new HashMap<String, String>();
		sParaTemp1.put("service", "alipay.wap.create.direct.pay.by.user");
        sParaTemp1.put("partner", settings.getProperty("ALIPAY_PARTNER"));
        sParaTemp1.put("seller_id", settings.getProperty("ALIPAY_PARTNER"));
        sParaTemp1.put("_input_charset", settings.getProperty("ALIPAY_INPUT_CHARSET"));
		sParaTemp1.put("payment_type", "1");
		sParaTemp1.put("notify_url", postData.getNotifyUrl());
		sParaTemp1.put("return_url", returnUrl);
		if(paymentInfo.getOrderType()!=null && OrderConstant.FAST_ORDER_TYPE.equals(paymentInfo.getOrderType().intValue())){
			sParaTemp1.put("notify_url", sParaTemp1.get("notify_url").replaceFirst("(.*://)?[^/]*/", settings.getProperty("fastPay.notify_url")));
			sParaTemp1.put("return_url", sParaTemp1.get("return_url").replaceFirst("(.*://)?[^/]*/", settings.getProperty("fastPay.notify_url")));
			returnUrl = sParaTemp1.get("return_url");
		}
		//第三方商城支付
		if (params != null && params.get("channelCode") != null) {
			returnUrl = settings.getProperty("ALIPAY_ESHOP_RETURN_URL")
					.replace("PID", paymentInfo.getPaymentId().toString())
					.replace("ORDERCODE", paymentInfo.getBizCode().toString())
					.replace("SHOPCODE", (String)params.get("channelCode"));
			sParaTemp1.put("return_url", returnUrl);
		}

		if(params !=null && StringUtils.equals(String.valueOf(params.get("sysSource")),"world")){
			returnUrl= settings.getProperty("ALIPAY_WORLD_RETURN_URL").replace("PID", paymentInfo.getPaymentId().toString()).replace("ORDERCODE", paymentInfo.getBizCode().toString());;
			sParaTemp1.put("return_url", returnUrl);
		}

		sParaTemp1.put("out_trade_no", postData.getOutTradeNo());
		sParaTemp1.put("subject", postData.getSubject());
		sParaTemp1.put("total_fee", postData.getTotalFee());
		// 支付宝收银台页面 “返回”按钮的跳转地址
		sParaTemp1.put("show_url", returnUrl);
		
		
		try {
			
			Map<String, String> resultMap = AlipaySubmit.BuildRequestParaAdapter(sParaTemp1, settings.getProperty("ALIPAY_RSA_PRIVATE_KEY"));
//			System.out.println(AlipaySubmit.buildRequest1(sParaTemp1, "get", "确认",settings.getProperty("ALIPAY_RSA_PRIVATE_KEY")));
			
	        postData.setService(resultMap.get("service"));
	        postData.setPartner(resultMap.get("partner"));
	        postData.setSellerEmail(resultMap.get("seller_id"));
	        postData.setSignType(resultMap.get("sign_type"));
	        postData.setSign(resultMap.get("sign"));
	        postData.setWap_action_url(AlipaySubmit.ALIPAY_GATEWAY_NEW+"_input_charset=UTF-8");
	        postData.setShowUrl(returnUrl);
	        postData.setReturnUrl(returnUrl);
			
		} catch (Exception e) {
			log.error("", e);
		} 
		return postData;
	}
	
	private AppPayData constructAppSdkData(PaymentInfo paymentInfo) {
		AliAppPayData orderPayDto = new AliAppPayData();
		orderPayDto.setPartner(settings.getProperty("ALIPAY_PARTNER"));
		orderPayDto.setSeller(settings.getProperty("ALIPAY_SELLER_EMAIL"));
		orderPayDto.setTradeNo(String.valueOf(paymentInfo.getBizCode()));
//		Calendar c = Calendar.getInstance();
//		c.add(Calendar.MINUTE, -30);
//		if(PaymentConstant.BIZ_TYPE.DSS.code.equals(paymentInfo.getBizType())) {
//			if(paymentInfo.getUpdateTime().before(c.getTime())) {
//				paymentInfo.setSerial(String.valueOf(Long.valueOf(paymentInfo.getSerial())+1));
//				paymentInfo.setUpdateTime(new Date());
//				paymentInfoDao.updateNotNullById(paymentInfo);
//			}
//			orderPayDto.setTradeNo(String.valueOf(paymentInfo.getSerial()));
//		}
		orderPayDto.setProductName(settings.getProperty("ALIPAY_SUBJECT"));
		orderPayDto.setAmount(String.format("%.2f", paymentInfo.getAmount()));
		orderPayDto.setNotifyUrl(settings.getProperty("ALIPAY_NOTIFY_URL"));
		
		String orderMessage2Sign = orderPayDto.getOrderMessage2Sign();
		log.info("orderMessage2Sign={}", orderMessage2Sign);
		orderPayDto.setOrderInfo(orderMessage2Sign);
		String sign = RSA.sign(orderMessage2Sign, settings.getProperty("ALIPAY_RSA_PRIVATE_KEY"), "utf-8");
		log.info("sign={}", sign);
		try {
			orderPayDto.setSignedString(URLEncoder.encode(sign, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			log.error("", e);
		}
		
		return orderPayDto;
	}
	
	@Override
	protected PayCallbackData getPayCallbackData(Map<String, String> parameterMap) {
		PayCallbackData callbackData = new AliPayCallbackData(parameterMap);
		return callbackData;
	}
	public TradeStatusResult queryPayStatus(PaymentInfo paymentInfo){
		try {
			String paymentTradeNo = paymentInfo.getPaymentTradeNo();
			if(PaymentConstant.BIZ_TYPE.DSS.code.equals(paymentInfo.getBizType())) {
				paymentTradeNo = paymentInfo.getSerial();
			}
			log.info("支付宝查询订单状态：{}", paymentTradeNo);
			TradeStatusResult result = AlipaySubmit.singleTradeQuery(settings, paymentTradeNo,false);
			log.info("支付宝订单查询结果：{}", result);
			return result;
		} catch (Exception e) {
			log.error("", e);
			TradeStatusResult result = new TradeStatusResult();
			result.setSuccess(false);
			result.setErrorMsg("查询异常");
			return result;
		}
	}
	@Override
	protected boolean verifyResponse(Map<String, String> parameterMap) {
		return AlipayNotify.verify(parameterMap, settings, "alipay");
	}
	public void setPaymentInfoDAO(PaymentInfoDao paymentInfoDao) {
		this.paymentInfoDao = paymentInfoDao;
	}

	
	@Override
	protected RefundResult doRefund(RefundPayinfo refundPayinfoDO) {
		Map<String, String> sParam = new HashMap<String, String>();
		
		AliPayRefundPostData refundData = new AliPayRefundPostData(settings);
		Double amount = refundPayinfoDO.getAmount();
		PaymentInfo paymentInfoDO = paymentInfoDao.queryById(refundPayinfoDO.getPaymentId());
		refundData.setDetailData(paymentInfoDO.getGatewayTradeNo()+ "^" + amount + "^" +"订单退款");
		refundData.setBatchNo(DateUtil.format(new Date(), "yyyyMMddHHmmss"));
		refundData.setBatchNum("1");
		sParam.put("out_trade_no", paymentInfoDO.getPaymentTradeNo());
		sParam.put("trade_no", paymentInfoDO.getGatewayTradeNo());
		sParam.put("payType", "支付宝网关");
		sParam.put("payUser", "alipay");
		
		String sHtmlText;
		try {
			log.info("支付宝退款参数：{}", refundData);
			sHtmlText = AlipaySubmit.refund(refundData);
			log.info("支付宝退款结果：{}", sHtmlText);
			Document doc_notify_data = DocumentHelper.parseText(sHtmlText);
			String isSuccess = doc_notify_data.selectSingleNode("//alipay/is_success").getText();
			sParam.put("is_success", isSuccess);
			if("F".equals(isSuccess)){
				sParam.put("error", doc_notify_data.selectSingleNode("//alipay/error").getText());
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			sParam.put("is_success", "F");
			sParam.put("error", e.getMessage());
		}
		
		return new AlipayRefundResult(sParam);
	}
	

//	@Override
//	public String refundCallback(Map<String, String> params) throws ServiceException {
//		RefundPayinfo refundInfoDO = refundPayinfoService.selectOneBySerial(params.get("batch_no"));
//		refundInfoDO.setModifyTime(new Date());
//		if("T".equals(params.get("is_success")))
//			refundInfoDO.setStatus(RefundInfoConstant.REFUND_TYPE.REFUNDED.code);
//		else
//			refundInfoDO.setStatus(RefundInfoConstant.REFUND_TYPE.FAIL_REFUND.code);
//		refundInfoDO.setNotified(RefundInfoConstant.NOTIFY_STATUS.NOTIFIED.code);
//		refundInfoDO.setNotifyTime(DateUtil.format(params.get("notify_time"), "yyyy-MM-dd HH:mm:ss"));
//		refundInfoDO.setModifyUserId("alipay");
//		refundInfoDO.setCallbackInfo(params.get("result_details"));
//		refundPayinfoService.update(refundInfoDO,false);
//		
//		return "success";
//	}
	@Override
	protected RefundCallbackData getRefundCallbackData(Map<String, String> parameterMap) {
		return new AliPayRefundCallbackData(parameterMap);
	}

	@Override
	public TradeStatusResult queryRefundStatus(RefundPayinfo refundPayinfoDO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getReturnMsg(PaymentInfo paymentInfoObj, PayCallbackData callbackData) {
		if(callbackData.isSuccess()) return "success";
		return "fail";
	}

//	@Override
//	public String refund(String gateway, Long paymentInfoId) throws ServiceException {
//		try {
//			PaymentGatewayDO paymentGatewayDO = paymentGatewayDAO.selectOneByCode(gateway);
//			PaymentInfo paymentInfoDO = paymentInfoDao.selectById(paymentInfoId);
//			if(PaymentInfoConstant.PAYMENT_STATUS.PAYED.code.equals(paymentInfoDO.getStatus())){
//				RefundPayinfo refundInfoDO = new RefundPayinfo();
//				refundInfoDO.setAmount(paymentInfoDO.getAmount());
//				refundInfoDO.setBizCode(paymentInfoDO.getBizCode());
//				refundInfoDO.setCreateTime(new Date());
//				refundInfoDO.setCreateUserId(paymentInfoDO.getCreateUserId());
//				refundInfoDO.setGatewayId(paymentInfoDO.getGatewayId());
//				refundInfoDO.setGatewayTradeNo(paymentInfoDO.getGatewayTradeNo());
//				refundInfoDO.setPaymentId(paymentInfoId);
//				refundInfoDO.setRefundType(1);
//				refundInfoDO.setGatewayId(paymentGatewayDO.getGatewayId());
//				refundInfoDO.setSerial(DateUtil.format(new Date(), "yyyyMMdd")+paymentInfoId+SerialGenerator.generat(4));
//				refundInfoDO.setStatus(RefundInfoConstant.REFUND_TYPE.REFUNDING.code);
//				refundPayinfoService.insert(refundInfoDO);
//			}
//			
//		} catch (Exception e) {
//			log.error(e);
//			throw new ServiceException(e);
//		}
//		return null;
    //	}

}
