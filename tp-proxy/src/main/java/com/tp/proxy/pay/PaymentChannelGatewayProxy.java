package com.tp.proxy.pay;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.pay.PaymentChannelGateway;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.pay.IPaymentChannelGatewayService;
/**
 * 渠道对应网关关系表代理层
 * @author szy
 *
 */
@Service
public class PaymentChannelGatewayProxy extends BaseProxy<PaymentChannelGateway>{

	@Autowired
	private IPaymentChannelGatewayService paymentChannelGatewayService;

	@Override
	public IBaseService<PaymentChannelGateway> getService() {
		return paymentChannelGatewayService;
	}
}
