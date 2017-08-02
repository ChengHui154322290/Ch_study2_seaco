package com.tp.model.dss;

import java.io.Serializable;
import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.common.vo.DssConstant;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 账户流水表
  */
public class AccountDetail extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1456900594216L;

	/**流水编号 数据类型bigint(14)*/
	@Id
	private Long accountDetailId;
	
	/**用户账号 数据类型bigint(14)*/
	private Long userId;
	
	/**用户类型(0:卡券推广，1：店铺分销) 数据类型tinyint(4)*/
	private Integer userType;
	
	/**账户余额 数据类型double(8,2)*/
	private Double surplusAmount;
	
	/**金额 数据类型double(6,2)*/
	private Double amount;
	
	/**日期 数据类型datetime*/
	private Date accountTime;
	
	/**账务类型(1：进账，2：出帐) 数据类型tinyint(1)*/
	private Integer accountType;
	
	/**业务类型(1：订单提佣  2：新人注册  3：退款  4:提现) 数据类型tinyint(2)*/
	private Integer bussinessType;
	
	/**业务编码 数据类型bigint(16)*/
	private Long bussinessCode;
	
	/**创建者 数据类型varchar(32)*/
	private String createUser;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**只有是新人注册时才使用*/
	@Virtual
	private String refereeNickName;	
	@Virtual
	private String refereeName;
		
	
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

	
	public String getUserTypeCn(){
		return DssConstant.PROMOTER_TYPE.getCnName(userType);
	}
	
	public String getAccountTypeCn(){
		return DssConstant.ACCOUNT_TYPE.getCnName(accountType);
	}
	
	public String getBussinessTypeCn(){
		return DssConstant.BUSSINESS_TYPE.getCnName(bussinessType);
	}
	
	public Long getAccountDetailId(){
		return accountDetailId;
	}
	public Long getUserId(){
		return userId;
	}
	public Integer getUserType(){
		return userType;
	}
	public Double getSurplusAmount(){
		return surplusAmount;
	}
	public Double getAmount(){
		return amount;
	}
	public Date getAccountTime(){
		return accountTime;
	}
	public Integer getAccountType(){
		return accountType;
	}
	public Integer getBussinessType(){
		return bussinessType;
	}
	public Long getBussinessCode(){
		return bussinessCode;
	}
	public String getCreateUser(){
		return createUser;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public void setAccountDetailId(Long accountDetailId){
		this.accountDetailId=accountDetailId;
	}
	public void setUserId(Long userId){
		this.userId=userId;
	}
	public void setUserType(Integer userType){
		this.userType=userType;
	}
	public void setSurplusAmount(Double surplusAmount){
		this.surplusAmount=surplusAmount;
	}
	public void setAmount(Double amount){
		this.amount=amount;
	}
	public void setAccountTime(Date accountTime){
		this.accountTime=accountTime;
	}
	public void setAccountType(Integer accountType){
		this.accountType=accountType;
	}
	public void setBussinessType(Integer bussinessType){
		this.bussinessType=bussinessType;
	}
	public void setBussinessCode(Long bussinessCode){
		this.bussinessCode=bussinessCode;
	}
	public void setCreateUser(String createUser){
		this.createUser=createUser;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
}
