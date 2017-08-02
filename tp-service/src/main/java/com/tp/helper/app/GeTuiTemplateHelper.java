package com.tp.helper.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.gexin.rp.sdk.base.payload.APNPayload;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.tp.common.vo.app.GetuiConstant;
import com.tp.common.vo.app.PushConstant;
import com.tp.dto.app.PushContentDTO;
import com.tp.model.app.PushInfo;
import com.tp.util.JsonUtil;

/**
 * 推送模版
 * @author zhuss
 * @2016年3月7日 下午6:18:20
 */
public class GeTuiTemplateHelper {

	private static Logger log = LoggerFactory.getLogger(GeTuiTemplateHelper.class);
	
	/**
	 * 透传消息模版
	 * @return
	 */
	public static TransmissionTemplate getTransmissionTemplate(PushInfo pushInfo, String appId, String appKey){
		TransmissionTemplate template = new TransmissionTemplate();
		template.setAppId(appId);
		template.setAppkey(appKey);
		template.setTransmissionType(GetuiConstant.TransmissionType.WAIT);
		String message = convertMessageContent(pushInfo);
		log.info("[透传消息模版 - 推送消息体] = {}",message);	
		// iOS推送
		if (pushInfo.getPlatform().equalsIgnoreCase(PushConstant.PUSH_PLATFORM.IOS.getCode())) {
			PushInfo sendPushInfo = new PushInfo();
			BeanUtils.copyProperties(pushInfo, sendPushInfo);
			sendPushInfo.setContent(message);
			template.setAPNInfo(convertAPNInfo(sendPushInfo));
		}else{
			template.setTransmissionContent(message);
		}
		return template;
	}
	
	/**
	 * 封装ANDROID消息内容
	 * @param pushInfo
	 * @return
	 */
	private static String convertMessageContent(PushInfo pushInfo) {
		PushContentDTO content = new PushContentDTO();
		content.setType(String.valueOf(pushInfo.getPushType()));
		//content.setText(pushInfo.getContent());
		content.setSku(pushInfo.getSku());
		content.setTid(pushInfo.getTid());
		content.setLink(pushInfo.getLink());
		content.setTitle(pushInfo.getTitle());
		content.setPageTitle(pushInfo.getPageTitle());
		return JsonUtil.convertObjToStr(content);
	}
	
	/**
	 * 封装IOS消息内容
	 * @param pushInfo
	 * @return
	 */
	private static APNPayload convertAPNInfo(PushInfo pushInfo){
		APNPayload payload = new APNPayload();
	    payload.setBadge(0);
	    payload.setContentAvailable(1);
	    payload.setSound("default");
	    payload.setAlertMsg(new APNPayload.SimpleAlertMsg(pushInfo.getTitle()));
	    payload.addCustomMsg("content", pushInfo.getContent());
	    // payload.setCategory("$由客户端定义");
	    //字典模式使用下者
	    //payload.setAlertMsg(getDictionaryAlertMsg());
	    return payload;
	}
	
	/*private static APNPayload.DictionaryAlertMsg getDictionaryAlertMsg(){
	    APNPayload.DictionaryAlertMsg alertMsg = new APNPayload.DictionaryAlertMsg();
	    alertMsg.setBody("body");
	    alertMsg.setActionLocKey("ActionLockey");
	    alertMsg.setLocKey("LocKey");
	    alertMsg.addLocArg("loc-args");
	    alertMsg.setLaunchImage("launch-image");
	    // IOS8.2以上版本支持
	    alertMsg.setTitle("Title");
	    alertMsg.setTitleLocKey("TitleLocKey");
	    alertMsg.addTitleLocArg("TitleLocArg");
	    return alertMsg;
	}*/

	/**
	 * @param appTimerPush
	 * @param appId
	 * @param appKey
	 * @return
	 * @throws Exception
	 */                     
	/*public static ITemplate generateTransmissionTemplate(PushMessageDTO appTimerPush, String appId, String appKey) throws Exception {

		TransmissionTemplate template = new TransmissionTemplate();
		template.setAppId(appId);
		template.setAppkey(appKey);
		// iOS推送
		if (PlatformEnum.IOS.name().equalsIgnoreCase(appTimerPush.getPlatform())) {
			template.setTransmissionContent("os-toApp");
			template.setTransmissionType(GetuiConstant.TransmissionType.WAIT);
			template.setPushInfo("", 1, "", "", appTimerPush.getContent(), appTimerPush.getTitle(), "", "");
		} else {
			template.setTransmissionContent(appTimerPush.getContent());
			template.setTransmissionType(GetuiConstant.TransmissionType.WAIT);
		}
		if(logger.isInfoEnabled()){
			logger.info("推送消息体".concat(appTimerPush.getContent()));	
		}
		return template;
		
		
		TransmissionTemplate template = new TransmissionTemplate();
		template.setAppId(appId);
		template.setAppkey(appKey);
		template.setTransmissionType(GetuiConstant.TransmissionType.WAIT);
		String message = PushMessageServcie.generateMessageContent(pushMessage);
		template.setTransmissionContent(message);
		log.info("[透传消息模版 - 推送消息体] = {}",message);	
		// iOS推送
		if (PlatformEnum.IOS.name().equalsIgnoreCase(pushMessage.getPlatform())) {
			try{
				template.setPushInfo("", 1, "", "", message, pushMessage.getTitle(), "", "");
			}catch (Exception e) {
	            log.error("[创建透传模板时发生错误 Exception]", e);
	            throw new AppServiceException(AppPushConstant.APP_CREATE_TEMPLATE_ERROR);
	        }
		}
		return template;
	}*/
}
