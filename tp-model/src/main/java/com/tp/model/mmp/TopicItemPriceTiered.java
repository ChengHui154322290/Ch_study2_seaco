package com.tp.model.mmp;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 商品阶梯价信息
  */
public class TopicItemPriceTiered extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451440579116L;

	/**序号 数据类型bigint(18)*/
	@Id
	private Long id;
	
	/**活动序号 数据类型bigint(18)*/
	private Long topicId;
	
	/**活动商品序号 数据类型bigint(18)*/
	private Long prItemId;
	
	/**编号 数据类型int(11)*/
	private Integer number;
	
	/**阶梯人数 数据类型int(11)*/
	private Integer tieredAmount;
	
	/**阶梯价格 数据类型double(18,2)*/
	private Double tieredPrice;
	
	/**可购买阶梯 数据类型tinyint(1)*/
	private Integer buyTiered;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**创建人 数据类型varchar(32)*/
	private String createUser;
	
	/**修改时间 数据类型datetime*/
	private Date updateTime;
	
	/**修改人 数据类型varchar(32)*/
	private String updateUser;
	
	
	public Long getId(){
		return id;
	}
	public Long getTopicId(){
		return topicId;
	}
	public Long getPrItemId(){
		return prItemId;
	}
	public Integer getNumber(){
		return number;
	}
	public Integer getTieredAmount(){
		return tieredAmount;
	}
	public Double getTieredPrice(){
		return tieredPrice;
	}
	public Integer getBuyTiered(){
		return buyTiered;
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
	public void setTopicId(Long topicId){
		this.topicId=topicId;
	}
	public void setPrItemId(Long prItemId){
		this.prItemId=prItemId;
	}
	public void setNumber(Integer number){
		this.number=number;
	}
	public void setTieredAmount(Integer tieredAmount){
		this.tieredAmount=tieredAmount;
	}
	public void setTieredPrice(Double tieredPrice){
		this.tieredPrice=tieredPrice;
	}
	public void setBuyTiered(Integer buyTiered){
		this.buyTiered=buyTiered;
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
