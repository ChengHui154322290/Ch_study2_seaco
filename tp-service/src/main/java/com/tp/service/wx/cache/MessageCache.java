package com.tp.service.wx.cache;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.wx.MessageConstant;
import com.tp.dto.wx.KVDto;
import com.tp.redis.util.JedisCacheUtil;
import com.tp.service.wx.IMessageInfoService;
import com.tp.util.StringUtil;

@Service
public class MessageCache {
	
	private static final Logger log = LoggerFactory.getLogger(MessageCache.class);
	
	public static final String WX_MESSAGE_SUBSCRIBE_KEY = "xg_wx_msg_subscribe";
	public static final String WX_MESSAGE_CLICK_KEY = "xg_wx_msg_click";
	public static final String WX_MESSAGE_OFFLINE_KEY = "xg_wx_msg_offline";
	public static final String WX_MESSAGE_KEYWORD_KEY = "xg_wx_msg_keyword";
	public static final int WX_MESSAGE_LIVE = 365*86400;
	
	@Autowired
	private JedisCacheUtil jedisCacheUtil;
	
	@Autowired
	private IMessageInfoService messageInfoService;
	
	/**
	 * 设置MessageSubscribe
	 * @param token
	 */
	public void setMessageSubscribeCache(String message){
		jedisCacheUtil.setCache(WX_MESSAGE_SUBSCRIBE_KEY, message, WX_MESSAGE_LIVE);
	}
	
	/**
	 * 获取MessageSubscribe对象值
	 * @param key
	 * @return
	 */
	public String getMessageSubscribeCache(){
		String message = (String) jedisCacheUtil.getCache(WX_MESSAGE_SUBSCRIBE_KEY);
		if(StringUtil.isBlank(message)){
			message = messageInfoService.getTriggerMsg(MessageConstant.SCENE.SUBSCRIBE.getCode(), null);
			setMessageSubscribeCache(message);
		}
		log.info("[微信 - 获取关注消息 = {}]",message);
		return message;
	}
	
	/**
	 * 设置MessageClick
	 * @param token
	 */
	public void setMessageClickCache(String message,String key){
		jedisCacheUtil.setCache(WX_MESSAGE_CLICK_KEY+key, message, WX_MESSAGE_LIVE);
	}
	
	/**
	 * 获取MessageClick对象值
	 * @param key
	 * @return
	 */
	public String getMessageClickCache(String key){
		String message = (String) jedisCacheUtil.getCache(WX_MESSAGE_CLICK_KEY+key);
		if(StringUtil.isBlank(message)){
			message = messageInfoService.getTriggerMsg(MessageConstant.SCENE.CLICK.getCode(), key);
			setMessageClickCache(message,key);
		}
		return message;
	}
	
	/**
	 * 设置MessageOffline
	 * @param token
	 */
	public void setMessageOfflineCache(String message){
		jedisCacheUtil.setCache(WX_MESSAGE_OFFLINE_KEY, message, WX_MESSAGE_LIVE);
	}
	
	/**
	 * 获取MessageOffline对象值
	 * @param key
	 * @return
	 */
	public String getMessageOfflineCache(){
		String message = (String) jedisCacheUtil.getCache(WX_MESSAGE_OFFLINE_KEY);
		if(StringUtil.isBlank(message)){
			message = messageInfoService.getTriggerMsg(MessageConstant.SCENE.OFFLINE.getCode(), null);
			setMessageOfflineCache(message);
		}
		return message;
	}
	
	/**
	 * 设置Keyword
	 * @param token
	 */
	public void setMessageKeywordCache(List<KVDto> l){
		jedisCacheUtil.setCache(WX_MESSAGE_KEYWORD_KEY, l, WX_MESSAGE_LIVE);
	}
	
	/**
	 * 获取Keyword对象值
	 * @param key
	 * @return
	 */
	public List<KVDto> getMessageKeywordCache(){
		@SuppressWarnings("unchecked")
		List<KVDto> l =  (List<KVDto>) jedisCacheUtil.getCache(WX_MESSAGE_KEYWORD_KEY);
		if(CollectionUtils.isEmpty(l)){
			l = messageInfoService.getKeywordMsg();
			setMessageKeywordCache(l);
		}
		return l;
	}
	
	public void updateMessageCache(String code,String key,String newMessage){
		if(StringUtil.equalsIgnoreCase(code, MessageConstant.SCENE.SUBSCRIBE.getCode())){
			setMessageSubscribeCache(newMessage);
		}else if(StringUtil.equalsIgnoreCase(code, MessageConstant.SCENE.CLICK.getCode()) && StringUtil.isNotBlank(key)){
			setMessageClickCache(newMessage,key);
		}else if(StringUtil.equalsIgnoreCase(code, MessageConstant.SCENE.OFFLINE.getCode())){
			setMessageOfflineCache(newMessage);
		}else if(StringUtil.equalsIgnoreCase(code, MessageConstant.SCENE.KEYWORD.getCode())){
			setMessageKeywordCache(new ArrayList<>());
		}
	}
	
	public void delMessageCache(String code,String key){
		if(StringUtil.equalsIgnoreCase(code, MessageConstant.SCENE.SUBSCRIBE.getCode())){
			setMessageSubscribeCache(StringUtil.EMPTY);
		}else if(StringUtil.equalsIgnoreCase(code, MessageConstant.SCENE.CLICK.getCode()) && StringUtil.isNotBlank(key)){
			setMessageClickCache(StringUtil.EMPTY,key);
		}else if(StringUtil.equalsIgnoreCase(code, MessageConstant.SCENE.OFFLINE.getCode())){
			setMessageOfflineCache(StringUtil.EMPTY);
		}else if(StringUtil.equalsIgnoreCase(code, MessageConstant.SCENE.KEYWORD.getCode())){
			setMessageKeywordCache(new ArrayList<>());
		}
	}
}
