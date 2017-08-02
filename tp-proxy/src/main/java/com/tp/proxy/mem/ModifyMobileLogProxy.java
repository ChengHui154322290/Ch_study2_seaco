package com.tp.proxy.mem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.mem.ModifyMobileLog;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.mem.IModifyMobileLogService;
/**
 * 代理层
 * @author szy
 *
 */
@Service
public class ModifyMobileLogProxy extends BaseProxy<ModifyMobileLog>{

	@Autowired
	private IModifyMobileLogService modifyMobileLogService;

	@Override
	public IBaseService<ModifyMobileLog> getService() {
		return modifyMobileLogService;
	}
}
