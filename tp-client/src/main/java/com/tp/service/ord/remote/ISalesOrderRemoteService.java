package com.tp.service.ord.remote;

import java.util.List;
import java.util.Map;

import com.tp.common.vo.PageInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.ord.OrderReceiveGoodsDTO;
import com.tp.dto.ord.remote.OrderCountDTO;
import com.tp.dto.ord.remote.OrderDetails4UserDTO;
import com.tp.dto.ord.remote.OrderList4UserDTO;
import com.tp.dto.ord.remote.SubOrder4BackendDTO;
import com.tp.dto.ord.remote.SubOrder4CouponDTO;
import com.tp.dto.ord.remote.YiQiFaOrderDto;
import com.tp.dto.stg.ResultOrderDeliverDTO;
import com.tp.model.ord.MemRealinfo;
import com.tp.model.ord.OrderConsignee;
import com.tp.model.ord.OrderDelivery;
import com.tp.model.ord.OrderInfo;
import com.tp.model.ord.OrderItem;
import com.tp.model.ord.OrderPromotion;
import com.tp.model.ord.OrderStatusLog;
import com.tp.model.ord.SubOrder;
import com.tp.query.ord.SubOrderQO;
import com.tp.result.ord.SubOrderExpressInfoDTO;

/**
 * 订单远程服务
 * 
 * @author szy
 * @version 0.0.1
 */
public interface ISalesOrderRemoteService {

    /**
     * 会员中心 - 订单列表查询
     * 
     * @param query
     * @return
     */
    PageInfo<OrderList4UserDTO> findOrderList4UserPage(SubOrderQO query);

    /**
     * 会员中心 - 退货订单列表查询
     * 
     * @param query
     * @return
     */
    PageInfo<OrderList4UserDTO> findRejectOrderList4UserPage(SubOrderQO query);

    /**
     * 会员中心 - 订单详情查询
     * 
     * @param memberId
     * @param code
     * @param memberId
     * @param code
     * @return
     */
    OrderDetails4UserDTO findOrderDetails4User(Long memberId, Long code);

    /**
     * 根据（父/子）订单号逻辑删除
     * 
     * @param memberId
     * @param code
     * @return 影响行数
     */
    int deleteByCode(Long memberId, Long code);

    /**
     * 后台查询
     * 
     * @param query
     * @return
     */
    PageInfo<SubOrder4BackendDTO> findSubOrder4BackendPage(SubOrderQO query);

    /**
     * 根据(父/子)订单号查询
     * 
     * @param code
     * @return
     */
    SubOrder4BackendDTO findSubOrder4BackendByCode(Long code);

    /**
     * <pre>
     * 发货后的订单操作
     * </pre>
     * 
     * @param orderDeliver
     */
    void operateOrderForDeliver(OrderDelivery orderDeliver);

    /**
     * 
     * <pre>
     * 批量处理 发货后的订单操作
     * </pre>
     * 
     * @param orderDeliverList
     *            需要处理的订单发货信息
     * @return 订单处理结果，errorSize表示处理失败数据条数，errorDataList表示失败的元素，orderOperatorErrorList列表记录错误原因
     */
    ResultOrderDeliverDTO operateOrderListForDeliver(List<OrderDelivery> orderDeliverList);

    /**
     * 
     * <pre>
     * 收货后订单操作
     * </pre>
     * 
     * @param orderReceiveGoodsDTO
     */
    void operateOrderForReceiveGoods(OrderReceiveGoodsDTO orderReceiveGoodsDTO);

    /**
     * 
     * <pre>
     * 收货后订单操作,用户操作
     * </pre>
     * 
     * @param memberID
     * @param orderReceiveGoodsDTO
     */
    void operateOrderForReceiveGoodsByUser(Long memberID, OrderReceiveGoodsDTO orderReceiveGoodsDTO);

    /**
     * 
     * <pre>
     * 根据子订单编号查询子订单
     * </pre>
     * 
     * @param code
     * @return
     */
    SubOrder findSubOrderByCode(Long code);

    /**
     * 根据（父/子）订单号查询日志
     * 
     * @param code
     * @return
     */
    List<OrderStatusLog> findStatusLogListByCode(Long code);

    /**
     * 根据父订单编号获取支付ID号
     * 
     * @param orderNo
     * @return
     */
    ResultInfo<Long> queryOrderPaymentInfoByOrderNo(Long orderNo);

    /**
     * 根据优惠券ID查询子订单优惠信息
     * 
     * @param couponId
     * @return
     */
    SubOrder4CouponDTO findSubOrderCouponByCouponId(Long couponId);

