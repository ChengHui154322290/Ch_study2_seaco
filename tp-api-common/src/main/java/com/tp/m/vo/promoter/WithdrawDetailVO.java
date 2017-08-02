package com.tp.m.vo.promoter;

import java.util.Date;

import com.tp.m.base.BaseVO;

public class WithdrawDetailVO implements BaseVO{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5199956948715424120L;
	
	private Integer withdrawStatus;				// 提现状态
	private String withdrawBank;					// 提现银行名称
	private String withdrawBankAccount;		// 提现银行账号
	private Double withdrawAmount;			// 提现金额
	private String withdrawTime;						// 提现时间
	private Integer withdrawType;					// 提现类型
	
	
	public Integer getWithdrawType() {
		return withdrawType;
	}
	public void setWithdrawType(Integer withdrawType) {
		this.withdrawType = withdrawType;
	}
	public Integer getWithdrawStatus() {
		return withdrawStatus;
	}
	public void setWithdrawStatus(Integer withdrawStatus) {
		this.withdrawStatus = withdrawStatus;
	}
	public String getWithdrawBank() {
		return withdrawBank;
	}
	public void setWithdrawBank(String withdrawBank) {
		this.withdrawBank = withdrawBank;
	}
	public String getWithdrawBankAccount() {
		return withdrawBankAccount;
	}
	public void setWithdrawBankAccount(String withdrawBankAccount) {
		this.withdrawBankAccount = withdrawBankAccount;
	}
	public Double getWithdrawAmount() {
		return withdrawAmount;
	}
	public void setWithdrawAmount(Double withdrawAmount) {
		this.withdrawAmount = withdrawAmount;
	}
	public String getWithdrawTime() {
		return withdrawTime;
	}
	public void setWithdrawTime(String withdrawTime) {
		this.withdrawTime = withdrawTime;
	}
		
}
