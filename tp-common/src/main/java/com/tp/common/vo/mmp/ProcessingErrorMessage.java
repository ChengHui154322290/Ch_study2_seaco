/**
 * 
 */
package com.tp.common.vo.mmp;

/**
 * @author szy
 *
 */
public interface ProcessingErrorMessage {

	/** 命名错误 */
	final static String VALID_TOPIC_INFO = "专场活动信息无效!";

	final static String INVALID_TOPIC_CHANGE_INFO = "专场活动变更单无效!";
	
	final static String SYNC_TOPIC_INFO_TO_REDIS_ERROR = "同步缓存数据异常!";

	/** 命名错误 */
	final static String VALID_TOPIC_ID = "指定的专场信息无效!";

	final static String INVALID_TOPIC_CHANGE_ID = "指定的活动变更单无效!";
	
	final static String INVALID_TOPIC_ITEM_INFO = "专场活动商品无效!";

	final static String MERGE_TOPIC_STATUS_ERROR = "待合并的活动状态异常";

	final static String TOPIC_CHANGE_IS_NOT_AUDITED = "导入活动未审批！";

	final static String END_TIME_LT_START_TIME = "专场活动结束时间小于当前时间!";

	final static String TOPIC_CHANGE_HAS_SAME_UNAUDITED = "存在相同未审批通过或未取消的活动变更单";

	final static String COPY_TOPIC_POLICY_FAILD = "复制专场策略信息失败!";

	final static String COPY_TOPIC_RELATE_FAILD = "复制关联专场信息失败!";

	final static String COPY_TOPIC_ITEM_FAILD = "复制专场商品信息失败!";
	
	final static String COPY_TOPIC_COUPON_FAILD = "复制优惠券信息失败!";

	final static String COPY_TOPIC_FAILD = "复制专场活动失败!";

	final static String SAVE_TOPIC_FAILD = "保存专场活动失败!";

	final static String SAVE_TOPIC_CHANGE_FAILD = "保存活动变更单失败!";
	
	final static String SAVE_TOPIC_ITEM_CHANGE_FAILD_WHEN_NEW_INVENTORY = "保存活动变更单失败,申请新活动商品库存失败";

	final static String CANCEL_TOPIC_FAILD = "取消专场活动失败!";

	final static String CANCEL_TOPIC_CHANGE_FAILD = "取消活动变更单失败!";

	final static String APPROVE_TOPIC_FAILD = "审批专场活动失败!";

	final static String APPROVE_TOPIC_CHANGE_FAILD = "审批活动变更单失败!";

	final static String APPROVE_TOPIC_CHANGE_STATUS_ERROR = "只有审核中的活动变更单才能审批";

	final static String TERMINATE_TOPIC_FAILD = "终止专场活动失败!";

	final static String REFUSE_TOPIC_FAILD = "驳回专场活动失败!";

	final static String REFUSE_TOPIC_CHANGE_FAILD = "驳回活动变更单失败!";

	final static String REFUSE_TOPIC_CHANGE_STATUS_ERROR = "只有审核中的活动变更单才能驳回";

	final static String REQUEST_TOPIC_ITEM_FAILD = "获取活动商品信息失败!";

	final static String MERGE_TOPIC_INFO_ERROR = "合并审批通过变更单出错!";

	final static String INCREASE_ITEM_STORAGE_FAILD = "增加活动商品库存失败!";

	final static String ROLLBACK_STORAGE_INVENTORY_FAILD = "库存回退失败!";

	final static String CHECK_STORAGE_INVENTORY_FAILD = "可用库存检查失败!";
	
	final static String PAYBACK_STORAGE_INVENTORY_FAILD = "退还库存失败!";

	final static String NO_ENOUGH_STORAGE_INVENTORY = "商品[%s]库存不足";

	final static String HAVENT_STORAGE_INVENTORY = "sku[%s]没有匹配库存信息";

	final static String EXIST_MORE_THAN_ONE_STORAGE_INVENTORY = "商品[%s]存在多个库存信息";

	final static String GET_VALID_ITEM_FAILD = "获取商品信息异常";

	final static String DISTRIB_STORAGE_INVENTORY_FAILD = "分配库存失败!";

	final static String METHOD_ARGUMENT_ERROR = "库存数不正确，活动占用库存可能为0!";

	final static String NO_SUPPLIER_WAREHOUSE = "分配库存失败，商家没有仓库信息!";

	final static String EXIST_RELATE_TOPIC = "已存在关联专题信息:ID【%s】!";
	
	final static String ITEM_CHANGE_HAS_ORDER = "商品已导入正在编辑的变更单,不能解除锁定状态!";
	
	final static String COMMON_ERROR = "数据处理异常";

	final static String QUERY_FAILD = "查询条件异常！";

	final static String CART_EMPTY = "购物车没有商品！";

	final static String CART_TYPE_ERROR = "购物车类型错误！";
	
	final static String AREA_OUT_RANGE = "专题不适用当前地区！";

	final static String HAS_FORBIDDEN_WORDS = "提交信息中存在违禁词,请重新检查!";

	final static String STORAGE_LOCK_FAILED_PAYBACK_FAILED = "锁定库存失败后，回滚库存失败!";

	final static String REMOTE_ERROR_CODE = "000";
	
	final static String REMOTE_ERROR_MSG = "远程接口访问异常[%s]";
	
	final static String STORAGE_INVENTORY_ERROR_001 = "001";

	final static String STORAGE_INVENTORY_ERROR_002 = "002";

	final static String STORAGE_INVENTORY_ERROR_003 = "003";

	final static String STORAGE_INVENTORY_ERROR_004 = "004";

	final static String POLICY_INFO_IS_INVALID = "待更新活动限购策略无效";

	final static String POLICY_CHANGE_INFO_IS_INVALID = "活动限购策略变更单无效";
	
	
	final static String STOPCOUPON_FAILD = "终止优惠券失败!";
	final static String REFUSE_COUPON_FAILD = "驳回优惠券失败!";
	final static String PASSED_COUPON_FAILD = "批准优惠券失败!";
	final static String CANCELED_COUPON_FAILD = "取消优惠券失败!";
	
	final static String VALID_COUPON_ID = "指定的专场信息无效!";
	
	final static String PROMOTION_ITEM_SKU_NOENOUGH_INVENTORY_CODE = "001";

	final static String PROMOTION_ITEM_SKU_NOVALID_TOPIC_CODE = "002";

	final static String PROMOTION_ITEM_SKU_FORMAT_ERROR_CODE = "003";
	
	final static String PROMOTION_ITEM_SKU_NOENOUGH_INVENTORY_MSG = "活动商品[%s]没有有效库存";

	final static String PROMOTION_ITEM_SKU_NOVALID_TOPIC_MSG = "活动商品[%s]没有有效活动信息";

	final static String PROMOTION_ITEM_SKU_FORMAT_ERROR_MSG = "SKU无效";
}
