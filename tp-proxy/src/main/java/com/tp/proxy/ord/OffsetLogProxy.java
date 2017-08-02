package com.tp.proxy.ord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.ord.OffsetLog;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.ord.IOffsetLogService;
/**
 * 补偿单操作日志代理层
 * @author szy
 *
 */
@Service
public class OffsetLogProxy extends BaseProxy<OffsetLog>{

	@Autowired
	private IOffsetLogService offsetLogService;

	@Override
	public IBaseService<OffsetLog> getService() {
		return offsetLogService;
	}
}
