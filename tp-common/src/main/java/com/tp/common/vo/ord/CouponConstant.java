package com.tp.common.vo.ord;
/**
* 优惠券 常量
* @author szy
*/
public interface CouponConstant {
    
 /** 优惠券：0  现金券， 1-全场，2-自营和代销，3-平台,4  品类券*/    
	final static int COUPON_CASH = 0;//现金券
    final static int COUPON_WHOLE = 1;//全场券
    final static int COUPON_OWN = 2;//只在自营和代销使用券
    final static int COUPON_BUSINESS = 3;//只在第三方使用券
    final static int COUPON_CATEGORY = 4;//品类券
}
