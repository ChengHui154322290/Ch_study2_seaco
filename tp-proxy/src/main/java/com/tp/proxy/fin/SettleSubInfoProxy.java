package com.tp.proxy.fin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.fin.SettleSubInfo;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.fin.ISettleSubInfoService;
/**
 * 结算子项表代理层
 * @author szy
 *
 */
@Service
public class SettleSubInfoProxy extends BaseProxy<SettleSubInfo>{

	@Autowired
	private ISettleSubInfoService settleSubInfoService;

	@Override
	public IBaseService<SettleSubInfo> getService() {
		return settleSubInfoService;
	}
}
