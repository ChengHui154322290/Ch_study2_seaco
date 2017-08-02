package com.tp.m.vo.search;

import java.util.List;

import com.tp.m.base.BaseVO;

/**
 * 分类品牌信息
 * @author zhuss
 * @2016年3月2日 上午11:46:08
 */
public class NavigationVO implements BaseVO{

	private static final long serialVersionUID = 5752676570580972090L;

	private List<NavCategoryVO> categories ;

	private List<NavBrandVO> brands;

	public List<NavCategoryVO> getCategories() {
		return categories;
	}

	public void setCategories(List<NavCategoryVO> categories) {
		this.categories = categories;
	}

	public List<NavBrandVO> getBrands() {
		return brands;
	}

	public void setBrands(List<NavBrandVO> brands) {
		this.brands = brands;
	}
}
