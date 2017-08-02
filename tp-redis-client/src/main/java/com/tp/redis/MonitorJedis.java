package com.tp.redis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.PriorityOrdered;

import redis.clients.jedis.JedisShardInfo;

/**
 * 单个项目中只支持将 CacheShardedJedisPool.class配置一个spring bean
 * 
 * @author szy
 *
 */
public class MonitorJedis implements DisposableBean, BeanFactoryPostProcessor, PriorityOrdered {

	private static final Log logger = LogFactory.getLog(MonitorJedis.class);

	private Map<String, JedisShardInfo> jedisShardInfoMap = new HashMap<String, JedisShardInfo>();

	private Map<String, CacheShardedJedisPool> cacheShardedJedisPoolMap = new HashMap<String, CacheShardedJedisPool>();

	private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

	private int scheduleDelay = 1000;

	private RedisRunnable redisRunnable;

	public MonitorJedis() {

	}

	private List<RedisNode> defaultAliveNodes() {
		List<RedisNode> nodeList = new ArrayList<RedisNode>();
		if (!jedisShardInfoMap.isEmpty()) {
			for (Map.Entry<String, JedisShardInfo> entry : jedisShardInfoMap.entrySet()) {
				String host = entry.getValue().getHost();
				int port = entry.getValue().getPort();
				RedisNode node = new RedisNode();
				node.setAlive(true);
				node.setHost(host);
				node.setPort(port);
				nodeList.add(node);
			}
		}
		return nodeList;
	}

	public void watcher() {
		if (logger.isInfoEnabled()) {
			logger.info("=========================================watch redis scheduler start=========================================" + jedisShardInfoMap.size());
		}
		redisRunnable = new RedisRunnable();
		redisRunnable.setCacheShardedJedisPoolMap(cacheShardedJedisPoolMap);
		redisRunnable.setDefaultShardInfoMap(jedisShardInfoMap);
		scheduledExecutorService.scheduleWithFixedDelay(redisRunnable, 500, scheduleDelay, TimeUnit.MILLISECONDS);
	}

	@Override
	public int getOrder() {
		return LOWEST_PRECEDENCE - 11;
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		jedisShardInfoMap = beanFactory.getBeansOfType(redis.clients.jedis.JedisShardInfo.class);
		cacheShardedJedisPoolMap = beanFactory.getBeansOfType(com.tp.redis.CacheShardedJedisPool.class);
		this.watcher();
	}

	@Override
	public void destroy() throws Exception {
		if (null != redisRunnable) {
			redisRunnable.setFlag(false);
		}
		scheduledExecutorService.shutdown();
		if (logger.isInfoEnabled()) {
			logger.info("=========================================watch redis scheduler shutdown=========================================");
		}
	}

	public int getScheduleDelay() {
		return scheduleDelay;
	}

	public void setScheduleDelay(int scheduleDelay) {
		this.scheduleDelay = scheduleDelay;
	}

}
