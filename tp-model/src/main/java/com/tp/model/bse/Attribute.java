package com.tp.model.bse;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 属性值表
  */
public class Attribute extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450402786414L;

	/** 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/**编号 数据类型varchar(64)*/
	private String code;
	
	/**名称 数据类型varchar(64)*/
	private String name;
	
	/**状态: 0-无效, 1-有效 数据类型tinyint(4)*/
	private Integer status;
	
	/**备注 数据类型varchar(255)*/
	private String remark;
	
	/**排序 数据类型int(11)*/
	private Integer sortNo;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**更新时间 数据类型datetime*/
	private Date modifyTime;
	
	/**0-不是必须项, 1-是必须项 数据类型tinyint(4)*/
	private Integer isRequired;
	
	/**0-不允许多选, 1-允许多选 数据类型tinyint(4)*/
	private Integer allowMultiSelect;
	
	/**0-不允许自定义, 1-允许自定义 数据类型tinyint(4)*/
	private Integer allowCustom;
	
	
	public Long getId(){
		return id;
	}
	public String getCode(){
		return code;
	}
	public String getName(){
		return name;
	}
	public Integer getStatus(){
		return status;
	}
	public String getRemark(){
		return remark;
	}
	public Integer getSortNo(){
		return sortNo;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Date getModifyTime(){
		return modifyTime;
	}
	public Integer getIsRequired(){
		return isRequired;
	}
	public Integer getAllowMultiSelect(){
		return allowMultiSelect;
	}
	public Integer getAllowCustom(){
		return allowCustom;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setCode(String code){
		this.code=code;
	}
	public void setName(String name){
		this.name=name;
	}
	public void setStatus(Integer status){
		this.status=status;
	}
	public void setRemark(String remark){
		this.remark=remark;
	}
	public void setSortNo(Integer sortNo){
		this.sortNo=sortNo;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setModifyTime(Date modifyTime){
		this.modifyTime=modifyTime;
	}
	public void setIsRequired(Integer isRequired){
		this.isRequired=isRequired;
	}
	public void setAllowMultiSelect(Integer allowMultiSelect){
		this.allowMultiSelect=allowMultiSelect;
	}
	public void setAllowCustom(Integer allowCustom){
		this.allowCustom=allowCustom;
	}
}
