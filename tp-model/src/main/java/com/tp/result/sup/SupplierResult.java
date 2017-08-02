package com.tp.result.sup;

import java.util.List;

import com.tp.model.BaseDO;
import com.tp.model.sup.SupplierInfo;

/**
 * 供应商返回值
 *
 * @author szy
 */
public class SupplierResult extends BaseDO {

    /**
     *
     */
    private static final long serialVersionUID = -3348607677659759069L;

    /** 供应商列表 */
    private List<SupplierInfo> supplierInfoList;

    private Long totalCount;

    private String message;

    private String messageCode;

	public List<SupplierInfo> getSupplierInfoList() {
		return supplierInfoList;
	}

	public void setSupplierInfoList(List<SupplierInfo> supplierInfoList) {
		this.supplierInfoList = supplierInfoList;
	}

	public Long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessageCode() {
		return messageCode;
	}

	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}
}
