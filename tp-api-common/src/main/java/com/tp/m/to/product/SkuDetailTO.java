package com.tp.m.to.product;

import com.tp.m.base.BaseTO;

/**
 * sku详情
 * @author zhuss
 * @2016年1月5日 下午3:20:20
 */
public class SkuDetailTO implements BaseTO{

	private static final long serialVersionUID = -6043038303220003867L;
	private String groupid;
	private String specid;
	
	
	public SkuDetailTO() {
		super();
	}
	public SkuDetailTO(String groupid, String specid) {
		super();
		this.groupid = groupid;
		this.specid = specid;
	}
	public String getGroupid() {
		return groupid;
	}
	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}
	public String getSpecid() {
		return specid;
	}
	public void setSpecid(String specid) {
		this.specid = specid;
	}
}
