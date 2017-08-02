package com.tp.service.mem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.mem.PointRuleConfigDao;
import com.tp.model.mem.PointRuleConfig;
import com.tp.service.BaseService;
import com.tp.service.mem.IPointRuleConfigService;

@Service
public class PointRuleConfigService extends BaseService<PointRuleConfig> implements IPointRuleConfigService {

	@Autowired
	private PointRuleConfigDao pointRuleConfigDao;
	
	@Override
	public BaseDao<PointRuleConfig> getDao() {
		return pointRuleConfigDao;
	}

}
