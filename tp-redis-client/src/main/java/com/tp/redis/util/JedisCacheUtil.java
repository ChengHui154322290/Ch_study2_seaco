package com.tp.redis.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tp.redis.CacheShardedJedisPool;

import redis.clients.jedis.ShardedJedis;

/**
 * 用于存储cache 默认有效时间为30分钟
 * 
 * @author szy
 *
 */
public class JedisCacheUtil {

	private static final int LOCK_EXPIRE_SECONDS = 5 * 60;

	private static final int MAX_VALUE_SIZE = 1024 * 1024;

	private static final int LOG_VALUE_SIZE = 50 * 1024;

	private final static Log logger = LogFactory.getLog(JedisCacheUtil.class);

	/** 默认有缓存 失效时间 单位：秒 */
	public final static int DEFAULT_EXPIRE_SECONDS = 60 * 30;

	private CacheShardedJedisPool cacheShardJedisPool;

	/** 项目应用名(key前缀) */
	private String projectPrefixKey;

	private String getCompleteKey(String key) {
		if (null != this.projectPrefixKey && !"".equals(this.projectPrefixKey)) {
			return this.projectPrefixKey + ":" + key.trim();
		}
		return key;
	}

	public void setProjectPrefixKey(String projectPrefixKey) {
		this.projectPrefixKey = projectPrefixKey;
	}

	public void setCacheShardJedisPool(CacheShardedJedisPool cacheShardJedisPool) {
		this.cacheShardJedisPool = cacheShardJedisPool;
	}

	private CacheShardedJedisPool getCacheShardJedisPool() {
		return cacheShardJedisPool;
	}

	/**
	 * 设置 cache 针对字符串 默认有效时间为30分钟
	 * 
	 * @param key
	 * @param value
	 * @return 设置是否成功
	 */
	public boolean setCacheString(String key, String value) {
		return setCacheString(key, value, DEFAULT_EXPIRE_SECONDS);
	}

	/**
	 * 设置 cache 针对字符串
	 * 
	 * @param key
	 * @param value
	 * @param expireSeconds
	 *            单位：秒 失效时间
	 * @return 设置是否成功
	 */
	public boolean setCacheString(String key, String value, Integer expireSeconds) {
		if (null == key || "".equals(key.trim())) {
			return false;
		}
		if (null == value || "".equals(value.trim())) {
			return false;
		}
		if (null == expireSeconds) {
			return false;
		}
		if (value.getBytes().length > LOG_VALUE_SIZE) {
			logger.info("redis setCacheString===>" + key + ";value is more than 50KB ");
		}
		if (value.getBytes().length > MAX_VALUE_SIZE) {
			logger.error(key + " value is more than 1M ");
			return false;
		}
		key = this.getCompleteKey(key);
		ShardedJedis jds = null;
		boolean isSuccess = true;
		try {
			jds = getCacheShardJedisPool().getResource();
			String isOk = jds.set(key, value);
			if ("ok".equalsIgnoreCase(isOk) && null != expireSeconds) {
				jds.expire(key, expireSeconds);
			}
			return "ok".equalsIgnoreCase(isOk);
		} catch (Exception e) {
			isSuccess = false;
			logger.error(e.getMessage(), e);
		} finally {
			if (isSuccess) {
				getCacheShardJedisPool().returnResource(jds);
			} else {
				getCacheShardJedisPool().returnBrokenResource(jds);
			}
		}
		return false;
	}

	/**
	 * 读取cache中的值，针对 string类型 默认有效时间为30分钟
	 * 
	 * @param key
	 * @return value 为字符串
	 */
	public String getCacheString(String key) {
		if (null == key || "".equals(key.trim())) {
			return null;
		}
		key = this.getCompleteKey(key);
		ShardedJedis jds = null;
		boolean isSuccess = true;
		try {
			jds = getCacheShardJedisPool().getResource();
			return jds.get(key);
		} catch (Exception e) {
			isSuccess = false;
			logger.error(e.getMessage(), e);
		} finally {
			if (isSuccess) {
				getCacheShardJedisPool().returnResource(jds);
			} else {
				getCacheShardJedisPool().returnBrokenResource(jds);
			}
		}
		return null;
	}

	/**
	 * 使用默认失效時間设置cache中的值，针对 object类型
	 * 
	 * @param key
	 * @param value
	 *            序列化的对象
	 * @return 是否设置成功
	 */
	public boolean setCache(String key, Object value) {
		return setCache(key, value, DEFAULT_EXPIRE_SECONDS);
	}

