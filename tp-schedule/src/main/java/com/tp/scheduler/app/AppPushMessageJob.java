package com.tp.scheduler.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tp.proxy.app.PushInfoProxy;
import com.tp.scheduler.AbstractJobRunnable;

/**
 * app定时推送消息
 * @author szy
 *
 */
@Component
public class AppPushMessageJob extends AbstractJobRunnable {
	private static final Logger LOG = LoggerFactory.getLogger(AppPushMessageJob.class);
	@Autowired
	private PushInfoProxy pushInfoProxy;
	
	@Override
	public void execute() {
		LOG.info("定时推送任务启动..");
		pushInfoProxy.sendTimerMessage(60);
	}

	@Override
	public String getFixed() {
		return "AppPushMessageJob";
	}

}
