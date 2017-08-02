package com.tp.service.dss.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.Constant.AUTHOR_TYPE;
import com.tp.model.dss.CommisionDetail;
import com.tp.model.ord.SubOrder;
import com.tp.mq.MqMessageCallBack;
import com.tp.service.dss.ICommisionDetailService;

/**
 * 收货成功后修改订单状态
 * @author szy
 *
 */
@Service
public class OrderReceiveItemConsumer implements MqMessageCallBack {

	private static final Logger logger = LoggerFactory.getLogger(ReferralFeesConsumer.class);
	
	@Autowired
	private ICommisionDetailService commisionDetailService;
	
	@Override
	public boolean execute(Object o) {
		SubOrder subOrder = (SubOrder)o;
		CommisionDetail commisionDetail = new CommisionDetail();
		commisionDetail.setOrderCode(subOrder.getOrderCode());
		commisionDetail.setOrderStatus(subOrder.getOrderStatus());
		commisionDetail.setOrderReceiptTime(subOrder.getDoneTime());
		commisionDetail.setUpdateUser(AUTHOR_TYPE.SYSTEM);
		commisionDetailService.updateReceiveTimeByOrderCode(commisionDetail);
		return Boolean.TRUE;
	}

}
