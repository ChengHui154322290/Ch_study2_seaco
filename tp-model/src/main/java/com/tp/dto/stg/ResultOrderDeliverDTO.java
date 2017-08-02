package com.tp.dto.stg;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.tp.dto.OrderOperatorErrorDTO;
import com.tp.model.ord.OrderDelivery;


public class ResultOrderDeliverDTO implements Serializable{

	private static final long serialVersionUID = -5862426604533893463L;

	/** 失败处理数量 */
	private Integer errorSize;

	/** 失败处理元数据 */
	private List<OrderDelivery> errorDataList;

	/** 失败处理错误信息 */
	private List<OrderOperatorErrorDTO> orderOperatorErrorList;

	public Integer getErrorSize() {
		if (CollectionUtils.isNotEmpty(errorDataList)) {
			errorSize = errorDataList.size();
		} else {
			errorSize = 0;
		}
		return errorSize;
	}

	public List<OrderDelivery> getErrorDataList() {
		return errorDataList;
	}

	public void setErrorDataList(List<OrderDelivery> errorDataList) {
		this.errorDataList = errorDataList;
	}

	public List<OrderOperatorErrorDTO> getOrderOperatorErrorList() {
		return orderOperatorErrorList;
	}

	public void setOrderOperatorErrorList(
			List<OrderOperatorErrorDTO> orderOperatorErrorList) {
		this.orderOperatorErrorList = orderOperatorErrorList;
	}

	public void setErrorSize(Integer errorSize) {
		this.errorSize = errorSize;
	}
}
