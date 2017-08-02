package com.tp.dto.map;

import java.io.Serializable;

/**
 * 距离信息
 * @author szy
 *
 */
public class DistanceResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6984218448248226054L;
	private String 	origin_id	;//	起点坐标，起点坐标序列号（从１开始）
	private String 	dest_id	;//	终点坐标，终点坐标序列号（从１开始）
	private Integer distance	;//	路径距离，单位：米
	private Integer duration	;//	预计行驶时间，单位：秒
	public String getOrigin_id() {
		return origin_id;
	}
	public void setOrigin_id(String origin_id) {
		this.origin_id = origin_id;
	}
	public String getDest_id() {
		return dest_id;
	}
	public void setDest_id(String dest_id) {
		this.dest_id = dest_id;
	}
	public Integer getDistance() {
		return distance;
	}
	public void setDistance(Integer distance) {
		this.distance = distance;
	}
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
}
