package com.tp.proxy.ord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.ord.RejectLog;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.ord.IRejectLogService;
/**
 * 退货单操作日志代理层
 * @author szy
 *
 */
@Service
public class RejectLogProxy extends BaseProxy<RejectLog>{

	@Autowired
	private IRejectLogService rejectLogService;

	@Override
	public IBaseService<RejectLog> getService() {
		return rejectLogService;
	}
}
