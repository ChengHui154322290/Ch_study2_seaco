package com.tp.scheduler.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tp.redis.util.JedisCacheUtil;
import com.tp.scheduler.SynchronizedJob;
import com.tp.service.pay.IPaymentService;

/**
 * 检查退款是否已完成
 * 
 * @author szy
 *
 */
@Component
public class RefundStatusCheckJob  extends SynchronizedJob{
	
	@Autowired
	IPaymentService paymentService;
	@Autowired
	JedisCacheUtil jedisCacheUtil;
	
	private static final String JOB_KEY = "TASK_PAYMENT_CHECK_REFUND";//每个定时任务一个key
	
	@Override
	protected void jobImpl() {
		boolean lock = jedisCacheUtil.lock(JOB_KEY);// 获得锁
		if(!lock){
			return;
		}
		try {
			logger.info("检查退款是否已完成 任务开始...");
			paymentService.checkRefundStatus();
			logger.info("检查退款是否已完成 任务完成");
		} catch (Exception e) {
			logger.error("库存快照备份失败 {}",e.getMessage());
		} finally {
			if (lock) {
				jedisCacheUtil.unLock(JOB_KEY);// 释放锁
			}
		}
	
	
	}
}
