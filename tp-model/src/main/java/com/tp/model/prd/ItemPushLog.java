package com.tp.model.prd;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 第三方推送库存和成本价接口日志表
  */
public class ItemPushLog extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1499917200123L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/** 数据类型varchar(50)*/
	private String sku;
	
	/**第三方商品编号 数据类型varchar(50)*/
	private String goodsCode;
	
	/**日志类型  1-设置库存接口 ， 2-设置成本价接口 数据类型int(2)*/
	private Integer type;
	
	/**第三方商品库存数量 数据类型int(11)*/
	private Integer inventory;
	
	/**成本价 数据类型double(11,2)*/
	private Double costPrice;
	
	/** 数据类型varchar(255)*/
	private String createUser;
	
	/** 数据类型datetime*/
	private Date createTime;
	
	
	public Long getId(){
		return id;
	}
	public String getSku(){
		return sku;
	}
	public String getGoodsCode(){
		return goodsCode;
	}
	public Integer getType(){
		return type;
	}
	public Integer getInventory(){
		return inventory;
	}
	public Double getCostPrice(){
		return costPrice;
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
	public void setSku(String sku){
		this.sku=sku;
	}
	public void setGoodsCode(String goodsCode){
		this.goodsCode=goodsCode;
	}
	public void setType(Integer type){
		this.type=type;
	}
	public void setInventory(Integer inventory){
		this.inventory=inventory;
	}
	public void setCostPrice(Double costPrice){
		this.costPrice=costPrice;
	}
	public void setCreateUser(String createUser){
		this.createUser=createUser;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
}
