package com.tp.service.dss;

import static com.tp.util.BigDecimalUtil.add;
import static com.tp.util.BigDecimalUtil.formatToPrice;
import static com.tp.util.BigDecimalUtil.subtract;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.Constant.AUTHOR_TYPE;
import com.tp.common.vo.Constant.SPLIT_SIGN;
import com.tp.common.vo.DssConstant;
import com.tp.common.vo.DssConstant.ACCOUNT_TYPE;
import com.tp.common.vo.DssConstant.BUSSINESS_TYPE;
import com.tp.common.vo.DssConstant.PAYMENT_MODE;
import com.tp.common.vo.DssConstant.PROMOTER_TYPE;
import com.tp.common.vo.mmp.PointConstant;
import com.tp.dao.dss.AccountDetailDao;
import com.tp.dao.dss.WithdrawDetailDao;
import com.tp.model.dss.AccountDetail;
import com.tp.model.dss.PromoterInfo;
import com.tp.model.dss.WithdrawDetail;
import com.tp.model.mmp.PointDetail;
import com.tp.redis.util.JedisDBUtil;
import com.tp.service.BaseService;
import com.tp.service.mmp.IPointDetailService;
import com.tp.util.ThreadUtil;

@Service
public class AccountDetailService extends BaseService<AccountDetail> implements IAccountDetailService {

	@Autowired
	private AccountDetailDao accountDetailDao;
	@Autowired
	private WithdrawDetailDao withdrawDetailDao;
	@Autowired
	private IPointDetailService pointDetailService;
	@Autowired
	private IPromoterInfoService promoterInfoService;
	@Autowired
	private JedisDBUtil jedisDBUtil;
	
	@Override
	public BaseDao<AccountDetail> getDao() {
		return accountDetailDao;
	}
	
	/**
	 * 订单提佣
	 * @param accountDetail
	 */
	public void insertByOrderCommision(AccountDetail accountDetail){
		accountDetail.setAccountType(ACCOUNT_TYPE.INCOMING.code);
		accountDetail.setAmount(Math.abs(accountDetail.getAmount()));
		accountDetail.setBussinessType(BUSSINESS_TYPE.ORDER.code);
		accountDetail.setAccountTime(new Date());
		accountDetail.setCreateUser(AUTHOR_TYPE.SYSTEM);
		insert(accountDetail);
	}
	
	/**
	 * 拉新佣金
	 * @param accountDetail
	 */
	public void insertByReferralFees(AccountDetail accountDetail){
		accountDetail.setUserType(DssConstant.PROMOTER_TYPE.DISTRIBUTE.code);
		accountDetail.setAccountType(ACCOUNT_TYPE.INCOMING.code);
		accountDetail.setAmount(Math.abs(accountDetail.getAmount()));
		accountDetail.setBussinessType(BUSSINESS_TYPE.REFERRAL_FEES.code);
		accountDetail.setAccountTime(new Date());
		accountDetail.setCreateUser(AUTHOR_TYPE.SYSTEM);
		insert(accountDetail);
	}
	
	/**
	 * 退款返还佣金
	 * @param accountDetail
	 */
	public void insertByRefund(AccountDetail accountDetail){
		accountDetail.setAccountType(ACCOUNT_TYPE.OUTCOMING.code);
		accountDetail.setAmount(formatToPrice(subtract(BigDecimal.ZERO,Math.abs(accountDetail.getAmount()))).doubleValue());
		accountDetail.setBussinessType(BUSSINESS_TYPE.REFUND.code);
		accountDetail.setAccountTime(new Date());
		accountDetail.setCreateUser(AUTHOR_TYPE.SYSTEM);
		insert(accountDetail);
	}
	
	/**
	 * 提佣金
	 * @param accountDetail
	 */
	public void insertByWithdraw(AccountDetail accountDetail){
		accountDetail.setAccountType(ACCOUNT_TYPE.OUTCOMING.code);
		accountDetail.setAmount(formatToPrice(subtract(BigDecimal.ZERO,Math.abs(accountDetail.getAmount()))).doubleValue());
		accountDetail.setBussinessType(BUSSINESS_TYPE.WITHDRAW.code);
		accountDetail.setAccountTime(new Date());
		insert(accountDetail);
	}
		
	/**
	 * 已提现金额
	 * @param accountDetail
	 */
	public Double GetWithdrawedfees(AccountDetail accountDetail){
		return accountDetailDao.getWithdrawedfees(accountDetail);
	}

