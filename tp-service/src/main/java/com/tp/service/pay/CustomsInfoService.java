package com.tp.service.pay;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.pay.CustomsInfoDao;
import com.tp.model.pay.CustomsInfo;
import com.tp.redis.util.JedisCacheUtil;
import com.tp.service.BaseService;
import com.tp.service.pay.ICustomsInfoService;

@Service
public class CustomsInfoService extends BaseService<CustomsInfo> implements ICustomsInfoService {

	@Autowired
	private CustomsInfoDao customsInfoDao;
	@Autowired
	private JedisCacheUtil jedisCacheUtil;
	
	private static String CUSTOMS_INFO_KEY = "customs_info_cache_key_";
	
	@Override
	public BaseDao<CustomsInfo> getDao() {
		return customsInfoDao;
	}

	@Override
	public CustomsInfo getCustomsInfo(Long gatewayId, Long channelId) {
		if(gatewayId == null || channelId == null) return null;
		
		CustomsInfo customsInfo = (CustomsInfo) jedisCacheUtil.getCache(CUSTOMS_INFO_KEY + gatewayId + "_" + channelId);
		if(customsInfo == null) {
			CustomsInfo query = new CustomsInfo();
			query.setGatewayId(gatewayId);
			query.setChannelsId(channelId);
			customsInfo = queryUniqueByObject(query);
			if(customsInfo != null) {
				jedisCacheUtil.setCache(CUSTOMS_INFO_KEY + gatewayId + "_" + channelId, customsInfo, 60*30);
			}
			else {
				return null;
			}
		}
		return customsInfo;
	}

	@Override
	public boolean isNeedPush(Long gatewayId, Long channelId) {
		if(gatewayId == null || channelId == null) return false;
		
		CustomsInfo customsInfo = (CustomsInfo) jedisCacheUtil.getCache(CUSTOMS_INFO_KEY + gatewayId + "_" + channelId);
		if(customsInfo == null) {
			CustomsInfo query = new CustomsInfo();
			query.setGatewayId(gatewayId);
			query.setChannelsId(channelId);
			customsInfo = queryUniqueByObject(query);
			if(customsInfo != null) {
				jedisCacheUtil.setCache(CUSTOMS_INFO_KEY + gatewayId + "_" + channelId, customsInfo, 60*30);
			}
			else {
				return false;
			}
		}
		return customsInfo.getPush();
	}
	
}
