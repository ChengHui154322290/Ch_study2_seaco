package com.tp.dto.map;

import java.io.Serializable;
import java.util.List;

/**
 * 距离信息集合
 * @author szy
 *
 */
public class DistancesResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3974080219114191236L;

	private Integer status;//结果状态值  值为0或1，0表示请求失败；1表示请求成功
	
	private List<DistanceResult> results;

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	public List<DistanceResult> getResults() {
		return results;
	}

	public void setResults(List<DistanceResult> results) {
		this.results = results;
	}
}
