package com.tp.redis;

import java.util.HashMap;
import java.util.Map;

import redis.clients.jedis.JedisPool;

/**
 * @author szy
 *
 */
public class DBShardJedisPool {

	/** key为池名，value为一个jedispool或JedisSentinelPool */
	private Map<String, JedisPool> jedisPoolMap = new HashMap<String, JedisPool>();

	/** 默认池 */
	private JedisPool defaultPool;

	/** 路由策略 */
	private Strategy strategy;

	/** 要切分的字段值 */
	private int fieldValue;

	public DBShardJedisPool() {
		super();
	}

	public Strategy getStrategy() {
		return strategy;
	}

	public void setStrategy(Strategy strategy) {
		this.strategy = strategy;
	}

	public JedisPool getJedisPool(int fieldValue) {
		if (null != strategy && !jedisPoolMap.isEmpty()) {
			String key = strategy.getJedisPoolKey(fieldValue);
			if ("".equals(key)) {
				return defaultPool;
			}
			JedisPool pool = jedisPoolMap.get(key);
			if (null != pool) {
				return pool;
			}
			return defaultPool;
		}
		return defaultPool;
	}

	public JedisPool getJedisPool() {
		if (null != strategy && !jedisPoolMap.isEmpty()) {
			String key = strategy.getJedisPoolKey(this.fieldValue);
			if ("".equals(key)) {
				return defaultPool;
			}
			JedisPool pool = jedisPoolMap.get(key);
			if (null != pool) {
				return pool;
			}
			return defaultPool;
		}
		return defaultPool;
	}

	public void setDefaultPool(JedisPool defaultPool) {
		this.defaultPool = defaultPool;
	}

	public void setJedisPoolMap(Map<String, JedisPool> jedisPoolMap) {
		this.jedisPoolMap = jedisPoolMap;
	}

	public int getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(int fieldValue) {
		this.fieldValue = fieldValue;
	}

}
