package com.tp.model.bse;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 分类导航类目表
  */
public class NavigationCategory extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1456124164854L;

	/**类目id 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/**类目名称 数据类型varchar(20)*/
	private String name;

	/**导航类型 1 分类 2 品牌*/
	private Integer type;
	
	/**类目编码 数据类型varchar(50)*/
	private String code;
	
	/**类目级别:1-一级类目,2-二级类目,3-三级类目 数据类型tinyint(1)*/
	private Integer level;
	
	/**状态:0-无效 1-有效 数据类型tinyint(1)*/
	private Integer status;
	
	/**是否突出展示：0-否 1-是 数据类型tinyint(1)*/
	private Integer isHighlight;
	
	/**图片 数据类型varchar(2000)*/
	private String pic;
	
	/**父类目ID 数据类型bigint(11)*/
	private Long parentId;
	
	/**顺序 数据类型tinyint(4)*/
	private Integer sort;
	
	/**是否发布:0否 1是 数据类型tinyint(1)*/
	private Integer isPublish;
	
	/**创建人 数据类型varchar(50)*/
	private String createUser;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**修改人 */
	private String updateUser;
	
	/**修改时间 数据类型datetime*/
	private Date updateTime;

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getIsHighlight() {
		return isHighlight;
	}

	public void setIsHighlight(Integer isHighlight) {
		this.isHighlight = isHighlight;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getIsPublish() {
		return isPublish;
	}

	public void setIsPublish(Integer isPublish) {
		this.isPublish = isPublish;
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