	/**
	 * 设置cache中的值，针对 object类型
	 * 
	 * @param key
	 * @param value
	 *            序列化的对象
	 * @param expireSeconds
	 *            失效时间 单位：秒
	 * @return 是否设置成功
	 */
	public boolean setCache(String key, Object value, Integer expireSeconds) {
		if (null == key || "".equals(key.trim())) {
			return false;
		}
		if (null == expireSeconds) {
			return false;
		}
		if (null == value) {
			return false;
		}
		key = this.getCompleteKey(key);
		byte[] serializeValue = SerializeUtil.serialize(value);
		if (serializeValue.length > LOG_VALUE_SIZE) {
			logger.info("redis setCache===>" + key + ";value is more than 50KB ");
		}
		if (serializeValue.length > MAX_VALUE_SIZE) {
			logger.error(key + " value is more than 1M ");
			return false;
		}
		ShardedJedis jds = null;
		boolean isSuccess = true;
		try {
			jds = getCacheShardJedisPool().getResource();
			byte[] bytes = key.getBytes();
			String isOk = jds.set(bytes, serializeValue);
			if ("ok".equalsIgnoreCase(isOk) && null != expireSeconds) {
				jds.expire(bytes, expireSeconds);
			}
			return "ok".equalsIgnoreCase(isOk);
		} catch (Exception e) {
			isSuccess = false;
			logger.error(e.getMessage(), e);
		} finally {
			if (isSuccess) {
				getCacheShardJedisPool().returnResource(jds);
			} else {
				getCacheShardJedisPool().returnBrokenResource(jds);
			}
		}
		return false;
	}

	/**
	 * 读取cache中的值，针对 object类型
	 * 
	 * @param key
	 * @return 已反序列化好的对象
	 */
	public Object getCache(String key) {
		if (null == key || "".equals(key.trim())) {
			return null;
		}
		key = this.getCompleteKey(key);
		ShardedJedis jds = null;
		boolean isSuccess = true;
		try {
			jds = getCacheShardJedisPool().getResource();
			byte[] bytes = jds.get(key.getBytes());
			if (null != bytes) {
				return SerializeUtil.unserialize(bytes);
			}
			return null;
		} catch (Exception e) {
			isSuccess = false;
			logger.error(e.getMessage(), e);
		} finally {
			if (isSuccess) {
				getCacheShardJedisPool().returnResource(jds);
			} else {
				getCacheShardJedisPool().returnBrokenResource(jds);
			}
		}
		return null;
	}

	/**
	 * 自增加1
	 * 
	 * @param key
	 * @return
	 */
	public Long incr(String key) {
		if (null == key || "".equals(key.trim())) {
			return null;
		}
		key = this.getCompleteKey(key);
		ShardedJedis jds = null;
		boolean isSuccess = true;
		try {
			jds = getCacheShardJedisPool().getResource();
			return jds.incr(key);
		} catch (Exception e) {
			isSuccess = false;
			logger.error(e.getMessage(), e);
		} finally {
			if (isSuccess) {
				getCacheShardJedisPool().returnResource(jds);
			} else {
				getCacheShardJedisPool().returnBrokenResource(jds);
			}
		}
		return null;
	}

	/**
	 * 自增加 步长step
	 * 
	 * @param key
	 * @param step
	 *            步长
	 * @return
	 */
	public Long incrBy(String key, int step) {
		if (null == key || "".equals(key.trim())) {
			return null;
		}
		key = this.getCompleteKey(key);
		ShardedJedis jds = null;
		boolean isSuccess = true;
		try {
			jds = getCacheShardJedisPool().getResource();
			return jds.incrBy(key, step);
		} catch (Exception e) {
			isSuccess = false;
			logger.error(e.getMessage(), e);
		} finally {
			if (isSuccess) {
				getCacheShardJedisPool().returnResource(jds);
			} else {
				getCacheShardJedisPool().returnBrokenResource(jds);
			}
		}
		return null;
	}

	/**
	 * 自减1
	 * 
	 * @param key
	 * @return
	 */
	public Long decr(String key) {
		if (null == key || "".equals(key.trim())) {
			return null;
		}
		key = this.getCompleteKey(key);
		ShardedJedis jds = null;
		boolean isSuccess = true;
		try {
			jds = getCacheShardJedisPool().getResource();
			return jds.decr(key);
		} catch (Exception e) {
			isSuccess = false;
			logger.error(e.getMessage(), e);
		} finally {
			if (isSuccess) {
				getCacheShardJedisPool().returnResource(jds);
			} else {
				getCacheShardJedisPool().returnBrokenResource(jds);
			}
		}
		return null;
	}

