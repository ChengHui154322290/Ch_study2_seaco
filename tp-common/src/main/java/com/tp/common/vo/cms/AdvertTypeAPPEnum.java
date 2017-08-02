package com.tp.common.vo.cms;


/**
 * 元素类型
 * @author szy
 *
 */
public enum AdvertTypeAPPEnum {

	LINK("0", "超链接"), 
	ACTIVITYID("1", "专场id"),  			
	ACT_ITEMSKU("2", "专场id和商品sku"), 		
	SKILL("3","秒杀"),   			
	SHOPINGCAT("4","购物车"),     		
	LOGIN("5","登录"), 				
	REGISTER("6","注册"), 
	COUPON("7","优惠券"),
	REDPAPER("8","红包"),
	ORDER("9","全部订单"),
	CUSTOMER("10","售后"),
	MINE("11","个人中心"),
	SIGN("12","签到"),
	OFFLINE_GROUP("13","线下团购"),
	SEARCH("14","搜索"),
	BRANDID("15","品牌"),
	CATEGORYID("16","分类"),
	COUPON_CENTER("17","优惠券领取中心")
	; 			
	
	private String type;
	private String description;
	
	private AdvertTypeAPPEnum(String type, String description) {
		this.type = type;
		this.description = description;
	}
	
	public String getValue() {
		return this.type;
	}
	
	public String getDescription() {
		return this.description;
	}
	
}
