package com.tp.model.cmt;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 评论审核日志表
  */
public class ReviewAudit extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450404446076L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**评论id 数据类型bigint(20)*/
	private Long reviewId;
	
	/**审核结果 0-驳回 1-通过 数据类型tinyint(4)*/
	private Integer status;
	
	/**驳回理由 数据类型varchar(50)*/
	private String reason;
	
	/**备注 数据类型varchar(50)*/
	private String remark;
	
	/**审核人id 数据类型bigint(20)*/
	private Long createUserId;
	
	/**审核时间 数据类型datetime*/
	private Date createTime;
	
	
	public Long getId(){
		return id;
	}
	public Long getReviewId(){
		return reviewId;
	}
	public Integer getStatus(){
		return status;
	}
	public String getReason(){
		return reason;
	}
	public String getRemark(){
		return remark;
	}
	public Long getCreateUserId(){
		return createUserId;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setReviewId(Long reviewId){
		this.reviewId=reviewId;
	}
	public void setStatus(Integer status){
		this.status=status;
	}
	public void setReason(String reason){
		this.reason=reason;
	}
	public void setRemark(String remark){
		this.remark=remark;
	}
	public void setCreateUserId(Long createUserId){
		this.createUserId=createUserId;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
}
