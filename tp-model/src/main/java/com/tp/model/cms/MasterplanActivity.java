package com.tp.model.cms;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 达人活动主表
  */
public class MasterplanActivity extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451446451848L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**编号 数据类型varchar(20)*/
	private String cmaNum;
	
	/**活动名称 数据类型varchar(30)*/
	private String cmaActivityName;
	
	/**活动状态 数据类型varchar(10)*/
	private String cmaActivityStatus;
	
	/**优惠券批次号(即id) 数据类型bigint(20)*/
	private Long cmaCouponId;
	
	/**优惠券名称 数据类型varchar(50)*/
	private String cmaCouponName;
	
	/**计划招募人数 数据类型int(10)*/
	private Integer cmaPlanRecruit;
	
	/**实际招募人数 数据类型int(10)*/
	private Integer cmaActualRecruit;
	
	/**招募时间开始 数据类型datetime*/
	private Date cmaRecruitStartdate;
	
	/**招募时间结束 数据类型datetime*/
	private Date cmaRecruitEnddate;
	
	/**活动时间开始 数据类型datetime*/
	private Date cmaActivityStartdate;
	
	/**活动时间结束 数据类型datetime*/
	private Date cmaActivityEnddate;
	
	/**结算时间开始 数据类型datetime*/
	private Date cmaSettleStartdate;
	
	/**结算时间结束 数据类型datetime*/
	private Date cmaSettleEnddate;
	
	/**备注 数据类型varchar(512)*/
	private String cmaRemarks;
	
	/**达人标示 数据类型varchar(10)*/
	private String cmaIdent;
	
	
	public Long getId(){
		return id;
	}
	public String getCmaNum(){
		return cmaNum;
	}
	public String getCmaActivityName(){
		return cmaActivityName;
	}
	public String getCmaActivityStatus(){
		return cmaActivityStatus;
	}
	public Long getCmaCouponId(){
		return cmaCouponId;
	}
	public String getCmaCouponName(){
		return cmaCouponName;
	}
	public Integer getCmaPlanRecruit(){
		return cmaPlanRecruit;
	}
	public Integer getCmaActualRecruit(){
		return cmaActualRecruit;
	}
	public Date getCmaRecruitStartdate(){
		return cmaRecruitStartdate;
	}
	public Date getCmaRecruitEnddate(){
		return cmaRecruitEnddate;
	}
	public Date getCmaActivityStartdate(){
		return cmaActivityStartdate;
	}
	public Date getCmaActivityEnddate(){
		return cmaActivityEnddate;
	}
	public Date getCmaSettleStartdate(){
		return cmaSettleStartdate;
	}
	public Date getCmaSettleEnddate(){
		return cmaSettleEnddate;
	}
	public String getCmaRemarks(){
		return cmaRemarks;
	}
	public String getCmaIdent(){
		return cmaIdent;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setCmaNum(String cmaNum){
		this.cmaNum=cmaNum;
	}
	public void setCmaActivityName(String cmaActivityName){
		this.cmaActivityName=cmaActivityName;
	}
	public void setCmaActivityStatus(String cmaActivityStatus){
		this.cmaActivityStatus=cmaActivityStatus;
	}
	public void setCmaCouponId(Long cmaCouponId){
		this.cmaCouponId=cmaCouponId;
	}
	public void setCmaCouponName(String cmaCouponName){
		this.cmaCouponName=cmaCouponName;
	}
	public void setCmaPlanRecruit(Integer cmaPlanRecruit){
		this.cmaPlanRecruit=cmaPlanRecruit;
	}
	public void setCmaActualRecruit(Integer cmaActualRecruit){
		this.cmaActualRecruit=cmaActualRecruit;
	}
	public void setCmaRecruitStartdate(Date cmaRecruitStartdate){
		this.cmaRecruitStartdate=cmaRecruitStartdate;
	}
	public void setCmaRecruitEnddate(Date cmaRecruitEnddate){
		this.cmaRecruitEnddate=cmaRecruitEnddate;
	}
	public void setCmaActivityStartdate(Date cmaActivityStartdate){
		this.cmaActivityStartdate=cmaActivityStartdate;
	}
	public void setCmaActivityEnddate(Date cmaActivityEnddate){
		this.cmaActivityEnddate=cmaActivityEnddate;
	}
	public void setCmaSettleStartdate(Date cmaSettleStartdate){
		this.cmaSettleStartdate=cmaSettleStartdate;
	}
	public void setCmaSettleEnddate(Date cmaSettleEnddate){
		this.cmaSettleEnddate=cmaSettleEnddate;
	}
	public void setCmaRemarks(String cmaRemarks){
		this.cmaRemarks=cmaRemarks;
	}
	public void setCmaIdent(String cmaIdent){
		this.cmaIdent=cmaIdent;
	}
}
