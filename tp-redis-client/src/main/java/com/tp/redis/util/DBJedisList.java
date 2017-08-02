package com.tp.redis.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tp.redis.ApplicationContextBeans;
import com.tp.redis.DBShardJedisPool;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.Transaction;

/**
 * 此类 用作 redis 持久化存储 ，象购物车使用
 * 
 * @author szy
 *
 * @param <T>
 */
public class DBJedisList<T> {

	private Log log = LogFactory.getLog(DBJedisList.class);

	private final static String DB_JEDIS_POOL_NAME = "dbMasterShardJedisPool";

	/** key */
	private byte[] key;

	/** db redis 连接池 */
	private DBShardJedisPool dbJedisPool;

	private JedisPool jedisPool;

	private int shardfieldValue;

	private int expireSeconds;

	private int getShardfieldValue() {
		return shardfieldValue;
	}

	private void setShardfieldValue(int shardfieldValue) {
		this.shardfieldValue = shardfieldValue;
	}

	private void setDbJedisPool(DBShardJedisPool dbJedisPool) {
		this.dbJedisPool = dbJedisPool;
	}

	private JedisPool getDbJedisPool() {
		if (null == this.jedisPool) {
			this.jedisPool = dbJedisPool.getJedisPool(getShardfieldValue());
			return this.jedisPool;
		}
		return this.jedisPool;
	}

	private static byte[] stringToByteArray(String str) {
		return str.getBytes();
	}

	public DBJedisList(String key) {
		this(key, 0, DB_JEDIS_POOL_NAME, null);
	}

	public DBJedisList(String key, String db_jedis_pool_name) {
		this(key, 0, db_jedis_pool_name, null);
	}

	/**
	 * 
	 * @param key
	 * @param expireSeconds
	 *            失效时间 单位：秒
	 */
	private DBJedisList(String key, String db_jedis_pool_name, Integer expireSeconds) {
		this(key, 0, db_jedis_pool_name, expireSeconds);
	}

	/**
	 * 
	 * @param key
	 * @param shardfieldValue
	 *            字段值
	 * @param routeValue
	 *            路由值
	 */
	public DBJedisList(String key, int shardfieldValue, String db_jedis_pool_name) {
		this(key, shardfieldValue, db_jedis_pool_name, null);
	}

	public DBJedisList(String key, int shardfieldValue) {
		this(key, shardfieldValue, DB_JEDIS_POOL_NAME, null);
	}

	private DBJedisList(String key, int shardfieldValue, Integer expireSeconds) {
		this(key, shardfieldValue, DB_JEDIS_POOL_NAME, expireSeconds);
	}

	private DBJedisList(String key, int shardfieldValue, String db_jedis_pool_name, Integer expireSeconds) {
		this.key = stringToByteArray(key);
		DBShardJedisPool dbJedisPool = (DBShardJedisPool) ApplicationContextBeans.getBean(db_jedis_pool_name);
		setDbJedisPool(dbJedisPool);
		this.setShardfieldValue(shardfieldValue);
		this.setFieldValue(shardfieldValue);
		if (expireSeconds != null && expireSeconds.intValue() > 0) {
			Jedis jedis = null;
			boolean isSuccess = true;
			try {
				jedis = this.getDbJedisPool().getResource();
				jedis.expire(this.key, expireSeconds);
			} catch (Exception ex) {
				isSuccess = false;
				log.error(ex.getMessage(), ex);
			} finally {
				if (isSuccess) {
					this.getDbJedisPool().returnResource(jedis);
				} else {
					this.getDbJedisPool().returnBrokenResource(jedis);
				}
			}
		}
	}

	/**
	 * 设置后 用于在 dbJedisPool中的计算，以便从多个池中取出一个池
	 * 
	 * @param shardfieldValue
	 *            字段值
	 * @param routeValue
	 *            路由值
	 */
	private void setFieldValue(int shardfieldValue) {
		dbJedisPool.setFieldValue(shardfieldValue);
	}

