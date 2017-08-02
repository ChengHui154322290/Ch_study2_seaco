package com.tp.model.mmp;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 审核日志记录
  */
public class CouponAudit extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451440579103L;

	/**自增主键 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**优惠券批次主键 数据类型bigint(20)*/
	private Long couponId;
	
	/**审核人id 数据类型bigint(20)*/
	private Long auditId;
	
	/**审核人姓名 数据类型varchar(50)*/
	private String auditName;
	
	/**审核执行操作 数据类型varchar(20)*/
	private String auditOperation;
	
	/**备注 数据类型varchar(255)*/
	private String remark;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**创建人 数据类型bigint(20)*/
	private Long createId;
	
	
	public Long getId(){
		return id;
	}
	public Long getCouponId(){
		return couponId;
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
	public Long getCreateId(){
		return createId;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setCouponId(Long couponId){
		this.couponId=couponId;
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
	public void setCreateId(Long createId){
		this.createId=createId;
	}
}
