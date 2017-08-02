package com.tp.dto.ord.directOrderNB;

import java.io.Serializable;
import java.util.List;

public class RetMessageNBDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String Result;//T：成功，F：失败
	private String ResultMsg;//返回信息，成功时为空
	private String OrderStatus;//订单状态
	private String Logistics;//快递公司
	private String LogisticsNumber;//运单号
	private List<ProductNBDto> Products;
	
	public String getResult() {
		return Result;
	}
	public void setResult(String result) {
		Result = result;
	}
	public String getResultMsg() {
		return ResultMsg;
	}
	public void setResultMsg(String resultMsg) {
		ResultMsg = resultMsg;
	}
	public String getOrderStatus() {
		return OrderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		OrderStatus = orderStatus;
	}
	public String getLogistics() {
		return Logistics;
	}
	public void setLogistics(String logistics) {
		Logistics = logistics;
	}
	public String getLogisticsNumber() {
		return LogisticsNumber;
	}
	public void setLogisticsNumber(String logisticsNumber) {
		LogisticsNumber = logisticsNumber;
	}
	public List<ProductNBDto> getProducts() {
		return Products;
	}
	public void setProducts(List<ProductNBDto> products) {
		Products = products;
	}
	
	
	
}
