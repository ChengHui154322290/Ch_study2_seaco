package com.tp.service.ord.local;

import java.util.List;
import java.util.Map;

import com.tp.dto.mmp.OrderCouponDTO;
import com.tp.dto.ord.CartDTO;
import com.tp.dto.ord.SeaOrderItemDTO;

/**
 * 订单确认服务
 * 
 * @author szy
 * @version 0.0.1
 */
public interface IOrderConfirmLocalService {

	/**
	 * 
	 * <pre>
	 * 计算购物车中商品的价格
	 * </pre>
	 * 
	 * @param cartDTO
	 * @param couponIdList
	 * @return
	 */
	CartDTO calcItemPrice(Long memberId, List<Long> couponIdList);

	/**
	 * 
	 * <pre>
	 * 获取用户本次订购可用的促销优惠券信息
	 * </pre>
	 * 
	 * @param memberId
	 * @param itemType
	 * @return Map<String, List<CouponDTO>>key=优惠券类型 0 : 满减券 1：现金券
	 *         ，value=用户的优惠券列表
	 */
	Map<String, List<OrderCouponDTO>> getOrderCouponInfo(Long memberId, Integer sourceType,Integer itemType);
	
	/**
	 * 
	 * <pre>
	 * 抢购获取用户本次订购可用的促销优惠券信息
	 * </pre>
	 * 
	 * @param memberId
	 * @param itemType
	 * @return Map<String, List<CouponDTO>>key=优惠券类型 0 : 满减券 1：现金券
	 *         ，value=用户的优惠券列表
	 */
	Map<String, List<OrderCouponDTO>> getOrderCouponInfoBuyNow(Long memberId, Integer sourceType, Integer itemType, String uuid);

	/**
	 * 
	 * <pre>
	 * 计算海淘购物车中海淘商品的价格
	 * </pre>
	 * 
	 * @param cartDTO
	 * @param couponIdList
	 * @return
	 */
	SeaOrderItemDTO calcItemPrice4Sea(Long memberId,List<Long> couponIdList,Integer sourceType);
	
	/**
	 * 
	 * <pre>
	 * 计算购物车中商品的价格 (快速购买)
	 * </pre>
	 * 
	 * @param buyNowId
	 * @param couponIdList
	 * @return
	 */
	CartDTO calcItemPriceBuyNow(Long memberId,String buyNowId, List<Long> couponIdList);
	/**
	 * 
	 * <pre>
	 * 计算海淘购物车中海淘商品的价格 (快速购买)
	 * </pre>
	 * 
	 * @param buyNowId
	 * @param couponIdList
	 * @return
	 */
	SeaOrderItemDTO calcItemPrice4SeaBuyNow(Long memberId,String buyNowId,List<Long> couponIdList,Integer sourceType);
	
}
