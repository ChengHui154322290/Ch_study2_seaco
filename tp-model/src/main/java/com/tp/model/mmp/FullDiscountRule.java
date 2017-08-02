package com.tp.model.mmp;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 优惠政策规则
  */
public class FullDiscountRule extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451440579108L;

	/**序号 数据类型bigint(18)*/
	@Id
	private Long id;
	
	/**优惠政策序号 数据类型bigint(18)*/
	private Long discountId;
	
	/**满减类型 1-按金额 2-按件 数据类型smallint(1)*/
	private Integer type;
	
	/**规则名称 数据类型varchar(50)*/
	private String ruleName;
	
	/**满金额 数据类型double(18,2)*/
	private Double fullPrice;
	
	/**满件数 数据类型smallint(1)*/
	private Integer fullCount;
	
	/**满减金额 数据类型double(18,2)*/
	private Double discountPrice;
	
	/**限制参与次数 数据类型smallint(1)*/
	private Integer limitUse;
	
	/**允许累积 数据类型tinyint(1)*/
	private Integer overlay;
	
	/** 数据类型varchar(500)*/
	private String remark;
	
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
	public Long getDiscountId(){
		return discountId;
	}
	public Integer getType(){
		return type;
	}
	public String getRuleName(){
		return ruleName;
	}
	public Double getFullPrice(){
		return fullPrice;
	}
	public Integer getFullCount(){
		return fullCount;
	}
	public Double getDiscountPrice(){
		return discountPrice;
	}
	public Integer getLimitUse(){
		return limitUse;
	}
	public Integer getOverlay(){
		return overlay;
	}
	public String getRemark(){
		return remark;
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
	public void setDiscountId(Long discountId){
		this.discountId=discountId;
	}
	public void setType(Integer type){
		this.type=type;
	}
	public void setRuleName(String ruleName){
		this.ruleName=ruleName;
	}
	public void setFullPrice(Double fullPrice){
		this.fullPrice=fullPrice;
	}
	public void setFullCount(Integer fullCount){
		this.fullCount=fullCount;
	}
	public void setDiscountPrice(Double discountPrice){
		this.discountPrice=discountPrice;
	}
	public void setLimitUse(Integer limitUse){
		this.limitUse=limitUse;
	}
	public void setOverlay(Integer overlay){
		this.overlay=overlay;
	}
	public void setRemark(String remark){
		this.remark=remark;
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
