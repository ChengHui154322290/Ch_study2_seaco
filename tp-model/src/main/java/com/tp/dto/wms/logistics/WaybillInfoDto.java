package com.tp.dto.wms.logistics;

import java.io.Serializable;
import java.util.List;

import com.tp.common.vo.OrderConstant.OrderType;
import com.tp.common.vo.wms.WmsWaybillConstant.WaybillType;

/**
 * @author Administrator
 * 运单信息
 */
public class WaybillInfoDto implements Serializable{
	
	private static final long serialVersionUID = -2691724240547407264L;

	/**订单编号*/
	private String orderCode;
	
	/** 订单类型 **/
	private OrderType orderType;
	
	/** 推送类型 **/
	private WaybillType waybillType;
	
	/** 进口类型:0直邮1保税区 */
	private Integer importType;
	
	/*----------------订单信息 -------------*/
	/**包裹重量*/
	private String grossWeight;
	
	/**包裹净重*/
	private String netWeight;
	
	/**商品发货数量*/
	private String packAmount;
	
	/** 邮费是否到付:0是1否 默认1**/
	private Integer isPostagePay;
	
	/** 是否货到付款:0是1否 默认1**/
	private Integer isDeliveryPay;
	
	/**发货地 - 省*/
	private String sendProvince;
	
	/** 发货地 - 市 **/
	private String sendCity;
	
	/** 发货地 - 区 **/
	private String sendArea;
	
	/** 发货地 - 地址 **/
	private String sendAddress;
	
	/** 发货地址 - 粗略 例如：浙江杭州 **/
	private String sendRoughArea;
	
	/** 发件人姓名 */
	private String senderName;
	
	/** 发件人电话 **/
	private String senderTel;
	
	/** 发件人单位 **/
	private String senderCompany;
	/*----------------物流信息 -------------*/
	/**快递企业编码*/
	private String logisticsCode;
	
	/**快递企业名称*/
	private String logisticsName;
	
	/**运单号*/
	private String waybillNo;
	
	/*----------------收件人信息 -------------*/
	/**收货人姓名*/
	private String consignee;
	
	/**邮编*/
	private String postCode;
	
	/**省名称*/
	private String province;
	
	/**市名称*/
	private String city;
	
	/**区名称*/
	private String area;
	
	/**收件地址*/
	private String address;
	
	/**移动电话*/
	private String mobile;
	
	/**固定电话*/
	private String tel;

	/*----------------订单详情 -------------*/
	/** 包裹商品详情 **/
	private List<WaybillItemInfoDto> details;
	
	/*--------------直邮相关信息-----------*/
	/** 航班号 */
	private String voyageNo;
	
	/** 总提运单号 */
	private String deliveryNo;
	

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
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

	public String getPackAmount() {
		return packAmount;
	}

	public void setPackAmount(String packAmount) {
		this.packAmount = packAmount;
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

	public String getSendProvince() {
		return sendProvince;
	}

	public void setSendProvince(String sendProvince) {
		this.sendProvince = sendProvince;
	}

	public String getSendCity() {
		return sendCity;
	}

	public void setSendCity(String sendCity) {
		this.sendCity = sendCity;
	}

	public String getSendArea() {
		return sendArea;
	}

	public void setSendArea(String sendArea) {
		this.sendArea = sendArea;
	}

	public String getSendAddress() {
		return sendAddress;
	}

	public void setSendAddress(String sendAddress) {
		this.sendAddress = sendAddress;
	}

	public String getSendRoughArea() {
		return sendRoughArea;
	}

	public void setSendRoughArea(String sendRoughArea) {
		this.sendRoughArea = sendRoughArea;
	}

	public String getLogisticsCode() {
		return logisticsCode;
	}

	public void setLogisticsCode(String logisticsCode) {
		this.logisticsCode = logisticsCode;
	}

	public String getLogisticsName() {
		return logisticsName;
	}

	public void setLogisticsName(String logisticsName) {
		this.logisticsName = logisticsName;
	}

	public String getWaybillNo() {
		return waybillNo;
	}

	public void setWaybillNo(String waybillNo) {
		this.waybillNo = waybillNo;
	}

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
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

	public List<WaybillItemInfoDto> getDetails() {
		return details;
	}

	public void setDetails(List<WaybillItemInfoDto> details) {
		this.details = details;
	}

	public OrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}

	public WaybillType getWaybillType() {
		return waybillType;
	}

	public void setWaybillType(WaybillType waybillType) {
		this.waybillType = waybillType;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getSenderTel() {
		return senderTel;
	}

	public void setSenderTel(String senderTel) {
		this.senderTel = senderTel;
	}

	public String getSenderCompany() {
		return senderCompany;
	}

	public void setSenderCompany(String senderCompany) {
		this.senderCompany = senderCompany;
	}

	public Integer getImportType() {
		return importType;
	}

	public void setImportType(Integer importType) {
		this.importType = importType;
	}

	public String getVoyageNo() {
		return voyageNo;
	}

	public void setVoyageNo(String voyageNo) {
		this.voyageNo = voyageNo;
	}

	public String getDeliveryNo() {
		return deliveryNo;
	}

	public void setDeliveryNo(String deliveryNo) {
		this.deliveryNo = deliveryNo;
	}
}
