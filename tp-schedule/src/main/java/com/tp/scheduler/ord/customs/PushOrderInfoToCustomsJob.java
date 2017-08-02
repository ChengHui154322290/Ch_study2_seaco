package com.tp.scheduler.ord.customs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tp.scheduler.AbstractJobRunnable;
import com.tp.service.ord.customs.ISeaOrderDeliveryLocalService;

/**
 * 订单推送海关 
 */
@Component
public class PushOrderInfoToCustomsJob extends AbstractJobRunnable{

	private static final Logger logger = LoggerFactory.getLogger(PushOrderInfoToCustomsJob.class);
	
	private static final String CURRENT_JOB_PREFIXED = "pushOrderInfoToCustoms";
	
	@Autowired
	private ISeaOrderDeliveryLocalService seaOrderDeliveryLocalService;
	
	@Override
	public void execute() {
		logger.info("[申报海关 - 推送数据job启动...]");
		try {
			seaOrderDeliveryLocalService.declareSeaOrderToCustoms();
		} catch (Exception e) {
			logger.error("[申报海关 - 推送数据异常]：", e);
		}
		logger.info("[申报海关 - 推送数据job结束]");
	}

	@Override
	public String getFixed() {
		return CURRENT_JOB_PREFIXED;
	}

}
