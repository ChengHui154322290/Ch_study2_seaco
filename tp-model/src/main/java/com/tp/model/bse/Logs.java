package com.tp.model.bse;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * base数据库操作日志表
  */
public class Logs extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450402786420L;

	/**主键id 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**用户id 数据类型bigint(20)*/
	private Long userId;
	
	/**被操作的表的名称 数据类型varchar(255)*/
	private String optionClassName;
	
	/**对应的记录的主键id 数据类型bigint(20)*/
	private Long optionRecordId;
	
	/**被操作的字段名称 数据类型varchar(255)*/
	private String optionFieldName;
	
	/**操作类型 数据类型int(11)*/
	private Integer optionType;
	
	/**先前的值 数据类型varchar(255)*/
	private String previousValue;
	
	/**操作后的值 数据类型varchar(255)*/
	private String afterwardsValue;
	
	/**操作时间 数据类型datetime*/
	private Date optionTime;
	
	/**版本号 数据类型varchar(255)*/
	private String version;
	
	
	public Long getId(){
		return id;
	}
	public Long getUserId(){
		return userId;
	}
	public String getOptionClassName(){
		return optionClassName;
	}
	public Long getOptionRecordId(){
		return optionRecordId;
	}
	public String getOptionFieldName(){
		return optionFieldName;
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
	public Date getOptionTime(){
		return optionTime;
	}
	public String getVersion(){
		return version;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setUserId(Long userId){
		this.userId=userId;
	}
	public void setOptionClassName(String optionClassName){
		this.optionClassName=optionClassName;
	}
	public void setOptionRecordId(Long optionRecordId){
		this.optionRecordId=optionRecordId;
	}
	public void setOptionFieldName(String optionFieldName){
		this.optionFieldName=optionFieldName;
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
	public void setOptionTime(Date optionTime){
		this.optionTime=optionTime;
	}
	public void setVersion(String version){
		this.version=version;
	}
}
