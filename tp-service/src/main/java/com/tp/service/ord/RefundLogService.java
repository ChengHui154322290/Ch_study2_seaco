package com.tp.service.ord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.ord.RefundLogDao;
import com.tp.model.ord.RefundLog;
import com.tp.service.BaseService;
import com.tp.service.ord.IRefundLogService;

@Service
public class RefundLogService extends BaseService<RefundLog> implements IRefundLogService {

	@Autowired
	private RefundLogDao refundLogDao;
	
	@Override
	public BaseDao<RefundLog> getDao() {
		return refundLogDao;
	}

}
