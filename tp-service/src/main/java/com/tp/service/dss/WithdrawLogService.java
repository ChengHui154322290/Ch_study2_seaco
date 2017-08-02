package com.tp.service.dss;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.dss.WithdrawLogDao;
import com.tp.model.dss.WithdrawLog;
import com.tp.service.BaseService;
import com.tp.service.dss.IWithdrawLogService;

@Service
public class WithdrawLogService extends BaseService<WithdrawLog> implements IWithdrawLogService {

	@Autowired
	private WithdrawLogDao withdrawLogDao;
	
	@Override
	public BaseDao<WithdrawLog> getDao() {
		return withdrawLogDao;
	}

}
