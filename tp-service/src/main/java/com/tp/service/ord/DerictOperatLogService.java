package com.tp.service.ord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.ord.DerictOperatLogDao;
import com.tp.model.ord.DerictOperatLog;
import com.tp.service.BaseService;

@Service
public class DerictOperatLogService extends BaseService<DerictOperatLog> implements IDerictOperatLogService {

	@Autowired
	private DerictOperatLogDao derictOperatLogDao;
	
	@Override
	public BaseDao<DerictOperatLog> getDao() {
		return derictOperatLogDao;
	}

}
