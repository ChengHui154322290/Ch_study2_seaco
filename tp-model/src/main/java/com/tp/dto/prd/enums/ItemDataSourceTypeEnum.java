package com.tp.dto.prd.enums;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * <pre>
 * 商品数据来源类型
 * </pre>
 *
 * @author szy
 */
public enum ItemDataSourceTypeEnum {
	
	
	SELLER("SELLER","商家平台");
	private String code;
	
	private String desc;
	
	ItemDataSourceTypeEnum(String code,String desc){  
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
		ItemDataSourceTypeEnum[] values = ItemDataSourceTypeEnum.values();
		for (ItemDataSourceTypeEnum item : values) {
			if(item.getCode().equals(code)){
				return item.getDesc();
			}
		}
		return null;
	}

	
}
