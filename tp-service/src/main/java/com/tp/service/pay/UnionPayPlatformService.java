package com.tp.service.pay;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.tp.dto.pay.cbdata.PayCallbackData;
import com.tp.dto.pay.cbdata.RefundCallbackData;
import com.tp.model.pay.PaymentInfo;
import com.tp.model.pay.RefundPayinfo;
import com.tp.result.pay.RefundResult;
import com.tp.result.pay.TradeStatusResult;
import com.tp.service.pay.IPaymentInfoService;
import com.tp.service.pay.cbdata.UnionPayCallbackData;
import com.tp.service.pay.cbdata.UnionRefundCallbackData;
import com.tp.service.pay.cbdata.UnionRefundResult;
import com.tp.service.pay.unionpay.sdk.HttpClient;
import com.tp.service.pay.unionpay.sdk.SDKConfig;
import com.tp.service.pay.unionpay.sdk.SDKUtil;
import com.tp.util.DateUtil;


//@Service
public abstract class UnionPayPlatformService extends AbstractPayPlatformService {
	private Logger log = LoggerFactory.getLogger(UnionPayPlatformService.class);
	//@Autowired
	Properties paymentConfig;
	
	@Autowired
	IPaymentInfoService  paymentInfoService;
	

	/**
	 * java main方法 数据提交 　　 对数据进行签名
	 * 
	 * @param contentData
	 * @return　签名后的map对象
	 * FIXME 如何自Map与PostData方便转换
	 */
	@SuppressWarnings("unchecked")
	protected static Map<String, String> signData(Map<String, ?> contentData, boolean forApp) {
		Entry<String, String> obj = null;
		Map<String, String> submitFromData = new HashMap<String, String>();
		for (Iterator<?> it = contentData.entrySet().iterator(); it.hasNext();) {
			obj = (Entry<String, String>) it.next();
			String value = obj.getValue();
			if (StringUtils.isNotBlank(value)) {
				// 对value值进行去除前后空处理
				submitFromData.put(obj.getKey(), value.trim());
			}
		}
		/**
		 * 签名
		 */
		SDKUtil.sign(submitFromData, "UTF-8", forApp);

		return submitFromData;
	}

