package com.tp.model.stg;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * storage数据库操作日志表
  */
public class StorageLogs extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450403690117L;

	/**主键id 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**用户id 数据类型bigint(20)*/
	private Long userId;
	
	/**被操作的表的名称 数据类型varchar(30)*/
	private String optionClassName;
	
	/**对应的记录的主键id 数据类型bigint(20)*/
	private Long optionRecordId;
	
	/**被操作的字段名称 数据类型varchar(30)*/
	private String optionFieldName;
	
	/**被操作的字段属性 数据类型varchar(20)*/
	private String optionFieldProperty;
	
	/**操作类型 1-增加 2-修改 3-删除 数据类型tinyint(4)*/
	private Integer optionType;
	
	/**先前的值 数据类型varchar(255)*/
	private String previousValue;
	
	/**操作后的值 数据类型varchar(255)*/
	private String afterwardsValue;
	
	/**操作时间 数据类型datetime*/
	private Date optionTime;
	
	
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
	public String getOptionFieldProperty(){
		return optionFieldProperty;
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
	public void setOptionFieldProperty(String optionFieldProperty){
		this.optionFieldProperty=optionFieldProperty;
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
}
