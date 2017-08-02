package com.tp.proxy.fin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.fin.SettleInfo;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.fin.ISettleInfoService;
/**
 * 结算信息表代理层
 * @author szy
 *
 */
@Service
public class SettleInfoProxy extends BaseProxy<SettleInfo>{

	@Autowired
	private ISettleInfoService settleInfoService;

	@Override
	public IBaseService<SettleInfo> getService() {
		return settleInfoService;
	}
}
