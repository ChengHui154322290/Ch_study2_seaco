/**
 * 
 */
package com.tp.model.wms.sto;

import java.io.Serializable;

/**
 * @author Administrator
 * 申通出库单明细
 */
public class StoStockoutOrderDetail implements Serializable{

	private static final long serialVersionUID = -1723806092589200025L;

	/** 客户订单号 */
	private String txLogisticID;
	
	/** 收件人 */
	private String receiveMan;
	
	/** 收件省 */
	private String receiveProvince;
	
	/** 收件市 */
	private String receiveCity;
	
	/** 收件区 */
	private String receiveCounty;
	
	/** 收件人地址 */
	private String receiveManAddress;
	
	/** 收件人电话 */
	private String  receiveManPhone;
	
	/** 商品名称 */
	private String itemName;
	
	/** 商品SKU */
	private String itemSku;
	
	/** 商品数量 */
	private Integer itemCount;
	
	/** 商品单价 */
	private Double unitPrice;
	
	/** 商品总价 */
	private Double allPrice;
	
	/** 商品重量 */
	private Double itemWeight;
	
	/** 运费 */
	private Double feeAmount;
	
	/** 保费 */
	private Double insureAmount;
	
	/** 购买人身份证号码 */
	private String buyerIdNumber;
	
	/** 购买人 */
	private String buyerName;
	
	/** 快递（例：STO、HTO等） */
	private String carrier;
	
	/** 发件仓库 */
	private String sendWarehouse;
	
	/** 商家编码 */
	private String merchantNum;
	
	/** 快递单号 */
	private String mailNo;
	
	/** 批次 */
	private String pc;

	public String getTxLogisticID() {
		return txLogisticID;
	}

	public void setTxLogisticID(String txLogisticID) {
		this.txLogisticID = txLogisticID;
	}

	public String getReceiveMan() {
		return receiveMan;
	}

	public void setReceiveMan(String receiveMan) {
		this.receiveMan = receiveMan;
	}

	public String getReceiveProvince() {
		return receiveProvince;
	}

	public void setReceiveProvince(String receiveProvince) {
		this.receiveProvince = receiveProvince;
	}

	public String getReceiveCity() {
		return receiveCity;
	}

	public void setReceiveCity(String receiveCity) {
		this.receiveCity = receiveCity;
	}

	public String getReceiveCounty() {
		return receiveCounty;
	}

	public void setReceiveCounty(String receiveCounty) {
		this.receiveCounty = receiveCounty;
	}

	public String getReceiveManAddress() {
		return receiveManAddress;
	}

	public void setReceiveManAddress(String receiveManAddress) {
		this.receiveManAddress = receiveManAddress;
	}

	public String getReceiveManPhone() {
		return receiveManPhone;
	}

	public void setReceiveManPhone(String receiveManPhone) {
		this.receiveManPhone = receiveManPhone;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemSku() {
		return itemSku;
	}

	public void setItemSku(String itemSku) {
		this.itemSku = itemSku;
	}

	public Integer getItemCount() {
		return itemCount;
	}

	public void setItemCount(Integer itemCount) {
		this.itemCount = itemCount;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Double getAllPrice() {
		return allPrice;
	}

	public void setAllPrice(Double allPrice) {
		this.allPrice = allPrice;
	}

	public Double getItemWeight() {
		return itemWeight;
	}

	public void setItemWeight(Double itemWeight) {
		this.itemWeight = itemWeight;
	}

	public Double getFeeAmount() {
		return feeAmount;
	}

	public void setFeeAmount(Double feeAmount) {
		this.feeAmount = feeAmount;
	}

	public Double getInsureAmount() {
		return insureAmount;
	}

	public void setInsureAmount(Double insureAmount) {
		this.insureAmount = insureAmount;
	}

	public String getBuyerIdNumber() {
		return buyerIdNumber;
	}

	public void setBuyerIdNumber(String buyerIdNumber) {
		this.buyerIdNumber = buyerIdNumber;
	}

	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	public String getCarrier() {
		return carrier;
	}

	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}

	public String getSendWarehouse() {
		return sendWarehouse;
	}

	public void setSendWarehouse(String sendWarehouse) {
		this.sendWarehouse = sendWarehouse;
	}

	public String getMerchantNum() {
		return merchantNum;
	}

	public void setMerchantNum(String merchantNum) {
		this.merchantNum = merchantNum;
	}

	public String getMailNo() {
		return mailNo;
	}

	public void setMailNo(String mailNo) {
		this.mailNo = mailNo;
	}

	public String getPc() {
		return pc;
	}

	public void setPc(String pc) {
		this.pc = pc;
	}
}
