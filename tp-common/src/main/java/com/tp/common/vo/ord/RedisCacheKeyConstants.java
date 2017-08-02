package com.tp.common.vo.ord;

/**
 * redis(cache) key常量
 * 
 * @author szy
 * @version 0.0.1
 */
public interface RedisCacheKeyConstants {

	/** 父订单号索引值 */
	String ORDER_CODE_INDEX = "so_c_idx_p";
	/** 子订单号索引值 */
	String SUB_CODE_INDEX = "so_c_idx_s";
}
