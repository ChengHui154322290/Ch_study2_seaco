package com.tp.service.mem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.mem.PointInfoDao;
import com.tp.model.mem.PointInfo;
import com.tp.service.BaseService;
import com.tp.service.mem.IPointInfoService;

@Service
public class PointInfoService extends BaseService<PointInfo> implements IPointInfoService {

	@Autowired
	private PointInfoDao pointInfoDao;
	
	@Override
	public BaseDao<PointInfo> getDao() {
		return pointInfoDao;
	}

}
