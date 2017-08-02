package com.tp.dao.ord;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.tp.common.dao.BaseDao;
import com.tp.dto.ord.OrderItemCMBC;
import com.tp.model.ord.SubOrder;
import com.tp.query.ord.SubOrderQO;

public interface SubOrderDao extends BaseDao<SubOrder> {

	Integer updateSubOrderStatus(SubOrder subOrder);

	Integer deleteByOrderCode(SubOrder sub);

	List<SubOrder> selectPageByQO(SubOrderQO query);
	List<SubOrder> selectPageByQO_coupons_shop_scan(Map<String, Object> querymap);

	Integer selectCountByQO(SubOrderQO query);
	Integer selectCountByQO_coupons_shop_scan(Map<String, Object> querymap);

	Integer deleteByCode(SubOrder sub);

	Integer batchUpdateSubOrderStatusByCode(Map<String, Object> map);

	List<SubOrder> querySubOrderByWaitPutSeaWashes(Map<String, Object> map);

	List<SubOrder> querySubOrderBySeaOrderUnPayIsExpired(int minute);

	List<SubOrder> querySubOrderToFisherAfterPayThirtyMinutes(
			Map<String, Object> inputArgument);

	Integer selectPaymentCount(@Param("memberId")Long memberId,@Param("orderTypeList")List<Integer> orderTypeList,@Param("channelCode") String channelCode);

	SubOrder selectOneByCode(@Param("orderCode")Long orderCode);

	Long queryOrderCodeBySubOrderCodeAndCancelStatus(@Param("subOrderCode")Long subOrderCode);

	List<SubOrder> selectListByIdList(List<Long> subOrderIdList);

	void batchInsert(@Param("subOrderList") List<SubOrder> subOrderList);
	Map<String, Number> queryOrderCountByPromoterId(Map<String, Object> params);
	/**取消子订单*/
	Integer updateSubOrderListByCancel(@Param("orderCodeList")List<Long> orderCodeList,@Param("orderStatus")Integer orderStatus,@Param("currentStatus")Integer currentOrderStatus,@Param("cancelReason") String cancelReason);
	
	Integer getTotalCustomersForPormoter(SubOrder subOrder);	
	
	Double getShopTotalSales(Long PromoterId);
	Double getShopTotalSales_coupons_shop_scan(Map<String,Object> params);
	
	List<OrderItemCMBC> selectOrderItemsForPushCMBC(Map<String, Object> params);
	List<SubOrder> selectSubOrderForPushCMBC(Map<String, Object> params);
	
	List<SubOrder> queryUndeclaredSubOrders(Map<String, Object> inputArgument);
	
	List<SubOrder> queryUnPutWaybillSubOrders(Map<String, Object> inputArgument);	
}
