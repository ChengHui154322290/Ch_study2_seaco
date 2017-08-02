package com.tp.service.ord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.ord.PersonalgoodsStatusLogDao;
import com.tp.model.ord.PersonalgoodsStatusLog;
import com.tp.service.BaseService;
import com.tp.service.ord.IPersonalgoodsStatusLogService;

@Service
public class PersonalgoodsStatusLogService extends BaseService<PersonalgoodsStatusLog> implements IPersonalgoodsStatusLogService {

	@Autowired
	private PersonalgoodsStatusLogDao personalgoodsStatusLogDao;
	
	@Override
	public BaseDao<PersonalgoodsStatusLog> getDao() {
		return personalgoodsStatusLogDao;
	}

}
