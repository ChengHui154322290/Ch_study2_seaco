package com.tp.service.pay;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.NameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.OrderConstant;
import com.tp.dao.pay.PaymentInfoDao;
import com.tp.dto.pay.AppPayData;
import com.tp.dto.pay.appdata.AliInternationalAppPayData;
import com.tp.dto.pay.appdata.AliInternationalAppSdkPayData;
import com.tp.dto.pay.cbdata.PayCallbackData;
import com.tp.dto.pay.cbdata.RefundCallbackData;
import com.tp.dto.pay.postdata.AliPayInternationalPostData;
import com.tp.dto.pay.postdata.AliPayInternationalRefundPostData;
import com.tp.dto.pay.postdata.PayPostData;
import com.tp.exception.ServiceException;
import com.tp.model.pay.PaymentInfo;
import com.tp.model.pay.RefundPayinfo;
import com.tp.result.pay.RefundResult;
import com.tp.result.pay.TradeStatusResult;
import com.tp.service.pay.cbdata.AlipayRefundResult;
import com.tp.service.pay.util.AlipaySubmit;
import com.tp.service.pay.util.HttpProtocolHandler;
import com.tp.service.pay.util.HttpRequest;
import com.tp.service.pay.util.HttpResponse;
import com.tp.service.pay.util.HttpResultType;
import com.tp.util.AlipayUtil;

/**
 * 支付宝国际支付服务
 * 
 * @author szy
 * @version 0.0.1
 *          下午6:22:37 zhouhui Exp $
 */
@Service("alipayInternationalPayPlatformService")
public class AliPayInternationalPlatformService extends AbstractPayPlatformService {

	/**
	 * 支付宝提供给商户的服务接入网关URL(新)
	 */
	private static final String ALIPAY_GATEWAY_NEW = "https://mapi.alipay.com/gateway.do?";
	protected Logger log = LoggerFactory.getLogger(AliPayInternationalPlatformService.class);
	@Autowired
	private PaymentInfoDao paymentInfoDao;

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
		AliPayInternationalPostData postData = new AliPayInternationalPostData(settings, paymentInfo);
		return postData;
	}

	@Override
	public TradeStatusResult queryPayStatus(PaymentInfo paymentInfoDO) {
		try {
			String paymentTradeNo = paymentInfoDO.getPaymentTradeNo();
			log.info("国际支付宝查询订单状态：{}", paymentTradeNo);
			TradeStatusResult result = AlipaySubmit.singleTradeQuery(settings, paymentTradeNo,true);
			log.info("国际支付宝订单查询结果：{}", result);
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
	public TradeStatusResult queryRefundStatus(RefundPayinfo refundPayinfoDO) {
		return null;
	}

	@Override
	protected boolean verifyResponse(Map<String, String> parameterMap) {
		return false;
	}

	@Override
	protected PayCallbackData getPayCallbackData(Map<String, String> parameterMap) {
		// 支付宝国际于支付宝普通支付公用一个支付回调
		return null;
	}

	@Override
	protected RefundCallbackData getRefundCallbackData(
			Map<String, String> parameterMap) {
		//国际支付宝退款没有回调
		return null;
	}

	@Override
	protected RefundResult doRefund(RefundPayinfo refundPayinfoDO) throws ServiceException {
		
		AliPayInternationalRefundPostData refundData = new AliPayInternationalRefundPostData(settings);

		PaymentInfo paymentInfoDO = paymentInfoDao.queryById(refundPayinfoDO.getPaymentId());
		
		Map<String, String> sParam = new HashMap<String, String>();
		sParam.put("service", "forex_refund");
		sParam.put("partner", refundData.getPartner());
		sParam.put("_input_charset", refundData.getInputCharset());
		sParam.put("out_return_no", String.valueOf(refundPayinfoDO.getBizCode()));
		sParam.put("out_trade_no", paymentInfoDO.getPaymentTradeNo());
		sParam.put("return_rmb_amount", refundPayinfoDO.getAmount().toString());
		sParam.put("currency", refundData.getCurrency());
		sParam.put("gmt_return", refundData.getGmtReturn());
		sParam.put("reason", refundData.getReason());
		sParam.put("sign", AlipayUtil.buildRequestPara(sParam, refundData.getKey(), refundData.getInputCharset()));

		log.info("支付宝国际退款参数:{}",sParam);
		HttpProtocolHandler httpProtocolHandler = HttpProtocolHandler.getInstance();

		HttpRequest request = new HttpRequest(HttpResultType.BYTES);
		// 设置编码集
		request.setCharset(refundData.getInputCharset());

		request.setParameters(generatNameValuePair(sParam));
		request.setUrl(ALIPAY_GATEWAY_NEW + "_input_charset=" + refundData.getInputCharset());

		HttpResponse response = null;
		String strResult = "";
		
		try {
			response = httpProtocolHandler.execute(request, "", "");
			if (response == null) {
				return null;
			}
			strResult = response.getStringResult(refundData.getInputCharset());
			Map<String, String> map = convertXml2Map(strResult);
			log.info("支付宝国际退款返回参数：{}", map);
			AlipayRefundResult result = new AlipayRefundResult(map);
			return result;
		} catch (Exception e) {
			log.info(e.getMessage(),e);
			throw new ServiceException(e);
		}
	}

	@Override
	protected AppPayData constructAppPostData(PaymentInfo paymentInfo, boolean forSdk) {
		if(forSdk){
			AliInternationalAppSdkPayData payData = new AliInternationalAppSdkPayData(settings, paymentInfo);
			return payData;
		}
		else{
			AliInternationalAppPayData payData = new AliInternationalAppPayData(settings, paymentInfo);
			payData.setReturnUrl(settings.getProperty("ALIPAY_APP_RETURN_URL") + paymentInfo.getPaymentId());
			payData.setActionUrl("https://mapi.alipay.com/gateway.do");
			payData.setService("create_forex_trade_wap");
			payData.setSignType("MD5");
			if(paymentInfo.getOrderType()!=null && OrderConstant.FAST_ORDER_TYPE.equals(paymentInfo.getOrderType().intValue())){
				payData.setReturnUrl(payData.getReturnUrl().replaceFirst("(.*://)?[^/]*/", settings.getProperty("fastPay.notify_url")));
			}
			return payData;
		}
	}
}
