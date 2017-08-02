/**
 * 
 */
package com.tp.common.vo.mmp;

/**
 * @author szy
 *
 */
public interface TopicRedisKeyConstant {

	/** Redis 存放活动信息RKey的前缀 */
	final static String PR_PROMOTION_PREFIX = "pro_key_";

	/** Redis 存放活动商品信息RKey的前缀 */
	final static String PR_PROMOTION_ITEM_PREFIX = "proItem_key_";

	/** Redis 存放活动Id列表的RKey */
	final static String PR_PROMOTION_KEYS = "promotion_keys";

	/** Redis 存放活动商品Id列表的RKey */
	final static String PR_PROMOTION_ITEM_KEYS = "promotion_item_keys";

	/** Redis 提供商品详情页获取专场活动商品与活动商品信息关系的RKey */
	final static String PR_FOR_ITEM_KEYS = "for_item_keys";

	/** Redis 存放有效活动(状态为审核通过)Id列表的RKey */
	final static String PR_PROMOTION_AVALIABLE_KEYS = "promotion_avaliable_keys";

	/** Redis 存放已丢失活动Id列表的RKey */
	final static String PR_PROMOTION_LOST_KEYS = "promotion_lost_keys";

	/** Redis 存放已丢失活动商品Id列表的RKey */
	final static String PR_PROMOTION_ITEM_LOST_KEYS = "promotion_item_lost_keys";

	/** Redis 存放最后疯抢Id列表的RKey */
	final static String PR_PROMOTION_LASTRASH_KEYS = "promotion_lastrash_keys";

	/** Redis 存放所有平台最后疯抢Id列表的RKey */
	final static String PR_PROMOTION_ALL_LASTRASH_KEYS = "promotion_all_lastrash_keys";

	/** Redis 存放PC平台最后疯抢Id列表的RKey */
	final static String PR_PROMOTION_PC_LASTRASH_KEYS = "promotion_pc_lastrash_keys";

	/** Redis 存放APP平台最后疯抢Id列表的RKey */
	final static String PR_PROMOTION_APP_LASTRASH_KEYS = "promotion_app_lastrash_keys";

	/** Redis 存放WAP平台最后疯抢Id列表的RKey */
	final static String PR_PROMOTION_WAP_LASTRASH_KEYS = "promotion_wap_lastrash_keys";

	/** Redis 存放快乐孕期平台最后疯抢Id列表的RKey */
	final static String PR_PROMOTION_HAPPY_LASTRASH_KEYS = "promotion_happy_lastrash_keys";

	/** Redis 存放所有区域最后疯抢Id列表的RKey */
	final static String PR_PROMOTION_ALL_AREA_LASTRASH_KEYS = "promotion_all_area_lastrash_keys";

	/** Redis 存放华东区域最后疯抢Id列表的RKey */
	final static String PR_PROMOTION_EC_AREA_LASTRASH_KEYS = "promotion_ec_area_lastrash_keys";

	/** Redis 存放华北区域最后疯抢Id列表的RKey */
	final static String PR_PROMOTION_NC_AREA_LASTRASH_KEYS = "promotion_nc_area_lastrash_keys";

	/** Redis 存放华中区域最后疯抢Id列表的RKey */
	final static String PR_PROMOTION_CC_AREA_LASTRASH_KEYS = "promotion_cc_area_lastrash_keys";

	/** Redis 存放华南区域最后疯抢Id列表的RKey */
	final static String PR_PROMOTION_SC_AREA_LASTRASH_KEYS = "promotion_sc_area_lastrash_keys";

	/** Redis 存放东北区域最后疯抢Id列表的RKey */
	final static String PR_PROMOTION_NE_AREA_LASTRASH_KEYS = "promotion_ne_area_lastrash_keys";

	/** Redis 存放西南区域最后疯抢Id列表的RKey */
	final static String PR_PROMOTION_SW_AREA_LASTRASH_KEYS = "promotion_sw_area_lastrash_keys";

	/** Redis 存放西北区域最后疯抢Id列表的RKey */
	final static String PR_PROMOTION_NW_AREA_LASTRASH_KEYS = "promotion_nw_area_lastrash_keys";

	/** Redis 存放港澳台区域最后疯抢Id列表的RKey */
	final static String PR_PROMOTION_HMT_AREA_LASTRASH_KEYS = "promotion_hmt_area_lastrash_keys";
	
	/** Redis 存放西客商城商城品牌的列表KEY */
	final static String PR_PROMOTION_XG_BRAND_KEYS = "promotion_xg_brand_keys";
	
	/** Redis 存放西客商城商城活动的列表KEY */
	final static String PR_PROMOTION_XG_TOPIC_KEYS = "promotion_xg_topic_keys";
	
	/** Redis 存放西客商城商城活动的列表KEY */
	final static String PR_PROMOTION_XG_TOPIC_KEY_LIST_KEYS = "promotion_xg_topic_key_list_keys";
	
	/** Redis 存放西客商城商城活动商品的列表KEY */
	final static String PR_PROMOTION_XG_TI_KEYS = "promotion_xg_ti_keys";
	
	/** Redis 存放西客商城商品信息的列表KEY */
	final static String PR_PROMOTION_BBT_TI_KEYS = "promotion_bbt_ti_keys";
	
	/** 默认活动相关信息过期时间 */
	final static Integer DAYS_EXPIRE = 60 * 60 * 3;

	/** 最后疯抢过期时间 */
	final static Integer RASH_EXPIRE = 60 * 60 * 25;

	/** 活动筛选信息过滤结果缓存过期时间 */
	final static Integer COMMON_EXPIRE = 60;
	
	/** 活动信息拼接分隔符(目前用于拼接TopicId和SKU) */
	final static String TOPIC_INFO_SPLIT = "-";
}
