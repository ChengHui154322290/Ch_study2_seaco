package com.tp.model.mmp;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 审批信息表
  */
public class AuditInfo extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451440579101L;

	/**序号 数据类型bigint(18)*/
	@Id
	private Long id;
	
	/**1-活动信息 2-活动变更单 3-优惠券 4-运费模板 5-满减活动 6-阶梯团购  数据类型smallint(6)*/
	private Integer type;
	
	/**业务序号 数据类型bigint(18)*/
	private Long bizId;
	
	/**审核人id 数据类型bigint(20)*/
	private Long auditId;
	
	/**审核人 数据类型varchar(50)*/
	private String auditName;
	
	/**审核操作 数据类型varchar(20)*/
	private String auditOperation;
	
	/**备注 数据类型text*/
	private String remark;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	
	public Long getId(){
		return id;
	}
	public Integer getType(){
		return type;
	}
	public Long getBizId(){
		return bizId;
	}
	public Long getAuditId(){
		return auditId;
	}
	public String getAuditName(){
		return auditName;
	}
	public String getAuditOperation(){
		return auditOperation;
	}
	public String getRemark(){
		return remark;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setType(Integer type){
		this.type=type;
	}
	public void setBizId(Long bizId){
		this.bizId=bizId;
	}
	public void setAuditId(Long auditId){
		this.auditId=auditId;
	}
	public void setAuditName(String auditName){
		this.auditName=auditName;
	}
	public void setAuditOperation(String auditOperation){
		this.auditOperation=auditOperation;
	}
	public void setRemark(String remark){
		this.remark=remark;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
}
