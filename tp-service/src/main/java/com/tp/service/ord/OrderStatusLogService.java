package com.tp.service.ord;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.OrderConstant;
import com.tp.common.vo.OrderConstant.ORDER_STATUS;
import com.tp.common.vo.ord.LogTypeConstant.LOG_TYPE;
import com.tp.dao.ord.OrderInfoDao;
import com.tp.dao.ord.OrderStatusLogDao;
import com.tp.dao.ord.SubOrderDao;
import com.tp.model.ord.OrderInfo;
import com.tp.model.ord.OrderStatusLog;
import com.tp.model.ord.SubOrder;
import com.tp.service.BaseService;
import com.tp.service.ord.IOrderStatusLogService;

@Service
public class OrderStatusLogService extends BaseService<OrderStatusLog> implements IOrderStatusLogService {

	@Autowired
	private OrderStatusLogDao orderStatusLogDao;
	@Autowired
	private OrderInfoDao orderInfoDao;
	@Autowired
	private SubOrderDao subOrderDao;
	
	@Override
	public BaseDao<OrderStatusLog> getDao() {
		return orderStatusLogDao;
	}
	@Override
	public Integer batchInsert(List<OrderStatusLog> orderStatusLogDOList) {
		return orderStatusLogDao.batchInsert(orderStatusLogDOList);
	}
	@Override
	@Transactional
	public void updateCanceledStatus(OrderInfo order, String username, LOG_TYPE logType) {
		// 更新父订单信息
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setParentOrderCode(order.getParentOrderCode());
		orderInfo.setOrderStatus(ORDER_STATUS.CANCEL.code);
		orderInfo.setUpdateTime(new Date());
		orderInfoDao.updateStatusByCode(orderInfo);

		// 更新子订单信息
		SubOrder subQO = new SubOrder();
		subQO.setParentOrderCode(order.getParentOrderCode());
		subQO.setOrderStatus(ORDER_STATUS.CANCEL.code);
		subQO.setUpdateTime(new Date());
		subOrderDao.updateSubOrderStatus(subQO);

		// 添加订单状态日志信息
		OrderStatusLog orderStatusLog = new OrderStatusLog();
		orderStatusLog.setParentOrderCode(order.getParentOrderCode());
		orderStatusLog.setPreStatus(order.getOrderStatus());
		orderStatusLog.setCurrStatus(ORDER_STATUS.CANCEL.code);
		orderStatusLog.setContent("父订单取消");
		orderStatusLog.setCreateTime(new Date());
		orderStatusLog.setCreateUserId(order.getMemberId());
		orderStatusLog.setCreateUserName(username);
		orderStatusLog.setCreateUserType(logType.code);
		orderStatusLogDao.insert(orderStatusLog);

		List<SubOrder> subOrderList = getSubOrderByOrderCode(order.getParentOrderCode());
		for (SubOrder subOrder : subOrderList) {
			orderStatusLog = new OrderStatusLog();
			orderStatusLog.setParentOrderCode(order.getParentOrderCode());
			orderStatusLog.setOrderCode(subOrder.getOrderCode());
			orderStatusLog.setPreStatus(subOrder.getOrderStatus());
			orderStatusLog.setCurrStatus(ORDER_STATUS.CANCEL.code);
			orderStatusLog.setCreateTime(new Date());
			orderStatusLog.setCreateUserType(logType.code);
			orderStatusLog.setContent("子订单取消");
			orderStatusLog.setCreateUserId(subOrder.getMemberId());
			orderStatusLog.setCreateUserName(username);
			orderStatusLogDao.insert(orderStatusLog);
		}
	}

	private List<SubOrder> getSubOrderByOrderCode(Long orderCode) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("parentOrderCode", orderCode);
		return subOrderDao.queryByParam(params);
	}

	@Override
	public void updateCanceledStatus(SubOrder subOrder, long userId, String username, LOG_TYPE logType) {
		// 更新子订单信息
		subOrder.setOrderStatus(ORDER_STATUS.CANCEL.code);
		subOrder.setUpdateTime(new Date());
		subOrderDao.updateSubOrderStatus(subOrder);

		/* 所有子单都取消，则取消父单 */
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("orderCode", subOrder.getOrderCode());
		List<SubOrder> subList = subOrderDao.queryByParam(params);
		boolean isAllCanceld = true;
		for (SubOrder subO : subList) {
			if (!ORDER_STATUS.CANCEL.code.equals(subO.getOrderStatus())) {
				isAllCanceld = false;
				break;
			}
		}
		if (isAllCanceld) {
			OrderInfo salesOrderForUpdate = new OrderInfo();
			salesOrderForUpdate.setParentOrderCode(subOrder.getParentOrderCode());
			salesOrderForUpdate.setOrderStatus(OrderConstant.ORDER_STATUS.CANCEL.code);
			salesOrderForUpdate.setUpdateTime(new Date());
			orderInfoDao.updateSalesOrderStatusAfterCancel(salesOrderForUpdate);
		}

		// 添加订单状态日志信息
		OrderStatusLog orderStatusLog = new OrderStatusLog();
		orderStatusLog.setParentOrderCode(subOrder.getParentOrderCode());
		orderStatusLog.setOrderCode(subOrder.getOrderCode());
		orderStatusLog.setPreStatus(subOrder.getOrderStatus());
		orderStatusLog.setCurrStatus(OrderConstant.ORDER_STATUS.CANCEL.code);
		orderStatusLog.setCreateTime(new Date());
		orderStatusLog.setCreateUserId(userId);
		orderStatusLog.setCreateUserName(username);
		orderStatusLog.setCreateUserType(logType.code);
		orderStatusLogDao.insert(orderStatusLog);
	}
}
