package com.tp.proxy.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.app.SwitchInfo;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.app.ISwitchInfoService;
/**
 * app开关~~~代理层
 * @author szy
 *
 */
@Service
public class SwitchInfoProxy extends BaseProxy<SwitchInfo>{

	@Autowired
	private ISwitchInfoService switchInfoService;

	@Override
	public IBaseService<SwitchInfo> getService() {
		return switchInfoService;
	}
}
