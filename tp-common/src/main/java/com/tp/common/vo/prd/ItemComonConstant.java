package com.tp.common.vo.prd;



/***
 * 商品详情状态,颜色，尺寸常量类
 * @author szy
 *
 */
public class ItemComonConstant {
	
	/**商品默认均码***/
	public final static String ITEM_DEFAULT_SIZE = "-1"; 
	
	/**商品颜色默认均码**/
	public final static String ITEM_DEFAULT_COLOR = "-1";
	public final static Integer ITEM_GROUP_STATUS_ENABLE=1;
	public final static Integer ITEM_GROUP_STATUS_ALL=2;
	/**长期活动**/
	public final static Integer ITEM_FRONT_ACTIVITY_FOREVER=0;
	/**活动还没开始**/
	public final static Integer ITEM_FRONT_ACTIVITY_NOW=1;
	/**设置活动超时**/
	public final static String ITEM_FRONT_ACTIVITY_OVERTIME="1";
	/**设置活动还未开始**/
	public final static String ITEM_FRONT_ACTIVITY_NOTSTART="1";
	/**库存为0 无货**/
	public final static String ITEM_OUT_OF_STOCK="1";
	/**商品下架状态***/
	public final static String ITEM_UNDERCARRIAGE = "0";
	/**商品作废***/
	public final static String ITEM_NO_USE = "2";
	
	/**商品备货中***/
	public final static String TOPIC_BACKORDERED="7";
	/**长期活动 不需要倒计时***/
	public final static String ITEM_FOEVER_CUTDOWN = "0";
	/**错误跳转**/
	public final static String ITEM_TOPIC_ERROR = "1";
	/**没有折扣**/
	public final static String ITEM_NO_DISCOUNT= "1";
	/**单品团**/
	public final static String ITEM_TOPIC_SINGLE= "1";
	/**正常团**/
	public final static String ITEM_TOPIC_TOPIC= "3";
	/**自定义属性类型**/
	public final static Integer ITEM_ATTR_CUSTOM= 1;
	/**按全场计费***/
	public final static Integer ITEM_FREIGHT_ALL= 0;
	/**按单品计费***/
	public final static Integer ITEM_FREIGHT_SINGLE= 1;
	/**均码商品**/
	public final static long  ITEM_AVE_SPEC= -1;
	/**美团观点状态 0 显示**/
	public final static Integer VIEWPOINT_SHOW= 0;
	/***海淘商品页面判断标示符**/
	public final static String  ITEM_HAI_TAO= "1";
	/**既有立即购买 也有加入购物车**/
	public final static String ITEM_PAGE_BOTH_BUTTON= "BOTH";
	/**只有立即购买**/
	public final static String ITEM_PAGE_ONLY= "ONLY";
}
