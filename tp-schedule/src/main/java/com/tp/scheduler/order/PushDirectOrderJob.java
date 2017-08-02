package com.tp.scheduler.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tp.scheduler.AbstractJobRunnable;
import com.tp.service.ord.directOrder.IDirectOrderService;

@Component
public class PushDirectOrderJob extends AbstractJobRunnable {

	private static final Logger LOG = LoggerFactory.getLogger(PushDirectOrderJob.class);
	
	private static final String CURRENT_JOB_PREFIXED = "pushDirectOrderJob";
	
	@Autowired
	private IDirectOrderService directOrderService;
	
	@Override
	public void execute() {
		LOG.info("==========================第三方海外直邮订单 ==推送海外直邮订单 start==================================");
		directOrderService.pushDirectOrderForJob();
		LOG.info("==========================第三方海外直邮订单 ==推送海外直邮订单 end==================================");
	}

	@Override
	public String getFixed() {
		return CURRENT_JOB_PREFIXED;
	}

	
	
}
