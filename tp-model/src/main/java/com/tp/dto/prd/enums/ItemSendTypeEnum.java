package com.tp.dto.prd.enums;

import org.apache.commons.lang.StringUtils;



/***
 * 海淘商品 配送方式枚举类
 * @author szy
 *
 */
public enum ItemSendTypeEnum {
	

	DT("DT","海外直购"),
	GN("GN","国内直发"),
	BA("BA","保税区直发");
	private String code;
	
	private String desc;
	
	ItemSendTypeEnum(String code,String desc){  
		this.code=code;
		this.desc=desc;
	}

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
		ItemSendTypeEnum[] values = ItemSendTypeEnum.values();
		for (ItemSendTypeEnum item : values) {
			if(item.getCode().equals(code)){
				return item.getDesc();
			}
		}
		return null;
	}

}
