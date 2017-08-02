package com.tp.service.mem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.mem.UnionInfoDao;
import com.tp.model.mem.UnionInfo;
import com.tp.service.BaseService;
import com.tp.service.mem.IUnionInfoService;

@Service
public class UnionInfoService extends BaseService<UnionInfo> implements IUnionInfoService {

	@Autowired
	private UnionInfoDao unionInfoDao;
	
	@Override
	public BaseDao<UnionInfo> getDao() {
		return unionInfoDao;
	}

}
