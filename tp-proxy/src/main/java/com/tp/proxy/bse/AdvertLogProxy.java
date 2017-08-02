package com.tp.proxy.bse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.bse.AdvertLog;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.bse.IAdvertLogService;
/**
 * 广告链接操作日志代理层
 * @author szy
 *
 */
@Service
public class AdvertLogProxy extends BaseProxy<AdvertLog>{

	@Autowired
	private IAdvertLogService advertLogService;

	@Override
	public IBaseService<AdvertLog> getService() {
		return advertLogService;
	}
}
