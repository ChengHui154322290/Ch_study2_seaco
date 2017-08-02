package com.tp.common.vo.ord;


/**
 * 常量
 * 
 * @author szy
 */
public interface CartConstant {

	/** 最大购物车行数 */
	int MAX_LINE_QUANTITY = 99;
	/** 最大商品购买数 */
	int MAX_SKU_QUANTITY = 99;
	
	/** 有效 */
	int CART_STATUS_0 = 0;
	/** 库存不足 */
	int CART_STATUS_1 = 1;
	/** 已下架 */
	int CART_STATUS_2 = 2;
	/** 超过限购 */
	int CART_STATUS_3 = 3;
	
	/** 商品状态  '0-未上架 1-上架 2-作废'*/
	int ITEM_STATUS_0 = 0;
	int ITEM_STATUS_1 = 1;
	int ITEM_STATUS_2 = 2;
	
	/**最小购买数量 */
	int MIN_SKU_QUANTITY = 1;
	
	/** 类型 - 普通 */
	int TYPE_COMMON = 1;
	/** 类型 - 海淘 */
	int TYPE_SEA = 2;
	
	/**
	 * 显示购物车TAB
	 * 
	 * @author szy
	 * @version 0.0.1
	 */
	public enum ShowCartTab implements BaseEnum {
		/** 1 - 西客商城购物车*/
		COMMON(1, "COMMON"),
		/** 2 - 海淘购物车 */
		SEA(2, "SEA"),
		/** 3 - 全部购物车 */
		ALL(3, "ALL");
		
		public Integer code;
		public String cnName;
		
		ShowCartTab(Integer code, String cnName) {
			this.code = code;
			this.cnName = cnName;
		}
		
		public static String getCnName(Integer code) {
			for (ShowCartTab entry : ShowCartTab.values()) {
				if (entry.code.equals(code)) {
					return entry.cnName;
				}
			}
			return null;
		}

		@Override
		public boolean contains(int code) {
			for (ShowCartTab entry : values()) {
				if (entry.code.equals(code)) {
					return true;
				}
			}
			return false;
		}
	}
}
