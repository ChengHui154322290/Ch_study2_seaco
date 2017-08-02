package com.tp.common.vo.mkt;


/**
 * 渠道推广
 * @author zhuss
 * @2016年4月25日 下午5:14:42
 */
public interface ChannelPromoteConstant {

	// 状态
	public enum TYPE {
		PERSONAL(1, "个人"), COMPANY(2, "企业");
		private int code;
		private String desc;

		TYPE(int code, String desc) {
			this.code = code;
			this.desc = desc;
		}
		public static String getDesc(Integer code) {
			for (TYPE entry : TYPE.values()) {
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
	
	// 来源
	public enum SOURCE {
		WX(1, "微信");
		private int code;
		private String desc;

		SOURCE(int code, String desc) {
				this.code = code;
				this.desc = desc;
			}

		public static String getDesc(Integer code) {
			for (SOURCE entry : SOURCE.values()) {
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
