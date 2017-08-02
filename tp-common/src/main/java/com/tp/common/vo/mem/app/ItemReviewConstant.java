package com.tp.common.vo.mem.app;

public interface ItemReviewConstant {

	/** 是否审核通过 */
	public interface ISCHECK {
		boolean TRUE = true; /** 审核通过 */
		boolean FALSE = false; /** 审核未通过 */
	}
	
	/** 是否删除 */
	public interface ISDELETE {
		boolean TRUE = true;/** 删除 */
		boolean FALSE = false;/** 未删除 */
	}
	
	/** 是否匿名 */
	public interface ISANONYMOUS {
		boolean TRUE = true; /** 匿名 */
		boolean FALSE = false;/** 非匿名 */
	}
	
	/** 状态 */
	public interface STATUS {
		Integer ZERO = 0;
		Integer ONE = 1;
	}
	
}
