package com.tp.redis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * @author szy
 *
 */
public class RedisRunnable implements Runnable {

	private static final Log logger = LogFactory.getLog(RedisRunnable.class);

	private volatile boolean flag = true;

	private Map<String, JedisShardInfo> defaultShardInfoMap = new HashMap<String, JedisShardInfo>();// durable

	private Map<String, CacheShardedJedisPool> cacheShardedJedisPoolMap = new HashMap<String, CacheShardedJedisPool>();

	public void run() {
		if (flag) {
			reloadPool();
		}
	}

	private synchronized void reloadPool() {
		List<RedisNode> allNodeList = watchAllNodes();
		List<RedisNode> successNodes = successNodes(allNodeList);
		List<RedisNode> failNodes = failNodes(allNodeList);

		if (!successNodes.isEmpty()) {
			for (Iterator<RedisNode> iterator = successNodes.iterator(); iterator.hasNext();) {
				RedisNode node = (RedisNode) iterator.next();
				if (alive_node_in_cache_sharded_jedis_pool_map(node)) {
					iterator.remove();
				}
			}
		}

		if (!failNodes.isEmpty()) {
			for (Iterator<RedisNode> it = failNodes.iterator(); it.hasNext();) {
				RedisNode node = (RedisNode) it.next();
				if (!fail_node_in_cache_sharded_jedis_pool_map(node)) {
					it.remove();
				}
			}
		}

		if (failNodes.size() > 0 || successNodes.size() > 0) {

			Map<String, JedisShardInfo> successJedisShardInfoMap = new HashMap<String, JedisShardInfo>();
			if (!successNodes.isEmpty()) {
				for (RedisNode node : successNodes) {
					for (Map.Entry<String, JedisShardInfo> entry : defaultShardInfoMap.entrySet()) {
						JedisShardInfo value = entry.getValue();
						if (node.getHost().equals(value.getHost()) && node.getPort() == value.getPort()) {
							successJedisShardInfoMap.put(entry.getKey(), value);
						}
					}
				}
			}

			for (Map.Entry<String, CacheShardedJedisPool> entry : cacheShardedJedisPoolMap.entrySet()) {

				CacheShardedJedisPool onePool = entry.getValue();
				List<JedisShardInfo> jedisShardInfoList = onePool.getJedisShardInfos();

				if (null != jedisShardInfoList && jedisShardInfoList.size() > 0) {
					StringBuffer sb = new StringBuffer("");
					for (JedisShardInfo j : jedisShardInfoList) {
						sb.append(buildMapKey(j.getHost(), j.getPort()));
						sb.append(";");
					}
					logger.info("*************************************************************************** before remove jedisShardInfoList all node is===>" + sb.toString());
				}

				if (null != jedisShardInfoList && !jedisShardInfoList.isEmpty()) {
					for (Iterator<JedisShardInfo> iterator = jedisShardInfoList.iterator(); iterator.hasNext();) {
						JedisShardInfo shardInfo = (JedisShardInfo) iterator.next();
						if (!failNodes.isEmpty()) {
							for (RedisNode node : failNodes) {
								if (shardInfo.getHost().equals(node.getHost()) && shardInfo.getPort() == node.getPort()) {
									iterator.remove();
									if (logger.isInfoEnabled()) {
										logger.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ remove fail node==>" + node.getHost() + ":" + node.getPort());
									}
								}
							}
						}
					}
				}

				if (null != jedisShardInfoList && jedisShardInfoList.size() > 0) {
					StringBuffer sb = new StringBuffer("");
					for (JedisShardInfo j : jedisShardInfoList) {
						sb.append(buildMapKey(j.getHost(), j.getPort()));
						sb.append(";");
					}
					logger.info("*************************************************************************** after remove jedisShardInfoList all node is===>" + sb.toString());
				}

				if (!successJedisShardInfoMap.isEmpty()) {
					List<JedisShardInfo> needInsertList = new ArrayList<JedisShardInfo>();
					for (Map.Entry<String, JedisShardInfo> en : successJedisShardInfoMap.entrySet()) {
						JedisShardInfo jsi = en.getValue();
						boolean isInLastShardInfo = false;
						for (JedisShardInfo jedisShardInfo : jedisShardInfoList) {
							if (jsi.getHost().equals(jedisShardInfo.getHost()) && jsi.getPort() == jedisShardInfo.getPort()) {
								isInLastShardInfo = true;
								break;
							}
						}
						if (!isInLastShardInfo) {
							needInsertList.add(jsi);
						}
					}

					if (!needInsertList.isEmpty()) {
						jedisShardInfoList.addAll(needInsertList);
						for (JedisShardInfo node : needInsertList) {
							if (logger.isInfoEnabled()) {
								logger.info("************************************************************************** add success node===>" + node.getHost() + ":" + node.getPort());
							}
						}
					}

				}

				if (null != jedisShardInfoList && jedisShardInfoList.size() > 0) {
					onePool.setJedisShardInfos(jedisShardInfoList);
					if (logger.isInfoEnabled()) {
						StringBuffer sb = new StringBuffer("");
						for (JedisShardInfo j : jedisShardInfoList) {
							sb.append(buildMapKey(j.getHost(), j.getPort()));
							sb.append(";");
						}
						logger.info("*************************************************************************** reloadPool() start contains all node in===>" + sb.toString());
					}
					onePool.reloadPool();
					if (logger.isInfoEnabled()) {
						logger.info("*************************************************************************** reloadPool() end size===>" + jedisShardInfoList.size());
					}
				}

			}
		}

	}

