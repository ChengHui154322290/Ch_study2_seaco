package com.tp.proxy.ord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.ord.CancelLog;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.ord.ICancelLogService;
/**
 * 取消单操作日志代理层
 * @author szy
 *
 */
@Service
public class CancelLogProxy extends BaseProxy<CancelLog>{

	@Autowired
	private ICancelLogService cancelLogService;

	@Override
	public IBaseService<CancelLog> getService() {
		return cancelLogService;
	}
}
