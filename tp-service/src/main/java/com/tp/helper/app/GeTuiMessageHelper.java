package com.tp.helper.app;

import java.util.ArrayList;
import java.util.List;

import com.gexin.rp.sdk.base.ITemplate;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.base.impl.ListMessage;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.tp.model.app.PushInfo;

/**
 * 个推消息体
 * @author zhuss
 * @2016年3月7日 下午6:18:20
 */
public class GeTuiMessageHelper {

	/**
	 * 创建单体消息
	 * @param template
	 * @return
	 */
	public static SingleMessage getSingleMessage(PushInfo pushInfo,ITemplate template){
		SingleMessage singleMessage = new SingleMessage();
		singleMessage.setData(template);
		singleMessage.setOffline(true);
		singleMessage.setOfflineExpireTime(pushInfo.getActiveTime() * 1000 * 3600);
		singleMessage.setPushNetWorkType(pushInfo.getNetType());
		return singleMessage;
	}
	
	/**
	 * 创建列表推送消息的消息体
	 * @param template
	 * @return
	 */
	public ListMessage getListMessage(PushInfo pushInfo,ITemplate template){
		ListMessage listMessage = new ListMessage();
		return listMessage;
	}
	
	/**
	 * 创建指定应用群推送消息的消息体
	 * @param template
	 * @return
	 */
	public static AppMessage getAppMessage(PushInfo pushInfo,ITemplate template,String appId){
		AppMessage appMessage = new AppMessage();
		appMessage.setData(template);
		appMessage.setOffline(true);
		appMessage.setOfflineExpireTime(pushInfo.getActiveTime() * 1000 * 3600);
		/*List<String> phoneTypeList = new ArrayList<String>();
		phoneTypeList.add(platform.toUpperCase());
		message.setPhoneTypeList(phoneTypeList);*/

		List<String> appIdList = new ArrayList<String>();
		appIdList.add(appId);
		appMessage.setAppIdList(appIdList);
		appMessage.setPushNetWorkType(pushInfo.getNetType());
		return appMessage;
	}
}
