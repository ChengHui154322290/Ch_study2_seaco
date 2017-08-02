package com.tp.model.cms;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 缓存设置规则表
  */
public class RedisIndexRule extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451446451848L;

	/** 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/**功能名称 数据类型varchar(50)*/
	private String functionName;
	
	/**功能代码 数据类型varchar(10)*/
	private String functionCode;
	
	/**平台类型 数据类型varchar(20)*/
	private String platformType;
	
	/**地区 数据类型varchar(20)*/
	private String area;
	
	/**地区代码 数据类型varchar(10)*/
	private String areaCode;
	
	/**缓存key值(功能代码-平台类型) 数据类型varchar(20)*/
	private String firstKey;
	
	/**缓存key值(功能代码-平台类型-地区代码) 数据类型varchar(50)*/
	private String secondKey;
	
	/**页数(供分页使用) 数据类型int(5)*/
	private Integer page;
	
	/**状态(启用/停用) 数据类型int(1)*/
	private Integer status;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**创建人 数据类型bigint(11)*/
	private Long creater;
	
	/**修改时间 数据类型datetime*/
	private Date updateTime;
	
	/**修改人 数据类型bigint(11)*/
	private Long updater;
	
	
	public Long getId(){
		return id;
	}
	public String getFunctionName(){
		return functionName;
	}
	public String getFunctionCode(){
		return functionCode;
	}
	public String getPlatformType(){
		return platformType;
	}
	public String getArea(){
		return area;
	}
	public String getAreaCode(){
		return areaCode;
	}
	public String getFirstKey(){
		return firstKey;
	}
	public String getSecondKey(){
		return secondKey;
	}
	public Integer getPage(){
		return page;
	}
	public Integer getStatus(){
		return status;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Long getCreater(){
		return creater;
	}
	public Date getUpdateTime(){
		return updateTime;
	}
	public Long getUpdater(){
		return updater;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setFunctionName(String functionName){
		this.functionName=functionName;
	}
	public void setFunctionCode(String functionCode){
		this.functionCode=functionCode;
	}
	public void setPlatformType(String platformType){
		this.platformType=platformType;
	}
	public void setArea(String area){
		this.area=area;
	}
	public void setAreaCode(String areaCode){
		this.areaCode=areaCode;
	}
	public void setFirstKey(String firstKey){
		this.firstKey=firstKey;
	}
	public void setSecondKey(String secondKey){
		this.secondKey=secondKey;
	}
	public void setPage(Integer page){
		this.page=page;
	}
	public void setStatus(Integer status){
		this.status=status;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setCreater(Long creater){
		this.creater=creater;
	}
	public void setUpdateTime(Date updateTime){
		this.updateTime=updateTime;
	}
	public void setUpdater(Long updater){
		this.updater=updater;
	}
}
