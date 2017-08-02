package com.tp.model.dss;

import java.io.Serializable;
import java.util.Date;

import com.tp.model.BaseDO;
import com.tp.util.DateUtil;

public class WithdrawDetailResponse extends BaseDO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**提现日期 数据类型datetime*/
	private String withdrawTime;
			
	/**提现金额 数据类型double(8,2)*/
	private String withdrawAmount;
	
	/**提现状态(1:申请，2：审核中，3：审核通过，4：审核未通过，5：财务打款成功，6：财务打款失败) 数据类型tinyint(4)*/
	private String withdrawStatus;
	
	/**备注*/
	private String remark;

	public String getWithdrawTime() {
		return withdrawTime;
	}

	public void setWithdrawTime(String withdrawTime) {
		this.withdrawTime = withdrawTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getWithdrawAmount() {
		return withdrawAmount;
	}

	public void setWithdrawAmount(String withdrawAmount) {
		this.withdrawAmount = withdrawAmount;
	}

	public String getWithdrawStatus() {
		return withdrawStatus;
	}

	public void setWithdrawStatus(String withdrawStatus) {
		this.withdrawStatus = withdrawStatus;
	}
	
	
}
