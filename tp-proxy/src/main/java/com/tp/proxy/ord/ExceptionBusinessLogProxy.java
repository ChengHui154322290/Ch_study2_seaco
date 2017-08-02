package com.tp.proxy.ord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.ord.ExceptionBusinessLog;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.ord.IExceptionBusinessLogService;
/**
 * 异常业务日志表代理层
 * @author szy
 *
 */
@Service
public class ExceptionBusinessLogProxy extends BaseProxy<ExceptionBusinessLog>{

	@Autowired
	private IExceptionBusinessLogService exceptionBusinessLogService;

	@Override
	public IBaseService<ExceptionBusinessLog> getService() {
		return exceptionBusinessLogService;
	}
}
