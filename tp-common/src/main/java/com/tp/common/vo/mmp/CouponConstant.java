package com.tp.common.vo.mmp;

/**
 * 常量
 * 
 * @author szy
 */
public interface CouponConstant {

	/**
	 * 全部
	 */
	final String RANGE_ALL = "0";

	/**
	 * 自营 + 海淘
	 */
	final String RANGE_SELF = "1";

	/**
	 * 联营
	 */
	final String RANGE_JOINT = "2";
	
	/**
	 * 
	 * 有效
	 */
	final int COUPON_VALIDD = 0;
	
	/**
	 * 
	 * 无效
	 */
	final int COUPON_INVALIDD= 1;
	
	/** 时间有效 */
	final String COUPON_USE_TYPE_FIX = "0";
	
	/** 按获得日期 */
	final String COUPON_USE_TYPE_BY_GET = "1";
	/**用于商品*/
	final String  COUPON_USE_ITEM = "1";
	/**用于专场*/
	final String  COUPON_USE_TOPIC = "2";
	/**用于品牌*/
	final String  COUPON_USE_BRAND = "3";
	/**用于类别*/
	final String  COUPON_USE_CATEGORY = "4"; 
	/**卡券中心展示*/
	final String COUPON_SHOW_CENTER="2";
	/**卡券中心不展示*/
	final String COUPON_UN_SHOW_CENTER="1";
	
	
}
