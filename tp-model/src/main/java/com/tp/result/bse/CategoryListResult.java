package com.tp.result.bse;

import java.io.Serializable;
import java.util.List;

import com.tp.model.bse.Category;

/**
 * 
 * <pre>
 * 品类结果封装类
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 */
public class CategoryListResult implements Serializable {

	private static final long serialVersionUID = 6790285952445178687L;
	/**
	 * 父级品类id
	 */
	private Long categoryId;
	/**
	 * 父级品类下的子类
	 */
	private List<Category> descendantCategory;

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public List<Category> getDescendantCategory() {
		return descendantCategory;
	}

	public void setDescendantCategory(List<Category> descendantCategory) {
		this.descendantCategory = descendantCategory;
	}

}
