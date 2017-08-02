package com.tp.scheduler.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tp.redis.util.JedisCacheUtil;
import com.tp.scheduler.SynchronizedJob;
import com.tp.service.pay.IPaymentService;

/**
 * 检查订单是否已完成支付
 * 
 * @author szy
 *
 */
@Component
public class PaymentStatusCheckJob  extends SynchronizedJob{
	
	@Autowired
	IPaymentService paymentService;
	@Autowired
	JedisCacheUtil jedisCacheUtil;
	
	@Override
	protected void jobImpl() {
		try {
			logger.info("检查订单是否已完成支付 任务开始...");
			paymentService.checkPaymentStatus();
			logger.info("检查订单是否已完成支付　任务完成");
		} catch (Exception e) {
			logger.error("库存快照备份失败 {}",e.getMessage());
		}
	}
}
