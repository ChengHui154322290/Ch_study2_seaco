package com.tp.proxy.pay;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.dto.pay.AppPayData;
import com.tp.model.pay.PaymentInfo;
import com.tp.proxy.BaseProxy;
import com.tp.query.pay.AppPaymentCallDto;
import com.tp.service.IBaseService;
import com.tp.service.pay.IAppPaymentService;
/**
 * 支付信息表代理层
 * @author szy
 *
 */
@Service
public class AppPaymentProxy extends BaseProxy<PaymentInfo>{

	@Autowired
	private IAppPaymentService appPaymentService;

	@Override
	public IBaseService<PaymentInfo> getService() {
		return null;
	}
	public AppPayData getAppData(AppPaymentCallDto paymentCallDto) {
		return appPaymentService.getAppData(paymentCallDto);
	}
}
