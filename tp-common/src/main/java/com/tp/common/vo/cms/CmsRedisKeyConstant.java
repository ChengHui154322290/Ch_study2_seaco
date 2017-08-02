package com.tp.common.vo.cms;

/**
 * @author szy
 *
 */
public interface CmsRedisKeyConstant {

	/** 默认首页图片，公告资讯相关信息过期时间 */
	final static Integer DAYS_EXPIRE = 86400;//60 * 60 * 24(24小时失效)

	/** Redis 存放首页图片，公告资讯管理的key值 */
	final static String CMS_ADVERT_PREFIX = "cms_advert_";//广告管理缓存key的前缀
	final static String CMS_INDEX_ADVERT_ALL = "advertAll";//首页-广告管理的全部更新
	final static String CMS_ADVERT_CAROUSEL = "3";//首页-最上面-轮播图
	final static String CMS_ADVERT_INDEX_PREFERENT = "5";//首页-右边-最优惠的加载
	final static String CMS_ADVERT_LASTRUSHPRE = "6";//最后疯抢-右边-最优惠加载
	final static String CMS_ADVERT_INDEX_POPLAYER = "7";//读取首页刚进去-弹出层
	final static String CMS_ADVERT_USER_LOGINPIC = "8";//用户管理-用户登录-图片(一张)
	
	final static String APP_INDEX_ADVERT_TYPE = "101";//首页-广告位(APP)
	final static String APP_SECKILL_ADVERT_TYPE = "102";//秒杀-广告位信息(APP)
	final static String APP_INDEX_FUNCTION_TYPE = "103";//首页-功能标签信息(APP)
	final static String APP_GRAIN_ADVERT_TYPE = "104";//海淘-广告位信息(APP)
	final static String APP_PULSE_ADVERT_TYPE = "105";//广告-启动页面(APP)
	final static String APP_PAY_ADVERT_TYPE = "106";//广告-支付成功(APP)
	final static String APP_WAPINDEX_ADVERT_TYPE = "107";//wap-首页弹框(APP)
	final static String APP_WAP_CHOSEN_PICTURE = "108";//Wap-今日精选-首图(APP)
	
	/** Redis 存放公告资讯值 */
	final static String CMS_INDEX_ANNOUNCE_ALL = "announceAll";//首页-公告资讯的全部更新
	final static String CMS_INDEX_ANNOUNCE = "cms_announce_1";//首页-公告资讯的加载，1表示资讯类型
	final static String CMS_INDEX_HAITAO_ANNOUNCE = "cms_announce_5";//海淘自定义区域的加载
	
	
}
