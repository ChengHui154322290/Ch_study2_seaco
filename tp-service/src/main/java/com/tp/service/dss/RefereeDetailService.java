package com.tp.service.dss;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.DssConstant;
import com.tp.common.vo.Constant.DOCUMENT_TYPE;
import com.tp.dao.dss.RefereeDetailDao;
import com.tp.model.dss.AccountDetail;
import com.tp.model.dss.RefereeDetail;
import com.tp.service.BaseService;
import com.tp.service.IDocumentNumberGenerator;
import com.tp.service.dss.IAccountDetailService;
import com.tp.service.dss.IRefereeDetailService;

@Service
public class RefereeDetailService extends BaseService<RefereeDetail> implements IRefereeDetailService {

	@Autowired
	private RefereeDetailDao refereeDetailDao;
	@Autowired
	private IAccountDetailService accountDetailService;
	@Autowired
	private IDocumentNumberGenerator documentNumberGenerator;
	
	@Override
	public BaseDao<RefereeDetail> getDao() {
		return refereeDetailDao;
	}

	@Override
	public RefereeDetail insert(RefereeDetail refereeDetail){
		refereeDetail.setRefereeDetailCode(documentNumberGenerator.generate(DOCUMENT_TYPE.REFERRAL_FEES));
		AccountDetail accountDetail = new AccountDetail();
		accountDetail.setAmount(refereeDetail.getCommision());
		accountDetail.setBussinessCode(refereeDetail.getRefereeDetailCode());
		accountDetail.setCreateUser(refereeDetail.getCreateUser());
		accountDetail.setUserId(refereeDetail.getPromoterId());
		accountDetail.setUserType(DssConstant.PROMOTER_TYPE.DISTRIBUTE.code);
		accountDetailService.insertByReferralFees(accountDetail);
		return super.insert(refereeDetail);
	}
	
	
	@Override
	public List<RefereeDetail> queryRefereeByDetailCode( List<Long> detaiCodeList){
		return refereeDetailDao.queryRefereeByDetailCode(detaiCodeList);
	}	

	
}
