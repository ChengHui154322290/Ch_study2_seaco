package com.tp.model.mmp;

import java.io.Serializable;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 审核信息表
  */
public class ApprovalInfo extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451440579100L;

	/** 数据类型int(4)*/
	@Id
	private Integer id;
	
	/**业务类型 1-促销活动 2-促销变更单 3-优惠券 4-满减活动 数据类型tinyint(4)*/
	private Integer bizType;
	
	/**审批状态参考值 1 - 审批通过 2 - 审批驳回 数据类型tinyint(4)*/
	private Integer approvalValue;
	
	/** 数据类型int(4)*/
	private Integer bizValue;
	
	
	public Integer getId(){
		return id;
	}
	public Integer getBizType(){
		return bizType;
	}
	public Integer getApprovalValue(){
		return approvalValue;
	}
	public Integer getBizValue(){
		return bizValue;
	}
	public void setId(Integer id){
		this.id=id;
	}
	public void setBizType(Integer bizType){
		this.bizType=bizType;
	}
	public void setApprovalValue(Integer approvalValue){
		this.approvalValue=approvalValue;
	}
	public void setBizValue(Integer bizValue){
		this.bizValue=bizValue;
	}
}
