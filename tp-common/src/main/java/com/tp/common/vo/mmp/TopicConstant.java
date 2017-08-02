package com.tp.common.vo.mmp;

;

/**
 * 促销活动 常量
 * 
 * @author szy
 */
public interface TopicConstant {

	/** 默认每页大小 */
	final int DEFAULT_PER_PAGE_SIZE = 30;

	/** 默认当前页数 */
	final int DEFAULT_PAGE_NO = 1;

	/** 类型 - 全部 */
	final int TOPIC_TYPE_ALL = 0;

	/** 类型 - 单品团 */
	final int TOPIC_TYPE_SINGLE = 1;

	/** 类型 - 品牌团 */
	final int TOPIC_TYPE_BRAND = 2;

	/** 类型 - 主题团 */
	final int TOPIC_TYPE_TOPIC = 3;

	/** 状态 - 编辑中 */
	final int TOPIC_STATUS_EDITING = 0;

	/** 状态 - 审批中 */
	final int TOPIC_STATUS_AUDITING = 1;

	/** 状态 - 已取消 */
	final int TOPIC_STATUS_CANCELED = 2;

	/** 状态 - 审核通过 */
	final int TOPIC_STATUS_AUDITED = 3;

	/** 状态 - 已驳回 */
	final int TOPIC_STATUS_REFUSED = 4;

	/** 状态 - 终止 */
	final int TOPIC_STATUS_TERMINATION = 5;

	/** 专题进度 - 未开始 */
	final int TOPIC_PROCESS_NOTSTART = 0;

	/** 专题进度 - 进行中 */
	final int TOPIC_PROCESS_PROCESSING = 1;

	/** 专题进度 - 已结束 */
	final int TOPIC_PROCESS_ENDING = 2;

	/** 是否支持商户 - 支持 */
	final int TOPIC_SUPPORTSUPPILER_SUPPORT = 0;

	/** 是否支持商户 - 不支持 */
	final int TOPIC_SUPPORTSUPPILER_UNSUPPORT = 1;

	/** 删除标志 - 正常 */
	final int TOPIC_DELETION_NORMAL = 0;

	/** 删除标志 - 删除 */
	final int TOPIC_DELETION_DELETE = 1;

	/** 持续时间 - 长期有效 */
	final int TOPIC_DURATIONTYPE_LONG = 0;

	/** 持续时间 - 固定期限 */
	final int TOPIC_DURATIONTYPE_FIX = 1;

	/** 是否支持商家提报 - 支持 */
	final int TOPIC_SUPPORTSUPPLIERINFO_SUPPORT = 0;

	/** 是否支持商家提报 - 不支持 */
	final int TOPIC_SUPPORTSUPPLIERINFO_UNSUPPORT = 1;

	/** 图片服务器地址替换占位符 */
	final String PROMOTION_IMG_SERVER = "[promotion.img.server]";
	
	/** 品牌状态 有效 */
	final Boolean BRAND_STATUS_VALID = true;

	/** 品牌状态 无效 */
	final Boolean BRAND_STATUS_INVALID = false;
	
	
	////////店铺分销专用//////////////////
	/** 分销店铺显示方式 - 单商品显示 */
	final int SHOPSHOWMODE_PRODUCT = 1;

	/** 分销店铺显示方式 - 商品、专题显示 */
	final int SHOPSHOWMODE_PRODUCT_TOPIC = 2;


	
	////////主题是否显示//////////////////
	/** 隐藏主题 */
	final int SHOWTOPIC_HIDE = 0;

	/** 显示主题 */
	final int SHOWTOPIC_SHOW = 1;
	/** 允许使用西客币 */
	final String CAN_USE_XG_MONEY="1";
	/** 不允许使用西客币 */
	final String CAN_NOT_USE_XG_MONEY="0";

}
