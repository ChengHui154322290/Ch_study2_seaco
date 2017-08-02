package com.tp.proxy.ord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.ord.Kuaidi100Express;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.ord.IKuaidi100ExpressService;
/**
 * 快递100推送的快递日志记录代理层
 * @author szy
 *
 */
@Service
public class Kuaidi100ExpressProxy extends BaseProxy<Kuaidi100Express>{

	@Autowired
	private IKuaidi100ExpressService kuaidi100ExpressService;

	@Override
	public IBaseService<Kuaidi100Express> getService() {
		return kuaidi100ExpressService;
	}
}