	@Override
	protected boolean verifyResponse(Map<String, String> parameterMap) {
		final String encoding = "UTF-8";
		if (SDKUtil.validate(parameterMap, encoding)) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	protected PayCallbackData getPayCallbackData(Map<String, String> parameterMap) {
		
		return new UnionPayCallbackData(parameterMap);
	}
	
	protected RefundResult doRefund(RefundPayinfo refundPayinfoObj, boolean forApp) {
		
		Map<String, String> formData = getRefundFormData(forApp);
		
		Long paymentId = refundPayinfoObj.getPaymentId();
		PaymentInfo paymentInfoDO = paymentInfoDao.queryById(paymentId);
		String origQryId=paymentInfoDO.getGatewayTradeNo();
		
		// 消费退货的订单号，由商户生成
		formData.put("orderId", String.valueOf(refundPayinfoObj.getBizCode()));// M
		// 原始交易的queryId
		formData.put("origQryId", origQryId);// M

		// 　
		formData.put("txnTime", DateUtil.format(new Date(), "yyyyMMddHHmmss"));// M

		// 与原消费交易一致
		formData.put("txnAmt", String.valueOf(Math.round(refundPayinfoObj.getAmount()*100)));// M
		
		Map<String, String> submitFromData = (Map<String, String>) signData(formData, false);
		String requestBackUrl;
		if (forApp) {
			requestBackUrl = SDKConfig.getConfig().getBackRequestUrl();
		} else {
			requestBackUrl = SDKConfig.getConfig().getBackRequestUrl();
		}
		log.info("refund to {}:\n params:{}",  requestBackUrl, submitFromData);
		Map<String, String> resmap = submitUrl(submitFromData,requestBackUrl);
		log.info("refund result:{}",  resmap);
			
		return new UnionRefundResult(resmap);
	}
	
	@Override
	protected RefundCallbackData getRefundCallbackData(Map<String, String> parameterMap) {
		return new UnionRefundCallbackData(parameterMap);
	}

	/**
	 * java main方法 数据提交 提交到后台
	 * 
	 * @param contentData
	 * @return 返回报文 map
	 */
	protected Map<String, String> submitUrl(Map<String, String> submitFromData,String requestUrl) {
		String resultString = "";
		log.info("requestUrl===={}", requestUrl);
		log.info("submitFromData===={}",  submitFromData);
		/**
		 * 发送
		 */
		HttpClient hc = new HttpClient(requestUrl, 30000, 30000);
		try {
			int status = hc.send(submitFromData, "UTF-8");
			if (200 == status) {
				resultString = hc.getResult();
			} else {
				log.error("调用接口失败，status={}, result=\n{}", status, hc.getResult());
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		Map<String, String> resData = new HashMap<String, String>();
		/**
		 * 验证签名
		 */
		if (null != resultString && !"".equals(resultString)) {
			// 将返回结果转换为map
			resData = SDKUtil.convertResultStringToMap(resultString);
			if (SDKUtil.validate(resData, "UTF-8")) {
				log.info("验证签名成功:{}", resultString);
			} else {
				log.error("验证签名失败:{}", resultString);
			}
			// 打印返回报文
			log.info("打印返回报文：{}", resultString);
		}
		return resData;
	}
	
	
	
	private Map<String, String> getQueryFormData(boolean forApp) {

		Map<String, String> contentData = getStaticFormData(forApp);
		// 交易类型 00
		contentData.put("txnType", "00");// M
		// 默认00
		contentData.put("txnSubType", "00");// M
		
		return contentData;
	}
	
	/**
	 * 6.3　消费退货类交易 表单填写
	 * 
	 * @return
	 */
	private Map<String, String> getRefundFormData(boolean forApp) {

		Map<String, String> contentData = getStaticFormData(forApp);

		// 取值：04
		contentData.put("txnType", "04");// M
		// 默认:00
		contentData.put("txnSubType", "00");// M
		
		contentData.put("channelType", "07");// M
		// 后台返回商户结果时使用，如上送，则发送商户后台交易结果通知
		contentData.put("backUrl", paymentConfig.getProperty("unionPay.refundBackUrl"));// M
		
		
		contentData.put("termId", "");// O
		// 商户自定义保留域，交易应答时会原样返回
		contentData.put("reqReserved", "");// O
		
		// 渠道类型为语音支付时使用用法见VPC交易信息组合域子域用法
		contentData.put("vpcTransData", "");// C
		return contentData;
	}

	
	/**公共的请求参数 */
	private Map<String, String> getStaticFormData(boolean forApp) {

		Map<String, String> contentData = new HashMap<String, String>();

		// 固定填写
		contentData.put("version", paymentConfig.getProperty("unionPay.version"));// M
		// 默认取值：UTF-8
		contentData.put("encoding", paymentConfig.getProperty("unionPay.encoding"));// M
		// 01RSA02 MD5 (暂不支持)
		contentData.put("signMethod", "01");// M
		
		// 默认:000000
		contentData.put("bizType", paymentConfig.getProperty("unionPay.bizType"));// M
		// 0：普通商户直连接入2：平台类商户接入
	    contentData.put("accessType", "0");// M
	    if (forApp) {
			
	    	contentData.put("merId", paymentConfig.getProperty("unionAppPay.merId"));// M
		} else {
			
			contentData.put("merId", paymentConfig.getProperty("unionPay.merId"));// M
		}
		// 格式如下：{子域名1=值&子域名2=值&子域名3=值} 移动支付参考消费
		contentData.put("reserved", "");// O
				
		return contentData;
	}

	protected TradeStatusResult queryStatus(Long orderId, Date tradeDate, String gatewayTradeNo, boolean forApp) {
		String requestBackUrl = SDKConfig.getConfig().getBackRequestUrl();
		Map<String, String> queryFormData = getQueryFormData(forApp);
		//查询类需要覆盖回000000
		queryFormData.put("bizType", "000000");// M
		// 被查询交易的订单号
		queryFormData.put("orderId",String.valueOf(orderId));// M
		// 被查询交易的交易时间
		queryFormData.put("txnTime", DateUtil.format(tradeDate, "yyyyMMddHHmmss"));// M
		// 待查询交易的流水号
		queryFormData.put("queryId", gatewayTradeNo);// C
		
		Map<String, String> submitFromData = (Map<String, String>) signData(queryFormData, false);
		log.info("payment query params:{}",  submitFromData);
		Map<String, String> resmap = submitUrl(submitFromData,requestBackUrl);
		log.info("payment query result:{}", resmap);

//		String respCode = resmap.get("respCode");
//		String respMsg = resmap.get("respMsg");
		String traceNo = resmap.get("traceNo");
		String origRespCode = resmap.get("origRespCode");
		String origRespMsg = resmap.get("origRespMsg");
		
		TradeStatusResult result = new TradeStatusResult();
		result.setSuccess("00".equals(origRespCode));
		result.setErrorMsg(origRespMsg);
		result.setTradeNo(traceNo);
		return result;
	}

}
