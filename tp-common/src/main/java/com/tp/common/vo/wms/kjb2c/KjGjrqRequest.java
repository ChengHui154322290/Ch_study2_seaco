package com.tp.common.vo.wms.kjb2c;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.tp.common.vo.wms.KjGjrqRequestItem;

public class KjGjrqRequest implements java.io.Serializable {

	private static final long serialVersionUID = 80589987878500834L;

	/** 货主编码 */
//	@NotEmpty
	private String agentCode;

	/** 货主名称 */
//	@NotEmpty
	private String agentName;

	/** 明细行 */
	@NotNull
	private Integer tdq;

//	@NotEmpty
	private List<KjGjrqRequestItem> items;

	/**
	 * 设置 货主编码
	 * 
	 * @param agentCode
	 */
	public void setAgentCode(String agentCode) {
		this.agentCode = agentCode;
	}

	/**
	 * 设置 货主名称
	 * 
	 * @param agentName
	 */
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	/**
	 * 设置 明细行
	 * 
	 * @param tdq
	 */
	public void setTdq(Integer tdq) {
		this.tdq = tdq;
	}

	/**
	 * 获取 货主编码
	 * 
	 * @return agentCode
	 */
	public String getAgentCode() {
		return agentCode;
	}

	/**
	 * 获取 货主名称
	 * 
	 * @return agentName
	 */
	public String getAgentName() {
		return agentName;
	}

	/**
	 * 获取 明细行
	 * 
	 * @return tdq
	 */
	public Integer getTdq() {
		return tdq;
	}

	public List<KjGjrqRequestItem> getItems() {
		return items;
	}

	public void setItems(List<KjGjrqRequestItem> items) {
		this.items = items;
	}

}
