package com.tp.service.ord;

import java.util.List;

import com.tp.model.ord.OrderPromotion;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 订单促销明细表接口
  */
public interface IOrderPromotionService extends IBaseService<OrderPromotion>{
	/**
	 * 根据code查询
	 * 
	 * @param orderId
	 * @return
	 */
	List<OrderPromotion> selectByOrderId(Long orderId);

	/**
	 * 根据优惠券ID查询列表
	 * 
	 * @param couponCode
	 * @return
	 */
	List<OrderPromotion> selectListByCouponCode(String couponCode);

	/**
	 * 根据子订单号查询
	 * 
	 * @param orderCode
	 * @return
	 */
	List<OrderPromotion> selectListBySubCode(Long orderCode);

	/**
	 * 根据父订单号查询
	 * 
	 * @param orderCode
	 * @return
	 */
	List<OrderPromotion> selectListByOrderCode(Long orderCode);
}
