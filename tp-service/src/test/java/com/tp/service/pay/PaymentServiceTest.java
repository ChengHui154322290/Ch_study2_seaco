package com.tp.service.pay;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.tp.common.vo.PaymentConstant;
import com.tp.model.pay.PaymentInfo;
import com.tp.mq.RabbitMqProducer;
import com.tp.mq.exception.MqClientException;
import com.tp.service.ord.mq.SalesOrderPaidCallback;
import com.tp.service.pay.IPaymentInfoService;
import com.tp.service.pay.util.AlipaySubmit;
import com.tp.test.BaseTest;

public class PaymentServiceTest extends BaseTest{

	@Autowired
	private IPaymentInfoService paymentInfoService;
	@Autowired
	private RabbitMqProducer rabbitMqProducer;
	@Autowired
	private SalesOrderPaidCallback salesOrderPaidCallback;
	@Autowired
	Properties settings;
	@Test
	public void testCallback() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("paymentTradeNo", "1116011875511762");
		PaymentInfo paymentInfo = paymentInfoService.queryUniqueByParams(params);
		try {
			// 通知支付结果
			rabbitMqProducer.sendP2PMessage(PaymentConstant.PAYMENT_INFO_STATUS_QUEUE, paymentInfo);
		} catch (MqClientException e) {
		}
		//salesOrderPaidCallback.execute(paymentInfo);
	}
	
	@Test
	public void testQueryPaymentStatus() {
		try {
			System.out.println(AlipaySubmit.singleTradeQuery(settings, "42016033014", false));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
