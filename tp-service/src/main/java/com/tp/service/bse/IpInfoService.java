package com.tp.service.bse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.bse.IpInfoDao;
import com.tp.model.bse.IpInfo;
import com.tp.service.BaseService;
import com.tp.service.bse.IIpInfoService;

@Service
public class IpInfoService extends BaseService<IpInfo> implements IIpInfoService {

	@Autowired
	private IpInfoDao ipInfoDao;
	
	@Override
	public BaseDao<IpInfo> getDao() {
		return ipInfoDao;
	}

}
