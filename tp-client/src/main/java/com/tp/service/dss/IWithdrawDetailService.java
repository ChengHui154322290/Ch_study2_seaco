package com.tp.service.dss;

import java.util.List;

import com.tp.model.dss.WithdrawDetail;
import com.tp.service.IBaseService;
/**
  * @author szy 
  * 提现明细表接口
  */
public interface IWithdrawDetailService extends IBaseService<WithdrawDetail>{
	
	/**
	 * 审核提现
	 * @param withdrawDetail
	 * @return
	 */
	Integer updateByAudit(WithdrawDetail withdrawDetail);
	
	WithdrawDetail insert(WithdrawDetail withdrawDetail, Integer paymode);
	
	Integer updateByBatchAudit(List<WithdrawDetail> withdrawDetailList);

}
