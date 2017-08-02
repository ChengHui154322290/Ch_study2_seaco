package com.tp.dao.ord;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tp.common.dao.BaseDao;
import com.tp.model.ord.OrderItem;

public interface OrderItemDao extends BaseDao<OrderItem> {

	List<OrderItem> selectBySubOrderId(@Param("orderId")Long orderId);
	List<OrderItem> selectBySubOrderCode(@Param("orderCode")Long orderCode);
	List<OrderItem> selectByParentOrderId(@Param("parentOrderId")Long parentOrderId);
	List<OrderItem> selectByParentOrderCode(@Param("parentOrderCode")Long parentOrderCode);
	void batchInsert(@Param("orderItemList") List<OrderItem> orderItemList);
	int updateOrderItemAmount(OrderItem orderItem);
}
