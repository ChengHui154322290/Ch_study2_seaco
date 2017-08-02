package com.tp.result.bse;

import java.io.Serializable;

public class CategoryTagsLinkDTO implements Serializable{

	private static final long serialVersionUID = 1399035421345194739L;
	
	/**主键id */
	private Long id;
	
	/**小类id */
	private Long categoryId;
	
	/**标签id */
	private Long tagsId;
	
	/**标签名称 */
	private String name;
	
	/**标签状态 */
	private Boolean status;
	
	/**标签备注 */
	private String remark;

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

	public Long getTagsId() {
		return tagsId;
	}

	public void setTagsId(Long tagsId) {
		this.tagsId = tagsId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}
