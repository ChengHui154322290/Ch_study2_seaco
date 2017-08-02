package com.tp.model.bse;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 规格表
  */
public class Spec extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450402786420L;

	/**主键ID 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/**规格编号 数据类型varchar(11)*/
	private String code;
	
	/**规格 数据类型varchar(11)*/
	private String spec;
	
	/**默认1有效,0无效(状态) 数据类型tinyint(4)*/
	private Integer status;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**更新时间 数据类型datetime*/
	private Date modifyTime;
	
	/**备注 数据类型varchar(255)*/
	private String remark;
	
	/**规格排序 数据类型int(11)*/
	private Integer sort;
	
	
	public Long getId(){
		return id;
	}
	public String getCode(){
		return code;
	}
	public String getSpec(){
		return spec;
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
	public Integer getSort(){
		return sort;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setCode(String code){
		this.code=code;
	}
	public void setSpec(String spec){
		this.spec=spec;
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
	public void setSort(Integer sort){
		this.sort=sort;
	}
}
