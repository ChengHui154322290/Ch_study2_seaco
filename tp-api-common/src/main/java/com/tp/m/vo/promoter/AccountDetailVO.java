package com.tp.m.vo.promoter;

import com.tp.m.base.BaseVO;

public class AccountDetailVO implements BaseVO{
	private static final long serialVersionUID = 6194436293501331814L;
	private Long id;					//流水编号
	private Long userid;				//用户账号
	private String type;				//用户类型
	private String surplusamount;		//账户余额
	private String amount;				//金额
	private String accounttime;			//日期
	private String accounttype;			//账务类型
	private String bussinesstype;		//业务类型
	private String refereeNickName;		// 新人nickname
	private String refereeName;			// 新人姓名	
	private String bussinesstypedesc;		//业务类型描述	

	public String getBussinesstypedesc() {
		return bussinesstypedesc;
	}
	public void setBussinesstypedesc(String bussinesstypedesc) {
		this.bussinesstypedesc = bussinesstypedesc;
	}
	public String getRefereeNickName() {
		return refereeNickName;
	}
	public void setRefereeNickName(String refereeNickName) {
		this.refereeNickName = refereeNickName;
	}
	public String getRefereeName() {
		return refereeName;
	}
	public void setRefereeName(String refereeName) {
		this.refereeName = refereeName;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserid() {
		return userid;
	}
	public void setUserid(Long userid) {
		this.userid = userid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSurplusamount() {
		return surplusamount;
	}
	public void setSurplusamount(String surplusamount) {
		this.surplusamount = surplusamount;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getAccounttime() {
		return accounttime;
	}
	public void setAccounttime(String accounttime) {
		this.accounttime = accounttime;
	}
	public String getAccounttype() {
		return accounttype;
	}
	public void setAccounttype(String accounttype) {
		this.accounttype = accounttype;
	}
	public String getBussinesstype() {
		return bussinesstype;
	}
	public void setBussinesstype(String bussinesstype) {
		this.bussinesstype = bussinesstype;
	}	
}
