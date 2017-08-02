package com.tp.m.ao.wx;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.dto.wx.message.resp.BaseRespMessage;
import com.tp.m.helper.RequestHelper;
import com.tp.m.helper.WXMessageHelper;
import com.tp.m.util.StringUtil;

/**
 * 核心管理器
 * @author zhuss
 *
 */
@Service
public class CoreAO {

	private static Logger log = LoggerFactory.getLogger(CoreAO.class);
	
	@Autowired
	private EventAO eventAO;
	
	@Autowired
	private MessageAO messageAO;
	
	/**
	 * 处理微信发来的请求
	 * 
	 * @param request
	 * @return
	 */
	public String processRequest(HttpServletRequest request) {
		String reqMessage = "";
		String respMessage = "";
		try {
			Map<String, String> requestMap = WXMessageHelper.parseXml(request);
			requestMap.put("ip", RequestHelper.getIpAddr(request));
			log.info("[微信发来的请求数据] = {}",requestMap);
			String fromUserName = requestMap.get("FromUserName");
			String toUserName = requestMap.get("ToUserName");
			Long createTime = Long.valueOf(requestMap.get("CreateTime"));
			String msgType = requestMap.get("MsgType");
			// 接收事件推送
			if (StringUtil.equals(msgType, WXMessageHelper.REQ_MESSAGE_TYPE_EVENT)) {
				createTime = new Date().getTime();
				//回复的消息from和to   和接收消息的from和to正好相反
				BaseRespMessage baseMessage = new BaseRespMessage(fromUserName,toUserName,createTime/1000);
				respMessage = receiveEvent(baseMessage,requestMap);
			}else { // 接收普通消息 
				createTime = new Date().getTime();
				BaseRespMessage baseMessage = new BaseRespMessage(fromUserName,toUserName,createTime/1000);
				respMessage =receiveCommonMessage(msgType,baseMessage,requestMap);
			}
		} catch (Exception e) {
			log.error("XML消息对象 = {}", reqMessage);
			log.error("[处理用户向公众号发送消息 error] = {}", e);
		}
		return respMessage;
	}
	
	/**
	 * 接收事件推送
	 */
	public String receiveEvent(BaseRespMessage baseMessage,Map<String, String> requestMap){
		String respMessage = "";
		String eventType = requestMap.get("Event");
		String fromUserName = requestMap.get("FromUserName");
		String eventKey = requestMap.get("EventKey");
		String ip = requestMap.get("ip");
		if(StringUtil.isNotBlank(eventType)){
			if(StringUtil.equals(eventType,WXMessageHelper.EVENT_TYPE_SUBSCRIBE)){//关注事件欢迎语
				respMessage = eventAO.subcribeEvent(baseMessage, eventKey,fromUserName,ip);
			}else if(StringUtil.equals(eventType,WXMessageHelper.EVENT_TYPE_SCAN)){//用户已关注时的扫描带参数二维码
				respMessage = eventAO.scanEvent(baseMessage, eventKey,fromUserName,ip);
			}else if(StringUtil.equals(eventType,WXMessageHelper.EVENT_TYPE_CLICK)){//自定义菜单点击事件
				respMessage = eventAO.clickEvent(baseMessage, eventKey);
			}
		}
		log.info("接收事件推送 = {}",respMessage);
		return respMessage;
	}
	
	/**
	 * 接收普通消息
	 */
	public String receiveCommonMessage(String msgType , BaseRespMessage baseMessage,Map<String, String> requestMap){
		String respMessage = "";
		String content = requestMap.get("Content");
		if(StringUtil.isNotBlank(msgType) && StringUtil.isNotBlank(content)){
			if (msgType.equals(WXMessageHelper.REQ_MESSAGE_TYPE_TEXT)) {// 处理微信发过来的文本消息
				//关键字回复过滤处理
				respMessage = messageAO.keywordFilter(baseMessage, content);
			} 
			//如果消息没被处理默认转到多客服系统
			if(StringUtil.isBlank(respMessage))respMessage = messageAO.forwardDKF(baseMessage);
		}
		return respMessage;
	}
}
