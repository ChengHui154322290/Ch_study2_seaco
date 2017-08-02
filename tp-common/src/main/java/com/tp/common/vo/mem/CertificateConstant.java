package com.tp.common.vo.mem;

public interface CertificateConstant {
	
	public interface Type {
		/** 身份证 **/
		int ID_CARD = 0;
	}
	
	public interface VerifyStatus {
		/** 未提交 **/
		int UNCOMMITTED = 0;
		/** 审核中 **/
		int BEING_REVIEWED = 1;
		/** 审核未通过 **/
		int UNAPPROVE = 2;
		/** 审核通过 **/
		int APPROVE = 3;
	}
	
}
