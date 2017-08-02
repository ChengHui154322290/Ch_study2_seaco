package com.tp.redis;

public interface Strategy {
	
	/**
	 * 根据列值取jedis pool 的key
	 * @param num 列值
	 * @return
	 */
	String getJedisPoolKey(long fieldNum);

}
