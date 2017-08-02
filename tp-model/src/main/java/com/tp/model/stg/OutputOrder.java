package com.tp.model.stg;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 下单完成后向仓库发送发货出库订单
  */
public class OutputOrder extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450403690114L;

	/**主键 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**即订单编号 数据类型varchar(20)*/
	private String orderCode;
	
	/**仓库id 数据类型bigint(20)*/
	private Long warehouseId;
	
	/**仓库编号 数据类型varchar(20)*/
	private String warehouseCode;
	
	/**客户id 数据类型varchar(10)*/
	private String customerId;
	
	/**即订单编号 数据类型varchar(20)*/
	private String systemId;
	
	/**订单类型 数据类型varchar(10)*/
	private String orderType;
	
	/**配送方式 数据类型varchar(20)*/
	private String shipping;
	
	/**销售渠道 数据类型varchar(10)*/
	private String issuePartyId;
	
	/**销售渠道名称 数据类型varchar(15)*/
	private String issuePartyName;
	
	/**客户昵称 数据类型varchar(10)*/
	private String customerName;
	
	/**付款方式 数据类型varchar(10)*/
	private String payment;
	
	/**网址 数据类型varchar(255)*/
	private String website;
	
	/**运费 数据类型double(10,2)*/
	private Double freight;
	
	/**货到付款服务费 数据类型double(10,2)*/
	private Double serviceCharge;
	
	/**收件人 数据类型varchar(20)*/
	private String name;
	
	/**邮编 数据类型varchar(20)*/
	private String postCode;
	
	/**固定电话 数据类型varchar(20)*/
	private String phone;
	
	/**手机号 数据类型varchar(15)*/
	private String mobile;
	
	/**省 数据类型varchar(15)*/
	private String prov;
	
	/**市 数据类型varchar(15)*/
	private String city;
	
	/**区 数据类型varchar(15)*/
	private String district;
	
	/**? 数据类型varchar(100)*/
	private String address;
	
	/**订单实付总金额 数据类型double*/
	private Double itemValue;
	
	/**订单折扣前总额 数据类型double*/
	private Double itemsOriginValue;
	
	/**卖家备注 数据类型varchar(255)*/
	private String remark;
	
	/**付款时间 数据类型datetime*/
	private Date payTime;
	
	/**是否开票 数据类型varchar(6)*/
	private String isCashsale;
	
	/**订单优先级 数据类型varchar(6)*/
	private String priority;
	
	/**预期发货时间 数据类型datetime*/
	private Date expectedTime;
	
	/**要求交货时间 数据类型datetime*/
	private Date requiredTime;
	
	/**状态 0 失败 1 成功 数据类型tinyint(4)*/
	private Integer status;
	
	/**失败次数 数据类型int(11)*/
	private Integer failTimes;
	
	/** 数据类型datetime*/
	private Date createTime;
	
	/** 订单详情 **/
	@Virtual
	private List<OutputOrderDetail> outputOrderDetails;
	
	public Long getId(){
		return id;
	}
	public String getOrderCode(){
		return orderCode;
	}
	public Long getWarehouseId(){
		return warehouseId;
	}
	public String getWarehouseCode(){
		return warehouseCode;
	}
	public String getCustomerId(){
		return customerId;
	}
	public String getSystemId(){
		return systemId;
	}
	public String getOrderType(){
		return orderType;
	}
	public String getShipping(){
		return shipping;
	}
	public String getIssuePartyId(){
		return issuePartyId;
	}
	public String getIssuePartyName(){
		return issuePartyName;
	}
	public String getCustomerName(){
		return customerName;
	}
	public String getPayment(){
		return payment;
	}
	public String getWebsite(){
		return website;
	}
	public Double getFreight(){
		return freight;
	}
	public Double getServiceCharge(){
		return serviceCharge;
	}
	public String getName(){
		return name;
	}
	public String getPostCode(){
		return postCode;
	}
	public String getPhone(){
		return phone;
	}
	public String getMobile(){
		return mobile;
	}
	public String getProv(){
		return prov;
	}
	public String getCity(){
		return city;
	}
	public String getDistrict(){
		return district;
	}
	public String getAddress(){
		return address;
	}
	public Double getItemValue(){
		return itemValue;
	}
	public Double getItemsOriginValue(){
		return itemsOriginValue;
	}
	public String getRemark(){
		return remark;
	}
	public Date getPayTime(){
		return payTime;
	}
	public String getIsCashsale(){
		return isCashsale;
	}
	public String getPriority(){
		return priority;
	}
	public Date getExpectedTime(){
		return expectedTime;
	}
	public Date getRequiredTime(){
		return requiredTime;
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
	public void setWarehouseId(Long warehouseId){
		this.warehouseId=warehouseId;
	}
	public void setWarehouseCode(String warehouseCode){
		this.warehouseCode=warehouseCode;
	}
	public void setCustomerId(String customerId){
		this.customerId=customerId;
	}
	public void setSystemId(String systemId){
		this.systemId=systemId;
	}
	public void setOrderType(String orderType){
		this.orderType=orderType;
	}
	public void setShipping(String shipping){
		this.shipping=shipping;
	}
	public void setIssuePartyId(String issuePartyId){
		this.issuePartyId=issuePartyId;
	}
	public void setIssuePartyName(String issuePartyName){
		this.issuePartyName=issuePartyName;
	}
	public void setCustomerName(String customerName){
		this.customerName=customerName;
	}
	public void setPayment(String payment){
		this.payment=payment;
	}
	public void setWebsite(String website){
		this.website=website;
	}
	public void setFreight(Double freight){
		this.freight=freight;
	}
	public void setServiceCharge(Double serviceCharge){
		this.serviceCharge=serviceCharge;
	}
	public void setName(String name){
		this.name=name;
	}
	public void setPostCode(String postCode){
		this.postCode=postCode;
	}
	public void setPhone(String phone){
		this.phone=phone;
	}
	public void setMobile(String mobile){
		this.mobile=mobile;
	}
	public void setProv(String prov){
		this.prov=prov;
	}
	public void setCity(String city){
		this.city=city;
	}
	public void setDistrict(String district){
		this.district=district;
	}
	public void setAddress(String address){
		this.address=address;
	}
	public void setItemValue(Double itemValue){
		this.itemValue=itemValue;
	}
	public void setItemsOriginValue(Double itemsOriginValue){
		this.itemsOriginValue=itemsOriginValue;
	}
	public void setRemark(String remark){
		this.remark=remark;
	}
	public void setPayTime(Date payTime){
		this.payTime=payTime;
	}
	public void setIsCashsale(String isCashsale){
		this.isCashsale=isCashsale;
	}
	public void setPriority(String priority){
		this.priority=priority;
	}
	public void setExpectedTime(Date expectedTime){
		this.expectedTime=expectedTime;
	}
	public void setRequiredTime(Date requiredTime){
		this.requiredTime=requiredTime;
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
	public List<OutputOrderDetail> getOutputOrderDetails() {
		return outputOrderDetails;
	}
	public void setOutputOrderDetails(List<OutputOrderDetail> outputOrderDetails) {
		this.outputOrderDetails = outputOrderDetails;
	}
}
