package com.tp.service.pay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.pay.PaymentGatewayDao;
import com.tp.model.pay.PaymentChannelGateway;
import com.tp.model.pay.PaymentGateway;
import com.tp.service.BaseService;
import com.tp.service.pay.IPaymentChannelGatewayService;
import com.tp.service.pay.IPaymentGatewayService;

@Service
public class PaymentGatewayService extends BaseService<PaymentGateway> implements IPaymentGatewayService {
	
	private static final Map<String,List<PaymentGateway>> CHANNEL_GATEWAY_MAP = Collections.synchronizedMap(new HashMap<String,List<PaymentGateway>>());
	private static final Integer INTERVAL_TIME = 1000*60*30;
	private static Map<String,Long> lastRunTimes = Collections.synchronizedMap(new HashMap<String, Long>());

	@Autowired
	private PaymentGatewayDao paymentGatewayDao;
	
	@Autowired
	private IPaymentChannelGatewayService paymentChannelGatewayService;
	
	@Override
	public BaseDao<PaymentGateway> getDao() {
		return paymentGatewayDao;
	}

	@Override
	public List<PaymentGateway> selectAllOrderbyParentId() {
		// TODO Auto-generated method stub
		return paymentGatewayDao.selectAllOrderbyParentId();
	}

	@Override
	public List<PaymentGateway> queryPaymentGateWayLists(Long orderType, Long channelId) {
		if(channelId == null || channelId.intValue() == 0){
			orderType = -1l;
			channelId = -1l;
		}
		//使用jvm缓存："订单类型+@+支付渠道"作为缓存的key
		final String cacheKey = orderType+"@"+channelId; 
		List<PaymentGateway> paymentGatewayList = CHANNEL_GATEWAY_MAP.get(cacheKey);
		Long currentTime = System.currentTimeMillis();
		Long lastRunTime = lastRunTimes.get(cacheKey);
		if(lastRunTime==null||currentTime-lastRunTime>=INTERVAL_TIME || CollectionUtils.isEmpty(paymentGatewayList)){
			//缓存时间过了，或者没有从缓存中取到则需要重新从数据库取
			List<PaymentChannelGateway> channelGatewayList = paymentChannelGatewayService.queryChannelGateways(orderType, channelId);
			try {
				paymentGatewayList = selectAllOrderbyParentId();
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
			if(CollectionUtils.isNotEmpty(channelGatewayList) && CollectionUtils.isNotEmpty(paymentGatewayList)){
				List<PaymentGateway> paymentChannelGatewayList = new ArrayList<PaymentGateway>();
				for(PaymentGateway paymentGateway:paymentGatewayList){
					for(PaymentChannelGateway channelGateway:channelGatewayList){
						if(channelGateway.getGatewayId().equals(paymentGateway.getGatewayId()) && 1==paymentGateway.getStatus()){
							//只取启用状态（status=1）的
							paymentChannelGatewayList.add(paymentGateway);
						}
					}
				}
				CHANNEL_GATEWAY_MAP.put(cacheKey, paymentChannelGatewayList);
				lastRunTimes.put(cacheKey, currentTime);
				return paymentChannelGatewayList;
			}
			else{
				return new ArrayList<PaymentGateway>();
			}
		}
		return new ArrayList<PaymentGateway>(paymentGatewayList);
	}

}
