package com.tp.common.vo.ptm;



/**
 * 常量
 * 
 * @author haisheng.Long 2015-05-07 12:54:05
 */
public interface AccountConstant {

	/**
	 * 账号状态
	 * 
	 * @author 项硕
	 * @version 2015年5月7日 下午2:13:15
	 */
	public enum AccountStatus {
		/** 有效=1 */
		ENABLED(1, "有效"),
		/** 失效=0 */
		DISABLED(0, "失效"),
		/** 删除=-1 */
		DELETED(-1, "删除");

		public final Integer code;
		public final String cnName;
		
		private AccountStatus(Integer code, String cnName) {
			this.code = code;
			this.cnName = cnName;
		}

		public static String getCnName(Integer code) {
			if (code != null) {
				for (AccountStatus entry : AccountStatus.values()) {
					if (entry.code.intValue() == code) {
						return entry.cnName;
					}
				}
			}
			return null;
		}
	}
}
