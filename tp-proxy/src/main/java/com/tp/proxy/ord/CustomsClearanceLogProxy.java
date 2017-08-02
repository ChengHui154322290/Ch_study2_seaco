package com.tp.proxy.ord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.ord.CustomsClearanceLog;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.ord.ICustomsClearanceLogService;
/**
 * 订单相关申报海关全日志代理层
 * @author szy
 *
 */
@Service
public class CustomsClearanceLogProxy extends BaseProxy<CustomsClearanceLog>{

	@Autowired
	private ICustomsClearanceLogService customsClearanceLogService;

	@Override
	public IBaseService<CustomsClearanceLog> getService() {
		return customsClearanceLogService;
	}
}
