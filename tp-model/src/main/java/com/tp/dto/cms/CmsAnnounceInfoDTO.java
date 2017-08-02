package com.tp.dto.cms;

import java.io.Serializable;
import java.util.Date;


public class CmsAnnounceInfoDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/** 主键 */
	private Long id;

	/** 标题 */
	private String title;
	
	/** 类型 */
	private String type;
	
	/** 链接 */
	private String link;

	/** 内容 */
	private String content;

	/** 状态 */
	private Integer status;
	
	/** 顺序 */
	private Integer sort;

	/** 创建人 */
	private Integer creater;

	/** 创建时间 */
	private Date create_time;

	/** 修改人 */
	private Integer modifier;

	/** 修改时间 */
	private Date modify_time;
	
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
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
