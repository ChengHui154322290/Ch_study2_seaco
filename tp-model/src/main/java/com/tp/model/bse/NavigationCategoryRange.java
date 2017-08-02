package com.tp.model.bse;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 分类导航类目范围表
  */
public class NavigationCategoryRange extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1456124164855L;

	/** 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/**类目id 数据类型bigint(11)*/
	private Long categoryId;
	
	/**范围类型:1-后台分类,2-品牌 数据类型tinyint(1)*/
	private Integer type;

	private Integer sort;
	
	/** 数据类型varchar(1000)*/
	private String content;
	
	/**状态:0-正常，1-删除 数据类型tinyint(2)*/
	private Integer status;
	
	/**创建人 数据类型varchar(50)*/
	private String createUser;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**修改人 数据类型varchar(50)*/
	private String updateUser;
	
	/**修改时间 数据类型datetime*/
	private Date updateTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}
