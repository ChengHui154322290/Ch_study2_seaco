package com.tp.world.ao.wx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.DssConstant;
import com.tp.common.vo.QrcodeConstant;
import com.tp.common.vo.mem.MemberUnionType;
import com.tp.common.vo.wx.MessageConstant;
import com.tp.dto.wx.message.resp.BaseRespMessage;
import com.tp.enums.common.PlatformEnum;
import com.tp.m.query.user.QueryUser;
import com.tp.m.util.StringUtil;
import com.tp.model.dss.PromoterInfo;
import com.tp.proxy.dss.PromoterInfoProxy;
import com.tp.proxy.mem.MemberInfoProxy;
import com.tp.proxy.wx.MessageInfoProxy;
import com.tp.world.ao.mkt.ChannelPromoteAO;
import com.tp.world.convert.UserConvert;
import com.tp.world.convert.WXMessageConvert;
import com.tp.world.helper.WXMessageHelper;
import com.tp.world.helper.cache.ActivityCacheHelper;

@Service
public class EventAO {
	
	@Autowired
	private MessageInfoProxy messageInfoProxy;
	
	@Autowired
	private ChannelPromoteAO channelPromoteAO;
	
	@Autowired
	private ActivityCacheHelper activityCacheHelper;
	
	@Autowired
	private MemberInfoProxy memberInfoProxy;
	
	@Autowired
	private PromoterInfoProxy promoterInfoProxy;
	
	/**
	 * 关注事件
	 * @param baseMessage
	 * @param eventKey
	 * @return
	 */
	public String subcribeEvent(BaseRespMessage baseMessage,String eventKey,String formUserName,String ip){
		String respMessage = "";
		if(StringUtil.equals(getPrefix(eventKey), QrcodeConstant.QRCODE)){//通过二维码扫进来的
			eventKey = eventKey.replace(QrcodeConstant.QRCODE, StringUtil.EMPTY).trim();
			String msg = handleChannelPromoter(eventKey,formUserName,ip);
			if(StringUtil.isNotBlank(msg))return WXMessageConvert.convertTextRespMessage(baseMessage, msg);
		}
		String message = messageInfoProxy.getMessage(WXMessageHelper.EVENT_TYPE_SUBSCRIBE,null);
		if(StringUtil.isNotBlank(message))respMessage = WXMessageConvert.convertTextRespMessage(baseMessage, message);
		return respMessage;
	}
	
	/**
	 * 已关注用户扫码事件
	 * @param baseMessage
	 * @param eventKey
	 * @return
	 */
	public String scanEvent(BaseRespMessage baseMessage,String eventKey,String formUserName,String ip){
		String msg = handleChannelPromoter(eventKey,formUserName,ip);
		return WXMessageConvert.convertTextRespMessage(baseMessage, msg);
	}
	
	/**
	 * 菜单事件
	 * @param baseMessage
	 * @param eventKey
	 * @return
	 */
	public String clickEvent(BaseRespMessage baseMessage,String eventKey){
		String respMessage = "";
		// 事件KEY值，与创建菜单时的key值对应
		String message = messageInfoProxy.getMessage(WXMessageHelper.EVENT_TYPE_CLICK,eventKey);
		if(StringUtil.isNotBlank(message))respMessage = WXMessageConvert.convertTextRespMessage(baseMessage, message);
		return respMessage;
	}
	
	/**
	 * 处理线下的渠道推广:冗余zhonglai
	 */
	private String handleChannelPromoter(String eventKey,String fromUserName,String ip){
		if(StringUtil.equals(getPrefix(eventKey), QrcodeConstant.QRCODE_CHANNEL)){ //普通线下推广
			eventKey = eventKey.replace(QrcodeConstant.QRCODE_CHANNEL, StringUtil.EMPTY).trim();
			channelPromoteAO.saveChannelPromote(eventKey.split("_")[0], fromUserName, eventKey.split("_")[1]);
			return handleMessage(eventKey.split("_")[0]); 
		}else if(StringUtil.equals(getPrefix(eventKey), QrcodeConstant.QRCODE_PROMOTER_USER_CODE)){ //扫码推广员
			eventKey = eventKey.replace(QrcodeConstant.QRCODE_PROMOTER_USER_CODE, StringUtil.EMPTY).trim();
			String channel = eventKey.split("_")[0];
			String type = eventKey.split("_")[1];
			channelPromoteAO.saveChannelPromote(channel, fromUserName, type);
			//修改用户表数据
			PromoterInfo pi = new PromoterInfo();
			pi.setMobile(channel);
			pi.setPromoterType(DssConstant.PROMOTER_TYPE.SCANATTENTION.code);
			pi = promoterInfoProxy.queryUniqueByObject(pi).getData();
			if(null != pi){
				QueryUser userTO = new QueryUser();
				userTO.setApptype(PlatformEnum.WAP.name());
				userTO.setChannelcode(channel);
				userTO.setTpin(fromUserName);
				userTO.setScanPromoterId(pi.getPromoterId());
				userTO.setUniontype(MemberUnionType.WEIXIN.code.toString());
				userTO.setUnionval(fromUserName);
				userTO.setIp(ip);
				memberInfoProxy.unionLogin(UserConvert.convertMemCallDto(userTO));
			}
		}else if(StringUtil.equals(eventKey, QrcodeConstant.QRCODE_COUPON_CODE)){ //冗余zhonglai
			channelPromoteAO.saveChannelPromote(QrcodeConstant.CHANNEL_SPECIAL.ZL.channle, fromUserName, "2");
			return handleMessage(QrcodeConstant.CHANNEL_SPECIAL.ZL.channle);
		}
		return StringUtil.EMPTY;
	}
	
	/**
	 * 特殊处理渠道
	 * @param channel
	 * @return
	 */
	private String handleMessage(String channel){
		if(StringUtil.equals(channel, QrcodeConstant.CHANNEL_SPECIAL.ZL.channle)){
			String msg = messageInfoProxy.getMessage(MessageConstant.SCENE.OFFLINE.getCode(),null);
			if(StringUtil.isNotBlank(msg)){
				Integer code = activityCacheHelper.getZLCodeCache();
				msg = msg.replace("XXXX", StringUtil.getStrByObj(code)).replace("xxxx", StringUtil.getStrByObj(code));
				return msg;
			} 
		}
		return StringUtil.EMPTY;
	}
	
	/**
	 * 获取前缀
	 */
	private String getPrefix(String val){
		return val.substring(0,val.indexOf("_")+1);
	}
}
