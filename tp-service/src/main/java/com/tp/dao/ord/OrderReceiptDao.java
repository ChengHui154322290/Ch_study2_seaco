package com.tp.dao.ord;

import org.apache.ibatis.annotations.Param;

import com.tp.common.dao.BaseDao;
import com.tp.model.ord.OrderReceipt;

public interface OrderReceiptDao extends BaseDao<OrderReceipt> {

	OrderReceipt selectOneByParentOrderCode(@Param("parentOrderCode")Long parentOrderCode);
	OrderReceipt selectOneByParentOrderId(@Param("parentOrderId")Long parentOrderId);
}
