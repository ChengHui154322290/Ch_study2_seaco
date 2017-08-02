package com.tp.dto.ord.remote;

import java.io.Serializable;
import java.util.List;

import com.tp.model.ord.RejectInfo;
import com.tp.model.ord.RejectItem;
import com.tp.model.ord.RejectLog;

public class RejectDetailDTO implements Serializable{

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	private static final long serialVersionUID = -3892457043084759433L;
	
	private RejectInfo rejectInfo;
	
	private RejectItem rejectItem;
	
	private List<RejectDetailDTO> detailList;
	
	private List<RejectLog> rejectLogList;

	public RejectDetailDTO() {
	}

	public RejectDetailDTO(RejectInfo rejectInfo,
			RejectItem rejectItem) {
		this.rejectInfo = rejectInfo;
		this.rejectItem = rejectItem;
	}
	
	public RejectDetailDTO(RejectInfo rejectInfo,
			RejectItem rejectItem, List<RejectDetailDTO> detailList) {
		this.rejectInfo = rejectInfo;
		this.rejectItem = rejectItem;
		this.detailList = detailList;
	}

	public RejectDetailDTO(RejectInfo rejectInfo,
			RejectItem rejectItem, List<RejectDetailDTO> detailList,
			List<RejectLog> rejectLogList) {
		super();
		this.rejectInfo = rejectInfo;
		this.rejectItem = rejectItem;
		this.detailList = detailList;
		this.rejectLogList = rejectLogList;
	}

	public RejectInfo getRejectInfo() {
		return rejectInfo;
	}

	public void setRejectInfo(RejectInfo rejectInfo) {
		this.rejectInfo = rejectInfo;
	}

	public RejectItem getRejectItem() {
		return rejectItem;
	}

	public void setRejectItem(RejectItem rejectItem) {
		this.rejectItem = rejectItem;
	}

	public List<RejectDetailDTO> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<RejectDetailDTO> detailList) {
		this.detailList = detailList;
	}

	public List<RejectLog> getRejectLogList() {
		return rejectLogList;
	}

	public void setRejectLogList(List<RejectLog> rejectLogList) {
		this.rejectLogList = rejectLogList;
	}

	
}
