package com.tp.common.vo.wx;


/**
 * 微信消息常量类
 * @author zhuss
 */
public interface MenuConstant {

	//级别
	public enum LEVEL{
		PARENT(1, "一级菜单"),
		CHILD(2, "二级菜单");
		private int code;
		private String desc;

		LEVEL(int code, String desc) {
			this.code = code;
			this.desc = desc;
		}
		
		public static String getDesc(int code){
			for(LEVEL entry:LEVEL.values()){
				if(entry.code == code){
					return entry.desc;
				}
			}
			return null;
		}
		
		public int getCode() {
			return code;
		}

		public String getDesc() {
			return desc;
		}
	}
	
	// 类型
	public enum TYPE {
		CLICK("click", "点击菜单拉取消息"), 
		VIEW("view", "点击菜单跳转链接");
		private String code;
		private String desc;

		TYPE(String code, String desc) {
			this.code = code;
			this.desc = desc;
		}
		
		public static String getDesc(String code) {
			for (TYPE entry : TYPE.values()) {
				if (entry.code.equalsIgnoreCase(code) ) {
					return entry.desc;
				}
			}
			return null;
		}

		public String getCode() {
			return code;
		}

		public String getDesc() {
			return desc;
		}
	}
}
