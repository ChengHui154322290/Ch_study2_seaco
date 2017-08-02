package com.tp.dto.cms;

import java.io.Serializable;
import java.util.List;

import com.tp.model.BaseDO;

/**
 * 活动模板
 * 
 * @author szy
 */

public class CmsStaticActivityTempletDTO extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1780471457199866515L;

	/** 主键 */
	private Long id;

	/** 模板名称 */
	private String templetName;
	
	/** 模板主键 */
	private Long templetId;

	/** 模板类型 */
	private Integer type;

	/** 上传模板路径名(html的上传路径) */
	private String path;

	/** 状态(0正常，1禁用) */
	private Integer status;
	
	/** 变量的集合 */
	private List<CmsStaticActivityVariableDTO> list = null;

	/**
	 * 设置 主键
	 * 
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 设置 模板名称
	 * 
	 * @param templetName
	 */
	public void setTempletName(String templetName) {
		this.templetName = templetName;
	}

	/**
	 * 设置 模板类型
	 * 
	 * @param type
	 */
	public void setType(Integer type) {
		this.type = type;
	}

	/**
	 * 设置 上传模板路径名(html的上传路径)
	 * 
	 * @param path
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * 设置 状态(0正常，1禁用)
	 * 
	 * @param status
	 */
	public void setStatus(Integer status) {
		this.status = status;
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
	 * 获取 模板名称
	 * 
	 * @return templetName
	 */
	public String getTempletName() {
		return templetName;
	}

	/**
	 * 获取 模板类型
	 * 
	 * @return type
	 */
	public Integer getType() {
		return type;
	}

	/**
	 * 获取 上传模板路径名(html的上传路径)
	 * 
	 * @return path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * 获取 状态(0正常，1禁用)
	 * 
	 * @return status
	 */
	public Integer getStatus() {
		return status;
	}

	public List<CmsStaticActivityVariableDTO> getList() {
		return list;
	}

	public void setList(List<CmsStaticActivityVariableDTO> list) {
		this.list = list;
	}

	public Long getTempletId() {
		return templetId;
	}

	public void setTempletId(Long templetId) {
		this.templetId = templetId;
	}

}