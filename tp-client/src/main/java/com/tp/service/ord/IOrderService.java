package com.tp.service.ord;

import com.tp.common.vo.ord.OrderCodeType;
import com.tp.dto.ord.OrderDto;
import com.tp.dto.ord.OrderInsertInfoDTO;
import com.tp.exception.OrderServiceException;
import com.tp.model.ord.OrderInfo;
import com.tp.model.ord.SubOrder;
import com.tp.model.pay.PaymentInfo;
 /**
 * 订单 Service
 * @author szy
 */
public interface IOrderService {

	/**
	 * 插入  订单相关信息
	 * @param orderInsertInfoDTO
	 * @return 主键
	 * @throws OrderServiceException
	 * @author szy
	 */
	Long addOrder(OrderInsertInfoDTO orderInsertInfoDTO);
	
	/**
	 * 插入订单信息
	 * @param orderDto
	 * @return
	 */
	OrderDto insertOrder(OrderDto orderDto);
	
	/**
	 * 产生订单编号
	 * @param type
	 * @return 订单编号
	 * @throws OrderServiceException
	 * @author szy
	 */
	Long  generateCode(OrderCodeType type);
	
	/**
	 * 支付成功后,回调处理
	 * @param paymentInfoDO
	 * @return 更新行数
	 * @author szy
	 */
	void operateAfterSuccessPay(PaymentInfo paymentInfoDO);
	
	/**
	 * 付款前订单取消check
	 * @param orderCode
	 * @param userId
	 * @return
	 * author liuzhiyong
	 */
	boolean orderCancelCheck(Long parentOrderCode, Long memberId);
	
	/**
	 * 付款前订单取消后,更新订单状态
	 * @param orderCode
	 * @return
	 * author liuzhiyong
	 */
	OrderInfo operateAfterCancel(Long parentOrderCode, Long memberId, String userName);
	
	/**
	 * 根据子订单推送商品信息到仓库
	 * @param subOrderDO
	 * @param orderCode
	 */
	public Boolean putWareHouseShippingBySubOrder(SubOrder subOrder);
	
	/**
	 * 海淘自营订单推送仓库
	 * @param subOrder 
	 */
	boolean putWareHouseShippingBySeaSubOrder(SubOrder subOrder);

}
