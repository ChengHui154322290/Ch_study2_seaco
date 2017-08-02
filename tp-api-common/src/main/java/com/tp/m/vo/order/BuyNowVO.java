package com.tp.m.vo.order;

import com.tp.m.base.BaseVO;

/**
 * 立即购买
 * @author zhuss
 * @2016年1月25日 下午3:13:21
 */
public class BuyNowVO implements BaseVO{

	private static final long serialVersionUID = 6357307005653297856L;

	/**
	 * 缓存立即购买的uuid
	 */
	private String uuid;
	
	private String totalpoint;
	private String usedpoint;
	private String usedpointsign;
	
	public BuyNowVO() {
		super();
	}

	public BuyNowVO(String uuid) {
		super();
		this.uuid = uuid;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getTotalpoint() {
		return totalpoint;
	}

	public void setTotalpoint(String totalpoint) {
		this.totalpoint = totalpoint;
	}

	public String getUsedpoint() {
		return usedpoint;
	}

	public void setUsedpoint(String usedpoint) {
		this.usedpoint = usedpoint;
	}

	public String getUsedpointsign() {
		return usedpointsign;
	}

	public void setUsedpointsign(String usedpointsign) {
		this.usedpointsign = usedpointsign;
	}
}
