package com.tp.common.vo.app;

import com.tp.enums.common.PlatformEnum;

/**
 * APP版本管理常量
 * @author zhuss
 * @2016年4月7日 下午2:43:23
 */
public interface VersionConstant {
	
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
	
	//版本状态
	public enum VERSION_STATUS {
		NO(0, "冻结"), YES(1, "发布");
		private int code;
		private String desc;

		VERSION_STATUS(int code, String desc) {
			this.code = code;
			this.desc = desc;
		}
		public static String getDesc(Integer code){
			for(VERSION_STATUS entry:VERSION_STATUS.values()){
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
	
	// 版本平台
	public enum VERSION_PLATFORM {
		IOS(PlatformEnum.IOS.code, PlatformEnum.IOS.name()),
		ANDROID(PlatformEnum.ANDROID.code, PlatformEnum.ANDROID.name());
		private int code;
		private String desc;

		VERSION_PLATFORM(int code, String desc) {
			this.code = code;
			this.desc = desc;
		}

		public static String getDesc(Integer code) {
			for (VERSION_PLATFORM entry : VERSION_PLATFORM.values()) {
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
