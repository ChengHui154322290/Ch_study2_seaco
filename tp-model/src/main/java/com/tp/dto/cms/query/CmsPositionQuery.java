package com.tp.dto.cms.query;

import java.io.Serializable;

import com.tp.model.BaseDO;

/**
 * 位置管理
 * 
 * @author szy
 */

public class CmsPositionQuery extends BaseDO implements Serializable{

	private static final long serialVersionUID = 2955706398345947526L;

	/** 模块表主键 */
	private Long templeId;

	/** 位置名称 */
	private String positionName;

	/** 位置编号 */
	private String positionCode;

	/** 状态(0正常，1停用，2删除) */
	private Integer status;

	/** 页面名称 */
	private String pageName;

	/** 模块名称 */
	private String templeName;

	/** 顺序 */
	private Integer seq;

	/** 元素类型 */
	private Integer elementType;

	/**
	 * 设置 模块表主键
	 * 
	 * @param templeId
	 */
	public void setTempleId(Long templeId) {
		this.templeId = templeId;
	}

	/**
	 * 设置 位置名称
	 * 
	 * @param positionName
	 */
	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}

	/**
	 * 设置 位置编号
	 * 
	 * @param positionCode
	 */
	public void setPositionCode(String positionCode) {
		this.positionCode = positionCode;
	}

	/**
	 * 设置 状态(0正常，1停用，2删除)
	 * 
	 * @param status
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * 设置 页面名称
	 * 
	 * @param pageName
	 */
	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	/**
	 * 设置 模块名称
	 * 
	 * @param templeName
	 */
	public void setTempleName(String templeName) {
		this.templeName = templeName;
	}

	/**
	 * 设置 顺序
	 * 
	 * @param seq
	 */
	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	/**
	 * 设置 元素类型
	 * 
	 * @param elementType
	 */
	public void setElementType(Integer elementType) {
		this.elementType = elementType;
	}

	/**
	 * 获取 模块表主键
	 * 
	 * @return templeId
	 */
	public Long getTempleId() {
		return templeId;
	}

	/**
	 * 获取 位置名称
	 * 
	 * @return positionName
	 */
	public String getPositionName() {
		return positionName;
	}

	/**
	 * 获取 位置编号
	 * 
	 * @return positionCode
	 */
	public String getPositionCode() {
		return positionCode;
	}

	/**
	 * 获取 状态(0正常，1停用，2删除)
	 * 
	 * @return status
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * 获取 页面名称
	 * 
	 * @return pageName
	 */
	public String getPageName() {
		return pageName;
	}

	/**
	 * 获取 模块名称
	 * 
	 * @return templeName
	 */
	public String getTempleName() {
		return templeName;
	}

	/**
	 * 获取 顺序
	 * 
	 * @return seq
	 */
	public Integer getSeq() {
		return seq;
	}

	/**
	 * 获取 元素类型
	 * 
	 * @return elementType
	 */
	public Integer getElementType() {
		return elementType;
	}

}