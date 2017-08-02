package com.tp.service.dss;

import com.tp.model.dss.AccountDetail;
import com.tp.service.IBaseService;
/**
  * @author szy 
  * 账户流水表接口
  */
public interface IAccountDetailService extends IBaseService<AccountDetail>{
	/**
	 * 订单提佣
	 * @param accountDetail
	 */
	public void insertByOrderCommision(AccountDetail accountDetail);
	/**
	 * 拉新提佣
	 * @param accountDetail
	 */
	public void insertByReferralFees(AccountDetail accountDetail);
	/**
	 * 退款返还佣金
	 * @param accountDetail
	 */
	public void insertByRefund(AccountDetail accountDetail);
	/**
	 * 提取佣金
	 * @param accountDetail
	 */
	public void insertByWithdraw(AccountDetail accountDetail);
	

	/**
	 * 获取已提现金额
	 * @param accountDetail
	 */
	public Double GetWithdrawedfees(AccountDetail accountDetail);

}
