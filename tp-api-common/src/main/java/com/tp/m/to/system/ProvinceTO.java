package com.tp.m.to.system;

import com.tp.m.base.BaseTO;

/**
 * 省
 * @author zhuss
 */
public class ProvinceTO implements BaseTO{

	private static final long serialVersionUID = 7530038414874620898L;

	private String province;
	private String provcode;
	
	public ProvinceTO() {
		super();
	}
	public ProvinceTO(String province, String provcode) {
		super();
		this.province = province;
		this.provcode = provcode;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getProvcode() {
		return provcode;
	}
	public void setProvcode(String provcode) {
		this.provcode = provcode;
	}
}
