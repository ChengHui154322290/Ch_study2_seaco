package com.tp.query.ord;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.tp.model.ord.OrderInfo;

/**
 * 父订单查询对象
 * 
 * @author szy
 * @version 0.0.1
 */
public class SalesOrderQO extends OrderInfo {

	private static final long serialVersionUID = 7070872784217813262L;

	/** 订单状态列表 */
	private List<Integer> statusList;
	
	
	public List<Integer> getStatusList() {
		return CollectionUtils.isEmpty(statusList) ? null : statusList;
	}
	public void setStatusList(List<Integer> statusList) {
		this.statusList = statusList;
	}
}
