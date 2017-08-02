package com.tp.proxy.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.mmp.PolicyChange;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.mmp.IPolicyChangeService;
/**
 * 限购政策代理层
 * @author szy
 *
 */
@Service
public class PolicyChangeProxy extends BaseProxy<PolicyChange>{

	@Autowired
	private IPolicyChangeService policyChangeService;

	@Override
	public IBaseService<PolicyChange> getService() {
		return policyChangeService;
	}
}
