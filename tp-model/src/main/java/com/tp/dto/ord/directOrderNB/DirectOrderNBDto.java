package com.tp.dto.ord.directOrderNB;

import java.io.Serializable;
import java.util.List;

public class DirectOrderNBDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String SaleOrderCode;		//订单号
	private String BuyerAccount;		//买家账号
	private String BuyerNickName;		//买家昵称
	private String Province;			//省
	private String City;				//市
	private String District;			//区
	private String Address;				//收货地址
	private String ReceiverName;		//收件人
	private String ReceiverPhone;		//联系电话
	private String ZipCode;				//邮编 
	private String BuyerName;			//真实姓名
	private String IdCard;				//身份证号
	private String BuyerPayment;		//买家实付金额
	private String SellerRemark;		//卖家备注
	private String BuyerRemark;			//买家备注
	private String CreateTime;			//下单时间
	private String PayTime;				//付款时间
	private List<DirectOrderDetailNBDto> Details;	//商品明细
	
	
	public String getSaleOrderCode() {
		return SaleOrderCode;
	}
	public void setSaleOrderCode(String saleOrderCode) {
		SaleOrderCode = saleOrderCode;
	}
	public String getBuyerAccount() {
		return BuyerAccount;
	}
	public void setBuyerAccount(String buyerAccount) {
		BuyerAccount = buyerAccount;
	}
	public String getBuyerNickName() {
		return BuyerNickName;
	}
	public void setBuyerNickName(String buyerNickName) {
		BuyerNickName = buyerNickName;
	}
	public String getProvince() {
		return Province;
	}
	public void setProvince(String province) {
		Province = province;
	}
	public String getCity() {
		return City;
	}
	public void setCity(String city) {
		City = city;
	}
	public String getDistrict() {
		return District;
	}
	public void setDistrict(String district) {
		District = district;
	}
	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		Address = address;
	}
	public String getReceiverName() {
		return ReceiverName;
	}
	public void setReceiverName(String receiverName) {
		ReceiverName = receiverName;
	}
	public String getReceiverPhone() {
		return ReceiverPhone;
	}
	public void setReceiverPhone(String receiverPhone) {
		ReceiverPhone = receiverPhone;
	}
	public String getZipCode() {
		return ZipCode;
	}
	public void setZipCode(String zipCode) {
		ZipCode = zipCode;
	}
	public String getBuyerName() {
		return BuyerName;
	}
	public void setBuyerName(String buyerName) {
		BuyerName = buyerName;
	}
	public String getIdCard() {
		return IdCard;
	}
	public void setIdCard(String idCard) {
		IdCard = idCard;
	}
	public String getBuyerPayment() {
		return BuyerPayment;
	}
	public void setBuyerPayment(String buyerPayment) {
		BuyerPayment = buyerPayment;
	}
	public String getSellerRemark() {
		return SellerRemark;
	}
	public void setSellerRemark(String sellerRemark) {
		SellerRemark = sellerRemark;
	}
	public String getBuyerRemark() {
		return BuyerRemark;
	}
	public void setBuyerRemark(String buyerRemark) {
		BuyerRemark = buyerRemark;
	}
	public String getCreateTime() {
		return CreateTime;
	}
	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}
	public String getPayTime() {
		return PayTime;
	}
	public void setPayTime(String payTime) {
		PayTime = payTime;
	}
	public List<DirectOrderDetailNBDto> getDetails() {
		return Details;
	}
	public void setDetails(List<DirectOrderDetailNBDto> details) {
		Details = details;
	}
	
	

}
