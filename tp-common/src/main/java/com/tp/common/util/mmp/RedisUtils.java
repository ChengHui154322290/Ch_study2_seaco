/**
 * 
 */
package com.tp.common.util.mmp;

/**
 * @author szy
 *
 */
public class RedisUtils {

	public static String getBalanceKey(Long tid, String key) {
		if (tid == null) {
			return key;
		}
		Long branchId = tid % 10;
		return key + "_" + String.valueOf(branchId);
	}
}
