package com.tp.model.prd;

import java.io.Serializable;
import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 商品的属性组
  */
public class ItemAttribute extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450873698775L;

	/**主键 数据类型bigint(18)*/
	@Id
	private Long id;
	
	/** 数据类型bigint(18)*/
	private Long detailId;
	
	/**商品ID 数据类型bigint(18)*/
	private Long itemId;
	
	/**属性名称 用户直接输入的 数据类型varchar(50)*/
	private String attrKey;
	
	/**属性值 直接存储输入的值 数据类型varchar(200)*/
	private String attrValue;
	
	/**属性id，关联属性信息表 数据类型bigint(20)*/
	private Long attrId;
	
	/**值id，关联属性值id 数据类型bigint(20)*/
	private Long attrValueId;
	
	/**是否自定义 0-否 取attr_id和attr_vlaue_id 1-是(取attr_key attr_value) 2 服务商品属性组 数据类型tinyint(4)*/
	private Integer custom;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**更新时间 数据类型datetime*/
	private Date updateTime;
	
	/** 多选拼接字符串,*/
	@Virtual
	private String attrValueIds;
	
	public Long getId(){
		return id;
	}
	public Long getDetailId(){
		return detailId;
	}
	public Long getItemId(){
		return itemId;
	}
	public String getAttrKey(){
		return attrKey;
	}
	public String getAttrValue(){
		return attrValue;
	}
	public Long getAttrId(){
		return attrId;
	}
	public Long getAttrValueId(){
		return attrValueId;
	}
	public Integer getCustom(){
		return custom;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Date getUpdateTime(){
		return updateTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setDetailId(Long detailId){
		this.detailId=detailId;
	}
	public void setItemId(Long itemId){
		this.itemId=itemId;
	}
	public void setAttrKey(String attrKey){
		this.attrKey=attrKey;
	}
	public void setAttrValue(String attrValue){
		this.attrValue=attrValue;
	}
	public void setAttrId(Long attrId){
		this.attrId=attrId;
	}
	public void setAttrValueId(Long attrValueId){
		this.attrValueId=attrValueId;
	}
	public void setCustom(Integer custom){
		this.custom=custom;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setUpdateTime(Date updateTime){
		this.updateTime=updateTime;
	}
	public String getAttrValueIds() {
		return attrValueIds;
	}
	public void setAttrValueIds(String attrValueIds) {
		this.attrValueIds = attrValueIds;
	}
}
