package com.tp.proxy.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.app.SwitchConf;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.app.ISwitchConfService;
/**
 * 开关控制表代理层
 * @author szy
 *
 */
@Service
public class SwitchConfProxy extends BaseProxy<SwitchConf>{

	@Autowired
	private ISwitchConfService switchConfService;

	@Override
	public IBaseService<SwitchConf> getService() {
		return switchConfService;
	}
}
