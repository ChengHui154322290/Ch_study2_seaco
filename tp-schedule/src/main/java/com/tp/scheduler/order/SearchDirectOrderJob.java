package com.tp.scheduler.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tp.scheduler.AbstractJobRunnable;
import com.tp.service.ord.directOrder.IDirectOrderService;

@Component
public class SearchDirectOrderJob extends AbstractJobRunnable {

	private static final Logger LOG = LoggerFactory.getLogger(SearchDirectOrderJob.class);
	
	private static final String CURRENT_JOB_PREFIXED = "searchDirectOrderJob";
	
	@Autowired
	private IDirectOrderService directOrderService;
	
	@Override
	public void execute() {
		LOG.info("==========================第三方海外直邮订单 ==获取订单状态==================================");
		directOrderService.searchDirectOrderForJob();
		LOG.info("==========================第三方海外直邮订单 ==获取订单状态end==================================");
	}

	@Override
	public String getFixed() {
		return CURRENT_JOB_PREFIXED;
	}
	
}
