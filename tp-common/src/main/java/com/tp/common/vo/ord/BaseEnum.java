package com.tp.common.vo.ord;

/**
 * 基础枚举接口<br>
 * 所有常量型枚举都建议实现该接口，以统一管理
 * 
 * @author szy
 * @version 0.0.1
 */
public interface BaseEnum {

	/**
	 * 是否包含该数值
	 * 
	 * @param code
	 * @return
	 */
	boolean contains(int code);

	/**
	 * 
	 * <pre>
	 * 是否
	 * </pre>
	 * 
	 * @author szy
	 * @time 2015-4-13 下午2:21:57
	 */
	public enum WHETHER {
		NO(0, "否"), YES(1, "是");

		public Integer code;
		public String cnName;

		WHETHER(Integer code, String cnName) {
			this.code = code;
			this.cnName = cnName;
		}
	}
}
