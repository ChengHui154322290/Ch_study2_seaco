package com.tp.dto.prd.enums;


/**
 * 
 * <pre>
 * 商品审核结果枚举类
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 */
public enum AuditResultEnum {

	BASE_INFO("基础资料",1), 
	ITEM_PIC("商品图片",2), 
	ITEM_DETAIL("商品详情",3),
	ITEM_ATTR("商品属性",4),
	NOT_TARGET_ITEM("非目标商品",5);

	private String key;

	private Integer value;

	private AuditResultEnum(String key, Integer value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public static void main(String[] args){
		String keyByValue = AuditResultEnum.getKeyByValue(8);
		System.out.println(keyByValue);
		
	}
	
	public static String getKeyByValue(Integer value){
		if(null==value){
			return null;
		}
		 AuditResultEnum[] values = AuditResultEnum.values();
		 for (AuditResultEnum auditResultEnum : values) {
			 if(auditResultEnum.value.equals(value)){
				 return auditResultEnum.getKey();
			 }
		 }
		return null;	
	}
	
}
