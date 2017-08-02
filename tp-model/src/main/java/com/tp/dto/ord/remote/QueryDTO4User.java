package com.tp.dto.ord.remote;

import java.util.List;

import com.tp.model.BaseDO;

/**
 * 订单查询DTO（用户）
 * 
 * @author szy
 * @version 0.0.1
 */
public class QueryDTO4User extends BaseDO {

	private static final long serialVersionUID = -2944227507275227869L;
	
	/** 用户ID */
	private Long memberId;
	/** 订单状态 */
	private Integer status;
	/** 订单状态 */
	private List<Integer> statusList;

	public Long getMemberId() {
		return memberId;
	}
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public List<Integer> getStatusList() {
		return statusList;
	}
	public void setStatusList(List<Integer> statusList) {
		this.statusList = statusList;
	}
}
