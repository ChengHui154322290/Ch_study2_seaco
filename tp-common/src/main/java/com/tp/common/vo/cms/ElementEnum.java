package com.tp.common.vo.cms;


/**
 * 元素类型
 *
 */
public enum ElementEnum {

	ACTIVITY(1, "活动"),  			
	PICTURE(2, "图片"), 		
	WRITTEN(3,"文字"),   			
	DEFINED(4,"自定义编辑"),     		
	SEO(5,"SEO"), 				
	ANNOUNCE(6,"公告"), 				
	INFORMATION(7,"资讯"); 				
	
	private int type;
	private String description;
	
	private ElementEnum(int type, String description) {
		this.type = type;
		this.description = description;
	}
	
	public int getValue() {
		return this.type;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	//ElementEnum.WRITTEN.getValue());//取值，此处为3
}
