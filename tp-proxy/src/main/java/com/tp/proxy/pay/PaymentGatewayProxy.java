package com.tp.proxy.pay;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.pay.PaymentGateway;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.pay.IPaymentGatewayService;
/**
 * 支付网关配置表代理层
 * @author szy
 *
 */
@Service
public class PaymentGatewayProxy extends BaseProxy<PaymentGateway>{

	@Autowired
	private IPaymentGatewayService paymentGatewayService;

	@Override
	public IBaseService<PaymentGateway> getService() {
		return paymentGatewayService;
	}
	
	public List<PaymentGateway> queryPaymentGateWayLists(Long orderType, Long channelId) {
		return paymentGatewayService.queryPaymentGateWayLists(orderType, channelId);
	}
	
}
