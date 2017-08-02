package com.tp.shop.convert;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.tp.dto.wx.message.resp.Article;
import com.tp.dto.wx.message.resp.ArticleRespMessage;
import com.tp.dto.wx.message.resp.BaseRespMessage;
import com.tp.dto.wx.message.resp.Image;
import com.tp.dto.wx.message.resp.ImageRespMessage;
import com.tp.dto.wx.message.resp.TextRespMessage;
import com.tp.dto.wx.message.resp.Voice;
import com.tp.dto.wx.message.resp.VoiceRespMessage;
import com.tp.m.util.StringUtil;
import com.tp.shop.helper.WXMessageHelper;

public class WXMessageConvert {

	/**
	 * 封装响应Text类型数据
	 * @param baseMessage
	 * @param content
	 * @return
	 */
	public static String convertTextRespMessage(BaseRespMessage baseMessage,String content){
		if(StringUtil.isBlank(content)) return StringUtil.EMPTY;
		TextRespMessage textMessage = new TextRespMessage(baseMessage);
		textMessage.setContent(content);	
		textMessage.setMsgType(WXMessageHelper.RESP_MESSAGE_TYPE_TEXT);
		return WXMessageHelper.messageToXml(textMessage);
	}
	
	/**
	 * 封装响应图片类型数据
	 * @param baseMessage
	 * @param MediaId：通过素材管理接口上传多媒体文件，得到的id
	 * @return
	 */
	public static String convertImageRespMessage(BaseRespMessage baseMessage,String MediaId){
		if(StringUtil.isBlank(MediaId)) return StringUtil.EMPTY;
		ImageRespMessage imageMessage = new ImageRespMessage(baseMessage);
		imageMessage.setImage(new Image(MediaId));
		imageMessage.setMsgType(WXMessageHelper.RESP_MESSAGE_TYPE_IMAGE);
		return WXMessageHelper.messageToXml(imageMessage);
	}
	
	/**
	 * 封装响应语音类型数据
	 * @param baseMessage
	 * @param MediaId：通过素材管理接口上传多媒体文件，得到的id
	 * @return
	 */
	public static String convertVoiceRespMessage(BaseRespMessage baseMessage,String MediaId){
		if(StringUtil.isBlank(MediaId)) return StringUtil.EMPTY;
		VoiceRespMessage voiceMessage = new VoiceRespMessage(baseMessage);
		voiceMessage.setVoice(new Voice(MediaId));
		voiceMessage.setMsgType(WXMessageHelper.RESP_MESSAGE_TYPE_VOICE);
		return WXMessageHelper.messageToXml(voiceMessage);
	}
	
	/**
	 * 封装响应图文类型数据
	 * @param baseMessage
	 * @param content
	 * @return
	 */
	public static String convertNewsRespMessage(BaseRespMessage baseMessage,List<Article> articles){
		if(CollectionUtils.isEmpty(articles)) return StringUtil.EMPTY;
		ArticleRespMessage newsRespMessage = new ArticleRespMessage(baseMessage);
		newsRespMessage.setArticleCount(articles.size());
		newsRespMessage.setArticles(articles);
		newsRespMessage.setMsgType(WXMessageHelper.RESP_MESSAGE_TYPE_NEWS);
		return WXMessageHelper.messageToXml(newsRespMessage);
	}
}
