package com.tp.dto.stg;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


public class OutputBackDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6225405375138558311L;

	/** 即订单编号，*/
	private String orderNo;
	
	/** 运单号 */
	private String shipNo;
	
	/** 发运时间 */
	private Date shipTime;
	
	/** 发送的物流公司名 */
	private String carrierId;
	
	/** 发送的物流公司名 */
	private String carrierName;
	
	/** 客户id */
	private String customerId;
	
	/** 对应仓库的id号 */
	private String bgNo;
	
	/** 仓库id */
	private Long varehouseId;
	
	/** 订单包裹重量 */
	private Double weight;
	
	private List<OutputBackDetailDto> details;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getShipNo() {
		return shipNo;
	}

	public void setShipNo(String shipNo) {
		this.shipNo = shipNo;
	}

	public Date getShipTime() {
		return shipTime;
	}

	public void setShipTime(Date shipTime) {
		this.shipTime = shipTime;
	}

	public String getCarrierId() {
		return carrierId;
	}

	public void setCarrierId(String carrierId) {
		this.carrierId = carrierId;
	}

	public String getCarrierName() {
		return carrierName;
	}

	public void setCarrierName(String carrierName) {
		this.carrierName = carrierName;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getBgNo() {
		return bgNo;
	}

	public void setBgNo(String bgNo) {
		this.bgNo = bgNo;
	}

	public Long getVarehouseId() {
		return varehouseId;
	}

	public void setVarehouseId(Long varehouseId) {
		this.varehouseId = varehouseId;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public List<OutputBackDetailDto> getDetails() {
		return details;
	}

	public void setDetails(List<OutputBackDetailDto> details) {
		this.details = details;
	}

}
