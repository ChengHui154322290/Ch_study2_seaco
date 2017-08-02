package com.tp.service.sup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.sup.AuditRecordsDao;
import com.tp.model.sup.AuditRecords;
import com.tp.service.BaseService;
import com.tp.service.sup.IAuditRecordsService;

@Service
public class AuditRecordsService extends BaseService<AuditRecords> implements IAuditRecordsService {

	@Autowired
	private AuditRecordsDao auditRecordsDao;
	
	@Override
	public BaseDao<AuditRecords> getDao() {
		return auditRecordsDao;
	}
}
