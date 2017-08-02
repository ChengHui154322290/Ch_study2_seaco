package com.tp.enums.common;

/**
 * 来源枚举类
 * @author szy
 * @2016年1月8日 下午4:47:59
 */
public enum SourceEnum {
	XG(1,"西客"),
	SHOP(2, "第三方商城");
	public Integer code;
	
    public String desc;

	private SourceEnum(Integer code, String desc) {
		this.code = code;
		this.desc = desc;
	}
}
