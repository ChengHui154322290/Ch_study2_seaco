package com.tp.dao.pay;

import java.util.List;

import com.tp.common.dao.BaseDao;
import com.tp.model.pay.PaymentGateway;

public interface PaymentGatewayDao extends BaseDao<PaymentGateway> {
	
	List<PaymentGateway> selectAllOrderbyParentId();
}
