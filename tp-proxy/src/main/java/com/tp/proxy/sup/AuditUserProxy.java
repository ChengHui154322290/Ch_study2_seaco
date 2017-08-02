package com.tp.proxy.sup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.sup.AuditUser;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.sup.IAuditUserService;
/**
 * 审批流设置-用户表代理层
 * @author szy
 *
 */
@Service
public class AuditUserProxy extends BaseProxy<AuditUser>{

	@Autowired
	private IAuditUserService auditUserService;

	@Override
	public IBaseService<AuditUser> getService() {
		return auditUserService;
	}
}
