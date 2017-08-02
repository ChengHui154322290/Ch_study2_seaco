package com.tp.proxy.bse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.bse.IpInfo;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.bse.IIpInfoService;
/**
 * 代理层
 * @author szy
 *
 */
@Service
public class IpInfoProxy extends BaseProxy<IpInfo>{

	@Autowired
	private IIpInfoService ipInfoService;

	@Override
	public IBaseService<IpInfo> getService() {
		return ipInfoService;
	}
}
