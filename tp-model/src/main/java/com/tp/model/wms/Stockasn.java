package com.tp.model.wms;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 入库订单
  */
public class Stockasn  implements Serializable {

	private static final long serialVersionUID = 1464588984275L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;

	/**采购单号 数据类型varchar(50)*/
	private String orderCode;

	/**业务类型 数据类型varchar(10)*/
	private String orderType;

	/**合同号 数据类型varchar(50)*/
	private String contractCode;

	/**供应商编码 数据类型varchar(50)*/
	private String supplierCode;

	/**供应商名称 数据类型varchar(50)*/
	private String supplierName;

	/**订单生成时间 数据类型datetime*/
	private Date orderCreateTime;

	/**仓库id 数据类型bigint(20)*/
	private Long warehouseId;

	/**仓库编号 数据类型varchar(50)*/
	private String warehouseCode;

	/**仓库名称 数据类型varchar(50)*/
	private String warehouseName;

	/**预计到货开始日期 数据类型datetime*/
	private Date planStartTime;

	/**预计到货截止日期 数据类型datetime*/
	private Date planOverTime;

	/**毛重 数据类型double(12,2)*/
	private Double grossWeight;

	/**净重 数据类型double(12,2)*/
	private Double netWeight;

	/**件数 数据类型int(11)*/
	private Integer amount;

	/**批次号 数据类型varchar(30)*/
	private String batchNo;

	/**物流公司编码 数据类型varchar(30)*/
	private String logisticsCode;

	/**快递单号 数据类型varchar(50)*/
	private String expressCode;

	/**前物流订单号 数据类型varchar(30)*/
	private String preOrderCode;

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

	/**备注 数据类型varchar(200)*/
	private String remark;

	/**状态 0 失败 1 成功 数据类型tinyint(4)*/
	private Integer status;

	/**失败次数 数据类型int(11)*/
	private Integer failTimes;

	/** 数据类型datetime*/
	private Date createTime;

	private String createUser;

	private Long createUserId;

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Long getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}

	public Long getId(){
		return id;
	}
	public String getOrderCode(){
		return orderCode;
	}
	public String getOrderType(){
		return orderType;
	}
	public String getContractCode(){
		return contractCode;
	}
	public String getSupplierCode(){
		return supplierCode;
	}
	public String getSupplierName(){
		return supplierName;
	}
	public Date getOrderCreateTime(){
		return orderCreateTime;
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
	public Date getPlanStartTime(){
		return planStartTime;
	}
	public Date getPlanOverTime(){
		return planOverTime;
	}
	public Double getGrossWeight(){
		return grossWeight;
	}
	public Double getNetWeight(){
		return netWeight;
	}
	public Integer getAmount(){
		return amount;
	}
	public String getBatchNo(){
		return batchNo;
	}
	public String getLogisticsCode(){
		return logisticsCode;
	}
	public String getExpressCode(){
		return expressCode;
	}
	public String getPreOrderCode(){
		return preOrderCode;
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
	public void setContractCode(String contractCode){
		this.contractCode=contractCode;
	}
	public void setSupplierCode(String supplierCode){
		this.supplierCode=supplierCode;
	}
	public void setSupplierName(String supplierName){
		this.supplierName=supplierName;
	}
	public void setOrderCreateTime(Date orderCreateTime){
		this.orderCreateTime=orderCreateTime;
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
	public void setPlanStartTime(Date planStartTime){
		this.planStartTime=planStartTime;
	}
	public void setPlanOverTime(Date planOverTime){
		this.planOverTime=planOverTime;
	}
	public void setGrossWeight(Double grossWeight){
		this.grossWeight=grossWeight;
	}
	public void setNetWeight(Double netWeight){
		this.netWeight=netWeight;
	}
	public void setAmount(Integer amount){
		this.amount=amount;
	}
	public void setBatchNo(String batchNo){
		this.batchNo=batchNo;
	}
	public void setLogisticsCode(String logisticsCode){
		this.logisticsCode=logisticsCode;
	}
	public void setExpressCode(String expressCode){
		this.expressCode=expressCode;
	}
	public void setPreOrderCode(String preOrderCode){
		this.preOrderCode=preOrderCode;
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
}
