package com.tp.service.ord;

import java.util.List;

import com.tp.model.ord.OrderReceipt;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 订单发票表接口
  */
public interface IOrderReceiptService extends IBaseService<OrderReceipt>{

	OrderReceipt selectOneByOrderId(Long orderId);

	List<OrderReceipt> selectListByOrderIdList(List<Long> orderIdList);

}
