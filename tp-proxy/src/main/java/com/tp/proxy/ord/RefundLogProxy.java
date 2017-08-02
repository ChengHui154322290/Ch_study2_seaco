package com.tp.proxy.ord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.ord.RefundLog;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.ord.IRefundLogService;
/**
 * 退款单操作日志代理层
 * @author szy
 *
 */
@Service
public class RefundLogProxy extends BaseProxy<RefundLog>{

	@Autowired
	private IRefundLogService refundLogService;

	@Override
	public IBaseService<RefundLog> getService() {
		return refundLogService;
	}
}
