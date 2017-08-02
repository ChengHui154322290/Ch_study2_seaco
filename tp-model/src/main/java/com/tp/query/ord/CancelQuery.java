package com.tp.query.ord;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.tp.model.BaseDO;

public class CancelQuery extends BaseDO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5021379859458122539L;
	
	private List<String> refundNoList = new ArrayList<String>();
	
	private List<String> cancelNoList = new ArrayList<String>();

	public List<String> getRefundNoList() {
		return refundNoList;
	}

	public void setRefundNoList(List<String> refundNoList) {
		this.refundNoList = refundNoList;
	}

	public List<String> getCancelNoList() {
		return cancelNoList;
	}

	public void setCancelNoList(List<String> cancelNoList) {
		this.cancelNoList = cancelNoList;
	}
}
