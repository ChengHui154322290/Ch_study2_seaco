package com.tp.model.mmp;

import com.tp.model.BaseDO;

/**
 * 区域
 * 
 * @author szy
 */

public class AreaDO extends BaseDO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1450891492550186740L;

	/** 主键 */
	private Integer id;

	/** 地区名 */
	private String name;

	/**
	 * 设置 主键
	 * 
	 * @param id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * 设置 地区名
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取 主键
	 * 
	 * @return id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * 获取 地区名
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

}