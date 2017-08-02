package com.tp.service.ord;

import java.util.List;
import java.util.Map;

import com.tp.common.vo.PageInfo;
import com.tp.dto.ord.OrderItemCMBC;
import com.tp.model.ord.SubOrder;
import com.tp.query.ord.SubOrderQO;
import com.tp.result.ord.SubOrderExpressInfoDTO;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 子订单表接口
  */
public interface ISubOrderService extends IBaseService<SubOrder>{

	/**
	 * 根据订单号获取快递信息列表
	 * @param code
	 * @param packageNo
	 * @return
	 */
	public List<SubOrderExpressInfoDTO> queryExpressLogInfo(String code, String packageNo);

	/**
	 * 根据子订单编码查询订单信息
	 * @param orderNo
	 * @return
	 */
	public SubOrder findSubOrderByCode(Long orderNo);

	List<SubOrder> selectByCodeAndMemberID(Long subOrderCode, Long memberId);

	Integer selectCountDynamic(SubOrder sub);

	Integer selectPaymentCount(Long memberId,List<Integer> orderTypeList,String channelCode);

	List<SubOrder> querySubOrderToFisherAfterPayThirtyMinutes(
			Map<String, Object> inputArgument);

	List<SubOrder> querySubOrderBySeaOrderUnPayIsExpired(int minute);

	List<SubOrder> selectListByOrderCode(Long parentOrderCode);

	List<SubOrder> querySubOrderByWaitPutSeaWashes(Map<String, Object> map);

	Integer batchUpdateSubOrderStatusByCode(Map<String, Object> map);

	List<SubOrder> selectListByIdList(List<Long> idList);

	List<SubOrder> selectListByCodeList(List<Long> subCodeList);

	Integer deleteByCode(Long orderCode, Long memberId);

	List<SubOrder> selectListByOrderIdList(List<Long> orderIdList);

	Integer updateSubOrderStatus(SubOrder subOrder);

	SubOrder selectOneByCode(Long code);

	PageInfo<SubOrder> selectPageByQO(SubOrderQO query);
	
	PageInfo<SubOrder> selectPageByQO_coupons_shop_scan(SubOrderQO query);
	
	boolean orderCancelCheck(Long subOrderCode, Long memberId);

	void operateAfterCancel(Long subOrderCode, Long memberId, String userName);

	void updateOrderPutStatus(Long subOrderCode, Boolean isSuccess,
			String message);
	
	/**
	 * 根据分销、推广员统计订单金额及总数
	 * @param params
	 * @return
	 */
	public Map<String, Number> queryOrderCountByPromoterId(Map<String, Object> params);
	
	public List<SubOrder> queryUndeclaredSubOrders(Map<String, Object> map);
	
	public List<SubOrder> queryUnPutWaybillSubOrders(Map<String, Object> map);
	
	Integer getTotalCustomersForPormoter(SubOrderQO query);
	
	Double getShopTotalSales(Long promoterId);
	
	Double getShopTotalSales_coupons_shop_scan(Map<String, Object> promoterId);
		
	List<OrderItemCMBC> selectOrderItemsForPushCMBC(Map<String, Object> params);

	List<SubOrder> selectSubOrderForPushCMBC(Map<String, Object> params);	
}
