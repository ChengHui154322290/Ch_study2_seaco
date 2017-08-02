/**
 * 
 */
package com.tp.dto.ord.customs;

import java.io.Serializable;

import com.tp.common.vo.OrderConstant.ClearanceStatus;
import com.tp.common.vo.OrderConstant.PutCustomsStatus;
import com.tp.common.vo.OrderConstant.PutCustomsType;

/**
 * @author Administrator
 *
 */
public class PutCustomsDto implements Serializable{
	
	private static final long serialVersionUID = 1L;

	/** 报关类型 */
	private PutCustomsType type;
	
	/** 报关状态 */
	private Integer status;
	
	public PutCustomsDto(PutCustomsType type, Integer status) {
		this.type = type;
		this.status = status;
	}
	
	public String getStatusStr(){
		if (status == null || type == null || type == PutCustomsType.NEW) {
			return "";
		}
		if (type == PutCustomsType.CLEARANCE) {
			return ClearanceStatus.getClearanceDescByCode(status);
		}
		return PutCustomsStatus.getCustomsStatusDescByCode(status);
	}
	
	public PutCustomsType getType() {
		return type;
	}

	public void setType(PutCustomsType type) {
		this.type = type;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}
