package com.tp.model.bse;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 标签信息表
  */
public class Tags extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450402786421L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**标签名称 数据类型varchar(20)*/
	private String name;
	
	/**备注 数据类型varchar(50)*/
	private String remark;
	
	/**状态0-无效 1-有效 数据类型tinyint(4)*/
	private Integer status;
	
	/**创建人id 数据类型bigint(20)*/
	private Long createUserId;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**最后修改人id 数据类型bigint(20)*/
	private Long modifyUserId;
	
	/**最后修改时间 数据类型datetime*/
	private Date modifyTime;
	
	
	public Long getId(){
		return id;
	}
	public String getName(){
		return name;
	}
	public String getRemark(){
		return remark;
	}
	public Integer getStatus(){
		return status;
	}
	public Long getCreateUserId(){
		return createUserId;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Long getModifyUserId(){
		return modifyUserId;
	}
	public Date getModifyTime(){
		return modifyTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setName(String name){
		this.name=name;
	}
	public void setRemark(String remark){
		this.remark=remark;
	}
	public void setStatus(Integer status){
		this.status=status;
	}
	public void setCreateUserId(Long createUserId){
		this.createUserId=createUserId;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setModifyUserId(Long modifyUserId){
		this.modifyUserId=modifyUserId;
	}
	public void setModifyTime(Date modifyTime){
		this.modifyTime=modifyTime;
	}
}
