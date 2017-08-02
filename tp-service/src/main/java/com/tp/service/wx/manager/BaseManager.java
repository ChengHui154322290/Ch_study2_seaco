package com.tp.service.wx.manager;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.tp.exception.ServiceException;

/**
 * 基础管理
 * @author zhuss
 * @2016年4月27日 下午3:02:38
 */
public class BaseManager {
	private static final Logger log = LoggerFactory.getLogger(BaseManager.class);
	
	public static void handleError(JSONObject jsonObject,String apiName){
		if(jsonObject.containsKey("errmsg")){
			String message = jsonObject.getString("errmsg");
			Integer errcode = jsonObject.getInteger("errcode")==null?0:jsonObject.getInteger("errcode");
			if(!message.equals("ok") && errcode != 0){
				log.error("[调用微信API - {} ERROR] = {}",apiName,message);
				throw new ServiceException(message);
			}
		}
	}
}
