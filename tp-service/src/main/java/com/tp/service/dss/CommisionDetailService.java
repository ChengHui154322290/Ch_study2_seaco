package com.tp.service.dss;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.DssConstant;
import com.tp.common.vo.Constant.AUTHOR_TYPE;
import com.tp.dao.dss.CommisionDetailDao;
import com.tp.model.dss.AccountDetail;
import com.tp.model.dss.CommisionDetail;
import com.tp.service.BaseService;
import com.tp.service.dss.IAccountDetailService;
import com.tp.service.dss.ICommisionDetailService;

@Service
public class CommisionDetailService extends BaseService<CommisionDetail> implements ICommisionDetailService {

	@Autowired
	private CommisionDetailDao commisionDetailDao;
	@Autowired
	private IAccountDetailService accountDetailService;
	
	@Override
	public BaseDao<CommisionDetail> getDao() {
		return commisionDetailDao;
	}

	@Override
	public void insertByOrder(List<CommisionDetail> commisionDetailList) {
		commisionDetailDao.batchInsert(commisionDetailList);
	}

	@Override
	public void insertByRefund(List<CommisionDetail> commisionDetailList) {
		commisionDetailDao.batchInsert(commisionDetailList);
	}

	@Override
	public Integer updateReceiveTimeByOrderCode(CommisionDetail commisionDetail) {
		return commisionDetailDao.updateReceiveTimeByOrderCode(commisionDetail);
	}

	/**
	 * 把到期的未汇总的佣金进行汇总
	 * @param commisionDetailList
	 * @return
	 */
	@Override
	public void updateByCollectCommision(List<CommisionDetail> commisionDetailList){
		if(CollectionUtils.isNotEmpty(commisionDetailList)){
			commisionDetailList.forEach(new Consumer<CommisionDetail>(){
				public void accept(CommisionDetail commisionDetail) {
					//插入到流水帐明细中
					AccountDetail accountDetail = new AccountDetail();
					accountDetail.setAmount(commisionDetail.getCommision());
					accountDetail.setBussinessCode(commisionDetail.getBizCode());
					accountDetail.setBussinessType(commisionDetail.getBizType());
					accountDetail.setCreateUser(commisionDetail.getUpdateUser());
					accountDetail.setUserId(commisionDetail.getPromoterId());
					accountDetail.setUserType(commisionDetail.getPromoterType());
					if(DssConstant.BUSSINESS_TYPE.ORDER.code.equals(commisionDetail.getBizType())){
						accountDetailService.insertByOrderCommision(accountDetail);
					}else if(DssConstant.BUSSINESS_TYPE.REFUND.code.equals(commisionDetail.getBizType())){
						accountDetailService.insertByRefund(accountDetail);
					}
					//修改汇总状态
					Long commisionDetailId = commisionDetail.getCommisionDetailId();
					commisionDetail = new CommisionDetail();
					commisionDetail.setCommisionDetailId(commisionDetailId);
					commisionDetail.setCollectStatus(DssConstant.COLLECT_STATUS.YES.code);
					commisionDetail.setUpdateUser(AUTHOR_TYPE.JOB);
					commisionDetailDao.updateNotNullById(commisionDetail);
				}
			});
		}
	}

	@Override
	public Map<String, Number> queryStatisticByOrderPromoterId(Map<String,Object> params) {
		return commisionDetailDao.queryStatisticByOrderPromoterId(params);
	}
	
	
	@Override
	public Map<String, Number> queryStatisticByOrderPromoterId_In(Map<String,Object> params) {
		return commisionDetailDao.queryStatisticByOrderPromoterId_In(params);
	}

	@Override
	public Map<String, Number> queryStatisticByOrderPromoterId_Out(Map<String,Object> params) {
		return commisionDetailDao.queryStatisticByOrderPromoterId_Out(params);
	}

	@Override
	public List<CommisionDetail> queryStatisticByBizAmountSum(List<Long> idChildren,Integer indirect) {
		return commisionDetailDao.queryStatisticByBizAmountSum(idChildren,indirect);
	}
	
	@Override
	public List<CommisionDetail> queryStatisticByBizAmountCommisionSum(List<Long> idChildren,Integer indirect,Long beneficiary) {
		return commisionDetailDao.queryStatisticByBizAmountCommisionSum(idChildren,indirect,beneficiary);
	}

	@Override
	public List<Long> queryDistinctOrderCode(CommisionDetail commisionDetail){
		return commisionDetailDao.queryDistinctOrderCode(commisionDetail);
	}

	@Override
	public List<Long> queryAllCildPromotersDistinctOrderCode(CommisionDetail commisionDetail) {
		// TODO Auto-generated method stub
		return commisionDetailDao.queryAllCildPromotersDistinctOrderCode(commisionDetail);
	}
	
}
