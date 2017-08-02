package com.tp.proxy.dss;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.DssConstant;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.dss.ChannelInfo;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.dss.IChannelInfoService;
import com.tp.service.dss.IPromoterInfoService;
import com.tp.util.StringUtil;
/**
 * 营销渠道信息表代理层
 * @author szy
 *
 */
@Service
public class ChannelInfoProxy extends BaseProxy<ChannelInfo>{

	@Autowired
	private IChannelInfoService channelInfoService;

	@Autowired
	private IPromoterInfoService promoterInfoService;
	
	@Override
	public IBaseService<ChannelInfo> getService() {
		return channelInfoService;
	}
	
	//获取渠道信息和推广信息
	public ResultInfo<ChannelInfo> getChannelInfoByCode(String channelCode){
		if (StringUtil.isEmpty(channelCode)) {
			return new ResultInfo<>(new FailInfo("channelCode为空"));
		}
		Map<String, Object> params = new HashMap<>();
		params.put("channelCode", channelCode);
		ChannelInfo channelInfo = channelInfoService.queryUniqueByParams(params);
		if (channelInfo == null) {
			return new ResultInfo<>();
		}
		params.put("promoterType", DssConstant.PROMOTER_TYPE.COMPANY.code);
		channelInfo.setPromoterInfo(promoterInfoService.queryUniqueByParams(params));
		return new ResultInfo<>(channelInfo);
	}
}