	/**
	 * 自减 步长step
	 * 
	 * @param key
	 * @param step
	 *            步长
	 * @return
	 */
	public Long decrBy(String key, int step) {
		if (null == key || "".equals(key.trim())) {
			return null;
		}
		key = this.getCompleteKey(key);
		ShardedJedis jds = null;
		boolean isSuccess = true;
		try {
			jds = getCacheShardJedisPool().getResource();
			return jds.decrBy(key, step);
		} catch (Exception e) {
			isSuccess = false;
			logger.error(e.getMessage(), e);
		} finally {
			if (isSuccess) {
				getCacheShardJedisPool().returnResource(jds);
			} else {
				getCacheShardJedisPool().returnBrokenResource(jds);
			}
		}
		return null;
	}

	/**
	 * 删除string类型 的key ,请使用 deleteCacheStringKey 方法
	 * 
	 * @param key
	 * @param step
	 *            步长
	 * @return 被删除key 的数量
	 */
	@Deprecated
	public Long deleteKey(String key) {
		if (null == key || "".equals(key.trim())) {
			return 0L;
		}
		key = this.getCompleteKey(key);
		ShardedJedis jds = null;
		boolean isSuccess = true;
		try {
			jds = getCacheShardJedisPool().getResource();
			return jds.del(key);
		} catch (Exception e) {
			isSuccess = false;
			logger.error(e.getMessage(), e);
		} finally {
			if (isSuccess) {
				getCacheShardJedisPool().returnResource(jds);
			} else {
				getCacheShardJedisPool().returnBrokenResource(jds);
			}
		}
		return 0L;
	}

	/**
	 * 删除 setCacheString 这样方法设置的string key
	 * 
	 * @param key
	 * @return
	 */
	public Long deleteCacheStringKey(String key) {
		return deleteKey(key);
	}

	/**
	 * 将key值转为byte[]后再删除 byte[] 这样的key 删除 setCache 这样方法设置的string key
	 * 
	 * @param key
	 * @param step
	 *            步长
	 * @return
	 */
	@Deprecated
	public Long deleteStrToBytesKey(String key) {
		if (null == key || "".equals(key.trim())) {
			return 0L;
		}
		key = this.getCompleteKey(key);
		ShardedJedis jds = null;
		boolean isSuccess = true;
		try {
			jds = getCacheShardJedisPool().getResource();
			return jds.del(key.getBytes());
		} catch (Exception e) {
			isSuccess = false;
			logger.error(e.getMessage(), e);
		} finally {
			if (isSuccess) {
				getCacheShardJedisPool().returnResource(jds);
			} else {
				getCacheShardJedisPool().returnBrokenResource(jds);
			}
		}
		return 0L;
	}

	/**
	 * 删除 setCache 这样方法设置的string key
	 * 
	 * @param key
	 * @return
	 */
	public Long deleteCacheKey(String key) {
		return deleteStrToBytesKey(key);
	}

	/**
	 * 获取key的有效期时间 ：当key 不存在时，返回-2 当key 存在但没有设置剩余生存时间时，返回-1 。 否则，以秒为单位，返回key
	 * 的剩余生存时间。
	 * 
	 * @param key
	 * @param expireSeconds
	 * @return
	 */
	public Long getKeyExpire(String key) {
		if (null == key || "".equals(key.trim())) {
			return 0L;
		}
		key = this.getCompleteKey(key);
		ShardedJedis jds = null;
		boolean isSuccess = true;
		try {
			jds = getCacheShardJedisPool().getResource();
			return jds.ttl(key);
		} catch (Exception e) {
			isSuccess = false;
			logger.error(e.getMessage(), e);
		} finally {
			if (isSuccess) {
				getCacheShardJedisPool().returnResource(jds);
			} else {
				getCacheShardJedisPool().returnBrokenResource(jds);
			}
		}
		return 0L;
	}

