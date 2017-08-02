package com.tp.common.vo.mmp;

;

/**
 * 促销商品 常量
 * 
 * @author szy
 */
public interface TopicItemConstant {

	/** 批量输入 列分隔符 */
	public static String TOPIC_ITEM_BATCH_INPUT_SPILIT_SYMBOL = "	";

	/** 批量输入 行分隔符 Window回车 */
	public static String TOPIC_ITEM_BATCH_INPUT_LINE_SPILIT_SYMBOL_WIN = "\r\n";

	/** 批量输入 行分隔符UNIX回车 */
	public static String TOPIC_ITEM_BATCH_INPUT_LINE_SPILIT_SYMBOL_UNIX = "\r";

	/** 批量输入 数据缓存主键分隔符 */
	public static String TOPIC_ITEM_BATCH_INPUT_KEY_SPILIT = "_-_";

	/** 图片尺寸 1*1 */
	public static int TOPIC_ITEM_PIC_SIZE_1_M_1 = 1;

	/** 图片尺寸 2*2 */
	public static int TOPIC_ITEM_PIC_SIZE_2_M_2 = 2;

	/** 图片尺寸 4*4 */
	public static int TOPIC_ITEM_PIC_SIZE_4_M_4 = 4;

	/** 图片是否为主图 0-否 1-是 */
	int TOPIC_ITEM_PICTURE_IS_MAIN = 1;

	/** 图片是否为主图 0-否 1-是 */
	int TOPIC_ITEM_PICTURE_NOT_MAIN = 0;
	
	/** 活动商品锁定状态 - 未锁定*/
	int TOPIC_ITEM_LOCK_STATUS_UNLOCK = 0;
	
	/** 活动商品锁定状态 - 锁定*/
	int TOPIC_ITEM_LOCK_STATUS_LOCK = 1;
}
