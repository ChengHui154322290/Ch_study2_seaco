package com.tp.shop.ao.mkt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.m.exception.MobileException;
import com.tp.m.util.StringUtil;
import com.tp.model.mkt.ChannelPromote;
import com.tp.proxy.mkt.ChannelPromoteProxy;
import com.tp.shop.helper.PropertiesHelper;

/**
 * 渠道推广AO
 * @author zhuss
 *
 */
@Service
public class ChannelPromoteAO {
	
	private static final Logger log = LoggerFactory.getLogger(ChannelPromoteAO.class);

	@Autowired
	private ChannelPromoteProxy channelPromoteProxy;
	
	@Autowired
	private PropertiesHelper propertiesHelper;
	
	public String getUrl(String channel,String uuid,String type){
		try{
			saveChannelPromote(channel,uuid,type);
		}catch(MobileException me){
			log.error("[API接口 - 微信获取OPENID MobileException] = {}",me.getMessage());
		}catch(Exception ex){
			log.error("[API接口 - 微信获取OPENID Exception] = {}",ex);
		}
		return propertiesHelper.followUrl;
	}
	
	public void saveChannelPromote(String channel,String uuid,String type){
		if(StringUtil.isNotBlank(channel) && StringUtil.isNotBlank(uuid)){
			ChannelPromote channelPromote = new ChannelPromote();
			channelPromote.setChannel(channel);
			channelPromote.setUniqueId(uuid);
			channelPromote.setType(StringUtil.getIntegerByStr(type));
			channelPromoteProxy.save(channelPromote);
		}
	}
}
