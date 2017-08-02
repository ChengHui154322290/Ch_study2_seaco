package com.tp.service.mmp;

import java.util.List;

import com.tp.dto.mmp.CartCouponDTO;
import com.tp.dto.ord.CartDTO;
import com.tp.dto.ord.SeaOrderItemDTO;

/**
 * 计算总价接口
 *
 */
public interface IPriceService {

	/**
	 * 计算购物车的总价
	 * @param cartDTO
	 * @return
	 * @throws Exception
	 */
	public CartDTO cartTotalPrice(CartDTO cartDTO) throws Exception;
	
	/**
	 * 计算订单的总价
	 * @param cartDTO
	 * @param couponUserIdList
	 * @return
	 * @throws Exception
	 */
	public CartDTO orderTotalPrice(CartDTO cartDTO, List<Long> couponUserIdList, Long memberId) throws Exception;
	
	/**
	 * 计算订单的价格，并返回优惠券的使用情况
	 * @param cartDTO
	 * @param couponUserIdList
	 * @return
	 * @throws Exception
	 */
	public CartCouponDTO orderTotalPriceWithCoupon(CartDTO cartDTO, List<Long> couponUserIdList, Long memberId) throws Exception;
	
	/**
	 * 计算海淘订单总价
	 * @param orderDTO
	 * @param couponUserIdList
	 * @return
	 * @throws Exception
	 */
	public SeaOrderItemDTO hitaoOrderTotalPrice(SeaOrderItemDTO orderDTO , List<Long> couponUserIdList) throws Exception;
	
	public CartCouponDTO  hitaoOrderTotalPriceWithCoupon(SeaOrderItemDTO orderDTO , List<Long> couponUserIdList) throws Exception;
	
	
}
