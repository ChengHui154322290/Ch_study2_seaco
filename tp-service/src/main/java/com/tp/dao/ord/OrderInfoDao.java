package com.tp.dao.ord;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tp.common.dao.BaseDao;
import com.tp.model.ord.OrderInfo;

public interface OrderInfoDao extends BaseDao<OrderInfo> {

	Integer updateStatusByCode(OrderInfo orderInfo);

	Integer updateSalesOrderStatusAfterCancel(OrderInfo salesOrderForUpdate);

	int deleteByCode(OrderInfo order);

	List<OrderInfo> querySalesOrderByUnPayIsExpired(int minute);

	List<OrderInfo> querySalesOrderByUnPayOverFifteenMinutes(int minute);

	Integer updateSalesOrderStatusAfterSuccessPay(OrderInfo orderInfo);

	OrderInfo selectOneByCode(@Param("parentOrderCode")Long parentOrderCode);

	List<OrderInfo> getOrderNeedPushToCMBC(@Param("statusList") List<Integer> statusList);
}