	/**
	 * 设置此值时，key必须存在并且key在有效期内，否则 无效
	 * 
	 * @param key
	 * @param expireSeconds
	 * @return
	 */
	public Long setKeyExpire(String key, int expireSeconds) {
		if (null == key || "".equals(key.trim())) {
			return null;
		}
		key = this.getCompleteKey(key);
		ShardedJedis jds = null;
		boolean isSuccess = true;
		try {
			jds = getCacheShardJedisPool().getResource();
			return jds.expire(key, expireSeconds);
		} catch (Exception e) {
			isSuccess = false;
			logger.error(e.getMessage(), e);
		} finally {
			if (isSuccess) {
				getCacheShardJedisPool().returnResource(jds);
			} else {
				getCacheShardJedisPool().returnBrokenResource(jds);
			}
		}
		return null;
	}

	/**
	 * 判断此key是否存在
	 * 
	 * @param key
	 * @return
	 */
	public boolean keyExists(String key) {
		if (null == key || "".equals(key.trim())) {
			return false;
		}
		key = this.getCompleteKey(key);
		ShardedJedis jds = null;
		boolean isSuccess = true;
		try {
			jds = getCacheShardJedisPool().getResource();
			return jds.exists(key);
		} catch (Exception e) {
			isSuccess = false;
			logger.error(e.getMessage(), e);
		} finally {
			if (isSuccess) {
				getCacheShardJedisPool().returnResource(jds);
			} else {
				getCacheShardJedisPool().returnBrokenResource(jds);
			}
		}
		return false;
	}

	/**
	 * 取得有效时间内的锁
	 * 
	 * @param key
	 * @param expireSeconds
	 *            有效时间 单位 秒
	 * @return
	 */
	public boolean lock(String key, int expireSeconds) {
		if (null == key || "".equals(key.trim())) {
			return false;
		}
		if (expireSeconds == 0) {
			expireSeconds = LOCK_EXPIRE_SECONDS;
		}
		key = this.getCompleteKey(key);
		ShardedJedis jds = null;
		boolean isSuccess = true;
		try {
			jds = getCacheShardJedisPool().getResource();
			Long i = jds.setnx(key, System.currentTimeMillis() + "");
			if (i.intValue() == 1) {
				jds.expire(key, expireSeconds);
				return true;
			}
		} catch (Exception e) {
			isSuccess = false;
			logger.error(e.getMessage(), e);
		} finally {
			if (isSuccess) {
				getCacheShardJedisPool().returnResource(jds);
			} else {
				getCacheShardJedisPool().returnBrokenResource(jds);
			}
		}
		return false;
	}

	/**
	 * 获取锁,默认锁定五分钟
	 * 
	 * @param key
	 * @return
	 */
	public boolean lock(String key) {
		return this.lock(key, LOCK_EXPIRE_SECONDS);
	}

	/**
	 * 释放锁
	 * 
	 * @param key
	 * @return
	 */
	public boolean unLock(String key) {
		if (null == key || "".equals(key.trim())) {
			return false;
		}
		key = this.getCompleteKey(key);
		ShardedJedis jds = null;
		boolean isSuccess = true;
		try {
			jds = getCacheShardJedisPool().getResource();
			Long i = jds.del(key);
			if (i.intValue() == 1) {
				return true;
			}
		} catch (Exception e) {
			isSuccess = false;
			logger.error(e.getMessage(), e);
		} finally {
			if (isSuccess) {
				getCacheShardJedisPool().returnResource(jds);
			} else {
				getCacheShardJedisPool().returnBrokenResource(jds);
			}
		}
		return false;
	}
	
	/**
	 * expireSeconds时间内，key自增的值超过maxNum将返回false,否则返回true
	 * @param key 
	 * @param maxNum 
	 * @param expireSeconds
	 * @return
	 */
	public boolean watchMethodCall(String key, long maxNum, int expireSeconds) {
		return this.incrWatchKey(key, expireSeconds) <= maxNum;
	}

	private long incrWatchKey(String key, int expireSeconds) {
		long count = 0;
		ShardedJedis jedis = null;
		boolean isSuccess = true;
		try {
			jedis = getCacheShardJedisPool().getResource();
			if (!jedis.exists(key)) {
				String isOk = jedis.set(key, String.valueOf("0"));
				if ("ok".equalsIgnoreCase(isOk)) {
					jedis.expire(key, expireSeconds);
				}
			}
			count = jedis.incr(key);
		} catch (Exception ex) {
			isSuccess = false;
			logger.error(ex.getMessage(), ex);
		} finally {
			if (isSuccess) {
				this.getCacheShardJedisPool().returnResource(jedis);
			} else {
				this.getCacheShardJedisPool().returnBrokenResource(jedis);
			}
		}
		return count;
	}

}
