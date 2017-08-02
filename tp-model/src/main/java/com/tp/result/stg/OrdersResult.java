package com.tp.result.stg;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * <pre>
 * 入库明细信息(结果封装类撒)
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 */
public class OrdersResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2548289114897150236L;
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
	private List<OrderDetailsResult> list;


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

	public List<OrderDetailsResult> getList() {
		return list;
	}

	public void setList(List<OrderDetailsResult> list) {
		this.list = list;
	}

}
