package com.tp.proxy.ord;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.model.ord.OrderStatusLog;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.ord.IOrderStatusLogService;
/**
 * 订单状态日志表代理层
 * @author szy
 *
 */
@Service
public class OrderStatusLogProxy extends BaseProxy<OrderStatusLog>{

	@Autowired
	private IOrderStatusLogService orderStatusLogService;

	@Override
	public IBaseService<OrderStatusLog> getService() {
		return orderStatusLogService;
	}

	public List<OrderStatusLog> queryByOrderCode(String orderCode) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " order_code="+orderCode+" or parent_order_code="+orderCode );
		return orderStatusLogService.queryByParam(params);
	}
}
