package com.tp.m.enums;

/**
 * 专题枚举
 * @author zhuss
 * @2016年1月2日 下午4:10:32
 */
public interface TopicEnum {

	public enum Status{
		NORMAL("1","正常"),
		INVALID("2","无效"),
		NO_START("3","未开始"),
		NO_END("4","已结束");
		public String code;
		
	    public String desc;

		private Status(String code, String desc) {
			this.code = code;
			this.desc = desc;
		} 
		
	}
}
