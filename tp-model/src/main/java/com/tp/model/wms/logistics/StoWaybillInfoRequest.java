package com.tp.model.wms.logistics;

import java.io.Serializable;

/**
 * @author administrator 
 * 申通快递运单推送信息
 */
public class StoWaybillInfoRequest implements Serializable{
	
	private static final long serialVersionUID = -179952045489678113L;

	/** 运单号 **/
	private String bill;
	
	/** 收件人姓名 **/
	private String consignee;
	
	/** 收件人地址 **/
	private String consigneeAddress;
	
	/** 收件人省市区 **/
	private String consigneeArea;
	
	/** 收件人电话 **/
	private String consigneeTel;
	
	/** 主要商品名称 **/
	private String goodsName;
	
	/** 包裹毛重 */
	private String grossWeight;
	
	/** 包裹净重 **/
	private String netWeight;
	
	/** 订单号 **/
	private String ordercode;
	
	/** 商品发货数量 **/
	private String packNo;
	
	/** 发货地址：例如浙江杭州 **/
	private String sendArea;
	
	/** 包裹价值 **/
	private String worth;
	
	/** 总提运单号 */
	private String totalWayBill;

	public String getBill() {
		return bill;
	}

	public void setBill(String bill) {
		this.bill = bill;
	}

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public String getConsigneeAddress() {
		return consigneeAddress;
	}

	public void setConsigneeAddress(String consigneeAddress) {
		this.consigneeAddress = consigneeAddress;
	}

	public String getConsigneeArea() {
		return consigneeArea;
	}

	public void setConsigneeArea(String consigneeArea) {
		this.consigneeArea = consigneeArea;
	}

	public String getConsigneeTel() {
		return consigneeTel;
	}

	public void setConsigneeTel(String consigneeTel) {
		this.consigneeTel = consigneeTel;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getGrossWeight() {
		return grossWeight;
	}

	public void setGrossWeight(String grossWeight) {
		this.grossWeight = grossWeight;
	}

	public String getNetWeight() {
		return netWeight;
	}

	public void setNetWeight(String netWeight) {
		this.netWeight = netWeight;
	}

	public String getOrdercode() {
		return ordercode;
	}

	public void setOrdercode(String ordercode) {
		this.ordercode = ordercode;
	}

	public String getPackNo() {
		return packNo;
	}

	public void setPackNo(String packNo) {
		this.packNo = packNo;
	}

	public String getSendArea() {
		return sendArea;
	}

	public void setSendArea(String sendArea) {
		this.sendArea = sendArea;
	}

	public String getWorth() {
		return worth;
	}

	public void setWorth(String worth) {
		this.worth = worth;
	}

	public String getTotalWayBill() {
		return totalWayBill;
	}

	public void setTotalWayBill(String totalWayBill) {
		this.totalWayBill = totalWayBill;
	}	
}
