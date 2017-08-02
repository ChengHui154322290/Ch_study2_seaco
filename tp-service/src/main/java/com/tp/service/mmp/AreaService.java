package com.tp.service.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.mmp.AreaDao;
import com.tp.model.mmp.Area;
import com.tp.service.BaseService;
import com.tp.service.mmp.IAreaService;

@Service
public class AreaService extends BaseService<Area> implements IAreaService {

	@Autowired
	private AreaDao areaDao;
	
	@Override
	public BaseDao<Area> getDao() {
		return areaDao;
	}

}
