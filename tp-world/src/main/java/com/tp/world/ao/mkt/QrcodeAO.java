package com.tp.world.ao.mkt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.QrcodeConstant;
import com.tp.m.util.StringUtil;
import com.tp.proxy.mkt.ChannelPromoteProxy;

@Service
public class QrcodeAO {
	
	private static Logger log = LoggerFactory.getLogger(QrcodeAO.class);
	
	@Autowired
	private ChannelPromoteProxy channelPromoteProxy;
	
	/**
	 * 生成渠道二维码
	 * @param channel
	 * @param type
	 * @return
	 */
	public String createQrcode(String channel,String type){
		try{
			if(StringUtil.isBlank(channel)) return StringUtil.EMPTY;
			return channelPromoteProxy.saveChannel(channel, StringUtil.getIntegerByStr(type),QrcodeConstant.SCAN_TYPE.QR_LIMIT_STR_SCENE.name(),QrcodeConstant.QRCODE_CHANNEL);
		}catch(Exception me){
			log.error("[生成二维码 - 生成推广渠道推广 Exception] = {}", me);
			return StringUtil.EMPTY;
		}
	}
	
//	public String createQrcodeNew(String channel,String type,String choise){
//		try{
//			if(StringUtil.isBlank(channel)) return StringUtil.EMPTY;
//			return channelPromoteProxy.saveChannelNew(channel, StringUtil.getIntegerByStr(type),QrcodeConstant.SCAN_TYPE.QR_LIMIT_STR_SCENE.name(),QrcodeConstant.QRCODE_CHANNEL,Integer.valueOf(choise));
//		}catch(Exception me){
//			log.error("[生成二维码 - 生成推广渠道推广 Exception] = {}", me);
//			return StringUtil.EMPTY;
//		}
//	}
}