	private List<RedisNode> watchAllNodes() {
		List<RedisNode> nodeList = new ArrayList<RedisNode>();
		for (Map.Entry<String, JedisShardInfo> entry : defaultShardInfoMap.entrySet()) {
			String host = entry.getValue().getHost();
			int port = entry.getValue().getPort();
			boolean alive = watchOneNode(host, port);
			RedisNode node = new RedisNode();
			node.setAlive(alive);
			node.setHost(host);
			node.setPort(port);
			nodeList.add(node);
		}
		return nodeList;
	}

	private List<RedisNode> failNodes(List<RedisNode> nodeList) {
		List<RedisNode> failNodes = new ArrayList<RedisNode>();
		if (!nodeList.isEmpty()) {
			for (RedisNode node : nodeList) {
				boolean alive = node.isAlive();
				if (!alive) {
					failNodes.add(node);
				}
			}
		}
		return failNodes;
	}

	private List<RedisNode> successNodes(List<RedisNode> nodeList) {
		List<RedisNode> successNodes = new ArrayList<RedisNode>();
		if (!nodeList.isEmpty()) {
			for (RedisNode node : nodeList) {
				boolean alive = node.isAlive();
				if (alive) {
					successNodes.add(node);
				}
			}
		}
		return successNodes;
	}

	private boolean alive_node_in_cache_sharded_jedis_pool_map(RedisNode node) {
		boolean operator = false;
		for (Map.Entry<String, JedisShardInfo> entry : defaultShardInfoMap.entrySet()) {
			JedisShardInfo value = entry.getValue();
			if (node.getHost().equals(value.getHost()) && node.getPort() == value.getPort()) {
				operator = true;
				break;
			}
		}

		boolean isInCachePool = false;
		for (Map.Entry<String, CacheShardedJedisPool> entry : cacheShardedJedisPoolMap.entrySet()) {
			CacheShardedJedisPool onePool = entry.getValue();
			List<JedisShardInfo> jedisShardInfoList = onePool.getJedisShardInfos();
			for (Iterator<JedisShardInfo> iterator = jedisShardInfoList.iterator(); iterator.hasNext();) {
				JedisShardInfo shardInfo = (JedisShardInfo) iterator.next();
				if (shardInfo.getHost().equals(node.getHost()) && shardInfo.getPort() == node.getPort()) {
					isInCachePool = true;
					break;
				}
			}

		}

		if (operator && isInCachePool) {
			return true;
		}

		return false;
	}

	private boolean fail_node_in_cache_sharded_jedis_pool_map(RedisNode node) {
		boolean operator = false;
		for (Map.Entry<String, JedisShardInfo> entry : defaultShardInfoMap.entrySet()) {
			JedisShardInfo value = entry.getValue();
			if (node.getHost().equals(value.getHost()) && node.getPort() == value.getPort()) {
				operator = true;
				break;
			}
		}

		boolean isInCachePool = false;
		for (Map.Entry<String, CacheShardedJedisPool> entry : cacheShardedJedisPoolMap.entrySet()) {
			CacheShardedJedisPool onePool = entry.getValue();
			List<JedisShardInfo> jedisShardInfoList = onePool.getJedisShardInfos();
			for (Iterator<JedisShardInfo> iterator = jedisShardInfoList.iterator(); iterator.hasNext();) {
				JedisShardInfo shardInfo = (JedisShardInfo) iterator.next();
				if (shardInfo.getHost().equals(node.getHost()) && shardInfo.getPort() == node.getPort()) {
					isInCachePool = true;
					break;
				}
			}
		}

		if (operator && isInCachePool) {
			return true;
		}

		return false;
	}

