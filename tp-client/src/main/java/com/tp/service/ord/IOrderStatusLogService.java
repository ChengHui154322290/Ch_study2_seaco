package com.tp.service.ord;

import java.util.List;

import com.tp.common.vo.ord.LogTypeConstant.LOG_TYPE;
import com.tp.model.ord.OrderInfo;
import com.tp.model.ord.OrderStatusLog;
import com.tp.model.ord.SubOrder;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 订单状态日志表接口
  */
public interface IOrderStatusLogService extends IBaseService<OrderStatusLog>{

	void updateCanceledStatus(OrderInfo order, String username, LOG_TYPE logType);

	void updateCanceledStatus(SubOrder subOrder, long userId, String username,
			LOG_TYPE logType);

	Integer batchInsert(List<OrderStatusLog> orderStatusLogDOList);

}
