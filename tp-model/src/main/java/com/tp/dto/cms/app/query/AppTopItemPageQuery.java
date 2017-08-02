package com.tp.dto.cms.app.query;

import java.io.Serializable;

/**
 * APP的品牌明细加载的查询类
 * @author szy
 *
 */
public class AppTopItemPageQuery implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/** 专场id **/
	private Long specialid;
	
	/** 分类id **/
	private Long classifyid;
	
	/** 品牌id **/
	private Long brandid;
	
	/** 是否有库存：“0”表示显示商品，不管是否有货或者无货，“1”只显示有货商品 **/
	private String ishave;
	
	/** 价格排序：“0”表示不参考价格因素，“1”表示升序，“2”表示降序  **/
	private String isascending;
	
	/** 当前页数 **/
	private Integer curpage;
	
	/** 页数 **/
	private Integer pageSize;
	
	// filter by dss
	private Long promoterId;
	
	

	public Long getPromoterId() {
		return promoterId;
	}

	public void setPromoterId(Long promoterId) {
		this.promoterId = promoterId;
	}

	public Long getSpecialid() {
		return specialid;
	}

	public void setSpecialid(Long specialid) {
		this.specialid = specialid;
	}

	public Long getClassifyid() {
		return classifyid;
	}

	public void setClassifyid(Long classifyid) {
		this.classifyid = classifyid;
	}

	public Long getBrandid() {
		return brandid;
	}

	public void setBrandid(Long brandid) {
		this.brandid = brandid;
	}

	public String getIshave() {
		return ishave;
	}

	public void setIshave(String ishave) {
		this.ishave = ishave;
	}

	public String getIsascending() {
		return isascending;
	}

	public void setIsascending(String isascending) {
		this.isascending = isascending;
	}

	public Integer getCurpage() {
		return curpage;
	}

	public void setCurpage(Integer curpage) {
		this.curpage = curpage;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	
}
