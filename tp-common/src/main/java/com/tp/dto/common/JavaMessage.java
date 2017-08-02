package com.tp.dto.common;

import java.io.Serializable;

/**
 * 消息体
 * @author szy
 *
 */
public class JavaMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8222811850636847445L;

	private Long objectId;
	private String objectType;
	private String eventType;
	private Object addition;	//无特定，可以灵活跟随信息
	
	public JavaMessage(Long objectId, String objectType, String eventType) {
		this.objectId = objectId;
		this.objectType = objectType;
		this.eventType = eventType;
	}
	public Long getObjectId() {
		return objectId;
	}
	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}
	public String getObjectType() {
		return objectType;
	}
	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public Object getAddition() {
		return addition;
	}
	public void setAddition(Object addition) {
		this.addition = addition;
	}
	
}
