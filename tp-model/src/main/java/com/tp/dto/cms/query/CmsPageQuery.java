package com.tp.dto.cms.query;

import java.io.Serializable;

import com.tp.model.BaseDO;

/**
 * 页面管理
 * 
 */

public class CmsPageQuery extends BaseDO implements Serializable{

	private static final long serialVersionUID = 2150837259986889029L;

	/** 页面名称 */
	private String pageName;

	/** 页面编号 */
	private String pageCode;

	/** 状态(0正常，1停用，2删除) */
	private Integer status;

	/** 顺序 */
	private Integer seq;

	/**
	 * 设置 页面编号
	 * 
	 * @param pageCode
	 */
	public void setPageCode(String pageCode) {
		this.pageCode = pageCode;
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
	 * 设置 顺序
	 * 
	 * @param seq
	 */
	public void setSeq(Integer seq) {
		this.seq = seq;
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
	 * 获取 页面编号
	 * 
	 * @return pageCode
	 */
	public String getPageCode() {
		return pageCode;
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
	 * 获取 顺序
	 * 
	 * @return seq
	 */
	public Integer getSeq() {
		return seq;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

}