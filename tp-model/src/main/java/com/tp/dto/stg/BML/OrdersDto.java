package com.tp.dto.stg.BML;

import java.util.List;

/**
 * 
 * <pre>
 * 出库明细查询信息
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 */
public class OrdersDto {

	/**
	 * 仓库SO编号
	 */
	private String SONo;
	/**
	 * 管理号
	 */
	private String CustmorOrderNo1;
	/**
	 * 订单号
	 */
	private String CustmorOrderNo2;
	/**
	 * 订单金额
	 */
	private String OrderPrice;
	/**
	 * 承运人
	 */
	private String CarrierID;
	/**
	 * 销售渠道
	 */
	private String WebSite;

	/**
	 * orderd的详细信息
	 */
	private List<OrderDetails> list;

	/**
	 * 订单类型
	 */
	private String OrderType;

	public String getSONo() {
		return SONo;
	}

	public void setSONo(String sONo) {
		SONo = sONo;
	}

	public String getCustmorOrderNo1() {
		return CustmorOrderNo1;
	}

	public void setCustmorOrderNo1(String custmorOrderNo1) {
		CustmorOrderNo1 = custmorOrderNo1;
	}

	public String getCustmorOrderNo2() {
		return CustmorOrderNo2;
	}

	public void setCustmorOrderNo2(String custmorOrderNo2) {
		CustmorOrderNo2 = custmorOrderNo2;
	}

	public String getOrderPrice() {
		return OrderPrice;
	}

	public void setOrderPrice(String orderPrice) {
		OrderPrice = orderPrice;
	}

	public String getCarrierID() {
		return CarrierID;
	}

	public void setCarrierID(String carrierID) {
		CarrierID = carrierID;
	}

	public String getWebSite() {
		return WebSite;
	}

	public void setWebSite(String webSite) {
		WebSite = webSite;
	}


	public List<OrderDetails> getList() {
		return list;
	}

	public void setList(List<OrderDetails> list) {
		this.list = list;
	}

	public String getOrderType() {
		return OrderType;
	}

	public void setOrderType(String orderType) {
		OrderType = orderType;
	}

}
