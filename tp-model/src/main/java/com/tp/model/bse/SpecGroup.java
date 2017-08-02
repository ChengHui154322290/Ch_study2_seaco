package com.tp.model.bse;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 规格组表
  */
public class SpecGroup extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450402786421L;

	/**尺码组ID 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/**规格组编号 数据类型int(11)*/
	private Integer code;
	
	/**规格组名称 数据类型varchar(255)*/
	private String name;
	
	/**1为有效 ,0为无效 数据类型tinyint(4)*/
	private Integer status;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**更新时间 数据类型datetime*/
	private Date modifyTime;
	
	/**备注 数据类型varchar(255)*/
	private String remark;
	
	
	public Long getId(){
		return id;
	}
	public Integer getCode(){
		return code;
	}
	public String getName(){
		return name;
	}
	public Integer getStatus(){
		return status;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Date getModifyTime(){
		return modifyTime;
	}
	public String getRemark(){
		return remark;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setCode(Integer code){
		this.code=code;
	}
	public void setName(String name){
		this.name=name;
	}
	public void setStatus(Integer status){
		this.status=status;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setModifyTime(Date modifyTime){
		this.modifyTime=modifyTime;
	}
	public void setRemark(String remark){
		this.remark=remark;
	}
}
