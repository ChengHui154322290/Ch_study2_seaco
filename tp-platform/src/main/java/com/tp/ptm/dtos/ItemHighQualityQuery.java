package com.tp.ptm.dtos;

import java.io.Serializable;

public class ItemHighQualityQuery implements Serializable {

	private static final long serialVersionUID = 1634989252541139554L;
	
	/** 默认第一页 */
	public static final Integer DEFAULT_PAGE_NO = 1;
	/**  默认每页最大记录数 */
	public static final Integer DEFAULT_PAGE_SIZE = 200;

	private Integer pageNo = DEFAULT_PAGE_NO;

	private Integer pageSize = DEFAULT_PAGE_SIZE;

	public final Integer getPageNo() {
		return pageNo;
	}

	public final void setPageNo(Integer pageNo) {
		if(null == pageNo){
			this.pageNo = DEFAULT_PAGE_NO;
		}
		if (pageNo <= 0) {
			this.pageNo = DEFAULT_PAGE_NO;
		} else {
			this.pageNo = pageNo;
		}
	}

	public final Integer getPageSize() {
		return pageSize;
	}

	public final void setPageSize(Integer pageSize) {
		if (pageSize > DEFAULT_PAGE_SIZE) {
			this.pageSize = DEFAULT_PAGE_SIZE;
		} else {
			this.pageSize = pageSize;
		}
	}

}
