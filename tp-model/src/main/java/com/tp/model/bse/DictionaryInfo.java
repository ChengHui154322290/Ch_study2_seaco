package com.tp.model.bse;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 数据字典：信息
  */
public class DictionaryInfo extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450402786417L;

	/**主键id 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**字典code 数据类型varchar(32)*/
	private String code;
	
	/**种类id 数据类型varchar(32)*/
	private String catId;
	
	/**名称 数据类型varchar(64)*/
	private String name;
	
	/**商业排序号 数据类型int(11)*/
	private Integer sortNo;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**修改时间 数据类型datetime*/
	private Date modifyTime;
	
	
	public Long getId(){
		return id;
	}
	public String getCode(){
		return code;
	}
	public String getCatId(){
		return catId;
	}
	public String getName(){
		return name;
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
	public void setId(Long id){
		this.id=id;
	}
	public void setCode(String code){
		this.code=code;
	}
	public void setCatId(String catId){
		this.catId=catId;
	}
	public void setName(String name){
		this.name=name;
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
}
