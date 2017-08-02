package com.tp.test.app;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.base.payload.APNPayload;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.LinkTemplate;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.tp.model.app.PushInfo;
import com.tp.service.app.IPushInfoService;
import com.tp.test.BaseTest;

/**
 * APP推送消息单元测试
 * @author zhuss
 * @2016年2月24日 下午6:46:06
 */
public class AppPushTest extends BaseTest{

	@Autowired
	IPushInfoService appPushInfoService;
	
	@Value("${xg.apppush.ios.host}")
	private String andriodHost;
	@Value("${xg.apppush.ios.appkey}")
	private String andriodAppkey;
	@Value("${xg.apppush.ios.appId}")
	private String andriodAppId;
	@Value("${xg.apppush.ios.master}")
	private String andriodMaster;
	 
	@Test
	public void query() {
		PushInfo query = new PushInfo();
		List<PushInfo> list = appPushInfoService.queryByObject(query);
		for(PushInfo pi : list){
			System.out.println(pi.getId()+":"+pi.getTitle());
		}
	}
	
	@Test
	public void sendMsg() throws Exception{
		// 新建一个IGtPush实例，传入调用接口网址，appkey和masterSecret
        IGtPush push = new IGtPush(andriodHost, andriodAppkey, andriodMaster);
        push.connect();
        // 新建一个消息类型，根据app推送的话要使用AppMessage
        AppMessage message = new AppMessage();
        // 新建一个推送模版, 以链接模板为例子，就是说在通知栏显示一条含图标、标题等的通知，用户点击可打开您指定的网页
        TransmissionTemplate template = new TransmissionTemplate();
        //template.setPushInfo("", 1, "hello", "default", "deoadlfllaoeldll", "233", "", "");
        //template.setPushInfo(actionLocKey, badge, message, sound, payload, locKey, locArgs, launchImage);
        APNPayload payload = new APNPayload();
	    payload.setBadge(1);
	    payload.setContentAvailable(1);
	    payload.setSound("default");
	    payload.setCategory("xoxoxoxxoxoxo");
	    payload.setAlertMsg(new APNPayload.SimpleAlertMsg("hello"));
	   /* payload.addCustomMsg("message", "mmmmmmmssssssage");
	    payload.addCustomMsg("loc-key", "loololckey");
	    payload.addCustomMsg("badge", 1);*/
        template.setAPNInfo(payload);
        template.setAppId(andriodAppId);
        template.setAppkey(andriodAppkey);
        List<String> appIds = new ArrayList<String>();
        appIds.add(andriodAppId);
        // 模板设置好后塞进message里并设置，同时设置推送的app id,还可以配置这条message是否支持离线，以及过期时间等
        message.setData(template);
        message.setAppIdList(appIds);
        message.setOffline(true);
        message.setOfflineExpireTime(1000 * 600);
        // 调用IGtPush实例的pushMessageToApp接口，参数就是上面我们配置的message
        IPushResult ret = push.pushMessageToApp(message);
        System.out.println(ret.getResponse().toString());
	}
	
	/*@Test
	public void sendIosMsg(){
		try{
			 指定服务器信息   
		       AppleNotificationServer customServer = new AppleNotificationServerBasicImpl("D:/PushDev.p12", "111111", ConnectionToAppleServer.KEYSTORE_TYPE_PKCS12, "gateway.sandbox.push.apple.com", 2195); 

		        创建一个简单的payload   
		        PushNotificationPayload payload = PushNotificationPayload.alert("Hello World!"); 
		  
		        创建一个 push notification manager   
		        PushNotificationManager pushManager = new PushNotificationManager(); 

		        初始化连接   
		        pushManager.initializeConnection(customServer); 
		  
		        推送消息并获得结果  
		        List<Device> devices = new ArrayList<>();
		        List<PushedNotification> notifications = pushManager.sendNotifications(payload, devices); 
		        List<PushedNotification> failedNotifications = PushedNotification.findFailedNotifications(notifications);
		        List<PushedNotification> successfulNotifications = PushedNotification.findSuccessfulNotifications(notifications);
		        int failed = failedNotifications.size();
		        int successful = successfulNotifications.size();
		        System.out.println("failed size :"+failed + "success size :"+successful);
		        pushManager.stopConnection();
		}catch(Exception e){
			e.printStackTrace();
		}
	}*/
}
