package com.tp.dao.dss;

import com.tp.common.dao.BaseDao;
import com.tp.model.dss.AccountDetail;

public interface AccountDetailDao extends BaseDao<AccountDetail> {

	/**
	 */
	public Double getWithdrawedfees(AccountDetail detail);
}
