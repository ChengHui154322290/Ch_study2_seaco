package com.tp.proxy.ptm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.ptm.PlatformItemLog;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.ptm.IPlatformItemLogService;
/**
 * 平台请求日志表代理层
 * @author szy
 *
 */
@Service
public class PlatformItemLogProxy extends BaseProxy<PlatformItemLog>{

	@Autowired
	private IPlatformItemLogService itemLogService;

	@Override
	public IBaseService<PlatformItemLog> getService() {
		return itemLogService;
	}
}
