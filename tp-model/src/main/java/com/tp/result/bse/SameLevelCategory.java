package com.tp.result.bse;

import java.io.Serializable;
import java.util.List;

import com.tp.model.bse.Category;


/**
 * 
 * <pre>
 *    封装好的同level的category类
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 */
public class SameLevelCategory implements Serializable {

	private static final long serialVersionUID = -1763682643963686360L;
   /**
    * 类别的主键id
    */
	private Long id;

	/**
	 * 同level的 category 的list
	 */
	private List<Category> sameLevelCate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Category> getSameLevelCate() {
		return sameLevelCate;
	}

	public void setSameLevelCate(List<Category> sameLevelCate) {
		this.sameLevelCate = sameLevelCate;
	}

}
