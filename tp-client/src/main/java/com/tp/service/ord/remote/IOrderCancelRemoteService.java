package com.tp.service.ord.remote;


/**
 * 订单取消服务
 * 
 * @author szy
 * @version 0.0.1
 */
public interface IOrderCancelRemoteService {

	/**
	 * 付款前订单取消
	 * 
	 * @param orderCode
	 * @param userId
	 * @param userName
	 * @return
	 */
	void cancelOrder(Long orderCode, Long userId,String userName);
	
	/**
	 * 超时系统自动取消订单
	 * @param orderCode
	 * @param userId
	 * @param userName
	 */
	public void cancelOrderByJob(Long orderCode, Long userId, String userName);
	
	/**
	 * 兑换码失效自动取消订单
	 * @param orderCode
	 * @param userId
	 * @param userName
	 */
	public void cancelOrderRedeemByJob(Long orderCode, Long userId, String userName);
	
	/**
	 * 付款后子订单取消
	 * 
	 * @param subOrderCode
	 * @param userId
	 * @param userName
	 * @return
	 */
	void cancelOrderByPaymented(Long subOrderCode, Long userId,String userName);
	
	/**
	 * 后台取消订单
	 * 
	 * @param orderCode
	 * @param userId
	 * @param userName
	 */
	void cancelOrderByBackend(Long orderCode, Long userId,String userName,String reason);
}
