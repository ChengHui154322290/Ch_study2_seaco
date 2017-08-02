package com.tp.model.ord;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 
  */
public class MemRealinfo extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451468597510L;

	/** 数据类型bigint(12)*/
	@Id
	private Long id;
	
	/**会员id 数据类型bigint(12)*/
	private Long memberId;
	
	/**父订单号 数据类型bigint(20)*/
	private Long parentOrderCode;
	
	/**父订单号 数据类型bigint(14)*/
	private Long parentOrderId;
	
	/**真实姓名 数据类型varchar(20)*/
	private String realName;
	
	/**身份证号 数据类型varchar(30)*/
	private String identityCode;
	
	/**身份证图片正面 数据类型varchar(100)*/
	private String identityFrontImg;
	
	/**身份证图片反面 数据类型varchar(100)*/
	private String identityBackImg;
	
	/**用户常住地址 数据类型varchar(255)*/
	private String address;
	
	/** 数据类型datetime*/
	private Date createTime;
	
	/** 数据类型datetime*/
	private Date updateTime;
	
	
	public Long getId(){
		return id;
	}
	public Long getMemberId(){
		return memberId;
	}
	public Long getParentOrderCode(){
		return parentOrderCode;
	}
	public Long getParentOrderId(){
		return parentOrderId;
	}
	public String getRealName(){
		return realName;
	}
	public String getIdentityCode(){
		return identityCode;
	}
	public String getIdentityFrontImg(){
		return identityFrontImg;
	}
	public String getIdentityBackImg(){
		return identityBackImg;
	}
	public String getAddress(){
		return address;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Date getUpdateTime(){
		return updateTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setMemberId(Long memberId){
		this.memberId=memberId;
	}
	public void setParentOrderCode(Long parentOrderCode){
		this.parentOrderCode=parentOrderCode;
	}
	public void setParentOrderId(Long parentOrderId){
		this.parentOrderId=parentOrderId;
	}
	public void setRealName(String realName){
		this.realName=realName;
	}
	public void setIdentityCode(String identityCode){
		this.identityCode=identityCode;
	}
	public void setIdentityFrontImg(String identityFrontImg){
		this.identityFrontImg=identityFrontImg;
	}
	public void setIdentityBackImg(String identityBackImg){
		this.identityBackImg=identityBackImg;
	}
	public void setAddress(String address){
		this.address=address;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setUpdateTime(Date updateTime){
		this.updateTime=updateTime;
	}
}
