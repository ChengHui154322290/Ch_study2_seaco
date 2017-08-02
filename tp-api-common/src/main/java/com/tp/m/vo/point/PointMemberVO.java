package com.tp.m.vo.point;

import java.io.Serializable;

import com.tp.m.base.Page;

public class PointMemberVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4832340226830882935L;

	/**可使用积分数 */
	private String count;
	
	private Page<PointDetailVO> pointDetailPage = new Page<PointDetailVO>();

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public Page<PointDetailVO> getPointDetailPage() {
		return pointDetailPage;
	}

	public void setPointDetailPage(Page<PointDetailVO> pointDetailPage) {
		this.pointDetailPage = pointDetailPage;
	}

}
