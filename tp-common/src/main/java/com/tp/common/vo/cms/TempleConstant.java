package com.tp.common.vo.cms;

/**
 * 常量
 * 
 */
public interface TempleConstant {

	/** 模板参数 */
	final static String CMS_TEMPLE_TYPE_INDEX = "0";
	final static String CMS_TEMPLE_TYPE_ZHUANC = "1";
	
	/** 模板路径 */
	final static String CMS_TEMPLE_PATH = "/flt/index";
	
	/** 后台管理上传模板路径 */
	final static String CMS_SINGLE_TEMPLE_PATH = "";
	
	/***************************************** 启用/停用类型 **************************************/
	final static String USEING_TYPE = "0";//启用
	final static String FORBID_TYPE = "1";//禁用
	
	/***************************************** 公告资讯类型 **************************************/
	final static String CMS_FIX_ADVICE_INDEX_INFO = "1";//西客商城首页公告资讯
	final static String CMS_FIX_MAKERT_INDEX_INFO = "2";//西客商城首页市场资讯
	final static String CMS_FIX_ADVICE_SALE_INFO  = "3";//西客商城最后疯抢-公告资讯
	final static String CMS_FIX_MAKERT_SALE_INFO  = "4";//西客商城最后疯抢-市场资讯
	final static String HAITAO_INDEX_DEFINED_INFO = "5";//海淘首页-自定义区域
	final static String CMS_INDEX_DEFINED_INFO 	  = "6";//首页-自定义区域
	
	/***************************************** CMS后台模板管理类型 **************************************/
	final static String CMS_NOWDATE_SALE_TEMPLE = "4";//首页-今日特卖
	final static String CMS_SINGLE_SALE_TEMPLE = "10";//首页-单品团
	final static String CMS_TOMORROW_ADVANCE_TEMPLE  = "11";//明日预告
	final static String CMS_NOWDATE_HOARD_TEMPLE  = "12";//今日必海淘
	final static String CMS_LASTDATE_SALE_TEMPLE  = "13";//最后疯抢-今日特卖
	final static String HAITAO_RUSH_SALE_TEMPLE  = "15";//海淘首页-今日精选
	final static String CMS_INDEX_HOT_SELL  = "16";//西客商城首页-热销榜单
	final static String CMS_INDEX_TOPIC_DISCOUNT  = "17";//西客商城首页-品牌精选
	
	/***************************************** CMS模板图片类型 **************************************/
	final static String CMS_INDEX_CAROUSER_TEMPLE = "3"; //首页-轮播图
	final static String CMS_INDEX_RIGHT_TEMPLE = "5";	 //首页-右边-最优惠图片
	final static String CMS_LASTDATE_RIGHT_TEMPLE = "6"; //最后疯抢-右边-最优惠图片
	final static String CMS_INDEX_STATPIC_TEMPLE  = "7"; //首页-刚进入首页的弹出层
	final static String USER_INDEX_LOGINC_PICTURE = "8"; //用户管理-用户登录-图片
	final static String HAITAO_SPECIALSELL_PICTURE= "9"; //海淘首页-特卖精选-图片
	final static String CMS_INDEX_TOPIC_BPICTURE= "10"; //首页-顶端-大图
	final static String CMS_INDEX_TOPIC_SPICTURE= "11"; //首页-顶端-小图
	final static String CMS_INDEX_ALLOW_PICTURE= "12"; //首页-预留广告位
	final static String CMS_INDEX_FOOT_PICTURE= "13"; //首页-底部广告位
	final static String CMS_INDEX_RING_PICTURE= "14"; //首页-右边-广告位
	