	private String buildMapKey(String host, int port) {
		return host + ":" + port;
	}

	private long getOnceTimeOut(long waitTime, long timeout) {
		long onceTimeOut = 2000;
		long remainTime = timeout - waitTime;
		if (onceTimeOut > remainTime) {
			onceTimeOut = remainTime;
		}
		return onceTimeOut;
	}

	// private boolean watchOneNode(String host, int port) {
	// /* debug enable才打印日志，否则日志日久会积压太多 */
	// Jedis jedis = null;
	// int timeout = 8000;
	// long waitTime = 0;
	// int retryTimes = 3;
	// // if (logger.isDebugEnabled() && !isWindows()) {
	// // logger.debug("watch retry times is=>" + retryTimes);
	// // }
	//
	// int tryCount = 0;
	// int failTimes = 0;
	// while (0 == timeout || timeout > waitTime) {
	// tryCount++;
	// if (tryCount > retryTimes + 1) {
	// // if (logger.isDebugEnabled() && !isWindows()) {
	// // logger.warn("已经到达了设定的重试次数" + retryTimes + "次");
	// // }
	// break;
	// }
	// // if (logger.isDebugEnabled() && !isWindows()) {
	// // logger.debug("watch redis host===>" + host + ":" + port + "，第" +
	// // tryCount + "次尝试, waitTime:" + waitTime);
	// // }
	//
	// long onceTimeOut = getOnceTimeOut(waitTime, timeout);
	// waitTime += onceTimeOut;
	//
	// try {
	// jedis = new Jedis(host, port, 2000);
	// String key = "jedis_listen_key_" + host + port;
	// String isOk = jedis.set(key, "1");
	// if (null != isOk && !isOk.equalsIgnoreCase("OK")) {
	// failTimes++;
	// } else {
	// jedis.del(key);
	// }
	// } catch (Exception e) {
	// failTimes++;
	// // if (logger.isDebugEnabled() && !isWindows()) {
	// // logger.error(e.getMessage(), e);
	// // }
	// } finally {
	// if (null != jedis) {
	// try {
	// jedis.quit();
	// } catch (Exception e) {
	// // ingone
	// }
	// try {
	// jedis.disconnect();
	// } catch (Exception e) {
	// // ingone
	// }
	// try {
	// jedis.close();
	// } catch (Exception e) {
	// // ingone
	// }
	// }
	// }
	// }
	// if (failTimes >= tryCount) {
	// // if (logger.isDebugEnabled() && !isWindows()) {
	// // logger.info("watch fail node redis ====>" + host + ":" + port +
	// // "");
	// // }
	// return false;
	// }
	// return true;
	// }

	private boolean watchOneNode(String host, int port) {// 连续三次超时或连续8次操作返回的都失败，认为jedis不可用
		int failTimes = 0;
		int count = 8;
		int connect_time_out_times = 0;

		Jedis jedis = null;
		for (int i = 0; i < count; i++) {
			try {
				jedis = new Jedis(host, port, 3000);
				String key = "jedis_listen_key_" + host + port;
				String isOk = jedis.set(key, "1");
				if (null != isOk && !isOk.equalsIgnoreCase("OK")) {
					failTimes++;
				} else {
					jedis.del(key);
				}
			} catch (JedisConnectionException e) {
				connect_time_out_times++;
				failTimes++;
			} catch (Exception e) {
				failTimes++;
			} finally {
				closeRedis(jedis);
			}

			if (connect_time_out_times == 3) {
				return false;
			}

			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				logger.error(e.getMessage(), e);
			}
		}
		if (failTimes >= count) {
			return false;
		}
		return true;
	}

	private static void closeRedis(Jedis jedis) {
		if (null != jedis) {
			try {
				jedis.quit();
			} catch (Exception e) {
				// ingone
			}
			try {
				jedis.disconnect();
			} catch (Exception e) {
				// ingone
			}
			try {
				jedis.close();
			} catch (Exception e) {
				// ingone
			}
		}
	}

	private final static boolean isWindows() {
		String osName = System.getProperty("os.name");
		if (osName.toLowerCase().startsWith("windows")) {
			return true;
		}
		return false;
	}

	public void setDefaultShardInfoMap(Map<String, JedisShardInfo> defaultShardInfoMap) {
		this.defaultShardInfoMap = defaultShardInfoMap;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public void setCacheShardedJedisPoolMap(Map<String, CacheShardedJedisPool> poolsMap) {
		this.cacheShardedJedisPoolMap = poolsMap;
	}

}
