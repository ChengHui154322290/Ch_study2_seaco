package com.tp.service.app;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.ITemplate;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.http.IGtPush;
import com.tp.common.vo.app.PushConstant;
import com.tp.exception.AppServiceException;
import com.tp.helper.app.GeTuiMessageHelper;
import com.tp.helper.app.GeTuiTemplateHelper;
import com.tp.model.app.PushInfo;
import com.tp.util.JsonUtil;
import com.tp.util.StringUtil;

/**
 * 推送消息
 * @author zhuss
 * @2016年3月8日 下午7:07:39
 */
@Service
public class GeTuiService {
	private static Logger log = LoggerFactory.getLogger(GeTuiService.class);
	
	@Value("${xg.apppush.andriod.host}")
	private String andriodHost;
	@Value("${xg.apppush.andriod.appkey}")
	private String andriodAppkey;
	@Value("${xg.apppush.andriod.appId}")
	private String andriodAppId;
	@Value("${xg.apppush.andriod.master}")
	private String andriodMaster;
	
	@Value("${xg.apppush.ios.host}")
	private String IOSHost;
	@Value("${xg.apppush.ios.appkey}")
	private String IOSAppkey;
	@Value("${xg.apppush.ios.appId}")
	private String IOSAppId;
	@Value("${xg.apppush.ios.master}")
	private String IOSMaster;
	
	private String host;
	private String appId;
	private String appKey;
	private String master;
	
	private void init(String platform){
		if(platform.equalsIgnoreCase(PushConstant.PUSH_PLATFORM.IOS.getCode())){
			this.host =  IOSHost;
			this.appId = IOSAppId;
			this.appKey = IOSAppkey;
			this.master = IOSMaster;
		}else{
			this.host =  andriodHost;
			this.appId = andriodAppId;
			this.appKey = andriodAppkey;
			this.master = andriodMaster;
		}
	}

	/**
	 * 推送消息
	 * @param pushMessage
	 * @param platform：ios,android:app
	 * @return
	 */
	public IPushResult pushMessage(PushInfo pushInfo){
		IPushResult ret = null;
		if(pushInfo.getPushTarget().equals(PushConstant.PUSH_TARGET.SINGLE.getCode())){//执行单体发送
			if(StringUtil.isEmpty(pushInfo.getClientId()))throw new AppServiceException(PushConstant.ERROR_CODE.PUSH_CLIENTID_NULL);
			ret = pushSingleMessage(pushInfo,pushInfo.getPlatform());
		}else{//执行APP全局推送
			ret = pushAppMessage(pushInfo, pushInfo.getPlatform());	
		}
		// 返回结果
		if (ret != null && ret.getResponse().toString().contains("result=ok")){
			return ret;
		}
		return ret;
	}
	
	/**
	 * 单体推送
	 * @param messageTO
	 * @return
	 */
	private IPushResult pushSingleMessage(PushInfo pushInfo,String platform) {
		log.info("**************** 执行单体推送消息 开始 *****************");
		if(platform.equalsIgnoreCase(PushConstant.PUSH_PLATFORM.APP.getCode())){//如果推送消息为APP
			IPushResult androidRet = pushSingleMessage(pushInfo,PushConstant.PUSH_PLATFORM.ANDROID.getCode());
			if(androidRet != null && androidRet.getResponse().toString().contains("result=ok"))
				return pushSingleMessage(pushInfo,PushConstant.PUSH_PLATFORM.IOS.getCode());
			return androidRet;
		}
		init(platform);
		IGtPush push =connect();// 建立连接
		ITemplate template = GeTuiTemplateHelper.getTransmissionTemplate(pushInfo, appId, appKey);
		SingleMessage message = GeTuiMessageHelper.getSingleMessage(pushInfo,template);
		IPushResult ret = null;
		if(platform.equalsIgnoreCase(PushConstant.PUSH_PLATFORM.IOS.getCode())){//iOS APN 推送
			ret = push.pushAPNMessageToSingle(appId, pushInfo.getClientId(), message);
		}else if(platform.equalsIgnoreCase(PushConstant.PUSH_PLATFORM.ANDROID.getCode())){//Android 推送
			Target target = new Target();
			target.setAppId(appId);
			target.setClientId(pushInfo.getClientId());
			ret = push.pushMessageToSingle(message, target);
		}else throw new AppServiceException(PushConstant.ERROR_CODE.PUSH_TYPE_NULL);
		log.info("单体推送结果 = {}",JsonUtil.convertObjToStr(ret));
		log.info("**************** 执行单体推送消息 完成 *****************");
		return ret;
	}
	
	/**
	 * App定向推送
	 * @param messageTO
	 * @return
	 */
	private IPushResult pushAppMessage(PushInfo pushInfo, String platform) {
		log.info("**************** 执行指定应用群推送消息 开始 *****************");
		String oldPlatform = pushInfo.getPlatform();
		if(platform.equalsIgnoreCase(PushConstant.PUSH_PLATFORM.APP.getCode())){//如果推送消息为APP
			IPushResult androidRet = pushAppMessage(pushInfo,PushConstant.PUSH_PLATFORM.ANDROID.getCode());
			if(androidRet != null && androidRet.getResponse().toString().contains("result=ok"))
				return pushAppMessage(pushInfo,PushConstant.PUSH_PLATFORM.IOS.getCode());
			return androidRet;
		}
		init(platform);
		IGtPush push = connect();// 建立连接
		pushInfo.setPlatform(platform);
		ITemplate template = GeTuiTemplateHelper.getTransmissionTemplate(pushInfo, appId, appKey);
		AppMessage message = GeTuiMessageHelper.getAppMessage(pushInfo, template, appId);
		IPushResult ret = push.pushMessageToApp(message);
		log.info("全体推送结果 = {}",JsonUtil.convertObjToStr(ret));
		log.info("**************** 执行指定应用群推送消息 完成 *****************");
		pushInfo.setPlatform(oldPlatform);
		return ret;
	}
	
	/**
	 * 建立连接
	 * @param platform
	 * @return
	 */
	private IGtPush connect(){
		try{
			IGtPush push = new IGtPush(host, appKey, master);
			push.connect();// 建立连接
			return push;
		}catch (IOException e) {
			log.error("[个推服务器连接 - IOException] = {}",e);
			throw new AppServiceException(PushConstant.ERROR_CODE.APP_PUSH_CONNECT_ERROR);
		}
	}
}
