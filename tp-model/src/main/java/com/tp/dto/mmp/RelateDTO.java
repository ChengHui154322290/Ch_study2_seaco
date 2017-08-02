package com.tp.dto.mmp;

import java.io.Serializable;
import java.util.Date;

import com.tp.model.mmp.Relate;
import com.tp.model.mmp.RelateChange;
import com.tp.model.mmp.RelateInfo;

public class RelateDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7118979231258371859L;

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

	/** 活动变更单序号 */
	private Long topicChangeId;
	
	/**
	 * 主题编号
	 */
	private String secondTopicNumber;

	/**
	 * 主题名称
	 */
	private String secondTopicName;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the firstTopicId
	 */
	public Long getFirstTopicId() {
		return firstTopicId;
	}

	/**
	 * @param firstTopicId
	 *            the firstTopicId to set
	 */
	public void setFirstTopicId(Long firstTopicId) {
		this.firstTopicId = firstTopicId;
	}

	/**
	 * @return the secondTopicId
	 */
	public Long getSecondTopicId() {
		return secondTopicId;
	}

	/**
	 * @param secondTopicId
	 *            the secondTopicId to set
	 */
	public void setSecondTopicId(Long secondTopicId) {
		this.secondTopicId = secondTopicId;
	}

	/**
	 * @return the deletion
	 */
	public Integer getDeletion() {
		return deletion;
	}

	/**
	 * @param deletion
	 *            the deletion to set
	 */
	public void setDeletion(Integer deletion) {
		this.deletion = deletion;
	}

	/**
	 * @return the createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime
	 *            the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the updateTime
	 */
	public Date getUpdateTime() {
		return updateTime;
	}

	/**
	 * @param updateTime
	 *            the updateTime to set
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * @return the secondTopicNumber
	 */
	public String getSecondTopicNumber() {
		return secondTopicNumber;
	}

	/**
	 * @param secondTopicNumber
	 *            the secondTopicNumber to set
	 */
	public void setSecondTopicNumber(String secondTopicNumber) {
		this.secondTopicNumber = secondTopicNumber;
	}

	/**
	 * @return the secondTopicName
	 */
	public String getSecondTopicName() {
		return secondTopicName;
	}

	/**
	 * @param secondTopicName
	 *            the secondTopicName to set
	 */
	public void setSecondTopicName(String secondTopicName) {
		this.secondTopicName = secondTopicName;
	}

	/**
	 * @return the topicChangeId
	 */
	public Long getTopicChangeId() {
		return topicChangeId;
	}

	/**
	 * @param topicChangeId the topicChangeId to set
	 */
	public void setTopicChangeId(Long topicChangeId) {
		this.topicChangeId = topicChangeId;
	}

	/**
	 * 转换获得RelateDO
	 * 
	 * @return
	 */
	public RelateInfo getRelateDO() {
		RelateInfo relate = new RelateInfo();
		relate.setId(this.id);
		relate.setFirstTopicId(this.firstTopicId);
		relate.setSecondTopicId(this.secondTopicId);
		relate.setCreateTime(this.createTime);
		relate.setUpdateTime(this.updateTime);
		relate.setDeletion(this.deletion);
		return relate;
	}

	/**
	 * 转换获得RelateDTO
	 * 
	 * @param relateDO
	 */
	public void setRelateDTO(Relate relateDO) {
		this.id = relateDO.getId();
		this.firstTopicId = relateDO.getFirstTopicId();
		this.secondTopicId = relateDO.getSecondTopicId();
		this.createTime = relateDO.getCreateTime();
		this.updateTime = relateDO.getUpdateTime();
		this.deletion = relateDO.getDeletion();
	}
	
	/**
	 * 转换获得RelateDO
	 * 
	 * @return
	 */
	public RelateChange getRelateChange() {
		RelateChange relate = new RelateChange();
		relate.setId(this.id);
		relate.setTopicChangeId(this.topicChangeId);
		relate.setFirstTopicId(this.firstTopicId);
		relate.setSecondTopicId(this.secondTopicId);
		relate.setCreateTime(this.createTime);
		relate.setUpdateTime(this.updateTime);
		relate.setDeletion(this.deletion);
		return relate;
	}
	
	/**
	 * 转换获得RelateDTO
	 * 
	 * @param relateDO
	 */
	public void setRelateDTO(RelateChange relateChangeDO) {
		this.id = relateChangeDO.getId();
		this.firstTopicId = relateChangeDO.getFirstTopicId();
		this.secondTopicId = relateChangeDO.getSecondTopicId();
		this.createTime = relateChangeDO.getCreateTime();
		this.updateTime = relateChangeDO.getUpdateTime();
		this.deletion = relateChangeDO.getDeletion();
		this.topicChangeId = relateChangeDO.getTopicChangeId();
	}
}
