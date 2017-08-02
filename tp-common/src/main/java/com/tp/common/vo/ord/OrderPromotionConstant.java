package com.tp.common.vo.ord;;


 /**
 * 订单促销明细 常量
 * @author szy
 */
public interface OrderPromotionConstant{

	/**
	 * 促销类型
	 * 
	 * @author szy
	 * @version 0.0.1
	 */
	public enum PromotionType {

		/** 1 - 整单优惠 */
		ALL(1, "整单优惠"),
		/** 2 - 赠品 */
		GIFT(2, "赠品"),
		/** 3 - 包邮 */
		FREIGHT(3, "包邮");

		public Integer code;
		public String cnName;

		PromotionType(Integer code, String cnName) {
			this.code = code;
			this.cnName = cnName;
		}

		public static String getCnName(Integer code) {
			for (PromotionType entry : PromotionType.values()) {
				if (entry.code.equals(code)) {
					return entry.cnName;
				}
			}
			return null;
		}

	}
}
