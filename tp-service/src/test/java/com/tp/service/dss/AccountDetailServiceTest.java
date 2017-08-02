package com.tp.service.dss;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.tp.common.vo.DssConstant;
import com.tp.model.dss.AccountDetail;
import com.tp.service.dss.IAccountDetailService;
import com.tp.test.BaseTest;

public class AccountDetailServiceTest  extends BaseTest{

	@Autowired
	private IAccountDetailService accountDetailService;
	@Test
	public void testInsertByOrderCommision() {
		AccountDetail accountDetail = new AccountDetail();
		accountDetail.setAmount(1d);
		accountDetail.setBussinessCode(1116031409999959L);
		accountDetail.setBussinessType(DssConstant.BUSSINESS_TYPE.ORDER.code);
		accountDetail.setCreateUser("[job]");
		accountDetail.setUserId(29L);
		accountDetail.setUserType(DssConstant.PROMOTER_TYPE.COUPON.code);
		accountDetailService.insertByOrderCommision(accountDetail);
	}

	@Test
	public void testInsertByReferralFees() {
		fail("Not yet implemented");
	}

	@Test
	public void testInsertByRefund() {
		fail("Not yet implemented");
	}

	@Test
	public void testInsertByWithdraw() {
		fail("Not yet implemented");
	}

	@Test
	public void testInsertAccountDetail() {
		fail("Not yet implemented");
	}

}
