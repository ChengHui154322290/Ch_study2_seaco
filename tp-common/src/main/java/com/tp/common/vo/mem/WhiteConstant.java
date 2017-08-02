package com.tp.common.vo.mem;


/**
 * 白名单管理常量
 * @author zhuss
 * @2016年4月7日 下午2:43:23
 */
public interface WhiteConstant {
	
	public static final Integer DEFAULT_APP_PUSH_PAGE_SIZE = 20;
	public static final Integer DEFAULT_APP_PUSH_PAGE_ID = 1;
	
	public enum ERROR_CODE{
		SUCCESS(200,"success"),
		FAILED(400,"failed");
		public int code;
		public String message;
		ERROR_CODE(int code,String message){
			this.code = code;
			this.message = message;
		}
	}
	
	//状态
	public enum STATUS {
		NO(0, "冻结"), YES(1, "正常");
		private int code;
		private String desc;

		STATUS(int code, String desc) {
			this.code = code;
			this.desc = desc;
		}
		public static String getDesc(Integer code){
			for(STATUS entry:STATUS.values()){
				if(entry.code ==code){
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
	
	// 级别
	public enum LEVEL {
		SVIP(1,"SVIP"),
		KVIP(2,"卡VIP");
		private int code;
		private String desc;

		LEVEL(int code, String desc) {
			this.code = code;
			this.desc = desc;
		}

		public static String getDesc(Integer code) {
			for (LEVEL entry : LEVEL.values()) {
				if (entry.code == code) {
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
}
