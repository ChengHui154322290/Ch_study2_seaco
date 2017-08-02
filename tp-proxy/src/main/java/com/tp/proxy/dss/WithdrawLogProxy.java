package com.tp.proxy.dss;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.dss.WithdrawLog;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.dss.IWithdrawLogService;
/**
 * 提现日志表代理层
 * @author szy
 *
 */
@Service
public class WithdrawLogProxy extends BaseProxy<WithdrawLog>{

	@Autowired
	private IWithdrawLogService withdrawLogService;

	@Override
	public IBaseService<WithdrawLog> getService() {
		return withdrawLogService;
	}
}
