package com.tp.dto.cms;

import java.io.Serializable;
import java.util.Date;

public class CmsRedisIndexRuleDTO implements Serializable {

	private static final long serialVersionUID = -8351898288554936814L;

	/** 主键 */
	private Long id;

	/** 功能名称 */
	private String functionName;

	/** 功能代码 */
	private String functionCode;

	/** 平台类型 */
	private String platformType;
	
	/** 地区 */
	private String area;
	
	/** 地区代码 */
	private String areaCode;
	
	/** 缓存key值(功能代码-平台类型) */
	private String firstKey;
	
	/** 缓存key值(功能代码-平台类型-地区代码) */
	private String secondKey;

	/** 页数(供分页使用) */
	private Integer page;

	/** 状态(启用/停用) */
	private Integer status;

	/** 创建时间 */
	private Date createTime;

	/** 创建人 */
	private Long creater;
	
	/** 修改时间 */
	private Date updateTime;
	
	/** 修改人 */
	private Long updater;
	
	
	
	
	/** 当前页 */
	private Integer pageNo;

	/** 当前页数 */
	private Integer pageSize;

	/** 总页数 */
	private Integer totalCount;
	
	/** 总条数 */
	private Integer totalCountNum;
	
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public String getFunctionCode() {
		return functionCode;
	}

	public void setFunctionCode(String functionCode) {
		this.functionCode = functionCode;
	}

	public String getPlatformType() {
		return platformType;
	}

	public void setPlatformType(String platformType) {
		this.platformType = platformType;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getFirstKey() {
		return firstKey;
	}

	public void setFirstKey(String firstKey) {
		this.firstKey = firstKey;
	}

	public String getSecondKey() {
		return secondKey;
	}

	public void setSecondKey(String secondKey) {
		this.secondKey = secondKey;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getCreater() {
		return creater;
	}

	public void setCreater(Long creater) {
		this.creater = creater;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Long getUpdater() {
		return updater;
	}

	public void setUpdater(Long updater) {
		this.updater = updater;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getTotalCountNum() {
		return totalCountNum;
	}

	public void setTotalCountNum(Integer totalCountNum) {
		this.totalCountNum = totalCountNum;
	}

}
