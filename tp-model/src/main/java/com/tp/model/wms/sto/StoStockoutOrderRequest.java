/**
 * 
 */
package com.tp.model.wms.sto;

import java.io.Serializable;
import java.util.List;

/**
 * @author Administrator
 * 申通WMS出库单请求实体
 */
public class StoStockoutOrderRequest implements Serializable{
	

	private static final long serialVersionUID = 698569202125527683L;

	/** 用户名 */
	private String userName;
	
	/** 密码 */
	private String password;
	
	/** 订单列表 */
	private List<StoStockoutOrderDetail> orderList;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<StoStockoutOrderDetail> getOrderList() {
		return orderList;
	}

	public void setOrderList(List<StoStockoutOrderDetail> orderList) {
		this.orderList = orderList;
	}
}
