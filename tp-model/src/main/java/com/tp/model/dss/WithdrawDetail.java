package com.tp.model.dss;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.common.vo.DssConstant;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 提现明细表
  */
public class WithdrawDetail extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1456900594220L;

	/**提现明细编号 数据类型bigint(14)*/
	@Id
	private Long withdrawDetailId;
	
	/**提现明细编码 数据类型bigint(16)*/
	private Long withdrawDetailCode;
	
	/**提现日期 数据类型datetime*/
	private Date withdrawTime;
	
	/**提现金额 数据类型double(8,2)*/
	private Double withdrawAmount;
	
	/**提现状态(0:申请，1：审核中，2：审核通过，3：审核未通过，4：财务打款成功，5：财务打款失败) 数据类型tinyint(4)*/
	private Integer withdrawStatus;
	
	/**提现者 数据类型bigint(14)*/
	private Long withdrawor;
	
	/**提现者类型  数据类型tinyint(1)*/
	private Integer userType;
	
	/**提现银行 数据类型varchar(32)*/
	private String withdrawBank;
	
	/**提现银行卡号 数据类型varchar(32)*/
	private String withdrawBankAccount;
	
	/**提现前金额 数据类型double(8,2)*/
	private Double oldSurplusAmount;
	
	/**打款日期 数据类型datetime*/
	private Date payedTime;
	
	/**打款人 数据类型varchar(32)*/
	private String paymentor;
	
	/**创建者 数据类型varchar(32)*/
	private String createUser;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**更新者 数据类型varchar(32)*/
	private String updateUser;
	
	/**更新时间 数据类型datetime*/
	private Date updateTime;
	
	@Virtual
	private String withdraworName;
	@Virtual
	private String remark;
	@Virtual
	private Integer oldWithdrawStatus;
	/**支付宝帐户*/
	@Virtual
	private String alipayAccount;
	/**现有余额*/
	@Virtual
	private Double surplusAmount;
	@Virtual
	private List<Long> withdrawDetailIdList = new ArrayList<Long>();
	
	public String getWithdrawStatusCn(){
		return DssConstant.WITHDRAW_STATUS.getCnName(withdrawStatus);
	}
	public Long getWithdrawDetailId(){
		return withdrawDetailId;
	}
	public Long getWithdrawDetailCode(){
		return withdrawDetailCode;
	}
	public Date getWithdrawTime(){
		return withdrawTime;
	}
	public Double getWithdrawAmount(){
		return withdrawAmount;
	}
	public Integer getWithdrawStatus(){
		return withdrawStatus;
	}
	public Long getWithdrawor(){
		return withdrawor;
	}
	public String getWithdrawBank(){
		return withdrawBank;
	}
	public String getWithdrawBankAccount(){
		return withdrawBankAccount;
	}
	public Double getOldSurplusAmount(){
		return oldSurplusAmount;
	}
	public Date getPayedTime(){
		return payedTime;
	}
	public String getPaymentor(){
		return paymentor;
	}
	public String getCreateUser(){
		return createUser;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public String getUpdateUser(){
		return updateUser;
	}
	public Date getUpdateTime(){
		return updateTime;
	}
	public void setWithdrawDetailId(Long withdrawDetailId){
		this.withdrawDetailId=withdrawDetailId;
	}
	public void setWithdrawDetailCode(Long withdrawDetailCode){
		this.withdrawDetailCode=withdrawDetailCode;
	}
	public void setWithdrawTime(Date withdrawTime){
		this.withdrawTime=withdrawTime;
	}
	public void setWithdrawAmount(Double withdrawAmount){
		this.withdrawAmount=withdrawAmount;
	}
	public void setWithdrawStatus(Integer withdrawStatus){
		this.withdrawStatus=withdrawStatus;
	}
	public void setWithdrawor(Long withdrawor){
		this.withdrawor=withdrawor;
	}
	public void setWithdrawBank(String withdrawBank){
		this.withdrawBank=withdrawBank;
	}
	public void setWithdrawBankAccount(String withdrawBankAccount){
		this.withdrawBankAccount=withdrawBankAccount;
	}
	public void setOldSurplusAmount(Double oldSurplusAmount){
		this.oldSurplusAmount=oldSurplusAmount;
	}
	public void setPayedTime(Date payedTime){
		this.payedTime=payedTime;
	}
	public void setPaymentor(String paymentor){
		this.paymentor=paymentor;
	}
	public void setCreateUser(String createUser){
		this.createUser=createUser;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setUpdateUser(String updateUser){
		this.updateUser=updateUser;
	}
	public void setUpdateTime(Date updateTime){
		this.updateTime=updateTime;
	}
	public String getWithdraworName() {
		return withdraworName;
	}
	public void setWithdraworName(String withdraworName) {
		this.withdraworName = withdraworName;
	}
	public Integer getUserType() {
		return userType;
	}
	public void setUserType(Integer userType) {
		this.userType = userType;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getOldWithdrawStatus() {
		return oldWithdrawStatus;
	}
	public void setOldWithdrawStatus(Integer oldWithdrawStatus) {
		this.oldWithdrawStatus = oldWithdrawStatus;
	}
	public String getAlipayAccount() {
		return alipayAccount;
	}
	public void setAlipayAccount(String alipayAccount) {
		this.alipayAccount = alipayAccount;
	}
	public Double getSurplusAmount() {
		return surplusAmount;
	}
	public void setSurplusAmount(Double surplusAmount) {
		this.surplusAmount = surplusAmount;
	}
	public List<Long> getWithdrawDetailIdList() {
		return withdrawDetailIdList;
	}
	public void setWithdrawDetailIdList(List<Long> withdrawDetailIdList) {
		this.withdrawDetailIdList = withdrawDetailIdList;
	}
}
