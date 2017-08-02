package com.tp.model.prd;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 驳回详情
  */
public class ItemAuditRejectInfo extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451219235511L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**审核记录id 数据类型bigint(20)*/
	private Long auditDetailId;
	
	/**驳回类型,1,2,3,4,5 数据类型int(4)*/
	private Integer rejectType;
	
	/** 数据类型datetime*/
	private Date createTime;
	
	
	public Long getId(){
		return id;
	}
	public Long getAuditDetailId(){
		return auditDetailId;
	}
	public Integer getRejectType(){
		return rejectType;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setAuditDetailId(Long auditDetailId){
		this.auditDetailId=auditDetailId;
	}
	public void setRejectType(Integer rejectType){
		this.rejectType=rejectType;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
}
