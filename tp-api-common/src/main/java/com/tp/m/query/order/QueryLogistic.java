package com.tp.m.query.order;

import com.tp.m.base.BaseQuery;

public class QueryLogistic extends BaseQuery{

	private static final long serialVersionUID = 6701510809331801505L;

	private String code;//订单编号或者售后单号
	private String logisticcode;//物流单号
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getLogisticcode() {
		return logisticcode;
	}
	public void setLogisticcode(String logisticcode) {
		this.logisticcode = logisticcode;
	}
}
