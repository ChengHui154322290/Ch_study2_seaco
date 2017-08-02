package com.tp.model.ord;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 订单物流状态表
  */
public class OrderDelivery extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451468597511L;

	/**PK 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**订单编号 数据类型bigint(20)*/
	private Long parentOrderCode;
	
	/**子订单编号 数据类型bigint(20)*/
	private Long orderCode;
	
	/**物流单号 数据类型varchar(32)*/
	private String packageNo;
	
	/**物流公司ID 数据类型varchar(32)*/
	private String companyId;
	
	/**物流公司名称 数据类型varchar(64)*/
	private String companyName;
	
	/**发货时间 数据类型datetime*/
	private Date deliveryTime;
	
	/**联系信息 数据类型varchar(200)*/
	private String linkInfo;
	
	/**发货城市描述 数据类型varchar(128)*/
	private String startCity;
	
	/**收货城市描述 数据类型varchar(128)*/
	private String endCity;
	
	/**退货信息 数据类型varchar(200)*/
	private String refundInfo;
	
	/**供应商ID 数据类型bigint(20)*/
	private Long supplierId;
	
	/**供应商名称 数据类型varchar(64)*/
	private String supplierName;
	
	/**运费 数据类型double(10,2)*/
	private Double freight;
	
	/**重量，单位为kg，精确到g 数据类型double(10,3)*/
	private Double weight;
	
	/**是否成功推送给快递100平台,0:没有成功，1:成功 数据类型tinyint(1)*/
	private Integer postKuaidi100;
	
	/**推送快递100次数 数据类型tinyint(1)*/
	private Integer postKuaidi100Times;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**创建人ID 数据类型varchar(32)*/
	private String createUser;
	
	/**修改时间 数据类型datetime*/
	private Date updateTime;
	
	/**修改人ID 数据类型varchar(32)*/
	private String updateUser;
	
	
	public Long getId(){
		return id;
	}
	public Long getParentOrderCode(){
		return parentOrderCode;
	}
	public Long getOrderCode(){
		return orderCode;
	}
	public String getPackageNo(){
		return packageNo;
	}
	public String getCompanyId(){
		return companyId;
	}
	public String getCompanyName(){
		return companyName;
	}
	public Date getDeliveryTime(){
		return deliveryTime;
	}
	public String getLinkInfo(){
		return linkInfo;
	}
	public String getStartCity(){
		return startCity;
	}
	public String getEndCity(){
		return endCity;
	}
	public String getRefundInfo(){
		return refundInfo;
	}
	public Long getSupplierId(){
		return supplierId;
	}
	public String getSupplierName(){
		return supplierName;
	}
	public Double getFreight(){
		return freight;
	}
	public Double getWeight(){
		return weight;
	}
	public Integer getPostKuaidi100(){
		return postKuaidi100;
	}
	public Integer getPostKuaidi100Times(){
		return postKuaidi100Times;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public String getCreateUser(){
		return createUser;
	}
	public Date getUpdateTime(){
		return updateTime;
	}
	public String getUpdateUser(){
		return updateUser;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setParentOrderCode(Long parentOrderCode){
		this.parentOrderCode=parentOrderCode;
	}
	public void setOrderCode(Long orderCode){
		this.orderCode=orderCode;
	}
	public void setPackageNo(String packageNo){
		this.packageNo=packageNo;
	}
	public void setCompanyId(String companyId){
		this.companyId=companyId;
	}
	public void setCompanyName(String companyName){
		this.companyName=companyName;
	}
	public void setDeliveryTime(Date deliveryTime){
		this.deliveryTime=deliveryTime;
	}
	public void setLinkInfo(String linkInfo){
		this.linkInfo=linkInfo;
	}
	public void setStartCity(String startCity){
		this.startCity=startCity;
	}
	public void setEndCity(String endCity){
		this.endCity=endCity;
	}
	public void setRefundInfo(String refundInfo){
		this.refundInfo=refundInfo;
	}
	public void setSupplierId(Long supplierId){
		this.supplierId=supplierId;
	}
	public void setSupplierName(String supplierName){
		this.supplierName=supplierName;
	}
	public void setFreight(Double freight){
		this.freight=freight;
	}
	public void setWeight(Double weight){
		this.weight=weight;
	}
	public void setPostKuaidi100(Integer postKuaidi100){
		this.postKuaidi100=postKuaidi100;
	}
	public void setPostKuaidi100Times(Integer postKuaidi100Times){
		this.postKuaidi100Times=postKuaidi100Times;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setCreateUser(String createUser){
		this.createUser=createUser;
	}
	public void setUpdateTime(Date updateTime){
		this.updateTime=updateTime;
	}
	public void setUpdateUser(String updateUser){
		this.updateUser=updateUser;
	}
}
