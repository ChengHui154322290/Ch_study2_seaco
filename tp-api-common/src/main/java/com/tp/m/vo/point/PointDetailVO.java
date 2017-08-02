package com.tp.m.vo.point;

import java.io.Serializable;
import java.util.Date;

public class PointDetailVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3044468622315000013L;
	
	/**详情操作标题*/
	private String title;	
	/**业务类型 数据类型tinyint(2)*/
	private String bizTypeName;
	/**业务编码 数据类型varchar(32)*/
	private String bizId;
	/**积分 数据类型int(8)*/
	private String point;
	/**创建时间*/
	private String createTime;
	
	private String pointTypeName;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getBizTypeName() {
		return bizTypeName;
	}
	public void setBizTypeName(String bizTypeName) {
		this.bizTypeName = bizTypeName;
	}
	public String getBizId() {
		return bizId;
	}
	public void setBizId(String bizId) {
		this.bizId = bizId;
	}
	public String getPoint() {
		return point;
	}
	public void setPoint(String point) {
		this.point = point;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getPointTypeName() {
		return pointTypeName;
	}
	public void setPointTypeName(String pointTypeName) {
		this.pointTypeName = pointTypeName;
	}
}
