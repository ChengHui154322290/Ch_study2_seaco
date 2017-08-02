package com.tp.result.prd;

import java.io.Serializable;
import java.util.List;

import com.tp.model.prd.ItemAuditDetail;

/**
 * 
 * <pre>
 *  审核结果的详细分析类
 * </pre>
 *
 * @author szy	
 * @version 
 */
public class ItemAuditDetailResuslt implements Serializable {
	
	private static final long serialVersionUID = -5790830486548153824L;

	/** 审计结果的详细信息 */
	private ItemAuditDetail auditDetail;
	
	/** 拒绝理由的 key */
	private List<Integer> rejectTypeList;

	public ItemAuditDetail getAuditDetail() {
		return auditDetail;
	}

	public void setAuditDetail(ItemAuditDetail auditDetail) {
		this.auditDetail = auditDetail;
	}

	public List<Integer> getRejectTypeList() {
		return rejectTypeList;
	}

	public void setRejectTypeList(List<Integer> rejectTypeList) {
		this.rejectTypeList = rejectTypeList;
	} 
	
	
}
