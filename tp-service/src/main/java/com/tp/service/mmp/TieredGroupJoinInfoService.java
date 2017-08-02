package com.tp.service.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.mmp.TieredGroupJoinInfoDao;
import com.tp.model.mmp.TieredGroupJoinInfo;
import com.tp.service.BaseService;
import com.tp.service.mmp.ITieredGroupJoinInfoService;

@Service
public class TieredGroupJoinInfoService extends BaseService<TieredGroupJoinInfo> implements ITieredGroupJoinInfoService {

	@Autowired
	private TieredGroupJoinInfoDao tieredGroupJoinInfoDao;
	
	@Override
	public BaseDao<TieredGroupJoinInfo> getDao() {
		return tieredGroupJoinInfoDao;
	}

}
