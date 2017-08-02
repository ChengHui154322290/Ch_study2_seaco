package com.tp.dto.cms;

import java.io.Serializable;
import java.util.Date;

public class CmsAdvertiseInfoDTO implements Serializable/*,JSONAware*/ {

	private static final long serialVersionUID = 1L;
	
	/** 主键 */
	private Long id;
	
	/** 图片排序，用于同一个位置有不同排序 */
	private Integer sort;

	/** 图片名称 */
	private String advertname;

	/** 图片类型 */
	private String type;
	
	/** 图片链接 */
	private String link;

	/** 图片路径 */
	private String path;
	
	/** 图片位置 */
	private Integer position;
	
	/** 是否启用 */
	private String status;
	
	/** 开始时间 */
	private Date startdate;
	
	/** 结束时间 */
	private Date enddate;

	/** 创建人 */
	private Integer creater;

	/** 创建时间 */
	private Date create_time;

	/** 修改人 */
	private Integer modifier;

	/** 修改时间 */
	private Date modify_time;
	
	/** 开始时间字符串 */
	private String startdateStr;
	
	/** 结束时间字符串 */
	private String enddateStr;
	
	/** 当前页 */
	private Integer pageNo;

	/** 当前页数 */
	private Integer pageSize;

	/** 总页数 */
	private Integer totalCount;
	
	/** 总条数 */
	private Integer totalCountNum;
	
	/** 活动id */
	private Long activityid;
	
	/** 商品id */
	private Long productid;
	
	/** 商品sku */
	private String sku;
	
	/** 启动时间 */
	private Integer time;
	
	/** 活动类型 */
	private String actType;
	
	/** 平台标识 */
	private String platformType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getAdvertname() {
		return advertname;
	}

	public void setAdvertname(String advertname) {
		this.advertname = advertname;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getStartdate() {
		return startdate;
	}

	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}

	public Date getEnddate() {
		return enddate;
	}

	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}

	public Integer getCreater() {
		return creater;
	}

	public void setCreater(Integer creater) {
		this.creater = creater;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public Integer getModifier() {
		return modifier;
	}

	public void setModifier(Integer modifier) {
		this.modifier = modifier;
	}

	public Date getModify_time() {
		return modify_time;
	}

	public void setModify_time(Date modify_time) {
		this.modify_time = modify_time;
	}

	public String getStartdateStr() {
		return startdateStr;
	}

	public void setStartdateStr(String startdateStr) {
		this.startdateStr = startdateStr;
	}

	public String getEnddateStr() {
		return enddateStr;
	}

	public void setEnddateStr(String enddateStr) {
		this.enddateStr = enddateStr;
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

	public Long getActivityid() {
		return activityid;
	}

	public void setActivityid(Long activityid) {
		this.activityid = activityid;
	}

	public Long getProductid() {
		return productid;
	}

	public void setProductid(Long productid) {
		this.productid = productid;
	}

	public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}

	public String getActType() {
		return actType;
	}

	public void setActType(String actType) {
		this.actType = actType;
	}

	public String getPlatformType() {
		return platformType;
	}

	public void setPlatformType(String platformType) {
		this.platformType = platformType;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}



	
}
