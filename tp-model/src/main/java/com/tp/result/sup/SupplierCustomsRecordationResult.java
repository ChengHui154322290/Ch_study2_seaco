package com.tp.result.sup;

import java.util.List;

import com.tp.model.BaseDO;
import com.tp.model.sup.SupplierCustomsRecordation;
import com.tp.query.sup.SupplierQuery;

/**
 * 通关渠道返回值
 * 
 * @author szy
 *
 */
public class SupplierCustomsRecordationResult  extends BaseDO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -910951607741728905L;
	
    private List<SupplierCustomsRecordation> supplierCustomsRecordationList;
    
    private List<SupplierQuery>  supplierQueryList;
    
    private Integer totalCount;
    
    private Integer nullParamsCount;
    
    private String message;

    private String messageCode;

	public List<SupplierCustomsRecordation> getSupplierCustomsRecordationList() {
		return supplierCustomsRecordationList;
	}

	public void setSupplierCustomsRecordationList(
			List<SupplierCustomsRecordation> supplierCustomsRecordationList) {
		this.supplierCustomsRecordationList = supplierCustomsRecordationList;
	}

	public List<SupplierQuery> getSupplierQueryList() {
		return supplierQueryList;
	}

	public void setSupplierQueryList(List<SupplierQuery> supplierQueryList) {
		this.supplierQueryList = supplierQueryList;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getNullParamsCount() {
		return nullParamsCount;
	}

	public void setNullParamsCount(Integer nullParamsCount) {
		this.nullParamsCount = nullParamsCount;
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
