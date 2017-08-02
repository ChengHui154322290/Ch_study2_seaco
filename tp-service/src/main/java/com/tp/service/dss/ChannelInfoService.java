package com.tp.service.dss;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.dss.ChannelInfoDao;
import com.tp.model.dss.ChannelInfo;
import com.tp.service.BaseService;
import com.tp.service.dss.IChannelInfoService;

@Service
public class ChannelInfoService extends BaseService<ChannelInfo> implements IChannelInfoService {

	@Autowired
	private ChannelInfoDao channelInfoDao;
	
	@Override
	public BaseDao<ChannelInfo> getDao() {
		return channelInfoDao;
	}

}
