package com.tp.service.pay;

import java.util.List;

import com.tp.model.pay.PaymentGateway;
import com.tp.service.IBaseService;

import javax.xml.rpc.ServiceException;
/**
  * @author szy
  * 支付网关配置表接口
  */
public interface IPaymentGatewayService extends IBaseService<PaymentGateway>{

	List<PaymentGateway> selectAllOrderbyParentId();
	
	
	/**
	 * 根据订单类型、支付渠道查询对应支付网关
	 * @param orderType
	 * @param channelId
	 * @return
	 */
	List<PaymentGateway> queryPaymentGateWayLists(Long orderType, Long channelId);
	
}
