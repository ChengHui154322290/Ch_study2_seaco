package com.tp.service.sup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.sup.AuditUserDao;
import com.tp.model.sup.AuditUser;
import com.tp.service.BaseService;
import com.tp.service.sup.IAuditUserService;

@Service
public class AuditUserService extends BaseService<AuditUser> implements IAuditUserService {

	@Autowired
	private AuditUserDao auditUserDao;
	
	@Override
	public BaseDao<AuditUser> getDao() {
		return auditUserDao;
	}
	
}
