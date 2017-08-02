package com.tp.redis;

/**
 * 取模策略
 * 
 * @author szy
 *
 */
public class ModStrategy implements Strategy {

	/** key前缀 */
	private String prefixMapKey;

	/** 取模数 */
	private int modValue = 0;

	public int getModValue() {
		return modValue;
	}

	public void setModValue(int routeValue) {
		this.modValue = routeValue;
	}

	public String getPrefixMapKey() {
		return prefixMapKey;
	}

	public void setPrefixMapKey(String prefixMapKey) {
		this.prefixMapKey = prefixMapKey;
	}

	/**
	 * 根据列值取jedis pool 的key
	 * 
	 * @param fieldNum
	 *            列值
	 * @return prefix_map_key+"_"+取模后的值(如db_redis_cart_0.....db_redis_cart_9)
	 */
	public String getJedisPoolKey(long fieldNum) {
		if (this.getModValue() == 0){
			return "";
		}
		int length = this.prefixMapKey.length();
		String underLine = this.prefixMapKey.substring(length - 1, length);
		String prefix_key = this.prefixMapKey;
		if ("_".equals(underLine)) {
			prefix_key = this.prefixMapKey.substring(0, length - 1);
		}
		return prefix_key + "_" + (fieldNum % this.getModValue());
	}
}
