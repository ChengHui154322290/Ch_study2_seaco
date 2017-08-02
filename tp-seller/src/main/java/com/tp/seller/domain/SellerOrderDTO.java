package com.tp.seller.domain;

import java.util.Date;
import java.util.List;

import com.tp.model.ord.SubOrder;

/**
 * 商家订单信息
 */
public class SellerOrderDTO extends SubOrder {

    /**
     *
     */
    private static final long serialVersionUID = 3426801700085538894L;


    /** 买家账号 */
    private String nickName;

    private Long memberId;

    /** 收货人名称 */
    private String consigneeName;

    /** 收货人电话 */
    private String mobile;

    /** 收货人地址 */
    private String address;

    /** 仓库名称 */
    private String warehouseName;

    /** 实际支付总额 */
    private Double totalPay;

    /** 邮政编码 */
    private String postCode;

    /** 固定电话 */
    private String telephone;


    /** 商品列表 */
    private List<SellerOrderProductDTO> productList;
    
    /** 优惠金额 */
    private Double discount;
    /** 真实价格 */
    private Double realPrice;
    /** 保税区备案号 */
    private String customCode;
    /** 邮编 */
    private String postcode;
    /** 省份/直辖市名字 */
    private String province;
    /** 市名字 */
    private String city;
    /** 配送方式 */
    private String deliveryWay;
    /** 真实姓名 */
    private String realName;
    /** 身份证号 */
    private String identityCode;

    private String county;

    /**
     * 物流code
     */
    private String expressCode;

    /**
     * 物流名称
     */
    private String expressName;

    /** 快递单号 */
    private String packageNo;

    /** 身份证文件正面 */
    private String identifyFileFront;
    /** 身份证文件背面 */
    private String identifyFileBack;
    /** 身份证图片正面 */
    private String identifyImageFront;
    /** 身份证图片背面 */
    private String idenfifyImageBack;

    /** 发票抬头 */
    private String titleStr;

    /** 订单来源 */
    private String sourceStr;

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public String getConsigneeName() {
		return consigneeName;
	}

	public void setConsigneeName(String consigneeName) {
		this.consigneeName = consigneeName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public Double getTotalPay() {
		return totalPay;
	}

	public void setTotalPay(Double totalPay) {
		this.totalPay = totalPay;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public List<SellerOrderProductDTO> getProductList() {
		return productList;
	}

	public void setProductList(List<SellerOrderProductDTO> productList) {
		this.productList = productList;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Double getRealPrice() {
		return realPrice;
	}

	public void setRealPrice(Double realPrice) {
		this.realPrice = realPrice;
	}

	public String getCustomCode() {
		return customCode;
	}

	public void setCustomCode(String customCode) {
		this.customCode = customCode;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
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

	public String getDeliveryWay() {
		return deliveryWay;
	}

	public void setDeliveryWay(String deliveryWay) {
		this.deliveryWay = deliveryWay;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getIdentityCode() {
		return identityCode;
	}

	public void setIdentityCode(String identityCode) {
		this.identityCode = identityCode;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getExpressCode() {
		return expressCode;
	}

	public void setExpressCode(String expressCode) {
		this.expressCode = expressCode;
	}

	public String getExpressName() {
		return expressName;
	}

	public void setExpressName(String expressName) {
		this.expressName = expressName;
	}

	public String getPackageNo() {
		return packageNo;
	}

	public void setPackageNo(String packageNo) {
		this.packageNo = packageNo;
	}

	public String getIdentifyFileFront() {
		return identifyFileFront;
	}

	public void setIdentifyFileFront(String identifyFileFront) {
		this.identifyFileFront = identifyFileFront;
	}

	public String getIdentifyFileBack() {
		return identifyFileBack;
	}

	public void setIdentifyFileBack(String identifyFileBack) {
		this.identifyFileBack = identifyFileBack;
	}

	public String getIdentifyImageFront() {
		return identifyImageFront;
	}

	public void setIdentifyImageFront(String identifyImageFront) {
		this.identifyImageFront = identifyImageFront;
	}

	public String getIdenfifyImageBack() {
		return idenfifyImageBack;
	}

	public void setIdenfifyImageBack(String idenfifyImageBack) {
		this.idenfifyImageBack = idenfifyImageBack;
	}

	public String getTitleStr() {
		return titleStr;
	}

	public void setTitleStr(String titleStr) {
		this.titleStr = titleStr;
	}

	public String getSourceStr() {
		return sourceStr;
	}

	public void setSourceStr(String sourceStr) {
		this.sourceStr = sourceStr;
	}

}
