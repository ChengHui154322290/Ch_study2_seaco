package com.tp.service.ord.local;

import java.util.List;

import com.tp.dto.ord.CartDTO;
import com.tp.dto.ord.OrderInfoDTO;
import com.tp.dto.ord.SeaOrderItemDTO;
import com.tp.model.mem.MemberInfo;
import com.tp.model.pay.PaymentInfo;

/**
 * 订单提交服务
 * 
 * @author szy
 * @version 0.0.1
 */
public interface IOrderSubmitLocalService {

	/**
	 * 
	 * <pre>
	 * 订单提交之后信息保存
	 * </pre>
	 * 
	 * @param orderInfoDTO
	 * @param userMenber
	  * @param ip
	  * @param orderSource
	  * @return PaymentInfo
	 */
	List<PaymentInfo>   orderSubmit(OrderInfoDTO orderInfoDTO,  MemberInfo userMenber, String ip,Integer orderSource);
	

/**
 * 拆分海淘订单：先按供应商拆，再按仓库拆
 * @param cartDTO
 * @return :Map<SubOrder, List<OrderLineDO>>
 * key :SubOrder:子订单对象
 * 
 */
	public SeaOrderItemDTO  splitSeaOrder(CartDTO cartDTO);

}

