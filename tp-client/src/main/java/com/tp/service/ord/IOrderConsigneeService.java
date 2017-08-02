package com.tp.service.ord;

import java.util.List;

import com.tp.model.ord.OrderConsignee;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 订单收货人表接口
  */
public interface IOrderConsigneeService extends IBaseService<OrderConsignee>{

	OrderConsignee selectOneByOrderId(long orderId);

	List<OrderConsignee> selectListByOrderIdList(List<Long> orderIdList);

	OrderConsignee selectOneByOrderCode(Long orderCode);

}
