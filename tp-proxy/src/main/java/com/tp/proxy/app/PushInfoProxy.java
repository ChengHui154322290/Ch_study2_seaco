package com.tp.proxy.app;


import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.app.PushConstant;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.exception.AppServiceException;
import com.tp.model.app.PushInfo;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.app.IPushInfoService;
import com.tp.util.DateUtil;

/**
 * APP推送消息业务层
 * @author zhuss
 * @2016年2月24日 下午5:45:38
 */
@Service
public class PushInfoProxy extends BaseProxy<PushInfo>{
	private static Logger log = LoggerFactory.getLogger(PushInfoProxy.class);

	@Autowired
	private IPushInfoService pushInfoService;
	
	@Override
	public IBaseService<PushInfo> getService() {
		return pushInfoService;
	}

	/**
	 * 推送消息
	 * @param pushMessage
	 * @return
	 */
	public ResultInfo<String> sendMessage(PushInfo pushInfo){
		try{
			boolean result = pushInfoService.sendMessage(pushInfo);
			if(result)return new ResultInfo<>(PushConstant.ERROR_CODE.SUCCESS.message);
			return new ResultInfo<>(new FailInfo(PushConstant.ERROR_CODE.FAILED.message));
		} catch(AppServiceException ex){
			log.error("[APP个推 - 推送消息  AppServiceException]",ex.getMessage());
			return new ResultInfo<>(new FailInfo(ex.getMessage()));
		}catch(Exception e){
			log.error("[APP个推 - 推送消息  Exception]",e);
			return new ResultInfo<>(new FailInfo("推送失败"));
		}
	}

	/**
	 * 定时推送消息
	 * @param pushMessage
	 * @return
	 */
	public ResultInfo<Boolean> sendTimerMessage(Integer interval){
		try{
			pushInfoService.sendTimerMessage(interval);
			return new ResultInfo<>(Boolean.TRUE);
		} catch(AppServiceException ex){
			log.error("[APP个推 - 推送消息  AppServiceException]",ex.getMessage());
			return new ResultInfo<>(new FailInfo(ex.getMessage()));
		}catch(Exception e){
			log.error("[APP个推 - 推送消息  Exception]",e);
			return new ResultInfo<>(new FailInfo("推送失败"));
		}
	}
	
}
