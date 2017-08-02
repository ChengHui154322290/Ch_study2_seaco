package com.tp.proxy.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.mmp.ApprovalInfo;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.mmp.IApprovalInfoService;
/**
 * 审核信息表代理层
 * @author szy
 *
 */
@Service
public class ApprovalInfoProxy extends BaseProxy<ApprovalInfo>{

	@Autowired
	private IApprovalInfoService approvalInfoService;

	@Override
	public IBaseService<ApprovalInfo> getService() {
		return approvalInfoService;
	}
}
