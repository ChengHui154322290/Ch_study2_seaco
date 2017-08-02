package com.tp.dto.mem;

import java.io.Serializable;
import java.util.Date;

import com.tp.common.vo.mem.SceneCode;

/**
 * 
 *
 */
public class MemberPointDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4797775025824348890L;

	private Integer Point;
	
	private String sceneCode;
	
	private Date sendTime;

	public Integer getPoint() {
		return Point;
	}

	public void setPoint(Integer point) {
		Point = point;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public String getSceneCode() {
		return sceneCode;
	}

	public void setSceneCode(String sceneCode) {
		this.sceneCode = sceneCode;
	}

	@Override
	public String toString() {
		return "UserPointDto [Point=" + Point + ", sceneCode=" + SceneCode.getDesc(sceneCode)
				+ ", sendTime=" + sendTime + "]";
	}
}
