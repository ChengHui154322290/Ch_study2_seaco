/**
 * 
 */
package com.tp.m.vo.promoter;

import com.tp.m.base.BaseVO;

/**
 * @author Administrator
 *
 */
public class ShelvesVO implements BaseVO{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6201035260552044889L;
		
	/** 主键 */
	private Long topicid;

	/** 专题名称 */
	private String sku;

	/** 类型 0-专题 1:商品 */
	private Integer shelvestype;
		
	/** 专题上下架情况 0:下架 1:上架 */
	private Integer onshelves;


	public Long getTopicid() {
		return topicid;
	}

	public void setTopicid(Long topicid) {
		this.topicid = topicid;
	}

	public Integer getOnshelves() {
		return onshelves;
	}

	public void setOnshelves(Integer onshelves) {
		this.onshelves = onshelves;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public Integer getShelvestype() {
		return shelvestype;
	}

	public void setShelvestype(Integer shelvestype) {
		this.shelvestype = shelvestype;
	}


					
}
