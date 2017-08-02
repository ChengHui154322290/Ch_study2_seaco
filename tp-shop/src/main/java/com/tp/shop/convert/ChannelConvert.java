/**
 * 
 */
package com.tp.shop.convert;

import com.tp.m.util.DateUtil;
import com.tp.m.vo.promoter.ChannelInfoVO;
import com.tp.model.dss.ChannelInfo;

/**
 * @author Administrator
 *
 */
public class ChannelConvert {
	
	public static ChannelInfoVO convertChannelInfoVO(ChannelInfo channelInfo){
		ChannelInfoVO channelInfoVO = new ChannelInfoVO();
		channelInfoVO.setChannelid(channelInfo.getChannelId());
		channelInfoVO.setChannelcode(channelInfo.getChannelCode());
		channelInfoVO.setChannelname(channelInfo.getChannelName());
		channelInfoVO.setDisabled(channelInfo.getDisabled());
		channelInfoVO.setCreatetime(channelInfo.getCreateTime() == null ? "" : DateUtil.formatDate("yyyymmdd", channelInfo.getCreateTime()));
		
		channelInfoVO.setEshopname(channelInfo.getEshopName());
		channelInfoVO.setSharetitle(channelInfo.getShareTitle());
		channelInfoVO.setSharecontent(channelInfo.getShareContent());
		channelInfoVO.setDsstype(channelInfo.getCompanyDssType());
		channelInfoVO.setIsUsedPoint(channelInfo.getIsUsedPoint());
		if (channelInfo.getPromoterInfo() != null) {
			channelInfoVO.setPromoterinfo(PromoterConvert.convertBriefPromoterInfoVO(channelInfo.getPromoterInfo()));
		}
		return channelInfoVO;
	}
	
}
