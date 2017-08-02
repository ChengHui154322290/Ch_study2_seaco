/**
 * 
 */
package com.tp.scheduler.ord.customs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tp.scheduler.AbstractJobRunnable;
import com.tp.service.ord.ISubOrderService;
import com.tp.service.ord.customs.ISeaCustomsDeliveryMonitorService;

/**
 * @author Administrator
 * 查询通关状态
 */
@Component
public class CustomsOrderDeliveryMonitorJob extends AbstractJobRunnable{

	private static final Logger logger = LoggerFactory.getLogger(CustomsOrderDeliveryMonitorJob.class);
	
	private static final String CURRENT_JOB_PREFIXED = "customsOrderDeliveryMonitorJob";
	
	@Autowired
	private ISeaCustomsDeliveryMonitorService monitorService;
	
	@Override
	public void execute() {
		try{
			monitorService.sendCustomsClearanceReport();
		}catch(Exception e){
			logger.error("发送清单报告异常", e);
		}
	}

	@Override
	public String getFixed() {
		return CURRENT_JOB_PREFIXED;
	}
}
