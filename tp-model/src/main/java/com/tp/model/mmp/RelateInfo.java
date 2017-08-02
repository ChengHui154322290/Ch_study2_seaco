package com.tp.model.mmp;

import java.util.Date;

import com.tp.model.BaseDO;

/**
 * 关联专题
 * 
 * @author szy
 */

public class RelateInfo extends BaseDO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7674254887302997701L;

	/** 主键 */
	private Long id;

	/** 关联专题一方id */
	private Long firstTopicId;

	/** 关联的另一方 */
	private Long secondTopicId;

	/** 删除状态 0 -正常 1- 删除 */
	private Integer deletion;

	/** 创建时间 */
	private Date createTime;

	/** 修改时间 */
	private Date updateTime;

	/**
	 * 设置 主键
	 * 
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 设置 关联专题一方id
	 * 
	 * @param firstTopicId
	 */
	public void setFirstTopicId(Long firstTopicId) {
		this.firstTopicId = firstTopicId;
	}

	/**
	 * 设置 关联的另一方
	 * 
	 * @param secondTopicId
	 */
	public void setSecondTopicId(Long secondTopicId) {
		this.secondTopicId = secondTopicId;
	}

	/**
	 * 设置 删除状态 0 -正常 1- 删除
	 * 
	 * @param deletion
	 */
	public void setDeletion(Integer deletion) {
		this.deletion = deletion;
	}

	/**
	 * 设置 创建时间
	 * 
	 * @param createTime
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * 设置 修改时间
	 * 
	 * @param updateTime
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
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
	 * 获取 关联专题一方id
	 * 
	 * @return firstTopicId
	 */
	public Long getFirstTopicId() {
		return firstTopicId;
	}

	/**
	 * 获取 关联的另一方
	 * 
	 * @return secondTopicId
	 */
	public Long getSecondTopicId() {
		return secondTopicId;
	}

	/**
	 * 获取 删除状态 0 -正常 1- 删除
	 * 
	 * @return deletion
	 */
	public Integer getDeletion() {
		return deletion;
	}

	/**
	 * 获取 创建时间
	 * 
	 * @return createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * 获取 修改时间
	 * 
	 * @return updateTime
	 */
	public Date getUpdateTime() {
		return updateTime;
	}

}