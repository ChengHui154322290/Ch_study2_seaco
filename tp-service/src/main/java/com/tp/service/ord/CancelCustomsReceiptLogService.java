package com.tp.service.ord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.ord.CancelCustomsReceiptLogDao;
import com.tp.model.ord.CancelCustomsReceiptLog;
import com.tp.service.BaseService;
import com.tp.service.ord.ICancelCustomsReceiptLogService;

@Service
public class CancelCustomsReceiptLogService extends BaseService<CancelCustomsReceiptLog> implements ICancelCustomsReceiptLogService {

	@Autowired
	private CancelCustomsReceiptLogDao cancelCustomsReceiptLogDao;
	
	@Override
	public BaseDao<CancelCustomsReceiptLog> getDao() {
		return cancelCustomsReceiptLogDao;
	}

}
