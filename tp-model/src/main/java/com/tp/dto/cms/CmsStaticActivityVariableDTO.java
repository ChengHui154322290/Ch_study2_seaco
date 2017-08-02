package com.tp.dto.cms;

import java.io.Serializable;

import com.tp.model.BaseDO;

/**
 * 活动模板变量
 * 
 * @author szy
 */

public class CmsStaticActivityVariableDTO extends BaseDO implements Serializable {

	private static final long serialVersionUID = 8698944425031855936L;

	/** 主键 */
	private Long id;

	/** 模板表主键 */
	private Long templetId;

	/** 变量名(中文) */
	private String variableChName;
	
	/** 变量名(英文) */
	private String variableEnName;
	
	/** 类型 */
	private Integer type;

	/** 变量值 */
	private String variableValue;
	
	/** 如果为图片模板，需要追加http的头 */
	private String spelVariableValue;

	/**
	 * 设置 主键
	 * 
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 设置 模板表主键
	 * 
	 * @param templetId
	 */
	public void setTempletId(Long templetId) {
		this.templetId = templetId;
	}

	/**
	 * 设置 变量值
	 * 
	 * @param variableValue
	 */
	public void setVariableValue(String variableValue) {
		this.variableValue = variableValue;
	}

	/**
	 * 获取 主键
	 * 
	 * @return id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * 获取 模板表主键
	 * 
	 * @return templetId
	 */
	public Long getTempletId() {
		return templetId;
	}

	/**
	 * 获取 变量值
	 * 
	 * @return variableValue
	 */
	public String getVariableValue() {
		return variableValue;
	}

	public String getVariableChName() {
		return variableChName;
	}

	public void setVariableChName(String variableChName) {
		this.variableChName = variableChName;
	}

	public String getVariableEnName() {
		return variableEnName;
	}

	public void setVariableEnName(String variableEnName) {
		this.variableEnName = variableEnName;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getSpelVariableValue() {
		return spelVariableValue;
	}

	public void setSpelVariableValue(String spelVariableValue) {
		this.spelVariableValue = spelVariableValue;
	}

}