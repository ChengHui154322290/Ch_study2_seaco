package com.tp.service.dss;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.DssConstant;
import com.tp.common.vo.Constant.DOCUMENT_TYPE;
import com.tp.common.vo.DssConstant.BUSSINESS_TYPE;
import com.tp.common.vo.DssConstant.WITHDRAW_STATUS;
import com.tp.dao.dss.WithdrawDetailDao;
import com.tp.model.dss.AccountDetail;
import com.tp.model.dss.PromoterInfo;
import com.tp.model.dss.WithdrawDetail;
import com.tp.model.dss.WithdrawLog;
import com.tp.model.mem.MemberInfo;
import com.tp.redis.util.JedisDBUtil;
import com.tp.service.BaseService;
import com.tp.service.IDocumentNumberGenerator;
import com.tp.service.dss.IAccountDetailService;
import com.tp.service.dss.IPromoterInfoService;
import com.tp.service.dss.IWithdrawDetailService;
import com.tp.service.dss.IWithdrawLogService;
import com.tp.service.mem.IMemberInfoService;

@Service
public class WithdrawDetailService extends BaseService<WithdrawDetail> implements IWithdrawDetailService {

	@Autowired
	private WithdrawDetailDao withdrawDetailDao;
	@Autowired
	private IWithdrawLogService withdrawLogService;
	@Autowired
	private IAccountDetailService accountDetailService;
	@Autowired
	private IPromoterInfoService promoterInfoService;
	@Autowired
	private IMemberInfoService memberInfoService;
	@Autowired
	private IDocumentNumberGenerator documentNumberGenerator;
	@Autowired
	private JedisDBUtil jedisDBUtil;
	
	@Override
	public BaseDao<WithdrawDetail> getDao() {
		return withdrawDetailDao;
	}
	
	/**
	 * 提现申请
	 */
	@Override
	public WithdrawDetail insert(WithdrawDetail withdrawDetail, Integer paymode){
		if(!jedisDBUtil.lock("withdrawDetail:"+withdrawDetail.getWithdrawor())){
			Assert.isTrue(Boolean.FALSE,"正在进行提现，不能重复操作");
		}
		PromoterInfo promoterInfo = promoterInfoService.queryById(withdrawDetail.getWithdrawor());
		Assert.notNull(promoterInfo);
		withdrawDetail.setWithdrawDetailCode(documentNumberGenerator.generate(DOCUMENT_TYPE.WITHDRAW));
		withdrawDetail.setCreateUser(promoterInfo.getPromoterName());
		withdrawDetail.setUpdateUser(withdrawDetail.getCreateUser());
		withdrawDetail.setUserType(promoterInfo.getPromoterType());
		withdrawDetail.setOldSurplusAmount(promoterInfo.getSurplusAmount());
		if(withdrawDetail.getWithdrawAmount()==null || withdrawDetail.getWithdrawAmount()>promoterInfo.getSurplusAmount()){
			withdrawDetail.setWithdrawAmount(promoterInfo.getSurplusAmount());
		}
		withdrawDetail.setWithdrawStatus(WITHDRAW_STATUS.APPLY.code);
		withdrawDetail.setWithdrawor(promoterInfo.getPromoterId());
		if(DssConstant.PAYMENT_MODE.POINT.code.equals(paymode)){
			MemberInfo memberInfo = memberInfoService.queryById(promoterInfo.getMemberId());
			withdrawDetail.setWithdrawBank(DssConstant.PAYMENT_MODE.POINT.getCnName());
			String account = StringUtils.isBlank(memberInfo.getMobile()) ? memberInfo.getEmail()
					: memberInfo.getMobile();
			withdrawDetail.setWithdrawBankAccount( account );			
		}else if(DssConstant.PAYMENT_MODE.ALIPAY.code.equals(paymode)){
			withdrawDetail.setWithdrawBank(DssConstant.PAYMENT_MODE.ALIPAY.getCnName());
			withdrawDetail.setWithdrawBankAccount( promoterInfo.getAlipay() );			
		}else{
			withdrawDetail.setWithdrawBank(promoterInfo.getBankName());
			withdrawDetail.setWithdrawBankAccount(promoterInfo.getBankAccount());			
		}
		withdrawDetail.setWithdrawTime(new Date());
		withdrawDetailDao.insert(withdrawDetail);
		WithdrawLog withdrawLog = new WithdrawLog();
		withdrawLog.setCreateUser(withdrawDetail.getCreateUser());
		withdrawLog.setOldStatus(withdrawDetail.getWithdrawStatus());
		withdrawLog.setCurrentStatus(withdrawDetail.getWithdrawStatus());
		withdrawLog.setRemark("用户申请提现");
		withdrawLog.setActiveType(DssConstant.WITHDRAW_LOG_TYPE.getType(withdrawLog.getCurrentStatus()).getCode());
		withdrawLog.setWithdrawDetailId(withdrawDetail.getWithdrawDetailId());
		withdrawLog.setWithdrawDetailCode(withdrawDetail.getWithdrawDetailCode());
		withdrawLogService.insert(withdrawLog);	
		jedisDBUtil.unLock("withdrawDetail:"+withdrawDetail.getWithdrawor());
		return withdrawDetail;
	}

