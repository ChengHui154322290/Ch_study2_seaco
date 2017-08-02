package com.tp.model.prd;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 商品操作日志
  */
public class ItemLogs extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450873698779L;

	/**主键 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**操作人id 数据类型bigint(20)*/
	private Long userId;
	
	/**pridid 数据类型varchar(50)*/
	private String prdid;
	
	/**操作内容描述 数据类型varchar(255)*/
	private String optDesc;
	
	/**操作类型描述1增加,2更新,3删除 数据类型tinyint(4)*/
	private Integer optionType;
	
	/**先前的值 数据类型varchar(2000)*/
	private String previousValue;
	
	/**更新后的值 数据类型varchar(2000)*/
	private String afterwardsValue;
	
	/**操作时间 数据类型datetime*/
	private Date createTime;
	
	/** 数据类型varchar(32)*/
	private String createUser;
	
	
	public Long getId(){
		return id;
	}
	public Long getUserId(){
		return userId;
	}
	public String getPrdid(){
		return prdid;
	}
	public String getOptDesc(){
		return optDesc;
	}
	public Integer getOptionType(){
		return optionType;
	}
	public String getPreviousValue(){
		return previousValue;
	}
	public String getAfterwardsValue(){
		return afterwardsValue;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public String getCreateUser(){
		return createUser;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setUserId(Long userId){
		this.userId=userId;
	}
	public void setPrdid(String prdid){
		this.prdid=prdid;
	}
	public void setOptDesc(String optDesc){
		this.optDesc=optDesc;
	}
	public void setOptionType(Integer optionType){
		this.optionType=optionType;
	}
	public void setPreviousValue(String previousValue){
		this.previousValue=previousValue;
	}
	public void setAfterwardsValue(String afterwardsValue){
		this.afterwardsValue=afterwardsValue;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setCreateUser(String createUser){
		this.createUser=createUser;
	}
}
