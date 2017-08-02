/**
 * NewHeight.com Inc.
 * Copyright (c) 2007-2014 All Rights Reserved.
 */
package com.tp.scheduler.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tp.scheduler.AbstractJobRunnable;
import com.tp.service.ord.local.IKuaidi100LocalService;

/**
 * <pre>
 * 推送快递100平台JOB
 * </pre>
 * 
 * @author szy
 * @time 2015-2-3 下午2:59:55
 */
@Component
public class PushToKuaidi100Job extends AbstractJobRunnable{
	private static final Logger logger = LoggerFactory.getLogger(PushToKuaidi100Job.class);
	private static final String CURRENT_JOB_PREFIXED = "pushExpressToKuaidi100";
	
	@Autowired
	private IKuaidi100LocalService kuaidi100LocalService;
	
	@Override
	public void execute() {
		logger.info("推送快递100平台JOB执行.......");
		try {
			kuaidi100LocalService.pushExpressToKuaidi100();
			logger.info("推送快递100平台JOB执行.......end");
		} catch (Exception e) {
			logger.error("定时推送快递单号到快递100平台异常", e);
		}
	}

	@Override
	public String getFixed() {
		return CURRENT_JOB_PREFIXED;
	}

}