	/***************************************** CMS模板图片类型(APP) **************************************/
	final static String APP_INDEX_ADVERT_TYPE = "101";//首页-广告位
	final static String APP_SECKILL_ADVERT_TYPE  = "102";//秒杀-广告位信息
	final static String APP_INDEX_FUNCTION_TYPE  = "103";//首页-功能标签信息
	final static String APP_GRAIN_ADVERT_TYPE  = "104";//海淘-广告位信息
	final static String APP_PULSE_ADVERT_TYPE  = "105";//广告-启动页面
	final static String APP_PAY_ADVERT_TYPE  = "106";//广告-支付成功
	final static String APP_WAPINDEX_ADVERT_TYPE  = "107";//wap-首页弹框
	final static String APP_WAP_CHOSEN_PICTURE  = "108";//Wap-今日精选-首图
	
	/***************************************** CMS后台模板管理类型(APP) **************************************/
	final static String APP_NOWDATE_SALE_TEMPLE = "101";//首页-今日特卖
	final static String APP_SINGLE_SALE_TEMPLE = "102";//首页-单品团
	final static String APP_TOMORROW_ADVANCE_TEMPLE  = "103";//明日预告
	final static String APP_NOWDATE_HOARD_TEMPLE  = "104";//今日必海淘
	final static String APP_LASTDATE_SALE_TEMPLE  = "105";//最后疯抢-今日特卖
	final static String APP_SECKILL_SINGLE_TEMPLE  = "206";//秒杀-单品团
	final static String APP_WAP_CHOSEN_SINGLE_TEMPLE  = "207";//WAP-今日精选-单品团
	final static String APP_WAP_CHOSEN_ACTIVITY_TEMPLE  = "208";//WAP-今日精选-专场团
	
	/***************************************** APP-图片-活动类型 **************************************/
	final static String APP_ADVERT_LINK_TYPE = "0";//超链接
	final static String APP_ADVERT_ACTITY_TYPE = "1";//专场id
	final static String APP_ADVERT_ALL_TYPE = "2";//专场id和商品id
	
	/***************************************** 分页默认参数 **************************************/
	final static String CMS_START_PAGE = "1";//起始页
	final static String CMS_PAGE_SIZE = "20";//开始默认页数大小
	
	/***************************************** CMS中记录的平台标识 **************************************/
	final static String CMS_PLATFORM_PC_TYPE = "0";//pc端
	final static String CMS_PLATFORM_APP_TYPE = "1";//app端
	
	/***************************************** 与促销接口对应的平台标识 **************************************/
	final static Integer PROMOTION_PLATFORM_ALL = 0;//全部平台
	final static Integer PROMOTION_PLATFORM_PC = 1;//PC端
	final static Integer PROMOTION_PLATFORM_APP = 2;//APP端
	final static Integer PROMOTION_PLATFORM_WAP = 3;//WAP
	final static Integer PROMOTION_PLATFORM_HAPPY = 4;//快乐孕期端
	final static Integer PROMOTION_PLATFORM_OTHER = 5;//其他
	final static Integer PROMOTION_PLATFORM_HAITAO = 6;//海淘
	
	/***************************************** 长期活动标识 **************************************/
	final static Integer PROMOTION_LASTTYPE_LONG = 0;//长期活动
	final static Integer PROMOTION_LASTTYPE_SORT = 1;//固定期限活动
	
	/***************************************** 国家对应的图片地址 **************************************/
	final static String CMS_IMG_JAPAN =    "/img/national_japan.png";//日本
	final static String CMS_IMG_ENGLAND =  "/img/national_british.png";//英国
	final static String CMS_IMG_GERMANY  = "/img/national_germany.png";//德国
	final static String CMS_IMG_AMERICA =  "/img/national_usa.png";//美国
	final static String CMS_IMG_KOREA =    "/img/national_korea.png";//韩国
	final static String CMS_IMG_AUSTRALIA ="/img/national_australia.png";//澳大利亚
	
	/**CMS服务消息队列名称 **/
	public final static String CMS_ADVERTISE_PUB_MSG = "cms_advertise_queue_p2p";//公告资讯以及图片广告管理
}
