package com.tp.model.dss;

import java.io.Serializable;
import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 推荐新人佣金明细表
  */
public class RefereeDetail extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1456900594219L;

	/**拉新明细编号 数据类型bigint(14)*/
	@Id
	private Long refereeDetailId;
	
	/**拉新明细编号 数据类型bigint(16)*/
	private Long refereeDetailCode;
	
	/**拉新用户编号 数据类型bigint(14)*/
	private Long memberId;
	
	/**分销员编号 数据类型bigint(14)*/
	private Long promoterId;
	
	/**佣金 数据类型double(3,2)*/
	private Double commision;
	
	/**创建者 数据类型varchar(32)*/
	private String createUser;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	@Virtual
	private String nickName;
	@Virtual
	private String promoterName;
	@Virtual
	private String memberAccount;

	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public Long getRefereeDetailId(){
		return refereeDetailId;
	}
	public Long getMemberId(){
		return memberId;
	}
	public Long getPromoterId(){
		return promoterId;
	}
	public Double getCommision(){
		return commision;
	}
	public String getCreateUser(){
		return createUser;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public void setRefereeDetailId(Long refereeDetailId){
		this.refereeDetailId=refereeDetailId;
	}
	public void setMemberId(Long memberId){
		this.memberId=memberId;
	}
	public void setPromoterId(Long promoterId){
		this.promoterId=promoterId;
	}
	public void setCommision(Double commision){
		this.commision=commision;
	}
	public void setCreateUser(String createUser){
		this.createUser=createUser;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public Long getRefereeDetailCode() {
		return refereeDetailCode;
	}
	public void setRefereeDetailCode(Long refereeDetailCode) {
		this.refereeDetailCode = refereeDetailCode;
	}
	public String getPromoterName() {
		return promoterName;
	}
	public void setPromoterName(String promoterName) {
		this.promoterName = promoterName;
	}
	public String getMemberAccount() {
		return memberAccount;
	}
	public void setMemberAccount(String memberAccount) {
		this.memberAccount = memberAccount;
	}
}