	@Override
	public AccountDetail insert(AccountDetail accountDetail){
		String key = "accountdetail"+SPLIT_SIGN.COLON+accountDetail.getUserType()+accountDetail.getUserId();
		jedisDBUtil.lpush(key, accountDetail);
		insertAccountDetail(key);
		return accountDetail;
	}
	
	private void insertAccountDetail(final String key){
		ThreadUtil.excAsync(new AccountDetailRunnable(key),false);
	}
	
	class AccountDetailRunnable implements Runnable{
		private String key;
		public AccountDetailRunnable(String key){
			this.key = key;
		}
		@Override
		public void run() {
			logger.info("启动。。。。");
			if(jedisDBUtil.lock("lock:"+key)){
				AccountDetail accountDetail = (AccountDetail)jedisDBUtil.rpop(key);
				while(accountDetail!=null){
					Map<String,Object> params = new HashMap<String,Object>();
					params.put("promoterId", accountDetail.getUserId());
					PromoterInfo promoterInfo = promoterInfoService.queryUniqueByParams(params);
					if(promoterInfo!=null){
						accountDetail.setSurplusAmount(formatToPrice(add(promoterInfo.getSurplusAmount(),accountDetail.getAmount())).doubleValue());
						accountDetailDao.insert(accountDetail);
						PromoterInfo promoter = new PromoterInfo();
						promoter.setPromoterId(promoterInfo.getPromoterId());
						promoter.setUpdateUser(AUTHOR_TYPE.SYSTEM);
						promoter.setSurplusAmount(accountDetail.getSurplusAmount());
						if(ACCOUNT_TYPE.INCOMING.code.equals(accountDetail.getAccountType())){
							promoter.setAccumulatedAmount(formatToPrice(add(promoterInfo.getAccumulatedAmount(),Math.abs(accountDetail.getAmount()))).doubleValue());
						}
						if(BUSSINESS_TYPE.REFERRAL_FEES.code.equals(accountDetail.getBussinessType())){
							promoter.setReferralFees(formatToPrice(add(promoterInfo.getReferralFees(),Math.abs(accountDetail.getAmount()))).doubleValue());
						}
						promoterInfoService.updateNotNullById(promoter);
						if (BUSSINESS_TYPE.WITHDRAW.code.equals(accountDetail.getBussinessType())) { // 提现，判断是否提现到积分
							WithdrawDetail withdrawDetail = new WithdrawDetail();
							withdrawDetail.setWithdrawDetailCode(accountDetail.getBussinessCode());
							withdrawDetail.setWithdrawAmount(Math.abs(accountDetail.getAmount()));
							withdrawDetail.setWithdrawor(accountDetail.getUserId());
							withdrawDetail = withdrawDetailDao.queryByObject(withdrawDetail).get(0);
							if (!PAYMENT_MODE.ALIPAY.cnName.equals(withdrawDetail.getWithdrawBank().trim()) 
									&& !PAYMENT_MODE.UNPAY.cnName.equals(withdrawDetail.getWithdrawBank().trim())) { // 只有三种情况，不为支付宝跟银行则为提现到积分
								PointDetail pointDetail = new PointDetail();
								pointDetail.setBizId(withdrawDetail.getWithdrawDetailCode().toString());
								pointDetail.setBizType(PointConstant.BIZ_TYPE.WITHDRAW_POINT.code);
								pointDetail.setCreateUser(accountDetail.getCreateUser());
								pointDetail.setMemberId(promoterInfo.getMemberId());
								BigDecimal point = new BigDecimal(withdrawDetail.getWithdrawAmount()).setScale(2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
								pointDetail.setPoint(point.intValue());
								if (PROMOTER_TYPE.SCANATTENTION.code.equals(promoterInfo.getPromoterType())) {
									pointDetail.setTitle(PointConstant.BIZ_TYPE.WITHDRAW_POINT.title);
								} else {
									pointDetail.setTitle(PointConstant.BIZ_TYPE.WITHDRAW_POINT_SHOP.title);
								}
								pointDetail.setPointType(PointConstant.OPERATE_TYPE.ADD.type);
								pointDetail = pointDetailService.insert(pointDetail);
							}
						}
					}
					accountDetail = (AccountDetail)jedisDBUtil.rpop(key);
				}
			}else{
				try {
					TimeUnit.SECONDS.sleep(10);
					run();
				} catch (InterruptedException e) {
				}
			}
			jedisDBUtil.unLock("lock:"+key);
		}
	}
}
