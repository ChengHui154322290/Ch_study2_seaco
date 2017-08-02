package com.tp.proxy.dss;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.dss.AccountDetail;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.dss.IAccountDetailService;
/**
 * 账户流水表代理层
 * @author szy
 *
 */
@Service
public class AccountDetailProxy extends BaseProxy<AccountDetail>{

	@Autowired
	private IAccountDetailService accountDetailService;

	@Override
	public IBaseService<AccountDetail> getService() {
		return accountDetailService;
	}
	
	public Double GetWithdrawedfees(AccountDetail detail){
		return accountDetailService.GetWithdrawedfees(detail);
	}

}
