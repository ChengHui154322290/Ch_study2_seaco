package com.tp.service.ord;

import java.util.List;

import com.tp.common.vo.PageInfo;
import com.tp.model.ord.OrderInfo;
import com.tp.model.ord.SubOrder;
import com.tp.query.ord.SalesOrderQO;
import com.tp.result.ord.OrderResult;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 订单表接口
  */
public interface IOrderInfoService extends IBaseService<OrderInfo>{

	/**
	 * 根据订单编码查询订单全部信息
	 * @param orderCode
	 * @return
	 */
	OrderResult queryOrderResultByCode(Long orderCode);
	
	/**
	 * 根据父订单主键查询订单信息
	 * @param orderId
	 * @return
	 */
	OrderResult queryOrderById(Long orderId);

	List<OrderInfo> selectListByIdList(List<Long> idList);

	OrderInfo selectOneByCode(Long code);

	PageInfo<OrderInfo> selectPageByQO(SalesOrderQO qo);

	Integer deleteByCode(Long code, Long memberId);

	List<OrderInfo> querySalesOrderByUnPayIsExpired(int minute);

	List<OrderInfo> querySalesOrderByUnPayOverFifteenMinutes(int minute);

	List<OrderInfo> selectByCodeAndMemberID(Long orderCode, Long memberId);
	
	/**
	 * 拆分订单金额
	 * @param orderDto
	 * @return
	 */
	public Boolean updateOrderSplitAmount(List<SubOrder> subOrderList);
	
	List<OrderInfo> getOrderNeedPushToCMBC(List<Integer> statuslist);
}
