package com.tp.common.vo.cms;

/**
 * 西客商城商城常量
 * 
 */
public interface PageTempletConstant {

	/**
	 * 页面编码：
		页面：商城	PG_MTSC
		模块：	商城首页导航栏					SC_NAVIGAT
				商城首页：目录栏				XGSC_CATALOG
				商城首页：左侧广告位			XGSC_LEFT_ADVERT
				商城首页：右侧广告位(220*213)	XGSC_RIGHT_ADVERT
				商城首页：右侧广告位  上边		XGSC_RIGHT_TOP_ADVERT
				商城首页：中间广告位 下边(配置的品牌)		XGSC_MIDDLE_TOP_ADVERT
				商城首页：中间广告位 上边（轮播图）		XGSC_MIDDLE_UND_ADVERT
				商城首页：大牌精选				实时查询
				商城首页：每日专场				实时查询
				商城首页：最下面广告位			XGSC_UND_ADVERT
				商城首页：自定义编辑			XGSC_DEFINED
		位置编码：暂时不需要，如果还需要细化的，可以在位置编码继续往下面排列
	 */
	final static String PG_XGSC 	 = "PG_XGSC";//页面-商城
	final static String XGSC_TOP_ADVERT  = "XGSC_TOP_ADVERT";//模块-商城首页：顶部广告位
	final static String XGSC_NAVIGAT = "XGSC_NAVIGAT";//模块-商城首页导航栏
	final static String XGSC_CATALOG = "XGSC_CATALOG";//模块-商城首页：目录栏
	final static String XGSC_LEFT_ADVERT  = "XGSC_LEFT_ADVERT";//模块-商城首页：左侧广告位
	final static String XGSC_RIGHT_ADVERT = "XGSC_RIGHT_ADVERT";//模块-商城首页：右侧广告位(220*213)
	final static String XGSC_RIGHT_TOP_ADVERT 	  = "XGSC_RIGHT_TOP_ADVERT";//模块-商城首页：右侧广告位  上边
	final static String XGSC_MIDDLE_TOP_ADVERT 	  = "XGSC_MIDDLE_TOP_ADVERT";//模块-商城首页：中间广告位 上边
	final static String XGSC_MIDDLE_UND_ADVERT 	  = "XGSC_MIDDLE_UND_ADVERT";//模块-商城首页：中间广告位 下边 
	final static String XGSC_UND_ADVERT 	  	  = "XGSC_UND_ADVERT";//模块-商城首页：最下面广告位
	final static String XGSC_DEFINED 	  	  	  = "XGSC_DEFINED";//商城首页：自定义编辑
	
}
