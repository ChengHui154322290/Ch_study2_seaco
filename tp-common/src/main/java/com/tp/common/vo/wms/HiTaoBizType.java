package com.tp.common.vo.wms;

import org.apache.commons.lang.StringUtils;


/**
 * 海淘集货备货模式
 * <pre>
 * 0=未知 
 * 1=备货
 * 2=集货
 * </pre>
 * @author beck
 *
 */
public enum HiTaoBizType {
	UNKOWN("0","未知"),
	PRESTOCK("1","备货"),
	COLLSTOCK("2","集货");
	
	HiTaoBizType(String code,String desc){
		this.code = code;
		this.desc = desc;
	}
	
	private String code;
	
	private String desc;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public static String getDescByCode(String code){
		if(StringUtils.isBlank(code)){
			return null;
		}
		HiTaoBizType[] values = HiTaoBizType.values();
		for (HiTaoBizType item : values) {
			if(item.getCode().equals(code)){
				return item.getDesc();
			}
		}
		return null;
	}

}
