package com.tp.redis.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tp.redis.DBShardJedisPool;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisSentinelPool;

/**
 * 用于存储db 读操作走 默认走 从库 如果从库没有设置，读操作走主库
 *
 * @author szy
 *
 */
public class JedisDBUtil {

	private final Log log = LogFactory.getLog(JedisDBUtil.class);

	private static final int MAX_VALUE_SIZE = 1024 * 1024;

	private static final int LOCK_EXPIRE_SECONDS=5*60;

	/** 主库线程池 */
	private DBShardJedisPool dbMasterShardJedisPool;

	/** 从库线程池 */
	private DBShardJedisPool dbSlaveShardJedisPool;

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

	private DBShardJedisPool getDbSlaveShardJedisPool() {
		return dbSlaveShardJedisPool;
	}

	public void setDbSlaveJedisPool(DBShardJedisPool dbSlaveJedisPool) {
		this.dbSlaveShardJedisPool = dbSlaveJedisPool;
	}

	private DBShardJedisPool getDbMasterShardJedisPool() {
		return dbMasterShardJedisPool;
	}

	public void setDbMasterShardJedisPool(DBShardJedisPool dbMasterJedisPool) {
		this.dbMasterShardJedisPool = dbMasterJedisPool;
	}

	private JedisPool getMasterJedisPool(String key) {
		key = this.getCompleteKey(key);
		int intKey = strToInt(key);
		return this.getDbMasterShardJedisPool().getJedisPool(intKey);
	}

	private JedisPool getSlaveJedisPool(String key) {// 先从 从库池拿，没有再从主库池中拿
		key = this.getCompleteKey(key);
		int intKey = strToInt(key);
		if (null != this.getDbSlaveShardJedisPool()) {
			return this.getDbSlaveShardJedisPool().getJedisPool(intKey);
		}
		return this.getDbMasterShardJedisPool().getJedisPool(intKey);
	}

	public static int strToInt(String key) {
		if (null != key && !"".equals(key)) {
			int num = 0;
			for (int i = 0; i < key.length(); i++) {
				num += key.charAt(i);
			}
			return num;
		}
		return 0;
	}

	public boolean setDB(String key, Object value) {
		return this.setDB(key, value, null);
	}

	/**
	 * 将序列化对象存储到 redis db中
	 *
	 * @param key
	 * @param value
	 * @param expireSeconds
	 *            单位：秒 实现 java序列化的对象
	 * @return true：成功 false：失败
	 */
	public boolean setDB(String key, Object value, Integer expireSeconds) {
		if (null == key || "".equals(key.trim())) {
			return false;
		}
		if (null == value) {
			return false;
		}
		key = this.getCompleteKey(key);
		byte[] serializeValue = SerializeUtil.serialize(value);
		if (serializeValue.length > MAX_VALUE_SIZE) {
			log.error(key + " value is more than 1M ");
			return false;
		}
		Jedis jds = null;
		JedisPool masterJedisPool = getMasterJedisPool(key);
		boolean isSuccess = true;
		try {
			jds = masterJedisPool.getResource();
			String isOk = jds.set(key.getBytes(), serializeValue);
			boolean equalsIgnoreCase = "ok".equalsIgnoreCase(isOk);
			if (equalsIgnoreCase && null != expireSeconds) {
				jds.expire(key.getBytes(), expireSeconds);
			}
			return equalsIgnoreCase;
		} catch (Exception e) {
			isSuccess = false;
			log.error(e.getMessage(), e);
		} finally {
			if (null != masterJedisPool) {
				if (isSuccess) {
					masterJedisPool.returnResource(jds);
				} else {
					masterJedisPool.returnBrokenResource(jds);
				}
			}
		}
		return false;
	}

	 /**
     * 存储REDIS队列 顺序存储
     * @param byte[] key reids键名
     * @param byte[] value 键值
     */
    public Long lpush(String key, Object value) {
        if (null == key || "".equals(key.trim())) {
        	return null;
		}
		if (null == value) {
			return null;
		}
		key = getCompleteKey(key);
		byte[] serializeValue = SerializeUtil.serialize(value);
		Jedis jds = null;
		JedisPool masterJedisPool = getMasterJedisPool(key);
		boolean isSuccess = true;
		try {
			jds = masterJedisPool.getResource();
			Long isOk = jds.lpush(key.getBytes(), serializeValue);
			return isOk;
		} catch (Exception e) {
			isSuccess = false;
			log.error(e.getMessage(), e);
		} finally {
			if (null != masterJedisPool) {
				if (isSuccess) {
					masterJedisPool.returnResource(jds);
				} else {
					masterJedisPool.returnBrokenResource(jds);
				}
			}
		}
		return null;
    }
    
