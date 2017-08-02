package com.tp.proxy.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.mmp.PolicyInfo;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.mmp.IPolicyInfoService;
/**
 * 限购政策代理层
 * @author szy
 *
 */
@Service
public class PolicyInfoProxy extends BaseProxy<PolicyInfo>{

	@Autowired
	private IPolicyInfoService policyInfoService;

	@Override
	public IBaseService<PolicyInfo> getService() {
		return policyInfoService;
	}
}
