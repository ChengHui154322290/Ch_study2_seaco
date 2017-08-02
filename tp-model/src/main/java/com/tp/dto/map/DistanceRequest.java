package com.tp.dto.map;

import java.io.Serializable;

/**
 * 距离请求
 * @author szy
 *
 */
public class DistanceRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 475065537783066733L;

	/**起点*/
	private String origins;
	/**终点*/
	private String destination;
	public String getOrigins() {
		return origins;
	}
	public void setOrigins(String origins) {
		this.origins = origins;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
}
