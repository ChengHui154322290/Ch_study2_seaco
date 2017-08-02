package com.tp.common.vo.ord;

/**
 * 订单状态日志 常量
 * 
 * @author szy
 */
public interface OrderStatusLogConstant {
	/**
	 * 
	 * <pre>
	 * 订单状态日志类型
	 * </pre>
	 * 
	 * @author szy
	 */
	public enum LOG_TYPE {
		TRACKING(1, "跟踪"), 
		MONITOR(2, "监控"),
		ERROR(3, "错误");
		
		public Integer code;
		public String cnName;

		public static String getCnName(Integer code) {
			if (code != null) {
				for (LOG_TYPE entry : LOG_TYPE.values()) {
					if (entry.code.intValue() == code) {
						return entry.cnName;
					}
				}
			}
			return null;
		}

		LOG_TYPE(Integer code, String cnName) {
			this.code = code;
			this.cnName = cnName;
		}

		public Integer getCode() {
			return code;
		}

		public void setCode(Integer code) {
			this.code = code;
		}

		public String getCnName() {
			return cnName;
		}

		public void setCnName(String cnName) {
			this.cnName = cnName;
		}
	}

}