    /**
     * 根据支付等待时间查询未支付的到期的订单
     * 
     * @param minute
     *            时间差默认30分过期
     * @return
     */
    List<OrderInfo> querySalesOrderByUnPayIsExpired(int minute);

    List<SubOrder> querySubOrderBySeaOrderUnPayIsExpired(int minute);

    /**
     * 
     * <pre>
     * 查询物流日志信息列表 ,接口后续会切换到新的接口
     * </pre>
     * 
     * @param code
     *            子订单编号或者退货单号，必填
     * @param packageNo
     *            运单编号，选填
     * @return
     */
    List<SubOrderExpressInfoDTO> queryExpressLogInfo(Long code, String packageNo);

    /**
     * 
     * <pre>
     * 查询物流日志信息列表 用户操作  ,接口后续会切换到新的接口
     * </pre>
     * 
     * @param memberId
     *            会员ID
     * @param code
     *            子订单编号或者退货单号，必填
     * @param packageNo
     *            运单编号，选填
     * @return
     */
    List<SubOrderExpressInfoDTO> queryExpressLogInfoByUser(Long memberId, Long code, String packageNo);

    /**
     * 查询等待系统确认收货的订单
     * 
     * @param receivedDays
     * @param startPage
     * @param pageSize
     * @return
     */
    PageInfo<SubOrder> querySubOrderPageByReceived(int receivedDays, int startPage, int pageSize);

    /**
     * 根据支付等待时间查询下单超过15分钟未支付的订单
     * 
     * @param minute
     *            时间差默认15分未支付发短信提醒
     * @return
     */
    List<OrderInfo> querySalesOrderByUnPayOverFifteenMinutes(int minute);

    /**
     * 海豚订单支付成功30分钟后推送数据到仓库
     * 
     * @param minute
     * @return
     */
    List<SubOrder> querySubOrderByWaitPutSeaWashes(Map<String, Object> params);

    /**
     * 根据子订单推送商品信息到仓库
     * 
     * @param subOrderDO
     * @param orderCode
     */
    public boolean putWareHouseShippingBySubOrder(SubOrder subOrderDO);

    /**
     * 根据订单号查询促销信息列表
     * 
     * @param orderCode
     * @return
     */
    List<OrderPromotion> findPromotionListByOrderCode(Long orderCode);

    /**
     * 根据订单号查询优惠券
     * 
     * @param orderCode
     * @return
     */
    List<OrderPromotion> findCouponListByOrderCode(Long orderCode);

    /**
     * 查询支付30分钟之后需要推送到fisher平台的订单
     * 
     * @param minute
     * @return
     */

    List<SubOrder> querySubOrderToFisherAfterPayThirtyMinutes(Map<String, Object> inputArgument);

    /**
     * 查询订单对应的收货地址信息
     * 
     * @param orderCode
     * @return OrderConsigneeDO
     */
    OrderConsignee queryOrderConsigneeInfoByOrderCode(Long code);

    /**
     * 查询订单对应的用户实名认证信息
     * 
     * @param orderCode
     * @return MemRealinfoDO
     */
    MemRealinfo getOrderMemRealinfoByOrderCode(Long orderCode);

    /**
     * 查询子订单对应的商品行信息
     * 
     * @param orderId
     * @return OrderItem
     */
    List<OrderItem> getOrderLInesInfoBySubOrderId(Long orderId);

    /**
     * 根据会员id查询订单计数
     * 
     * @param memberId
     * @return
     */
    OrderCountDTO findOrderCountDTOByMemberId(long memberId,List<Integer> orderTypeList,String channelCode);

    /**
     * 根据子单号批量查询子单, null safe
     * 
     * @param codeList
     * @return
     */
    List<SubOrder> findSubOrderDTOListBySubCodeList(List<Long> subCodeList);
    
    PageInfo<OrderList4UserDTO> findOrderList4UserPageByQO(SubOrderQO qo);
    
    ResultInfo<Integer> getTotalCustomersForPromoterByQO(SubOrderQO qo);
    
    PageInfo<OrderList4UserDTO> findOrderList4UserPageByQO_coupons_shop_scan(SubOrderQO qo);
    
    /**
     *	保税区自营订单六十分钟后推送到海关 
     */
    List<SubOrder> queryUndeclaredSubOrders(Map<String, Object> map);
    
    /**
     * 海淘自营订单推送第三方物流公司 
     */
    List<SubOrder> queryUnPutWaybillSubOrders(Map<String, Object> map);
    
    /**
     * 海淘自营订单发货
     */
	boolean putWareHouseShippingBySeaSubOrder(SubOrder subOrder);
    
	/***
	 * 发货后发送MQ消息
	 * @param subOrderList
	 */
	public void sendMqMessageByDelivery(List<SubOrder> subOrderList);
}
