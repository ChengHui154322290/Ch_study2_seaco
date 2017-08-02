package com.tp.model.prd;

import java.io.Serializable;

import java.util.Date;

import org.apache.log4j.lf5.viewer.LogFactor5InputDialog;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 商品详情导入日志表
  */
public class ItemDetailImport extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1497923686707L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**item_import_log的主键  数据类型bigint(10)*/
	private Long logId;
	
	/** 数据类型bigint(20)*/
	private Long detailId;
	
	/** 数据类型varchar(50)*/
	private String sku;
	
	/**品牌名称 数据类型varchar(64)*/
	private String brandName;
	
	/**品牌故事 数据类型varchar(255)*/
	private String brandStory;
	
	/**商品名称 数据类型varchar(60)*/
	private String itemTitle;
	
	/**商品详情描述 数据类型text*/
	private String itemDetailDecs;
	
	/**材质 数据类型varchar(50)*/
	private String material;
	
	/**特殊说明 数据类型varchar(255)*/
	private String specialInstructions;
	
	/** 数据类型varchar(255)*/
	private String createUser;
	
	/** 数据类型datetime*/
	private Date createTime;
	
	/**导入状态  数据类型int*/
	private Integer status;
	
	/**失败原因  数据类型varchar(255)*/
	private String opMessage;
	
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getOpMessage() {
		return opMessage;
	}
	public void setOpMessage(String opMessage) {
		this.opMessage = opMessage;
	}
	public Long getLogId() {
		return logId;
	}
	public void setLogId(Long logId) {
		this.logId = logId;
	}
	public Long getId(){
		return id;
	}
	public Long getDetailId(){
		return detailId;
	}
	public String getSku(){
		return sku;
	}
	public String getBrandName(){
		return brandName;
	}
	public String getBrandStory(){
		return brandStory;
	}
	public String getItemTitle(){
		return itemTitle;
	}
	public String getItemDetailDecs(){
		return itemDetailDecs;
	}
	public String getMaterial(){
		return material;
	}
	public String getSpecialInstructions(){
		return specialInstructions;
	}
	public String getCreateUser(){
		return createUser;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setDetailId(Long detailId){
		this.detailId=detailId;
	}
	public void setSku(String sku){
		this.sku=sku;
	}
	public void setBrandName(String brandName){
		this.brandName=brandName;
	}
	public void setBrandStory(String brandStory){
		this.brandStory=brandStory;
	}
	public void setItemTitle(String itemTitle){
		this.itemTitle=itemTitle;
	}
	public void setItemDetailDecs(String itemDetailDecs){
		this.itemDetailDecs=itemDetailDecs;
	}
	public void setMaterial(String material){
		this.material=material;
	}
	public void setSpecialInstructions(String specialInstructions){
		this.specialInstructions=specialInstructions;
	}
	public void setCreateUser(String createUser){
		this.createUser=createUser;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
}
