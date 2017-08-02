/**
 * 
 */
package com.tp.dto.ord.fisher;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author szy
 *
 */
public class OrderInfoToFisherDTO implements Serializable  {

	private static final long serialVersionUID = -4092728591854831872L;

	/** 订单编号 */
	private String orderCode ;
	
	/** 下单时间 */
	private Date orderDate;
	
	/** 外部平台订单编号 */
	private String outCode ;

	/** 快递公司 */
	private String expressName ;
	
	/** 快递运单号 */
	private String expressNo ;
	
	/** 收货姓名 */
	private String receiverName ;
	
	/** 买家昵称 */
	private String buyernick ;
	
	/** 手机号码 */
	private String mobile ;
	
	/** 仓库代码 */
	private String warehouseCode ;
	
	/** 店铺ID */
	private String shopid;
	
	/** 店铺名称 */
	private String shop ;
	
	/** 买家留言 */
	private String buyerMessage ;
	
	/** 客服备注 */
	private String remark ;
	
	/** 系统备注 */
	private String systemRemark ;
	
	/** 固定电话 */
	private String telPhone ;
	
	/** 省份 */
	private String province ;
	
	/** 市 */
	private String city ;
	
	/** 区县 */
	private String district ;
	
	/** 收货地址 */
	private String receiverAddress ;
	
	/** 邮编 */
	private String receiverZip ;
	
	/** 是否货到付款 */
	private String  isCashOnDelivery;
	
	/** 支付类型 */
	private String payType ;
	
	/** 支付公司编码 */
	private String payCompanyCode ;
	
	/**支付单号 */
	private String payNumber ;
	
	/**订单总金额 */
	private BigDecimal orderTotalAmount ;
	
	/**订单货款 */
	private BigDecimal orderGoodsAmount ;
	
	/**订单税款 */
	private BigDecimal orderTaxAmount ;
	
	/**运费 */
	private BigDecimal feeAmount ;
	
	/** 成交时间 */
	private Date tradeTime;
	
	/** 成交币制 */
	private String currCode;
	
	/** 成交总价*/
	private BigDecimal totalAmount;
	
	/** 购买人ID */
	private String purchaserId;
	
	/** 支付人ID */
	private String id;
	
	/** 购买人姓名 */
	private String name;
	
	/** 联系电话 */
	private String telNumber;
	
	/** 证件类型代码 */
	private String paperType;
	
	/** 证件号码 */
	private String paperNumber;
	
	/** 购买人地址 */
	private String address;
	
	/** 订单详细信息 */
	private List<OrderLineInfoToFisherDTO>  orderDetls;

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public String getOutCode() {
		return outCode;
	}

	public void setOutCode(String outCode) {
		this.outCode = outCode;
	}

	public String getExpressName() {
		return expressName;
	}

	public void setExpressName(String expressName) {
		this.expressName = expressName;
	}

	public String getExpressNo() {
		return expressNo;
	}

	public void setExpressNo(String expressNo) {
		this.expressNo = expressNo;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getBuyernick() {
		return buyernick;
	}

	public void setBuyernick(String buyernick) {
		this.buyernick = buyernick;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	public String getShopid() {
		return shopid;
	}

	public void setShopid(String shopid) {
		this.shopid = shopid;
	}

	public String getShop() {
		return shop;
	}

	public void setShop(String shop) {
		this.shop = shop;
	}

	public String getBuyerMessage() {
		return buyerMessage;
	}

	public void setBuyerMessage(String buyerMessage) {
		this.buyerMessage = buyerMessage;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getSystemRemark() {
		return systemRemark;
	}

	public void setSystemRemark(String systemRemark) {
		this.systemRemark = systemRemark;
	}

	public String getTelPhone() {
		return telPhone;
	}

	public void setTelPhone(String telPhone) {
		this.telPhone = telPhone;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getReceiverAddress() {
		return receiverAddress;
	}

	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}

	public String getReceiverZip() {
		return receiverZip;
	}

	public void setReceiverZip(String receiverZip) {
		this.receiverZip = receiverZip;
	}

	public String getIsCashOnDelivery() {
		return isCashOnDelivery;
	}

	public void setIsCashOnDelivery(String isCashOnDelivery) {
		this.isCashOnDelivery = isCashOnDelivery;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getPayCompanyCode() {
		return payCompanyCode;
	}

	public void setPayCompanyCode(String payCompanyCode) {
		this.payCompanyCode = payCompanyCode;
	}

	public String getPayNumber() {
		return payNumber;
	}

	public void setPayNumber(String payNumber) {
		this.payNumber = payNumber;
	}

	public BigDecimal getOrderTotalAmount() {
		return orderTotalAmount;
	}

	public void setOrderTotalAmount(BigDecimal orderTotalAmount) {
		this.orderTotalAmount = orderTotalAmount;
	}

	public BigDecimal getOrderGoodsAmount() {
		return orderGoodsAmount;
	}

	public void setOrderGoodsAmount(BigDecimal orderGoodsAmount) {
		this.orderGoodsAmount = orderGoodsAmount;
	}

	public BigDecimal getOrderTaxAmount() {
		return orderTaxAmount;
	}

	public void setOrderTaxAmount(BigDecimal orderTaxAmount) {
		this.orderTaxAmount = orderTaxAmount;
	}

	public BigDecimal getFeeAmount() {
		return feeAmount;
	}

	public void setFeeAmount(BigDecimal feeAmount) {
		this.feeAmount = feeAmount;
	}

	public Date getTradeTime() {
		return tradeTime;
	}

	public void setTradeTime(Date tradeTime) {
		this.tradeTime = tradeTime;
	}

	public String getCurrCode() {
		return currCode;
	}

	public void setCurrCode(String currCode) {
		this.currCode = currCode;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getPurchaserId() {
		return purchaserId;
	}

	public void setPurchaserId(String purchaserId) {
		this.purchaserId = purchaserId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTelNumber() {
		return telNumber;
	}

	public void setTelNumber(String telNumber) {
		this.telNumber = telNumber;
	}

	public String getPaperType() {
		return paperType;
	}

	public void setPaperType(String paperType) {
		this.paperType = paperType;
	}

	public String getPaperNumber() {
		return paperNumber;
	}

	public void setPaperNumber(String paperNumber) {
		this.paperNumber = paperNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<OrderLineInfoToFisherDTO> getOrderDetls() {
		return orderDetls;
	}

	public void setOrderDetls(List<OrderLineInfoToFisherDTO> orderDetls) {
		this.orderDetls = orderDetls;
	}


	
	
}
