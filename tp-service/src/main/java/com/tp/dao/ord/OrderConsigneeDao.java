package com.tp.dao.ord;

import org.apache.ibatis.annotations.Param;

import com.tp.common.dao.BaseDao;
import com.tp.model.ord.OrderConsignee;

public interface OrderConsigneeDao extends BaseDao<OrderConsignee> {

	OrderConsignee selectOneByParentOrderCode(@Param("parentOrderCode")Long parentOrderCode);
	OrderConsignee selectOneByParentOrderId(@Param("parentOrderId")Long parentOrderId);
}
