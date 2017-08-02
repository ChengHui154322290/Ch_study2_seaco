package com.tp.service.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.mmp.TieredGroupInfoDao;
import com.tp.model.mmp.TieredGroupInfo;
import com.tp.service.BaseService;
import com.tp.service.mmp.ITieredGroupInfoService;

@Service
public class TieredGroupInfoService extends BaseService<TieredGroupInfo> implements ITieredGroupInfoService {

	@Autowired
	private TieredGroupInfoDao tieredGroupInfoDao;
	
	@Override
	public BaseDao<TieredGroupInfo> getDao() {
		return tieredGroupInfoDao;
	}

}
