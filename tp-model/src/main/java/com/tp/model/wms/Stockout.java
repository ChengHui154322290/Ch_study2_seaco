package com.tp.model.wms;

import java.io.Serializable;

import java.util.Date;
import java.util.List;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 出库订单
  */
public class Stockout extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1464588984275L;

	/**主键 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**订单编号 数据类型varchar(30)*/
	private String orderCode;
	
	/**订单类型 数据类型varchar(20)*/
	private String orderType;
	
	/**仓库id 数据类型bigint(20)*/
	private Long warehouseId;
	
	/**仓库编号数据类型varchar(20)*/
	private String warehouseCode;
	
	/**仓库名称 数据类型varchar(50)*/
	private String warehouseName;
	
	/** 仓库WMSCode */
	private String wmsCode;
	
	/** 仓库WMSName */
	private String wmsName;
	
	/**订单创建时间 数据类型datetime*/
	private Date orderCreateTime;
	
	/**订单支付时间 数据类型datetime*/
	private Date payTime;
	
	/**订单总金额 数据类型double(10,2)*/
	private Double totalAmount;
	
	/**订单实付金额 数据类型double(10,2)*/
	private Double payAmount;
	
	/**订单优惠金额 数据类型double(10,2)*/
	private Double discount;
	
	/**邮费 数据类型double(10,2)*/
	private Double postage;
	
	/**邮费是否到付:0是1否 数据类型tinyint(1)*/
	private Integer isPostagePay;
	
	/**是否货到付款:0是1否 数据类型tinyint(1)*/
	private Integer isDeliveryPay;
	
	/**是否紧急：0普通1紧急 数据类型tinyint(1)*/
	private Integer isUrgency;
	
	/**物流公司编码 数据类型varchar(32)*/
	private String logisticsCompanyCode;
	
	/**物流公司名称 数据类型varchar(50)*/
	private String logisticsCompanyName;
	
	/**快递单号 数据类型varchar(50)*/
	private String expressNo;
	
	/**会员ID 数据类型bigint(20)*/
	private Long memberId;
	
	/**会员昵称 数据类型varchar(50)*/
	private String memberName;
	
	/**收货人姓名 数据类型varchar(50)*/
	private String consignee;
	
	/**邮编 数据类型varchar(20)*/
	private String postCode;
	
	/**省名称 数据类型varchar(50)*/
	private String province;
	
	/**市名称 数据类型varchar(50)*/
	private String city;
	
	/**区名称 数据类型varchar(50)*/
	private String area;
	
	/**收件地址 数据类型varchar(500)*/
	private String address;
	
	/**移动电话 数据类型varchar(20)*/
	private String mobile;
	
	/**固定电话 数据类型varchar(20)*/
	private String tel;
	
	/**是否需要开发票：0是1否 数据类型tinyint(1)*/
	private Integer isInvoice;
	
	/**备注 数据类型varchar(500)*/
	private String remark;
	
	/**状态 0 失败 1 成功 数据类型tinyint(1)*/
	private Integer status;
	
	/**失败次数 数据类型int(11)*/
	private Integer failTimes;
	
	/** 失败原因  **/
	private String 	 errorMsg;
	
	/** 数据类型datetime*/
	private Date createTime;
	
	/** 商品详情 **/
	@Virtual
	private List<StockoutDetail> stockoutDetails;
	
	/** 发票详情 **/
	@Virtual
	private List<StockoutInvoice> stockoutInvoices;
	
	
	public Long getId(){
		return id;
	}
	public String getOrderCode(){
		return orderCode;
	}
	public String getOrderType(){
		return orderType;
	}
	public Long getWarehouseId(){
		return warehouseId;
	}
	public String getWarehouseCode(){
		return warehouseCode;
	}
	public String getWarehouseName(){
		return warehouseName;
	}
	public Date getOrderCreateTime(){
		return orderCreateTime;
	}
	public Date getPayTime(){
		return payTime;
	}
	public Double getTotalAmount(){
		return totalAmount;
	}
	public Double getPayAmount(){
		return payAmount;
	}
	public Double getDiscount(){
		return discount;
	}
	public Double getPostage(){
		return postage;
	}
	public Integer getIsPostagePay(){
		return isPostagePay;
	}
	public Integer getIsDeliveryPay(){
		return isDeliveryPay;
	}
	public Integer getIsUrgency(){
		return isUrgency;
	}
	public String getLogisticsCompanyCode(){
		return logisticsCompanyCode;
	}
	public String getLogisticsCompanyName(){
		return logisticsCompanyName;
	}
	public String getExpressNo(){
		return expressNo;
	}
	public Long getMemberId(){
		return memberId;
	}
	public String getMemberName(){
		return memberName;
	}
	public String getConsignee(){
		return consignee;
	}
	public String getPostCode(){
		return postCode;
	}
	public String getProvince(){
		return province;
	}
	public String getCity(){
		return city;
	}
	public String getArea(){
		return area;
	}
	public String getAddress(){
		return address;
	}
	public String getMobile(){
		return mobile;
	}
	public String getTel(){
		return tel;
	}
	public Integer getIsInvoice(){
		return isInvoice;
	}
	public String getRemark(){
		return remark;
	}
	public Integer getStatus(){
		return status;
	}
	public Integer getFailTimes(){
		return failTimes;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setOrderCode(String orderCode){
		this.orderCode=orderCode;
	}
	public void setOrderType(String orderType){
		this.orderType=orderType;
	}
	public void setWarehouseId(Long warehouseId){
		this.warehouseId=warehouseId;
	}
	public void setWarehouseCode(String warehouseCode){
		this.warehouseCode=warehouseCode;
	}
	public void setWarehouseName(String warehouseName){
		this.warehouseName=warehouseName;
	}
	public void setOrderCreateTime(Date orderCreateTime){
		this.orderCreateTime=orderCreateTime;
	}
	public void setPayTime(Date payTime){
		this.payTime=payTime;
	}
	public void setTotalAmount(Double totalAmount){
		this.totalAmount=totalAmount;
	}
	public void setPayAmount(Double payAmount){
		this.payAmount=payAmount;
	}
	public void setDiscount(Double discount){
		this.discount=discount;
	}
	public void setPostage(Double postage){
		this.postage=postage;
	}
	public void setIsPostagePay(Integer isPostagePay){
		this.isPostagePay=isPostagePay;
	}
	public void setIsDeliveryPay(Integer isDeliveryPay){
		this.isDeliveryPay=isDeliveryPay;
	}
	public void setIsUrgency(Integer isUrgency){
		this.isUrgency=isUrgency;
	}
	public void setLogisticsCompanyCode(String logisticsCompanyCode){
		this.logisticsCompanyCode=logisticsCompanyCode;
	}
	public void setLogisticsCompanyName(String logisticsCompanyName){
		this.logisticsCompanyName=logisticsCompanyName;
	}
	public void setExpressNo(String expressNo){
		this.expressNo=expressNo;
	}
	public void setMemberId(Long memberId){
		this.memberId=memberId;
	}
	public void setMemberName(String memberName){
		this.memberName=memberName;
	}
	public void setConsignee(String consignee){
		this.consignee=consignee;
	}
	public void setPostCode(String postCode){
		this.postCode=postCode;
	}
	public void setProvince(String province){
		this.province=province;
	}
	public void setCity(String city){
		this.city=city;
	}
	public void setArea(String area){
		this.area=area;
	}
	public void setAddress(String address){
		this.address=address;
	}
	public void setMobile(String mobile){
		this.mobile=mobile;
	}
	public void setTel(String tel){
		this.tel=tel;
	}
	public void setIsInvoice(Integer isInvoice){
		this.isInvoice=isInvoice;
	}
	public void setRemark(String remark){
		this.remark=remark;
	}
	public void setStatus(Integer status){
		this.status=status;
	}
	public void setFailTimes(Integer failTimes){
		this.failTimes=failTimes;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public List<StockoutDetail> getStockoutDetails() {
		return stockoutDetails;
	}
	public void setStockoutDetails(List<StockoutDetail> stockoutDetails) {
		this.stockoutDetails = stockoutDetails;
	}
	public List<StockoutInvoice> getStockoutInvoices() {
		return stockoutInvoices;
	}
	public void setStockoutInvoices(List<StockoutInvoice> stockoutInvoices) {
		this.stockoutInvoices = stockoutInvoices;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public String getWmsCode() {
		return wmsCode;
	}
	public void setWmsCode(String wmsCode) {
		this.wmsCode = wmsCode;
	}
	public String getWmsName() {
		return wmsName;
	}
	public void setWmsName(String wmsName) {
		this.wmsName = wmsName;
	}
}
