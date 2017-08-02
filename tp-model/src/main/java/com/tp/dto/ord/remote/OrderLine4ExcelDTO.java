package com.tp.dto.ord.remote;

import java.io.Serializable;
import java.util.Date;

/**
 * excel导出订单行dto
 * 
 * @author szy
 * @version 0.0.1
 */
public class OrderLine4ExcelDTO implements Serializable {

	private static final long serialVersionUID = -7078298081465815278L;
	
	private String code;
	private String orderCode;
	private String statusStr;
	private String typeStr;
	private String payWayStr;
	private String supplierName;
	private String loginName;
	private Double payTotal;
	private String consigneeAddress;
	private String consigneeMobile;
	private String consigneeName;
	private Date createTime;
	private Date payTime;
	private Date deliveryTime;
	private String seaChannel;
	private String payCode;
	/** 身份证号 */
	private String idCode;
	/** 真实姓名 */
	private String realName;
	/**身份证正面*/
	private String frontImg;
	/**身份证反面*/
	private String backImg;

	private Double discount;
	
	private String province;
	private String city;
	private String county;
	private String town;

	/* ------- 行数据 ------- */
	
	private String skuCode;
	private String barCode;
	private String spuName;
	private Integer quantity;
	private String topicId;
	private String productCode;
	private String unit;
	private Double price;
	private Double realPrice;//实付价
	/**税率*/
	private Double taxRate;
	/**税金()*/
	private Double duties;
	/**商品项小计*/
	private Double subAmount;
	/**商品实付小计*/
	private Double subRealAmount;
	
	/**下单来源*/
	private String sourceName;
	/**卡券推广员*/
	private String promoterName;
	/**店铺名称*/
	private String shopPromoterName;
	/**线下推广*/
	private String scanPromoterName;
	/**原税金*/
	private Double taxFee;
	/**销售价*/
	private Double salesPrice;
	/**使用的卡券*/
	private Double couponAmount;
	/**使用的优惠券*/
	private Double orderCouponAmount;
	/**虚拟币类型*/
	private String pointTypeZh;
	/**使用虚拟币抵扣金额*/
	private Double points;
	
	/** 快递公司 */
	private String expressName;
	/** 快递单号 */
	private String expressNo;

	public String getFrontImg() {
		return frontImg;
	}

	public void setFrontImg(String frontImg) {
		this.frontImg = frontImg;
	}

	public String getBackImg() {
		return backImg;
	}

	public void setBackImg(String backImg) {
		this.backImg = backImg;
	}

	public Double getSubRealAmount() {
		return subRealAmount;
	}
	public void setSubRealAmount(Double subRealAmount) {
		this.subRealAmount = subRealAmount;
	}
	public Double getRealPrice() {
		return realPrice;
	}
	public void setRealPrice(Double realPrice) {
		this.realPrice = realPrice;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public String getStatusStr() {
		return statusStr;
	}
	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}
	public String getTypeStr() {
		return typeStr;
	}
	public void setTypeStr(String typeStr) {
		this.typeStr = typeStr;
	}
	public String getPayWayStr() {
		return payWayStr;
	}
	public void setPayWayStr(String payWayStr) {
		this.payWayStr = payWayStr;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public Double getPayTotal() {
		return payTotal;
	}
	public void setPayTotal(Double payTotal) {
		this.payTotal = payTotal;
	}
	public String getConsigneeAddress() {
		return consigneeAddress;
	}
	public void setConsigneeAddress(String consigneeAddress) {
		this.consigneeAddress = consigneeAddress;
	}
	public String getConsigneeMobile() {
		return consigneeMobile;
	}
	public void setConsigneeMobile(String consigneeMobile) {
		this.consigneeMobile = consigneeMobile;
	}
	public String getConsigneeName() {
		return consigneeName;
	}
	public void setConsigneeName(String consigneeName) {
		this.consigneeName = consigneeName;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getPayTime() {
		return payTime;
	}
	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}
	public Date getDeliveryTime() {
		return deliveryTime;
	}
	public void setDeliveryTime(Date deliveryTime) {
		this.deliveryTime = deliveryTime;
	}
	public String getSeaChannel() {
		return seaChannel;
	}
	public void setSeaChannel(String seaChannel) {
		this.seaChannel = seaChannel;
	}
	public String getPayCode() {
		return payCode;
	}
	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}
	public String getIdCode() {
		return idCode;
	}
	public void setIdCode(String idCode) {
		this.idCode = idCode;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public Double getDiscount() {
		return discount;
	}
	public void setDiscount(Double discount) {
		this.discount = discount;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getSkuCode() {
		return skuCode;
	}
	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	public String getBarCode() {
		return barCode;
	}
	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	public String getSpuName() {
		return spuName;
	}
	public void setSpuName(String spuName) {
		this.spuName = spuName;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public String getTopicId() {
		return topicId;
	}
	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
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
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public String getTown() {
		return town;
	}
	public void setTown(String town) {
		this.town = town;
	}
	public Double getTaxRate() {
		return taxRate;
	}
	public void setTaxRate(Double taxRate) {
		this.taxRate = taxRate;
	}
	public Double getDuties() {
		return duties;
	}
	public void setDuties(Double duties) {
		this.duties = duties;
	}
	public Double getSubAmount() {
		return subAmount;
	}
	public void setSubAmount(Double subAmount) {
		this.subAmount = subAmount;
	}
	public String getSourceName() {
		return sourceName;
	}
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	public String getPromoterName() {
		return promoterName;
	}
	public void setPromoterName(String promoterName) {
		this.promoterName = promoterName;
	}
	public String getShopPromoterName() {
		return shopPromoterName;
	}
	public void setShopPromoterName(String shopPromoterName) {
		this.shopPromoterName = shopPromoterName;
	}
	public Double getTaxFee() {
		return taxFee;
	}
	public void setTaxFee(Double taxFee) {
		this.taxFee = taxFee;
	}
	public Double getSalesPrice() {
		return salesPrice;
	}
	public void setSalesPrice(Double salesPrice) {
		this.salesPrice = salesPrice;
	}
	public Double getCouponAmount() {
		return couponAmount;
	}
	public void setCouponAmount(Double couponAmount) {
		this.couponAmount = couponAmount;
	}
	public Double getOrderCouponAmount() {
		return orderCouponAmount;
	}
	public void setOrderCouponAmount(Double orderCouponAmount) {
		this.orderCouponAmount = orderCouponAmount;
	}

	public String getScanPromoterName() {
		return scanPromoterName;
	}

	public void setScanPromoterName(String scanPromoterName) {
		this.scanPromoterName = scanPromoterName;
	}

	public Double getPoints() {
		return points;
	}

	public void setPoints(Double points) {
		this.points = points;
	}

	public String getPointTypeZh() {
		return pointTypeZh;
	}

	public void setPointTypeZh(String pointTypeZh) {
		this.pointTypeZh = pointTypeZh;
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
	
}
