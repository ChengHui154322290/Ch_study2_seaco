package com.tp.common.vo.ord;

/**
 * 活动限购订单商品统计 常量
 * 
 * @author szy
 */
public interface TopicLimitItemConstant {

	/**
	 * 活动限购类型
	 * 
	 * @author szy
	 * @version 0.0.1
	 */
	public enum TopicLimitType implements BaseEnum {

		/** 1 - 用户限购 */
		USER(1, "USER"),
		/** 2 - IP限购 */
		IP(2, "IP"),
		/** 3 -收货人手机号限购 */
		MOBILE(3, "MOBILE");

		public Integer code;
		public String cnName;

		TopicLimitType(Integer code, String cnName) {
			this.code = code;
			this.cnName = cnName;
		}

		public static String getCnName(Integer code) {
			for (TopicLimitType entry : TopicLimitType.values()) {
				if (entry.code.equals(code)) {
					return entry.cnName;
				}
			}
			return null;
		}

		@Override
		public boolean contains(int code) {
			for (TopicLimitType entry : values()) {
				if (entry.code.equals(code)) {
					return true;
				}
			}
			return false;
		}

	}

}