    /**
     * 获取队列数据
     * @param byte[] key 键名
     * @return
     */
    public  Object rpop(String key) {
    	 if (null == key || "".equals(key.trim())) {
         	return null;
 		}
 		key = getCompleteKey(key);
 		Jedis jds = null;
 		JedisPool masterJedisPool = getMasterJedisPool(key);
 		boolean isSuccess = true;
 		try {
 			jds = masterJedisPool.getResource();
 			return SerializeUtil.unserialize(jds.rpop(key.getBytes()));
 		} catch (Exception e) {
 			isSuccess = false;
 			log.error(e.getMessage(), e);
 		} finally {
 			if (null != masterJedisPool) {
 				if (isSuccess) {
 					masterJedisPool.returnResource(jds);
 				} else {
 					masterJedisPool.returnBrokenResource(jds);
 				}
 			}
 		}
 		return null;
    }
 
    
	/**
	 * 从 从库 redis db中读取 key的值
	 *
	 * @param key
	 * @return 反序列化后的 Object
	 */
	public Object getDB(String key) {
		return this.getDB(key, true);
	}

	/**
	 * 从 redis db中读取 key的值
	 *
	 * @param key
	 * @param readSlave
	 *            true为读从库，false读主库
	 * @return 反序列化后的 Object
	 */
	public Object getDB(String key, boolean readSlave) {
		if (null == key || "".equals(key.trim())) {
			return null;
		}
		key = this.getCompleteKey(key);
		Jedis jds = null;
		JedisPool jedisPool = null;
		boolean isSuccess = true;
		try {
			if (readSlave) {
				jedisPool = getSlaveJedisPool(key);
			} else {
				jedisPool = getMasterJedisPool(key);
			}
			jds = jedisPool.getResource();
			byte[] bytes = jds.get(key.getBytes());
			if (null != bytes) {
				return SerializeUtil.unserialize(bytes);
			}
			return null;
		} catch (Exception e) {
			isSuccess = false;
			log.error(e.getMessage(), e);
		} finally {
			if (null != jedisPool) {
				if (isSuccess) {
					jedisPool.returnResource(jds);
				} else {
					jedisPool.returnBrokenResource(jds);
				}
			}
		}
		return null;
	}

	/**
	 * 将 字符串 存储到 redis db中
	 *
	 * @param key
	 * @param value
	 * @return true：成功 false：失败
	 */
	public boolean setDBString(String key, String value) {
		if (null == key || "".equals(key.trim())) {
			return false;
		}
		if (null == value || "".equals(value.trim())) {
			return false;
		}
		if (value.getBytes().length > MAX_VALUE_SIZE) {
			log.error(key + " value is more than 1M ");
			return false;
		}
		key = this.getCompleteKey(key);
		Jedis jds = null;
		boolean isSuccess = true;
		JedisPool masterJedisPool = getMasterJedisPool(key);
		try {
			jds = masterJedisPool.getResource();
			String isOk = jds.set(key, value);
			return "ok".equalsIgnoreCase(isOk);
		} catch (Exception e) {
			isSuccess = false;
			log.error(e.getMessage(), e);
		} finally {
			if (null != masterJedisPool) {
				if (isSuccess) {
					masterJedisPool.returnResource(jds);
				} else {
					masterJedisPool.returnBrokenResource(jds);
				}
			}
		}
		return false;
	}

	/**
	 * 从 从库 redis db中读取 key的值
	 *
	 * @param key
	 * @return String
	 */
	public String getDBString(String key) {
		return this.getDBString(key, true);
	}

	/**
	 * 从 redis db中读取 key的值
	 *
	 * @param key
	 * @param readSlave
	 *            true为读从库，false读主库
	 * @return String
	 */
	public String getDBString(String key, boolean readSlave) {
		if (null == key || "".equals(key.trim())) {
			return null;
		}
		key = this.getCompleteKey(key);
		Jedis jds = null;
		JedisPool jedisPool = null;
		boolean isSuccess = true;
		try {
			if (readSlave) {
				jedisPool = getSlaveJedisPool(key);
			} else {
				jedisPool = getMasterJedisPool(key);
			}
			jds = jedisPool.getResource();
			return jds.get(key);
		} catch (Exception e) {
			isSuccess = false;
			log.error(e.getMessage(), e);
		} finally {
			if (null != jedisPool) {
				if (isSuccess) {
					jedisPool.returnResource(jds);
				} else {
					jedisPool.returnBrokenResource(jds);
				}
			}
		}
		return null;
	}

	/**
	 * 获取key的有效期时间 ：当key 不存在时，返回-2 当key 存在但没有设置剩余生存时间时，返回-1 。 否则，以秒为单位，返回key
	 * 的剩余生存时间。
	 *
	 * @param key
	 *            适用于setDB方法保存的key
	 * @param expireSeconds
	 * @return
	 */
	public Long getDBKeyExpire(String key, boolean readSlave) {
		if (null == key || "".equals(key.trim())) {
			return null;
		}
		key = this.getCompleteKey(key);
		Jedis jds = null;
		JedisPool jedisPool = null;
		boolean isSuccess = true;
		try {
			if (readSlave) {
				jedisPool = getSlaveJedisPool(key);
			} else {
				jedisPool = getMasterJedisPool(key);
			}
			jds = jedisPool.getResource();
			return jds.ttl(key.getBytes());
		} catch (Exception e) {
			isSuccess = false;
			log.error(e.getMessage(), e);
		} finally {
			if (null != jedisPool) {
				if (isSuccess) {
					jedisPool.returnResource(jds);
				} else {
					jedisPool.returnBrokenResource(jds);
				}
			}
		}
		return 0L;
	}

