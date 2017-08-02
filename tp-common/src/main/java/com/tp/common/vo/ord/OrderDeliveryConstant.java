package com.tp.common.vo.ord;

/**
 * 订单物流状态 常量
 */
public interface OrderDeliveryConstant {
	/**
	 * 
	 * <pre>
	 * 订单物流推送给快递100平台状态
	 * </pre>
	 * 
	 * @author szy
	 */
	public enum postKuaidi100Status {
		POST_FALSE(0, "失败"), POST_TRUE(1, "成功");
		public Integer code;
		public String cnName;

		public static String getCnName(Integer code) {
			if (code != null) {
				for (postKuaidi100Status entry : postKuaidi100Status.values()) {
					if (entry.code.intValue() == code) {
						return entry.cnName;
					}
				}
			}
			return null;
		}

		postKuaidi100Status(Integer code, String cnName) {
			this.code = code;
			this.cnName = cnName;
		}
	}

	/**
	 * 
	 * <pre>
	 * 物流订单类型
	 * </pre>
	 * 
	 * @author szy
	 */
	public enum deliveryOrderType {
		ORDER(0, "订购"), REJECT(1, "退货");
		public Integer code;
		public String cnName;

		public static String getCnName(Integer code) {
			if (code != null) {
				for (deliveryOrderType entry : deliveryOrderType.values()) {
					if (entry.code.intValue() == code) {
						return entry.cnName;
					}
				}
			}
			return null;
		}

		deliveryOrderType(Integer code, String cnName) {
			this.code = code;
			this.cnName = cnName;
		}
	}
}
