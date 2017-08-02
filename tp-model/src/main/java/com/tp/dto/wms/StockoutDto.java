/**
 * 
 */
package com.tp.dto.wms;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.tp.model.stg.Warehouse;

/**
 * @author Administrator
 * 
 * 出库单数据
 */
public class StockoutDto implements Serializable{
	
	private static final long serialVersionUID = -4448591673226018346L;

	/*------------基本信息-----------*/
	/** 订单编号 **/
	private String orderCode;
	
	/** 订单创建时间 **/
	private Date orderCreateTime;
	
	/** 订单支付时间 **/
	private Date orderPayTime;
	
	/** 会员ID **/
	private Long memberId;
	
	/** 会员昵称 **/
	private String memberName;
	
	/** 订单总金额 **/
	private Double totalAmount;
	
	/** 实付金额 **/
	private Double payAmount;
	
	/** 优惠金额 **/
	private Double discountAmount;
	
	/** 邮费 **/
	private Double postAmount;
	
	/** 邮费是否到付:0是1否 **/
	private Integer isPostagePay;
	
	/** 是否货到付款:0是1否 **/
	private Integer isDeliveryPay;
	
	/** 是否紧急：0普通1紧急**/
	private Integer isUrgency;
	
	/** 仓库编号 **/
	private String warehouseCode;
	
	/** 仓库id **/
	private Long warehouseId;
	
	/** 仓库名称 **/
	private String warehouseName;
	
	/*------------ 配送信息 ------------*/
	/** 物流企业编码 **/
	private String logisticsCompanyCode;
	
	/** 物流企业名称 **/
	private String logisticsCompanyName;
	
	/** 快递运单号 **/
	private String expressNo;
	
	/*------------ 收件人信息 ------------*/
	/** 邮编 **/
	private String postCode;
	
	/** 收件人省份 **/
	private String province;
	
	/** 收件人城市 **/
	private String city;
	
	/** 收件人县区**/
	private String area;
	
	/** 收货地址 **/
	private String address;
	
	/** 收件人名字 **/
	private String consignee;
	
	/** 收件人手机号 **/
	private String mobile;
	
	/** 收件人电话 **/
	private String tel;
	
	/*------------发票信息-------------*/
	/** 是否需要开发票：0是1否 **/
	private Integer isInvoice;
	
	/** 发票信息 **/
	private List<StockoutInvoiceDto> invoiceInfoList;
	
	/*------------订单商品信息-------------*/
	/** 订单商品信息 **/
	private List<StockoutItem> orderItemList;
	
	/** 仓库数据 */
	private Warehouse warehouse;
	
	/** 备注 **/
	private String remark;

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public Date getOrderCreateTime() {
		return orderCreateTime;
	}

	public void setOrderCreateTime(Date orderCreateTime) {
		this.orderCreateTime = orderCreateTime;
	}

	public Date getOrderPayTime() {
		return orderPayTime;
	}

	public void setOrderPayTime(Date orderPayTime) {
		this.orderPayTime = orderPayTime;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Double getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(Double payAmount) {
		this.payAmount = payAmount;
	}

	public Double getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(Double discountAmount) {
		this.discountAmount = discountAmount;
	}

	public Double getPostAmount() {
		return postAmount;
	}

	public void setPostAmount(Double postAmount) {
		this.postAmount = postAmount;
	}

	public Integer getIsPostagePay() {
		return isPostagePay;
	}

	public void setIsPostagePay(Integer isPostagePay) {
		this.isPostagePay = isPostagePay;
	}

	public Integer getIsDeliveryPay() {
		return isDeliveryPay;
	}

	public void setIsDeliveryPay(Integer isDeliveryPay) {
		this.isDeliveryPay = isDeliveryPay;
	}

	public Integer getIsUrgency() {
		return isUrgency;
	}

	public void setIsUrgency(Integer isUrgency) {
		this.isUrgency = isUrgency;
	}

	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	public Long getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(Long warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getLogisticsCompanyCode() {
		return logisticsCompanyCode;
	}

	public void setLogisticsCompanyCode(String logisticsCompanyCode) {
		this.logisticsCompanyCode = logisticsCompanyCode;
	}

	public String getLogisticsCompanyName() {
		return logisticsCompanyName;
	}

	public void setLogisticsCompanyName(String logisticsCompanyName) {
		this.logisticsCompanyName = logisticsCompanyName;
	}

	public String getExpressNo() {
		return expressNo;
	}

	public void setExpressNo(String expressNo) {
		this.expressNo = expressNo;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
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

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public Integer getIsInvoice() {
		return isInvoice;
	}

	public void setIsInvoice(Integer isInvoice) {
		this.isInvoice = isInvoice;
	}

	public List<StockoutInvoiceDto> getInvoiceInfoList() {
		return invoiceInfoList;
	}

	public void setInvoiceInfoList(List<StockoutInvoiceDto> invoiceInfoList) {
		this.invoiceInfoList = invoiceInfoList;
	}

	public List<StockoutItem> getOrderItemList() {
		return orderItemList;
	}

	public void setOrderItemList(List<StockoutItem> orderItemList) {
		this.orderItemList = orderItemList;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}
}
