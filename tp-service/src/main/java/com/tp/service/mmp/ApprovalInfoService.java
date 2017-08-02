package com.tp.service.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.mmp.ApprovalInfoDao;
import com.tp.model.mmp.ApprovalInfo;
import com.tp.service.BaseService;
import com.tp.service.mmp.IApprovalInfoService;

@Service
public class ApprovalInfoService extends BaseService<ApprovalInfo> implements IApprovalInfoService {

	@Autowired
	private ApprovalInfoDao approvalInfoDao;
	
	@Override
	public BaseDao<ApprovalInfo> getDao() {
		return approvalInfoDao;
	}

}
