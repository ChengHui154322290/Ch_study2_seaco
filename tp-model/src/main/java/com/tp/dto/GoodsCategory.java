package com.tp.dto;

import java.util.List;

public class GoodsCategory {
	private static final long serialVersionUID = -2917786466159222875L;

	private Long id;
	
	/**
	 * 类别名称 唯一
	 */
	private String name;
	/**
	 * 上级类别id
	 */
	private long parent;
	/**
	 * 类别编号唯一 编号规则
	 * 
	 * <pre>
	 * 备件一级：起始 0000 最大值-0999
	 * 备件二级：起始 0000-0000 0000-9999 0999-9999
	 * 物料一级：1000-1999
	 * 电器城一级：2000-2999
	 * 生活用品一级：3000-3999
	 * 租赁买卖一级：4000-4999
	 * 船舶维修一级：5000-5999
	 * 物流订舱一级：6000-6999
	 * 航运保险一级：7000-7999
	 * ....
	 * </pre>
	 */
	private String codeNum;
	/**
	 * 类别级别
	 */
	private int level;

	/**
	 * 类别的下级类别
	 */
	private List<GoodsCategory> subGoodsCategorys;
	
	public GoodsCategory() {
		super();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getParent() {
		return parent;
	}

	public void setParent(long parent) {
		this.parent = parent;
	}

	public List<GoodsCategory> getSubGoodsCategorys() {
		return subGoodsCategorys;
	}

	public void setSubGoodsCategorys(List<GoodsCategory> subGoodsCategorys) {
		this.subGoodsCategorys = subGoodsCategorys;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getCodeNum() {
		return codeNum;
	}

	public void setCodeNum(String codeNum) {
		this.codeNum = codeNum;
	}

}
