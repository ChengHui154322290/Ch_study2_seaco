package com.tp.service.pay;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.pay.PaymentLogDao;
import com.tp.model.pay.PaymentLog;
import com.tp.service.BaseService;
import com.tp.service.pay.IPaymentLogService;

@Service
public class PaymentLogService extends BaseService<PaymentLog> implements IPaymentLogService {

	@Autowired
	private PaymentLogDao paymentLogDao;
	
	@Override
	public BaseDao<PaymentLog> getDao() {
		return paymentLogDao;
	}
}
