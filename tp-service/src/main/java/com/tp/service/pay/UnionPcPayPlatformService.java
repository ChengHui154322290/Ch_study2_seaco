package com.tp.service.pay;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.tp.dto.pay.AppPayData;
import com.tp.dto.pay.postdata.UnionPayPostData;
import com.tp.model.pay.PaymentInfo;
import com.tp.model.pay.RefundPayinfo;
import com.tp.result.pay.RefundResult;
import com.tp.result.pay.TradeStatusResult;
import com.tp.service.pay.unionpay.sdk.SDKConfig;
import com.tp.service.pay.unionpay.sdk.SDKUtil;

//@Service
public class UnionPcPayPlatformService extends UnionPayPlatformService {

	@PostConstruct
	public void init() {
		super.init();
		SDKConfig.getConfig().loadPropertiesFromSrc();
	}

	@PreDestroy
	public void destroy() {
		super.destroy();
	}

	@Override
	protected UnionPayPostData constructPostData(PaymentInfo paymentInfo) {
		UnionPayPostData unionpayPostData = new UnionPayPostData(paymentConfig, paymentInfo);
		unionpayPostData.setActionUrl(SDKConfig.getConfig().getFrontRequestUrl());
		unionpayPostData.setFrontUrl(paymentConfig.getProperty("unionPay.frontUrl") + paymentInfo.getPaymentId());
		unionpayPostData.setChannelType(paymentConfig.getProperty("unionPay.channelType"));
		unionpayPostData.setMerId(paymentConfig.getProperty("unionPay.merId"));

		Map<String, String> map = unionpayPostData.toMap();
		/**
		 * 签名
		 */
		SDKUtil.sign(map, "UTF-8", false);
		unionpayPostData.updateData(map);
		return unionpayPostData;
	}

	@Override
	protected AppPayData constructAppPostData(PaymentInfo paymentInfo, boolean forSdk) {
		// throw new RuntimeException("pc不支持手机端支付");
		return constructPostData(paymentInfo);
	}

	@Override
	protected RefundResult doRefund(RefundPayinfo refundPayinfoDO) {
		return doRefund(refundPayinfoDO, false);
	}

	@Override
	public TradeStatusResult queryPayStatus(PaymentInfo paymentInfoDO) {
		return queryStatus(paymentInfoDO.getBizCode(), paymentInfoDO.getCreateTime(), paymentInfoDO.getGatewayTradeNo(),
				false);
	}

	@Override
	public TradeStatusResult queryRefundStatus(RefundPayinfo refundPayinfoDO) {
		PaymentInfo paymentInfoDO = paymentInfoService.queryById(refundPayinfoDO.getPaymentId());
		return queryStatus(refundPayinfoDO.getBizCode(), refundPayinfoDO.getCreateTime(), paymentInfoDO.getGatewayTradeNo(), false);
	}

}
