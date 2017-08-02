package com.tp.redis;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.core.PriorityOrdered;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.util.Hashing;
import redis.clients.util.Pool;

/**
 * 适用于把redis当成cache来使用的连接池
 * @author szy
 *
 */
public class CacheShardedJedisPool extends Pool<ShardedJedis> implements PriorityOrdered {

	private volatile List<JedisShardInfo> jedisShardInfos;

	private GenericObjectPoolConfig poolConfig;

	private Hashing algo;

	private Pattern keyTagPattern;
	
	private volatile ShardedJedisFactory factory;

	public CacheShardedJedisPool(final GenericObjectPoolConfig poolConfig, List<JedisShardInfo> shards) {
		this(poolConfig, shards, Hashing.MURMUR_HASH);
	}

	public CacheShardedJedisPool(final GenericObjectPoolConfig poolConfig, List<JedisShardInfo> shards, Hashing algo) {
		this(poolConfig, shards, algo, null);
	}

	public CacheShardedJedisPool(final GenericObjectPoolConfig poolConfig, List<JedisShardInfo> shards, Pattern keyTagPattern) {
		this(poolConfig, shards, Hashing.MURMUR_HASH, keyTagPattern);
	}

	public CacheShardedJedisPool(final GenericObjectPoolConfig poolConfig, List<JedisShardInfo> shards, Hashing algo, Pattern keyTagPattern) {
		this.algo = algo;
		this.keyTagPattern = keyTagPattern;
		this.jedisShardInfos = shards;
		if (null == this.factory) {
			this.factory = new ShardedJedisFactory(shards, algo, keyTagPattern);
			initPool(poolConfig, this.factory);
		} else {
			factory.setShards(shards);
			internalPool.clear();
		}
	}

	@Override
	public ShardedJedis getResource() {
		ShardedJedis jedis = super.getResource();
		jedis.setDataSource(this);
		return jedis;
	}

	@Override
	public void returnBrokenResource(final ShardedJedis resource) {
		if (resource != null) {
			returnBrokenResourceObject(resource);
		}
	}

	@Override
	public void returnResource(final ShardedJedis resource) {
		if (resource != null) {
			resource.resetState();
			returnResourceObject(resource);
		}
	}

	public void reloadPool() {
		factory.setShards(this.jedisShardInfos);
		internalPool.clear();
	}

	public List<JedisShardInfo> getJedisShardInfos() {
		return jedisShardInfos;
	}

	public void setJedisShardInfos(List<JedisShardInfo> jedisShardInfos) {
		this.jedisShardInfos = jedisShardInfos;
	}

	public GenericObjectPoolConfig getPoolConfig() {
		return poolConfig;
	}

	public void setPoolConfig(GenericObjectPoolConfig poolConfig) {
		this.poolConfig = poolConfig;
	}

	/**
	 * PoolableObjectFactory custom impl.
	 */
	private static class ShardedJedisFactory implements PooledObjectFactory<ShardedJedis> {
		private List<JedisShardInfo> shards;
		
		public void setShards(List<JedisShardInfo> shards) {
			this.shards = shards;
		}

		private Hashing algo;
		private Pattern keyTagPattern;

		public ShardedJedisFactory(List<JedisShardInfo> shards, Hashing algo, Pattern keyTagPattern) {
			this.shards = shards;
			this.algo = algo;
			this.keyTagPattern = keyTagPattern;
		}

		@Override
		public PooledObject<ShardedJedis> makeObject() throws Exception {
			ShardedJedis jedis = new ShardedJedis(shards, algo, keyTagPattern);
			return new DefaultPooledObject<ShardedJedis>(jedis);
		}

		@Override
		public void destroyObject(PooledObject<ShardedJedis> pooledShardedJedis) throws Exception {
			final ShardedJedis shardedJedis = pooledShardedJedis.getObject();
			for (Jedis jedis : shardedJedis.getAllShards()) {
				try {
					try {
						jedis.quit();
					} catch (Exception e) {

					}
					jedis.disconnect();
				} catch (Exception e) {

				}
			}
		}

		@Override
		public boolean validateObject(PooledObject<ShardedJedis> pooledShardedJedis) {
			try {
				ShardedJedis jedis = pooledShardedJedis.getObject();
				for (Jedis shard : jedis.getAllShards()) {
					if (!shard.ping().equals("PONG")) {
						return false;
					}
				}
				return true;
			} catch (Exception ex) {
				return false;
			}
		}

		@Override
		public void activateObject(PooledObject<ShardedJedis> p) throws Exception {

		}

		@Override
		public void passivateObject(PooledObject<ShardedJedis> p) throws Exception {

		}
	}

	@Override
	public int getOrder() {
		return LOWEST_PRECEDENCE - 10;
	}

}
