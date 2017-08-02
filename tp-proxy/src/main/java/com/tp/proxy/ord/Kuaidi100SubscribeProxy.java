package com.tp.proxy.ord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.ord.Kuaidi100Subscribe;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.ord.IKuaidi100SubscribeService;
/**
 * 快递100订阅记录，即推送快递单号给快递100平台代理层
 * @author szy
 *
 */
@Service
public class Kuaidi100SubscribeProxy extends BaseProxy<Kuaidi100Subscribe>{

	@Autowired
	private IKuaidi100SubscribeService kuaidi100SubscribeService;

	@Override
	public IBaseService<Kuaidi100Subscribe> getService() {
		return kuaidi100SubscribeService;
	}
}
