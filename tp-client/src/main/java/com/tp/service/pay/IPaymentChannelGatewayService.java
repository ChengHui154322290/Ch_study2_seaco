package com.tp.service.pay;


import java.util.List;

import com.tp.model.pay.PaymentChannelGateway;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 渠道对应网关关系表接口
  */
public interface IPaymentChannelGatewayService extends IBaseService<PaymentChannelGateway>{

	/**
	 * 根据渠道代码查询对应支付网关关系
	 * @param channelId
	 * @return
	 */
	List<PaymentChannelGateway> queryChannelGateways(Long orderType, Long channelId);
}
