package com.tp.proxy.ptm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.ptm.PlatformSystemLog;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.ptm.IPlatformSystemLogService;
/**
 * 开放平台系统日志代理层
 * @author szy
 *
 */
@Service
public class PlatformSystemLogProxy extends BaseProxy<PlatformSystemLog>{

	@Autowired
	private IPlatformSystemLogService platformSystemLogService;

	@Override
	public IBaseService<PlatformSystemLog> getService() {
		return platformSystemLogService;
	}
}
