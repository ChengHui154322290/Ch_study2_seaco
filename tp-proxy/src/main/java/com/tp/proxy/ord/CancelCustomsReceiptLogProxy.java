package com.tp.proxy.ord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.ord.CancelCustomsReceiptLog;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.ord.ICancelCustomsReceiptLogService;
/**
 * 取消海淘单海关申报回执流水日志代理层
 * @author szy
 *
 */
@Service
public class CancelCustomsReceiptLogProxy extends BaseProxy<CancelCustomsReceiptLog>{

	@Autowired
	private ICancelCustomsReceiptLogService cancelCustomsReceiptLogService;

	@Override
	public IBaseService<CancelCustomsReceiptLog> getService() {
		return cancelCustomsReceiptLogService;
	}
}
