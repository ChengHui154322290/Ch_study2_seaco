package com.tp.proxy.sup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.sup.Audit;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.sup.IAuditService;
/**
 * 审批流设置主表代理层
 * @author szy
 *
 */
@Service
public class AuditProxy extends BaseProxy<Audit>{

	@Autowired
	private IAuditService auditService;

	@Override
	public IBaseService<Audit> getService() {
		return auditService;
	}
}
