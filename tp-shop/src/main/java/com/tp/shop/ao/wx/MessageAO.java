package com.tp.shop.ao.wx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.wx.MessageConstant;
import com.tp.dto.wx.message.resp.BaseRespMessage;
import com.tp.m.util.StringUtil;
import com.tp.proxy.wx.MessageInfoProxy;
import com.tp.shop.convert.WXMessageConvert;
import com.tp.shop.helper.WXMessageHelper;

/**
 * 消息管理业务层
 * @author Administrator
 *
 */
@Service
public class MessageAO {

	private static Logger log = LoggerFactory.getLogger(MessageAO.class);
	
	@Autowired
	private MessageInfoProxy messageInfoProxy;
	
	/**
	 * 处理关键字回复
	 * @return
	 */
	public String keywordFilter(BaseRespMessage baseMessage,String content){
		String respMessage = "";
		String message = messageInfoProxy.getMessage(MessageConstant.SCENE.KEYWORD.getCode(), content.trim());
		if(StringUtil.isNotBlank(message)) respMessage = WXMessageConvert.convertTextRespMessage(baseMessage, message);
		log.info("[关键字回复  content = {} respMessage = {}]",content,respMessage);
		return respMessage;
	}
	
	/**
	 * 转到多客服系统
	 */
	public String forwardDKF(BaseRespMessage baseMessage){
		baseMessage.setMsgType(WXMessageHelper.RESP_DKF_TYPE);
		String message = WXMessageHelper.resqMessageToXml(baseMessage);
		log.info("[微信 - 获取消息转发到多客服系统] = {}",message);
		return message;
	}
}
