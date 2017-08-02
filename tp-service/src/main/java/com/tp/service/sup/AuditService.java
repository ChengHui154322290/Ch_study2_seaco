package com.tp.service.sup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.sup.AuditDao;
import com.tp.model.sup.Audit;
import com.tp.service.BaseService;
import com.tp.service.sup.IAuditService;

@Service
public class AuditService extends BaseService<Audit> implements IAuditService {

	@Autowired
	private AuditDao auditDao;
	
	@Override
	public BaseDao<Audit> getDao() {
		return auditDao;
	}

}