	/**
	 * 获取key的有效期时间 ：当key 不存在时，返回-2 当key 存在但没有设置剩余生存时间时，返回-1 。 否则，以秒为单位，返回key
	 * 的剩余生存时间。
	 *
	 * @param key
	 *            适用于setDB方法保存的key
	 * @return
	 */
	public Long getDBKeyExpire(String key) {
		return getDBKeyExpire(key, true);
	}

	/**
	 *删除key 适用于setDB方法保存的key
	 *
	 * @param key
	 *
	 * @return
	 */
	public boolean deleteDBKey(String key) {
		if (null == key || "".equals(key.trim())) {
			return false;
		}
		key = this.getCompleteKey(key);
		Jedis jds = null;
		JedisPool jedisPool = null;
		boolean isSuccess = true;
		try {
			jedisPool = getMasterJedisPool(key);
			jds = jedisPool.getResource();
			return jds.del(key.getBytes()) > 0;
		} catch (Exception e) {
			isSuccess = false;
			log.error(e.getMessage(), e);
		} finally {
			if (null != jedisPool) {
				if (isSuccess) {
					jedisPool.returnResource(jds);
				} else {
					jedisPool.returnBrokenResource(jds);
				}
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
		Jedis jds = null;
		JedisPool jedisPool = null;
		boolean isSuccess = true;
		try {
			jedisPool = getMasterJedisPool(key);
			jds = jedisPool.getResource();
			Long i = jds.setnx(key, System.currentTimeMillis() + "");
			if (i.intValue() == 1) {
				jds.expire(key, expireSeconds);
				return true;
			}
		} catch (Exception e) {
			isSuccess = false;
			log.error(e.getMessage(), e);
		} finally {
			if (null != jedisPool) {
				if (isSuccess) {
					jedisPool.returnResource(jds);
				} else {
					jedisPool.returnBrokenResource(jds);
				}
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
		Jedis jds = null;
		JedisPool jedisPool = null;
		boolean isSuccess = true;
		try {
			jedisPool = getMasterJedisPool(key);
			jds = jedisPool.getResource();
			Long i = jds.del(key);
			if (i.intValue() == 1) {
				return true;
			}
		} catch (Exception e) {
			isSuccess = false;
			log.error(e.getMessage(), e);
		} finally {
			if (null != jedisPool) {
				if (isSuccess) {
					jedisPool.returnResource(jds);
				} else {
					jedisPool.returnBrokenResource(jds);
				}
			}
		}
		return false;
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
		Jedis jds = null;
		JedisPool jedisPool = null;
		boolean isSuccess = true;
		try {
			jedisPool = getMasterJedisPool(key);
			jds = jedisPool.getResource();
			return jds.incr(key);
		} catch (Exception e) {
			isSuccess = false;
			log.error(e.getMessage(), e);
		} finally {
			if (null != jedisPool) {
				if (isSuccess) {
					jedisPool.returnResource(jds);
				} else {
					jedisPool.returnBrokenResource(jds);
				}
			}
		}
		return null;
	}
	
	/**
	 * 将 字符串 存储到 redis db中
	 *
	 * @param key
	 * @param value
	 * @return true：成功 false：失败
	 */
	public boolean setIncr(String key, Long value) {
		if (null == key || "".equals(key.trim())) {
			return false;
		}
		key = this.getCompleteKey(key);
		Jedis jds = null;
		JedisPool jedisPool = null;
		boolean isSuccess = true;
		try {
			jedisPool = getMasterJedisPool(key);
			jds = jedisPool.getResource();
			jds.set(key, value.toString());
		} catch (Exception e) {
			isSuccess = false;
			log.error(e.getMessage(), e);
		} finally {
			if (null != jedisPool) {
				if (isSuccess) {
					jedisPool.returnResource(jds);
				} else {
					jedisPool.returnBrokenResource(jds);
				}
			}
		}
		return false;
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
		Jedis jds = null;
		JedisPool jedisPool = null;
		boolean isSuccess = true;
		try {
			jedisPool = getMasterJedisPool(key);
			jds = jedisPool.getResource();
			return jds.incrBy(key, step);
		} catch (Exception e) {
			isSuccess = false;
			log.error(e.getMessage(), e);
		} finally {
			if (null != jedisPool) {
				if (isSuccess) {
					jedisPool.returnResource(jds);
				} else {
					jedisPool.returnBrokenResource(jds);
				}
			}
		}
		return null;
	}

}
