package com.tp.model.stg.vo.feedback;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.tp.util.EnhanceDateConverter;



public class OutputBackVO implements Serializable{

	private static final long serialVersionUID = 9066698393178301238L;

	private String orderNo;
	
	private String shipNo;
	
	@XStreamConverter(value = EnhanceDateConverter.class, strings = {"yyyy-MM-dd HH:mm:ss" })
	private Date shipTime;
	
	private String carrierID;
	
	private String carrierName;
	
	private String customerId;
	
	private String bgNo;
	
	private double weight;
	
	private List<SkuVO>  send;

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

	public String getCarrierID() {
		return carrierID;
	}

	public void setCarrierID(String carrierID) {
		this.carrierID = carrierID;
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

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public List<SkuVO> getSend() {
		return send;
	}

	public void setSend(List<SkuVO> send) {
		this.send = send;
	}

}
