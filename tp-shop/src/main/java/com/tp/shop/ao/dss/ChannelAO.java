/**
 * 
 */
package com.tp.shop.ao.dss;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.dto.common.ResultInfo;
import com.tp.m.base.MResultVO;
import com.tp.m.enums.MResultInfo;
import com.tp.m.exception.MobileException;
import com.tp.m.vo.promoter.ChannelInfoVO;
import com.tp.model.dss.ChannelInfo;
import com.tp.proxy.dss.ChannelInfoProxy;
import com.tp.shop.convert.ChannelConvert;

/**
 * @author Administrator
 *
 */
@Service
public class ChannelAO {
	
	private static final Logger log = LoggerFactory.getLogger(ChannelAO.class);
	
	@Autowired
	private ChannelInfoProxy channelInfoProxy;
	
	public MResultVO<ChannelInfoVO> getChannelInfo(String channelCode){
		try {
			ResultInfo<ChannelInfo> result = channelInfoProxy.getChannelInfoByCode(channelCode);
			if (!result.isSuccess()) {
				log.error("[API接口 - 查询渠道信息失败：{}]", result.getMsg().getDetailMessage());
				return new MResultVO<>(MResultInfo.SYSTEM_ERROR);
			}
			if (result.getData() == null) {
				log.error("[API接口 - 查询渠道信息失败：渠道信息不存在]");
				return new MResultVO<>(MResultInfo.CHANNEL_INFO_NOT_EXIST);
			}
			return new MResultVO<>(MResultInfo.SUCCESS, ChannelConvert.convertChannelInfoVO(result.getData())); 		
		} catch (MobileException e) {
			log.error("[API接口 - 获取渠道信息  MobileException]={}", e.getMessage());
			return new MResultVO<>(e);
		}catch (Exception e) {
			log.error("[API接口 - 获取渠道信息  Exception]={}", e);
			return new MResultVO<>(MResultInfo.CONN_ERROR);
		}
	}
}
