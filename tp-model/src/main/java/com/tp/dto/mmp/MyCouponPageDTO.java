package com.tp.dto.mmp;

import java.io.Serializable;

import com.tp.common.vo.PageInfo;

public class MyCouponPageDTO  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5653163107098806936L;
	
	PageInfo<MyCouponDTO> page;
	
	Integer normalCount;
	
	Integer usedCount;
	
	Integer overdueCount;

	public PageInfo<MyCouponDTO> getPage() {
		return page;
	}

	public void setPage(PageInfo<MyCouponDTO> page) {
		this.page = page;
	}

	public Integer getNormalCount() {
		return normalCount;
	}

	public void setNormalCount(Integer normalCount) {
		this.normalCount = normalCount;
	}

	public Integer getUsedCount() {
		return usedCount;
	}

	public void setUsedCount(Integer usedCount) {
		this.usedCount = usedCount;
	}

	public Integer getOverdueCount() {
		return overdueCount;
	}

	public void setOverdueCount(Integer overdueCount) {
		this.overdueCount = overdueCount;
	}



}
