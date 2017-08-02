package com.tp.dto.ord;

import java.util.ArrayList;
import java.util.List;

import com.tp.model.ord.RejectInfo;
import com.tp.model.ord.RejectLog;


public class RejectInfoDTO extends RejectInfo implements BaseDTO{

	private static final long serialVersionUID = -1141427714848837164L;

	private List<RejectLog> rejectLogList = new ArrayList<RejectLog>();

	public List<RejectLog> getRejectLogList() {
		return rejectLogList;
	}

	public void setRejectLogList(List<RejectLog> rejectLogList) {
		this.rejectLogList = rejectLogList;
	} 
	
}
