package com.tp.dto.ord.kuaidi100;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 
 * <pre>
 * 查询结果
 * </pre>
 * 
 * @author szy
 * @time 2015-2-3 下午12:04:59
 */
public class Result implements Serializable {
	private static final long serialVersionUID = -2532537521893810132L;
	/** 消息体 */
	private String message = "";
	/** 单号 */
	private String nu = "";
	/** 是否签收标记 */
	private String ischeck = "0";
	/** 快递公司编码,一律用小写字母 */
	private String com = "";
	/** 通讯状态 */
	private String status = "0";
	/** 具体跟踪日志 */
	private ArrayList<ResultItem> data = new ArrayList<ResultItem>();
	/** 快递单当前签收状态，包括0在途中、1已揽收、2疑难、3已签收、4退签、5同城派送中、6退回、7转单等7个状态 */
	private String state = "0";
	/** 快递单明细状态标记 */
	private String condition = "";

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getNu() {
		return nu;
	}

	public void setNu(String nu) {
		this.nu = nu;
	}

	public String getCom() {
		return com;
	}

	public void setCom(String com) {
		this.com = com;
	}

	public ArrayList<ResultItem> getData() {
		return data;
	}

	public void setData(ArrayList<ResultItem> data) {
		this.data = data;
	}

	public String getIscheck() {
		return ischeck;
	}

	public void setIscheck(String ischeck) {
		this.ischeck = ischeck;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

}
