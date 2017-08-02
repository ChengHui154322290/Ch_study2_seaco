package com.tp.service.pay;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.pay.PaymentChannelGatewayDao;
import com.tp.model.pay.PaymentChannelGateway;
import com.tp.service.BaseService;
import com.tp.service.pay.IPaymentChannelGatewayService;

@Service
public class PaymentChannelGatewayService extends BaseService<PaymentChannelGateway> implements IPaymentChannelGatewayService {

	@Autowired
	private PaymentChannelGatewayDao paymentChannelGatewayDao;
	
	@Override
	public BaseDao<PaymentChannelGateway> getDao() {
		return paymentChannelGatewayDao;
	}

	@Override
	public List<PaymentChannelGateway> queryChannelGateways(Long orderType, Long channelId) {
		// TODO Auto-generated method stub
		Map<String, Object> params = new HashMap<>();
		params.put("channelId", channelId);
		params.put("orderType", orderType);
		return paymentChannelGatewayDao.queryByParamNotEmpty(params);
	}
}
