package com.tp.query.bse;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.tp.model.BaseDO;
import com.tp.model.bse.FrontCategoryLink;

/**
 * 前台类目
 * 
 * @author szy
 */

public class FrontCategoryQuery extends BaseDO {

	private static final long serialVersionUID = 410549151074216075L;

	/** 主键 */
	private Long id;

	/** 类目名称 */
	private String name;

	/** 类目编码 */
	private String code;

	/** 类目级别:1-一级类目,2-二级类目,3-三级类目 */
	private Integer level;
	
	/** 后台分类 **/
	private Integer bLevel;

	/** 状态:0-有效，1-无效 */
	private Boolean status;

	/** 是否突出展示：0-是，1-否 */
	private Boolean isHighlight;

	/** 图标地址 */
	private String logoUrl;

	/** 父类目ID */
	private Long parentId;

	/** 顺序 */
	private Integer seq;

	/** 是否发布:0-发布，1-否 */
	private Boolean isPublish;

	/** 创建人 */
	private Long createUserId;

	/** 创建时间 */
	private Date createTime;

	/** 修改人 */
	private Long modifyUserId;

	/** 修改时间 */
	private Date modifyTime;
	
	private Date queryBeginTime;
	
	private Date queryEndTime;
	
	private Boolean isPoint;
	
	private List<FrontCategoryLink> list;
	
	private Set<Long> ids = new HashSet<Long>();

	/**
	 * 设置 主键
	 * 
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 设置 类目名称
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 设置 类目编码
	 * 
	 * @param code
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * 设置 类目级别:1-一级类目,2-二级类目,3-三级类目
	 * 
	 * @param level
	 */
	public void setLevel(Integer level) {
		this.level = level;
	}

	/**
	 * 设置 状态:0-有效，1-无效
	 * 
	 * @param status
	 */
	public void setStatus(Boolean status) {
		this.status = status;
	}

	/**
	 * 设置 是否突出展示：0-是，1-否
	 * 
	 * @param isHighlight
	 */
	public void setIsHighlight(Boolean isHighlight) {
		this.isHighlight = isHighlight;
	}

	/**
	 * 设置 图标地址
	 * 
	 * @param logoUrl
	 */
	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	/**
	 * 设置 父类目ID
	 * 
	 * @param parentId
	 */
	public void setParentId(Long parentId) {
		this.parentId = parentId;
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
	 * 设置 是否发布:0-发布，1-否
	 * 
	 * @param isPublish
	 */
	public void setIsPublish(Boolean isPublish) {
		this.isPublish = isPublish;
	}

	/**
	 * 设置 创建人
	 * 
	 * @param createUserId
	 */
	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
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
	 * 设置 修改人
	 * 
	 * @param modifyUserId
	 */
	public void setModifyUserId(Long modifyUserId) {
		this.modifyUserId = modifyUserId;
	}

	/**
	 * 设置 修改时间
	 * 
	 * @param modifyTime
	 */
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
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
	 * 获取 类目名称
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * 获取 类目编码
	 * 
	 * @return code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * 获取 类目级别:1-一级类目,2-二级类目,3-三级类目
	 * 
	 * @return level
	 */
	public Integer getLevel() {
		return level;
	}

	/**
	 * 获取 状态:0-有效，1-无效
	 * 
	 * @return status
	 */
	public Boolean getStatus() {
		return status;
	}

	/**
	 * 获取 是否突出展示：0-是，1-否
	 * 
	 * @return isHighlight
	 */
	public Boolean getIsHighlight() {
		return isHighlight;
	}

	/**
	 * 获取 图标地址
	 * 
	 * @return logoUrl
	 */
	public String getLogoUrl() {
		return logoUrl;
	}

	/**
	 * 获取 父类目ID
	 * 
	 * @return parentId
	 */
	public Long getParentId() {
		return parentId;
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
	 * 获取 是否发布:0-发布，1-否
	 * 
	 * @return isPublish
	 */
	public Boolean getIsPublish() {
		return isPublish;
	}

	/**
	 * 获取 创建人
	 * 
	 * @return createUserId
	 */
	public Long getCreateUserId() {
		return createUserId;
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
	 * 获取 修改人
	 * 
	 * @return modifyUserId
	 */
	public Long getModifyUserId() {
		return modifyUserId;
	}

	/**
	 * 获取 修改时间
	 * 
	 * @return modifyTime
	 */
	public Date getModifyTime() {
		return modifyTime;
	}

	public List<FrontCategoryLink> getList() {
		return list;
	}

	public void setList(List<FrontCategoryLink> list) {
		this.list = list;
	}

	public Date getQueryBeginTime() {
		return queryBeginTime;
	}

	public void setQueryBeginTime(Date queryBeginTime) {
		this.queryBeginTime = queryBeginTime;
	}

	public Date getQueryEndTime() {
		return queryEndTime;
	}

	public void setQueryEndTime(Date queryEndTime) {
		this.queryEndTime = queryEndTime;
	}

	public Boolean getIsPoint() {
		return isPoint;
	}

	public void setIsPoint(Boolean isPoint) {
		this.isPoint = isPoint;
	}

	public Integer getbLevel() {
		return bLevel;
	}

	public void setbLevel(Integer bLevel) {
		this.bLevel = bLevel;
	}

	public Set<Long> getIds() {
		return ids;
	}

	public void setIds(Set<Long> ids) {
		this.ids = ids;
	}

}