	public long add(T o) {
		if (null != o) {
			try {
				byte[] b = serialize(o);
				Jedis jedis = null;
				boolean isSuccess = true;
				try {
					jedis = this.getDbJedisPool().getResource();
					return jedis.lpush(this.key, b);
				} catch (Exception ex) {
					isSuccess = false;
					log.error(ex.getMessage(), ex);
				} finally {
					if (isSuccess) {
						this.getDbJedisPool().returnResource(jedis);
					} else {
						this.getDbJedisPool().returnBrokenResource(jedis);
					}
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
		return 0;
	}

	/**
	 * 根据下标删除一个元素
	 */
	public boolean remove(int index) {
		Jedis jedis = null;
		boolean isSuccess = true;
		try {
			jedis = this.getDbJedisPool().getResource();
			byte[] value = jedis.lindex(this.key, index);
			if (value != null) {
				return jedis.lrem(this.key, 1, value) > 0;
			}
		} catch (Exception ex) {
			isSuccess = false;
			log.error(ex.getMessage(), ex);
		} finally {
			if (isSuccess) {
				this.getDbJedisPool().returnResource(jedis);
			} else {
				this.getDbJedisPool().returnBrokenResource(jedis);
			}
		}
		return false;
	}

	/**
	 * 根据值删除一个元素
	 */
	public boolean remove(T t) {
		if (null != t) {
			byte[] bytes = serialize(t);
			Jedis jedis = null;
			boolean isSuccess = true;
			try {
				jedis = this.getDbJedisPool().getResource();
				return jedis.lrem(this.key, 1, bytes) > 0;
			} catch (Exception ex) {
				isSuccess = false;
				log.error(ex.getMessage(), ex);
			} finally {
				if (isSuccess) {
					this.getDbJedisPool().returnResource(jedis);
				} else {
					this.getDbJedisPool().returnBrokenResource(jedis);
				}
			}
		}
		return false;
	}

	/**
	 * 取某下标位置的值
	 * 
	 * @param index
	 * @return
	 */
	public T get(int index) {
		Jedis jedis = null;
		boolean isSuccess = true;
		try {
			jedis = this.getDbJedisPool().getResource();
			byte[] value = jedis.lindex(this.key, index);
			if (value != null) {
				T deserialize = unserialize(value);
				return deserialize;
			}
		} catch (Exception ex) {
			isSuccess = false;
			log.info(ex.getMessage(), ex);
		} finally {
			if (isSuccess) {
				this.getDbJedisPool().returnResource(jedis);
			} else {
				this.getDbJedisPool().returnBrokenResource(jedis);
			}
		}
		return null;
	}

	private T unserialize(byte[] value) throws Exception {
		return (T) SerializeUtil.unserialize(value);
	}

	/**
	 * 设置某位置的值
	 * 
	 * @param index
	 * @param t
	 * @return
	 */
	public boolean set(int index, T t) {
		if (index >= 0 && null != t) {
			Jedis jedis = null;
			boolean isSuccess = true;
			try {
				jedis = this.getDbJedisPool().getResource();
				if (jedis.llen(this.key) > 0) {
					String returnStr = jedis.lset(this.key, index, serialize(t));
					if ("ok".equalsIgnoreCase(returnStr)) {
						return true;
					}
					return false;
				}
			} catch (Exception ex) {
				isSuccess = false;
				log.error(ex.getMessage(), ex);
			} finally {
				if (isSuccess) {
					this.getDbJedisPool().returnResource(jedis);
				} else {
					this.getDbJedisPool().returnBrokenResource(jedis);
				}
			}
		}
		return false;
	}

	private byte[] serialize(T t) {
		return SerializeUtil.serialize(t);
	}

	public boolean contains(T t, List<T> list) {
		if (!list.isEmpty()) {
			return list.contains(t);
		}
		return false;
	}

	public boolean contains(T t) {
		List<T> list = this.getList();
		if (!list.isEmpty()) {
			return list.contains(t);
		}
		return false;
	}

	/**
	 * 
	 * @return 返回列表中所有数据
	 */
	public List<T> getList() {
		return this.subList(0, -1);
	}

	private List<T> getDeserializableList(List<byte[]> list) {
		if (null != list && !list.isEmpty()) {
			List<T> tList = new ArrayList<T>();
			for (byte[] str : list) {
				try {
					if (null != str) {
						T t = (T) SerializeUtil.unserialize(str);
						tList.add(t);
					}
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
			return tList;
		}

		return new ArrayList<T>();
	}

	/**
	 * 
	 * @param start
	 *            开始下标
	 * @param end
	 *            结束下标
	 * @return 包括start 和 end 下标 的值
	 */
	public List<T> subList(int start, int end) {
		Jedis jedis = null;
		boolean isSuccess = true;
		try {
			jedis = this.getDbJedisPool().getResource();
			List<byte[]> list = jedis.lrange(this.key, start, end);
			return getDeserializableList(list);
		} catch (Exception ex) {
			isSuccess = false;
			log.error(ex.getMessage(), ex);
		} finally {
			if (isSuccess) {
				this.getDbJedisPool().returnResource(jedis);
			} else {
				this.getDbJedisPool().returnBrokenResource(jedis);
			}
		}
		return new ArrayList<T>();
	}

	/**
	 * 将参数 list 添加到当前key中
	 * 
	 * @param list
	 * @return 添加后 当前key中所有数据项总数 大于等于 list.size
	 */
	public int addAll(List<T> list) {
		if (null != list && !list.isEmpty()) {
			byte[][] bytes = new byte[list.size()][];
			int num = 0;
			for (T t : list) {
				if (t != null) {
					bytes[num] = serialize(t);
					num++;
				}
			}
			Jedis jedis = null;
			boolean isSuccess = true;
			try {
				jedis = this.getDbJedisPool().getResource();
				return jedis.lpush(this.key, bytes).intValue();
			} catch (Exception ex) {
				isSuccess = false;
				log.error(ex.getMessage(), ex);
			} finally {
				if (isSuccess) {
					this.getDbJedisPool().returnResource(jedis);
				} else {
					this.getDbJedisPool().returnBrokenResource(jedis);
				}
			}
		}
		return 0;
	}

	public int watchAddAll(List<T> list, boolean needWatch) {
		int count = 0;
		if (null != list && !list.isEmpty()) {
			byte[][] bytes = new byte[list.size()][];
			int num = 0;
			for (T t : list) {
				if (t != null) {
					bytes[num] = serialize(t);
					num++;
				}
			}
			Jedis jedis = null;
			boolean isSuccess = true;
			try {
				jedis = this.getDbJedisPool().getResource();
				if (needWatch) {
					jedis.watch(this.key);
				}
				Transaction trans = jedis.multi();
				trans.ltrim(this.key, 0, 0);
				trans.lpop(this.key);
				trans.lpush(this.key, bytes);
				List<Object> results = trans.exec();
				if (null != results) {
					if (null != results.get(2)) {
						String string = results.get(2).toString();
						count = Integer.valueOf(string);
					}
				}
			} catch (Exception ex) {
				isSuccess = false;
				log.error(ex.getMessage());
			} finally {
				if (isSuccess) {
					this.getDbJedisPool().returnResource(jedis);
				} else {
					this.getDbJedisPool().returnBrokenResource(jedis);
				}
			}
		}
		return count;
	}

//	/**
//	 * expireSeconds时间内，key自增的值超过maxNum将返回false,否则返回true
//	 * @param maxNum 
//	 * @param expireSeconds
//	 * @return
//	 */
//	public boolean watchMethodCall(int maxNum, int expireSeconds) {
//		return this.incrWatchKey(maxNum, expireSeconds) <= maxNum;
//	}
//
//	private int incrWatchKey(int maxNum, int expireSeconds) {
//		int count = 0;
//		Jedis jedis = null;
//		boolean isSuccess = true;
//		try {
//			jedis = this.getDbJedisPool().getResource();
//			if (!jedis.exists(this.key)) {
//				String isOk = jedis.set(key, String.valueOf("0").getBytes());
//				if ("ok".equalsIgnoreCase(isOk)) {
//					jedis.expire(key, expireSeconds);
//				}
//			}
//			jedis.watch(this.key);
//			Transaction trans = jedis.multi();
//			trans.incr(this.key);
//			List<Object> results = trans.exec();
//			if (null != results) {
//				if (null != results.get(0)) {
//					String string = results.get(0).toString();
//					count = Integer.valueOf(string);
//				}
//			}
//		} catch (Exception ex) {
//			isSuccess = false;
//			log.error(ex.getMessage(), ex);
//		} finally {
//			if (isSuccess) {
//				this.getDbJedisPool().returnResource(jedis);
//			} else {
//				this.getDbJedisPool().returnBrokenResource(jedis);
//			}
//		}
//		return count;
//	}

	public int watchAddAll(List<T> list) {
		return this.watchAddAll(list, true);
	}

	/**
	 * 当前列表的size
	 */
	public int size() {
		Jedis jedis = null;
		boolean isSuccess = true;
		try {
			jedis = this.getDbJedisPool().getResource();
			return jedis.llen(this.key).intValue();
		} catch (Exception ex) {
			isSuccess = false;
			log.error(ex.getMessage(), ex);
		} finally {
			if (isSuccess) {
				this.getDbJedisPool().returnResource(jedis);
			} else {
				this.getDbJedisPool().returnBrokenResource(jedis);
			}
		}
		return 0;
	}

	/**
	 * 清除当前key中所有数据
	 */
	public void clear() {
		Jedis jedis = null;
		boolean isSuccess = true;
		try {
			jedis = this.getDbJedisPool().getResource();
			jedis.ltrim(this.key, 0, 0);
			jedis.lpop(this.key);
		} catch (Exception ex) {
			isSuccess = false;
			log.error(ex.getMessage(), ex);
		} finally {
			if (isSuccess) {
				this.getDbJedisPool().returnResource(jedis);
			} else {
				this.getDbJedisPool().returnBrokenResource(jedis);
			}
		}
	}

	/**
	 * 删除当前键
	 */
	public boolean deleteKey() {
		Jedis jedis = null;
		boolean isSuccess = true;
		try {
			jedis = this.getDbJedisPool().getResource();
			return jedis.del(this.key).intValue() > 0;
		} catch (Exception ex) {
			isSuccess = false;
			log.error(ex.getMessage(), ex);
		} finally {
			if (isSuccess) {
				this.getDbJedisPool().returnResource(jedis);
			} else {
				this.getDbJedisPool().returnBrokenResource(jedis);
			}
		}
		return false;
	}

}
