package com.tp.model.cms;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 达人会员表
  */
public class MasterplanMember extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451446451848L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**手机号 数据类型varchar(20)*/
	private String cmmMobile;
	
	/**用户ID 数据类型bigint(20)*/
	private Long cmmUserId;
	
	/**用户名 数据类型varchar(60)*/
	private String cmmUserName;
	
	/**申请宣言 数据类型varchar(3000)*/
	private String cmmApplyDeclaration;
	
	/**申请时间 数据类型datetime*/
	private Date cmmApplyDate;
	
	/**审核状态 数据类型varchar(10)*/
	private String cmmExamineStatus;
	
	/**审核意见 数据类型varchar(512)*/
	private String cmmExamineSuggest;
	
	/**审核时间 数据类型datetime*/
	private Date cmmExamineDate;
	
	/**备注 数据类型varchar(521)*/
	private String cmmRemarks;
	
	/**达人活动表主键 数据类型bigint(20)*/
	private Long cmmActivityId;
	
	/**优惠券批次号(即id) 数据类型bigint(20)*/
	private Long cmmCouponId;
	
	/**优惠券名称 数据类型varchar(50)*/
	private String cmmCouponName;
	
	
	public Long getId(){
		return id;
	}
	public String getCmmMobile(){
		return cmmMobile;
	}
	public Long getCmmUserId(){
		return cmmUserId;
	}
	public String getCmmUserName(){
		return cmmUserName;
	}
	public String getCmmApplyDeclaration(){
		return cmmApplyDeclaration;
	}
	public Date getCmmApplyDate(){
		return cmmApplyDate;
	}
	public String getCmmExamineStatus(){
		return cmmExamineStatus;
	}
	public String getCmmExamineSuggest(){
		return cmmExamineSuggest;
	}
	public Date getCmmExamineDate(){
		return cmmExamineDate;
	}
	public String getCmmRemarks(){
		return cmmRemarks;
	}
	public Long getCmmActivityId(){
		return cmmActivityId;
	}
	public Long getCmmCouponId(){
		return cmmCouponId;
	}
	public String getCmmCouponName(){
		return cmmCouponName;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setCmmMobile(String cmmMobile){
		this.cmmMobile=cmmMobile;
	}
	public void setCmmUserId(Long cmmUserId){
		this.cmmUserId=cmmUserId;
	}
	public void setCmmUserName(String cmmUserName){
		this.cmmUserName=cmmUserName;
	}
	public void setCmmApplyDeclaration(String cmmApplyDeclaration){
		this.cmmApplyDeclaration=cmmApplyDeclaration;
	}
	public void setCmmApplyDate(Date cmmApplyDate){
		this.cmmApplyDate=cmmApplyDate;
	}
	public void setCmmExamineStatus(String cmmExamineStatus){
		this.cmmExamineStatus=cmmExamineStatus;
	}
	public void setCmmExamineSuggest(String cmmExamineSuggest){
		this.cmmExamineSuggest=cmmExamineSuggest;
	}
	public void setCmmExamineDate(Date cmmExamineDate){
		this.cmmExamineDate=cmmExamineDate;
	}
	public void setCmmRemarks(String cmmRemarks){
		this.cmmRemarks=cmmRemarks;
	}
	public void setCmmActivityId(Long cmmActivityId){
		this.cmmActivityId=cmmActivityId;
	}
	public void setCmmCouponId(Long cmmCouponId){
		this.cmmCouponId=cmmCouponId;
	}
	public void setCmmCouponName(String cmmCouponName){
		this.cmmCouponName=cmmCouponName;
	}
}
