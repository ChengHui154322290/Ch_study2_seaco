/**
 * NewHeight.com Inc.
 * Copyright (c) 2007-2014 All Rights Reserved.
 */
package com.tp.result.ord;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.tp.dto.OrderOperatorErrorDTO;
import com.tp.dto.ord.OrderDeliverDTO;

/**
 * <pre>
 * 发货后订单处理结果
 * </pre>
 * 
 * @author szy
 * @time 2015-1-21 下午1:38:32
 */
public class OrderDeliverResult extends ResultBaseDTO implements Serializable {
	private static final long serialVersionUID = -2036773358303759793L;

	/** 失败处理数量 */
	private Integer errorSize;

	/** 失败处理元数据 */
	private List<OrderDeliverDTO> errorDataList;

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

	public void setErrorSize(Integer errorSize) {
		this.errorSize = errorSize;
	}

	public List<OrderDeliverDTO> getErrorDataList() {
		return errorDataList;
	}

	public void setErrorDataList(List<OrderDeliverDTO> errorDataList) {
		this.errorDataList = errorDataList;
	}

	public List<OrderOperatorErrorDTO> getOrderOperatorErrorList() {
		return orderOperatorErrorList;
	}

	public void setOrderOperatorErrorList(List<OrderOperatorErrorDTO> orderOperatorErrorList) {
		this.orderOperatorErrorList = orderOperatorErrorList;
	}

}
