package com.tp.m.to.product;

import com.tp.m.base.BaseTO;


/**
 * 规格详情
 * @author zhuss
 * @2016年1月5日 下午3:27:53
 */
public class SpecGroupDetailTO implements BaseTO{

	private static final long serialVersionUID = -6929771876886609436L;

	private String specid;
	private String specname;
	
	
	public SpecGroupDetailTO() {
		super();
	}
	public SpecGroupDetailTO(String specid, String specname) {
		super();
		this.specid = specid;
		this.specname = specname;
	}
	public String getSpecid() {
		return specid;
	}
	public void setSpecid(String specid) {
		this.specid = specid;
	}
	public String getSpecname() {
		return specname;
	}
	public void setSpecname(String specname) {
		this.specname = specname;
	}
}
