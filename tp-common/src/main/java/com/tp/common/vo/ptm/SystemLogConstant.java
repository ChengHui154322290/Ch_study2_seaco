package com.tp.common.vo.ptm;

/**
 * 常量
 * 
 * @author haisheng.Long 2015-05-07 14:08:56
 */
public interface SystemLogConstant {

	/**
	 * 日志类型
	 * 
	 * @author 项硕
	 * @version 2015年5月7日 下午2:13:15
	 */
	public enum LogType {
		/** 订单-1 */
		ORDER(1, "订单"),
		/** 仓库-2 */
		STORAGE(2, "仓库");

		public final Integer code;
		public final String cnName;
		
		private LogType(Integer code, String cnName) {
			this.code = code;
			this.cnName = cnName;
		}

		public static String getCnName(Integer code) {
			if (code != null) {
				for (LogType entry : LogType.values()) {
					if (entry.code.intValue() == code) {
						return entry.cnName;
					}
				}
			}
			return null;
		}
	}

	/**
	 * 日志级别
	 * 
	 * @author 项硕
	 * @version 2015年5月7日 下午2:13:15
	 */
	public enum LogLevel {
		/** INFO-1 */
		INFO(1, "INFO"),
		/** ERROR-2 */
		ERROR(2, "ERROR");
		
		public final Integer code;
		public final String cnName;
		
		private LogLevel(Integer code, String cnName) {
			this.code = code;
			this.cnName = cnName;
		}
		
		public static String getCnName(Integer code) {
			if (code != null) {
				for (LogLevel entry : LogLevel.values()) {
					if (entry.code.intValue() == code) {
						return entry.cnName;
					}
				}
			}
			return null;
		}
	}

	/**
	 * 日志状态
	 * 
	 * @author 项硕
	 * @version 2015年5月7日 下午2:13:15
	 */
	public enum LogStatus {
		/** INFO-1 */
		UNDO(1, "未处理"),
		/** ERROR-2 */
		DONE(2, "已处理");
		
		public final Integer code;
		public final String cnName;
		
		private LogStatus(Integer code, String cnName) {
			this.code = code;
			this.cnName = cnName;
		}
		
		public static String getCnName(Integer code) {
			if (code != null) {
				for (LogStatus entry : LogStatus.values()) {
					if (entry.code.intValue() == code) {
						return entry.cnName;
					}
				}
			}
			return null;
		}
	}
}
