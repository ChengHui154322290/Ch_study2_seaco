package com.tp.common.vo.cms;

/**
 * 功能名对应图片尺寸大小
 * 注意：1.所有的广告是不需要图片压缩，直接上原图。
 * 	   2.APP是在APP那边做处理。CMS直接返回原生态的图片地址。
 */
public interface PictureSizeConstant {

	/**
	 * 非海淘
	 */
	/*final static Integer LOGO 	 	  					  = 100;//logo图标*/
	final static Integer INDEX_SINGLE_DAILY_STORE 	 	  = 278;//首页-每日必海淘
	final static Integer INDEX_DISCOUNT_BRANCH_LIST 	  = 520;//首页-今日特卖-品牌团
	final static Integer INDEX_TRAILER_ADVANCE 	  		  = 378;//首页-即将上新
	final static Integer INDEX_ITEM_PICTURE 	  	      = 275;//首页-品牌团-商品图片
	final static Integer INDEX_INTEREST_PICTURE 	  	  = 378;//品牌团,主题团-感兴趣
	
	/**
	 * 海淘
	 */
	final static Integer HAITAO_ACTIVITY_IMAGE 	  = 576;//商城-大牌精选
	
	/**
	 * 商城
	 */
	final static Integer MALL_BIG_CARE_SELECT 	  = 576;//商城-大牌精选
	final static Integer MALL_TODAY_TOPIC_LIST 	  = 576;//商城-每日专场
	final static Integer MALL_ITEM_PICTURE 	  	  = 275;//商城-品牌团-商品图片
	
}
