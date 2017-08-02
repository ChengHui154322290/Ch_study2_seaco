package com.tp.proxy.pay;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.pay.PaymentLog;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.pay.IPaymentLogService;
/**
 * 支付业务日志代理层
 * @author szy
 *
 */
@Service
public class PaymentLogProxy extends BaseProxy<PaymentLog>{

	@Autowired
	private IPaymentLogService paymentLogService;

	@Override
	public IBaseService<PaymentLog> getService() {
		return paymentLogService;
	}
}
