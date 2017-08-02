package com.tp.mq.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rabbitmq.client.Address;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.tp.mq.domain.MqBaseConfig;

/**
 * @author szy
 *
 */
public class MqConsumerConnectionSingleton {

	private static final Log logger = LogFactory.getLog(MqConsumerConnectionSingleton.class);

	private static class ConnectionHolder {
		private static final ConcurrentHashMap<Long, Connection> holder = new ConcurrentHashMap<Long, Connection>();
	}

	private static class ConnectionCntHolder {
		private static final int connectionCount = MqBaseConfig.getBaseConfig().getConsumerConnectionCount();
	}

	private static class ExecutorServiceHolder {

		private static final ExecutorService executorService;

		private static final ConnectionFactory factory;

		private static final Address[] addrArr;

		static {
			addrArr = getAddressArray(MqBaseConfig.getBaseConfig().getHosts().trim());
			int threadCount = MqBaseConfig.getBaseConfig().getConsumerThreadCount();
			executorService = Executors.newFixedThreadPool(threadCount);

			factory = new ConnectionFactory();
			String username = MqBaseConfig.getBaseConfig().getUsername().trim();
			String password = MqBaseConfig.getBaseConfig().getPassword().trim();

			factory.setUsername(username);
			factory.setPassword(password);
			factory.setRequestedHeartbeat(MqBaseConfig.getBaseConfig().getRequestedHeartbeat());
			factory.setAutomaticRecoveryEnabled(MqBaseConfig.getBaseConfig().isAutomaticRecoveryEnabled());
		}

		private static Address[] getAddressArray(String addrArrStr) {
			addrArrStr = addrArrStr.trim();
			String[] addArr = addrArrStr.split(",");
			Address[] addrArr = new Address[addArr.length];

			int i = 0;
			for (String info : addArr) {
				String[] infoArr = info.split(":");
				addrArr[i] = new Address(infoArr[0], java.lang.Integer.parseInt(infoArr[1]));
				i++;
			}
			return addrArr;
		}

		public static Connection getConnection(Long prefix) {
			Connection connection = null;
			try {
				connection = factory.newConnection(executorService, addrArr);
			} catch (Exception e) {
				logger.error(e.toString());
			}
			return connection;
		}

		public static void shutDownPool() {
			try {
				if (null != executorService) {
					executorService.shutdown();
				}
			} catch (Exception e) {
				logger.error("MqConsumerConnectionSingleton shutDownPool exception for=>" + e.toString());
			}
		}

	}

	public static Connection connection() {
		long curr = RandomUtils.nextInt(0, ConnectionCntHolder.connectionCount);
		if (!isOk(ConnectionHolder.holder.get(curr))) {
			synchronized (MqConsumerConnectionSingleton.class) {
				if (!isOk(ConnectionHolder.holder.get(curr))) {
					destory(curr);
					Connection connection = ExecutorServiceHolder.getConnection(curr);
					if (connection != null) {
						ConnectionHolder.holder.put(curr, connection);
					}
				}
			}
		}
		return ConnectionHolder.holder.get(curr);
	}

	public static boolean isOk(Connection connection) {
		if (connection == null) {
			return false;
		} else {
			if (connection.isOpen()) {
				return true;
			} else {
				return false;
			}
		}
	}

	public static void destoryall() {
		for (Map.Entry<Long, Connection> entry : ConnectionHolder.holder.entrySet()) {
			destory(entry.getKey());
			if (logger.isDebugEnabled()) {
				logger.debug(("mq product entry key is " + entry.getKey()));
			}
		}
	}

	public static void destoryallDAndShutDown() {
		destoryall();
		ExecutorServiceHolder.shutDownPool();
	}

	private static void destory(Long curr) {
		Connection connection = ConnectionHolder.holder.get(curr);
		if (connection != null) {
			try {
				connection.abort();
			} catch (Exception e) {
				logger.error(e.toString());
			}
			try {
				connection.close();
			} catch (Exception e) {
				logger.error(e.toString());
			}
		}
		ConnectionHolder.holder.remove(curr);
	}

	private MqConsumerConnectionSingleton() {

	}

	public static String addArrInfo(Address[] addrArr) {
		StringBuffer sbf = new StringBuffer();
		for (Address info : addrArr) {
			sbf.append(" host=");
			sbf.append(info.getHost());
			sbf.append(",port=");
			sbf.append(info.getPort());
		}
		return sbf.toString();
	}

}
