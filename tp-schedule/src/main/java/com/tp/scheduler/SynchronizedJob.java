package com.tp.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 只允许一个线程在跑的定时任务继承此类
 * 
 * @author szy
 *
 */
public abstract class SynchronizedJob {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	private boolean isRunning = false;
	
	abstract protected void jobImpl();
	
	final public void execute() {
		try {
			synchronized (this) {
				if (isRunning) {
					logger.warn("job is already running....");
					return ;
				}
				
				isRunning=true;
			}
			
			logger.info("start to run job ....");
			jobImpl();
			logger.info("job has run!");
		} catch (Exception e) {
			logger.error("定时任务发生错误", e);
		} finally {
			isRunning=false;
		}

	}

}
