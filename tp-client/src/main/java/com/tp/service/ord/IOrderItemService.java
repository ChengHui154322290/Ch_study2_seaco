package com.tp.service.ord;

import java.util.List;

import com.tp.model.ord.OrderItem;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 订单行表接口
  */
public interface IOrderItemService extends IBaseService<OrderItem>{

	List<OrderItem> selectListByOrderIdList(List<Long> orderIdList);
	
	List<OrderItem> selectListByParentOrderIdList(List<Long> orderIdList);

	List<OrderItem> selectListBySubId(long subOrderId);

	List<OrderItem> selectListByOrderId(long orderId);

}