	@Override
	public Integer updateByAudit(WithdrawDetail withdrawDetail) {
		WithdrawDetail withdrawDetailOld = withdrawDetailDao.queryById(withdrawDetail.getWithdrawDetailId());
		Integer withdrawStatus = withdrawDetailOld.getWithdrawStatus();
		if(WITHDRAW_STATUS.UNPASS.code.equals(withdrawStatus) || WITHDRAW_STATUS.PAYED.code.equals(withdrawStatus) || WITHDRAW_STATUS.UNPAY.code.equals(withdrawStatus)){
			return 0;
		}
		withdrawDetail.setWithdrawTime(new Date());
		WithdrawLog withdrawLog = new WithdrawLog();
		withdrawLog.setCreateUser(withdrawDetail.getUpdateUser());
		withdrawLog.setOldStatus(withdrawDetailOld.getWithdrawStatus());
		withdrawLog.setCurrentStatus(withdrawDetail.getWithdrawStatus());
		if(StringUtils.isBlank(withdrawDetail.getRemark())){
			withdrawDetail.setRemark(withdrawDetail.getWithdrawStatusCn());
		}
		withdrawLog.setRemark(withdrawDetail.getRemark());
		withdrawLog.setActiveType(DssConstant.WITHDRAW_LOG_TYPE.getType(withdrawLog.getCurrentStatus()).getCode());
		withdrawLog.setWithdrawDetailId(withdrawDetailOld.getWithdrawDetailId());
		withdrawLog.setWithdrawDetailCode(withdrawDetailOld.getWithdrawDetailCode());
		withdrawLogService.insert(withdrawLog);
		Integer result =  withdrawDetailDao.updateNotNullById(withdrawDetail);
		if(DssConstant.WITHDRAW_STATUS.PAYED.code.equals(withdrawDetail.getWithdrawStatus())){
			AccountDetail accountDetail = new AccountDetail();
			accountDetail.setAmount(withdrawDetailOld.getWithdrawAmount());
			accountDetail.setBussinessCode(withdrawDetailOld.getWithdrawDetailCode());
			accountDetail.setBussinessType(BUSSINESS_TYPE.WITHDRAW.code);
			accountDetail.setCreateUser(withdrawDetailOld.getUpdateUser());
			accountDetail.setUserId(withdrawDetailOld.getWithdrawor());
			accountDetail.setUserType(withdrawDetailOld.getUserType());
			accountDetailService.insertByWithdraw(accountDetail);
		}
		return result;
	}
	
	public Integer updateByBatchAudit(List<WithdrawDetail> withdrawDetailList) {
		Integer result =0;
		for(WithdrawDetail withdrawDetail:withdrawDetailList){
			withdrawDetail.setWithdrawTime(new Date());
			WithdrawLog withdrawLog = new WithdrawLog();
			withdrawLog.setCreateUser(withdrawDetail.getUpdateUser());
			withdrawLog.setOldStatus(withdrawDetail.getOldWithdrawStatus());
			withdrawLog.setCurrentStatus(withdrawDetail.getWithdrawStatus());
			if(StringUtils.isBlank(withdrawDetail.getRemark())){
				withdrawDetail.setRemark(withdrawDetail.getWithdrawStatusCn());
			}
			withdrawLog.setRemark(withdrawDetail.getRemark());
			withdrawLog.setActiveType(DssConstant.WITHDRAW_LOG_TYPE.getType(withdrawLog.getCurrentStatus()).getCode());
			withdrawLog.setWithdrawDetailId(withdrawDetail.getWithdrawDetailId());
			withdrawLog.setWithdrawDetailCode(withdrawDetail.getWithdrawDetailCode());
			withdrawLogService.insert(withdrawLog);
			result =+  withdrawDetailDao.updateNotNullById(withdrawDetail);
			if(DssConstant.WITHDRAW_STATUS.PAYED.code.equals(withdrawDetail.getWithdrawStatus())){
				AccountDetail accountDetail = new AccountDetail();
				accountDetail.setAmount(withdrawDetail.getWithdrawAmount());
				accountDetail.setBussinessCode(withdrawDetail.getWithdrawDetailCode());
				accountDetail.setBussinessType(BUSSINESS_TYPE.WITHDRAW.code);
				accountDetail.setCreateUser(withdrawDetail.getUpdateUser());
				accountDetail.setUserId(withdrawDetail.getWithdrawor());
				accountDetail.setUserType(withdrawDetail.getUserType());
				accountDetailService.insertByWithdraw(accountDetail);
			}
		}
		return result;
	}
}
