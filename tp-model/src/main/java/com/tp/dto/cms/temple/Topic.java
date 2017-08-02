package com.tp.dto.cms.temple;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 品牌下产品
 * 2015-1-9
 */
public class Topic implements Serializable {

	private static final long serialVersionUID = 5301450512283576984L;

	private Integer allPages = 0; //总页数
	
	private Integer totalCount = 0;//总数
	
	private Integer currentPages =1; //当前页数
	
	private String name; //品牌名称
	
	private List<Products>  productsList = new ArrayList<Products>();
	
	/** 爆款商品list */
	private List<Products> hotProductsList = new ArrayList<Products>();
	
	public Integer getAllPages() {
		return allPages;
	}

	public void setAllPages(Integer allPages) {
		this.allPages = allPages;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getCurrentPages() {
		return currentPages;
	}

	public void setCurrentPages(Integer currentPages) {
		this.currentPages = currentPages;
	}

	public List<Products> getProductsList() {
		return productsList;
	}

	public void setProductsList(List<Products> productsList) {
		this.productsList = productsList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Products> getHotProductsList() {
		return hotProductsList;
	}

	public void setHotProductsList(List<Products> hotProductsList) {
		this.hotProductsList = hotProductsList;
	}
	
}
