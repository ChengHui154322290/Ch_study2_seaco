package com.tp.service.app;

import com.tp.model.app.PushInfo;
import com.tp.service.IBaseService;

/**
 * APP推送消息接口
 * @author zhuss
 * @2016年2月24日 下午6:45:37
 */
public interface IPushInfoService extends IBaseService<PushInfo>{
	
	/**
	 * 推送消息
	 * @param pushMessage
	 * @return
	 */
	boolean sendMessage(PushInfo pushInfo);
	
	/**
	 * 发送定时个推
	 */
	public void sendTimerMessage(Integer interval);
	
}
