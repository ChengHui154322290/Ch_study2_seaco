package com.tp.service.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.mmp.PointSignConfigDao;
import com.tp.model.mmp.PointSignConfig;
import com.tp.service.BaseService;
import com.tp.service.mmp.IPointSignConfigService;

@Service
public class PointSignConfigService extends BaseService<PointSignConfig> implements IPointSignConfigService {

	@Autowired
	private PointSignConfigDao pointSignConfigDao;
	
	@Override
	public BaseDao<PointSignConfig> getDao() {
		return pointSignConfigDao;
	}

}
