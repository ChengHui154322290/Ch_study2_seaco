package com.tp.mq.listener;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.tp.mq.ConsumerP2pMessage;
import com.tp.mq.ConsumerTopicMessage;
import com.tp.mq.MqMessageCallBack;
import com.tp.mq.domain.ConsumerSchedulQueueConfigs;
import com.tp.mq.domain.MqConsumerP2pConfig;
import com.tp.mq.domain.MqConsumerTopicConfig;
import com.tp.mq.domain.MqMessageConfigs;
import com.tp.mq.domain.MqP2pConfig;
import com.tp.mq.domain.MqTopicConfig;
import com.tp.mq.util.MqConsumerConnectionSingleton;
import com.tp.mq.util.MqProductConnectionSingleton;

/**
 * p2p and topic message web listener
 * 
 * @author szy
 *
 */
public class RabbitMqMessageListener implements ServletContextListener {

	private static final Log logger = LogFactory.getLog(RabbitMqMessageListener.class);

	private static final String CHAR = "------------------------------------------";

	private ConcurrentMap<String, ConsumerP2pMessage> p2pMessageMap = new ConcurrentHashMap<String, ConsumerP2pMessage>();

	private ConcurrentMap<String, ConsumerTopicMessage> topicMessageMap = new ConcurrentHashMap<String, ConsumerTopicMessage>();

	private ConcurrentMap<String, Thread> p2pThreadMap = new ConcurrentHashMap<String, Thread>();

	private ConcurrentMap<String, Thread> topicThreadMap = new ConcurrentHashMap<String, Thread>();

	private MqMessageConfigs messageConfigs;

	private ConsumerSchedulQueueConfigs consumerSchedulQueueConfigs;

	public void contextInitialized(ServletContextEvent servletContextEvent) {

		WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContextEvent.getServletContext());
		consumerSchedulQueueConfigs = (ConsumerSchedulQueueConfigs) webApplicationContext.getBean("consumerSchedulQueueConfigs");// 业务bean
		messageConfigs = (MqMessageConfigs) webApplicationContext.getBean("messageConfigs");

		if (null != consumerSchedulQueueConfigs) {
			List<MqConsumerP2pConfig> p2pConfigListeners = consumerSchedulQueueConfigs.getP2pConfigListeners();
			if (null != p2pConfigListeners) {
				for (MqConsumerP2pConfig p2pConfig : p2pConfigListeners) {

					if (null != p2pConfig && null != p2pConfig.getP2pConfig()) {
						MqP2pConfig c = p2pConfig.getP2pConfig();
						if (null != c) {
							String queueName = c.getQueueName();
							if (null != queueName && !"".equals(queueName.trim())) {
								MqMessageCallBack callback = p2pConfig.getMessageCallBack();

								ConsumerP2pMessage p2p = new ConsumerP2pMessage();
								p2p.setQueueName(queueName);
								p2p.setMessageCallBack(callback);
								p2p.setMessageConfigs(messageConfigs);
								p2p.setRecheckCount(p2pConfig.getRecheckCount() > 2 ? 2 : p2pConfig.getRecheckCount());
								p2p.setRecheckIntervalTime(p2pConfig.getRecheckIntervalTime() > 500 ? 500 : p2pConfig.getRecheckIntervalTime());

								Thread p2pThread = new Thread(p2p);
								p2pThread.setDaemon(true);

								String threadName = "p2p-thread-" + queueName;
								p2pThread.setName(threadName);
								p2pMessageMap.put(threadName, p2p);
								p2pThreadMap.put(threadName, p2pThread);

								p2pThread.start();
							}
						}
					}
				}
			}

			List<MqConsumerTopicConfig> topicConfigListeners = consumerSchedulQueueConfigs.getTopicConfigListeners();
			if (null != topicConfigListeners) {
				for (MqConsumerTopicConfig topicConfig : topicConfigListeners) {
					if (null != topicConfig) {
						MqTopicConfig tConfig = topicConfig.getTopicConfig();
						if (null != tConfig) {
							String queueName = tConfig.getQueueName();
							if (null != queueName && !"".equals(queueName.trim())) {
								MqMessageCallBack callback = topicConfig.getMessageCallBack();
								ConsumerTopicMessage topic = new ConsumerTopicMessage();
								topic.setQueueName(queueName);
								topic.setMessageCallBack(callback);
								topic.setMessageConfigs(messageConfigs);

								Thread topicThread = new Thread(topic);
								topicThread.setDaemon(true);

								String name = "topic-thread-" + queueName;
								topicThread.setName(name);
								topicMessageMap.put(name, topic);
								topicThreadMap.put(name, topicThread);

								topicThread.start();
							}
						}
					}
				}
			}
		}
	}

	public void contextDestroyed(ServletContextEvent context) {
		if (null != p2pMessageMap && !p2pMessageMap.isEmpty()) {
			for (Map.Entry<String, ConsumerP2pMessage> entry : p2pMessageMap.entrySet()) {
				ConsumerP2pMessage p2p = entry.getValue();
				p2p.stopRequest();
			}
		}
		try {
			TimeUnit.MILLISECONDS.sleep(50);
		} catch (InterruptedException e1) {
			logger.error(CHAR + " TimeUnit.MILLISECONDS.sleep(50); " + e1.getMessage() + CHAR);
		}
		if (null != p2pThreadMap && !p2pThreadMap.isEmpty()) {
			for (Map.Entry<String, Thread> entry : p2pThreadMap.entrySet()) {
				Thread thread = entry.getValue();
				try {
					if (null != thread) {
						thread.interrupt();
					}
				} catch (SecurityException e) {
					logger.error(CHAR + "thread name=>" + thread.getName() + " interrupt exception for " + e.getMessage() + CHAR);
				}
			}
		}

		if (null != topicMessageMap && !topicMessageMap.isEmpty()) {
			for (Map.Entry<String, ConsumerTopicMessage> entry : topicMessageMap.entrySet()) {
				ConsumerTopicMessage topic = entry.getValue();
				topic.stopRequest();
			}
		}
		try {
			TimeUnit.MILLISECONDS.sleep(50);
		} catch (InterruptedException e1) {
			logger.error(CHAR + " TimeUnit.MILLISECONDS.sleep(50); " + e1.getMessage() + CHAR);
		}
		if (null != topicThreadMap && !topicThreadMap.isEmpty()) {
			for (Map.Entry<String, Thread> entry : topicThreadMap.entrySet()) {
				Thread thread = entry.getValue();
				try {
					if (null != thread) {
						thread.interrupt();
					}
				} catch (SecurityException e) {
					logger.error(CHAR + "thread name=>" + thread.getName() + " interrupt exception for " + e.getMessage() + CHAR);
				}
			}
		}

		try {
			TimeUnit.MILLISECONDS.sleep(200);
		} catch (InterruptedException e1) {
			logger.error(CHAR + "TimeUnit.MILLISECONDS.sleep(200) exception for " + e1.getMessage() + CHAR);
		}

		if (null != p2pMessageMap && !p2pMessageMap.isEmpty()) {
			for (Map.Entry<String, ConsumerP2pMessage> entry : p2pMessageMap.entrySet()) {
				p2pMessageMap.remove(entry.getKey());
			}
		}

		if (null != topicMessageMap && !topicMessageMap.isEmpty()) {
			for (Map.Entry<String, ConsumerTopicMessage> entry : topicMessageMap.entrySet()) {
				topicMessageMap.remove(entry.getKey());
			}
		}

		if (null != p2pThreadMap && !p2pThreadMap.isEmpty()) {
			for (Map.Entry<String, Thread> entry : p2pThreadMap.entrySet()) {
				p2pThreadMap.remove(entry.getKey());
			}
		}

		if (null != topicThreadMap && !topicThreadMap.isEmpty()) {
			for (Map.Entry<String, Thread> entry : topicThreadMap.entrySet()) {
				topicThreadMap.remove(entry.getKey());
			}
		}

		try {
			TimeUnit.MILLISECONDS.sleep(200);
		} catch (InterruptedException e1) {
			logger.error("TimeUnit.MILLISECONDS.sleep(200) exception for " + e1.getMessage());
		}

		try {
			MqProductConnectionSingleton.destoryallDAndShutDown();
			logDebug(CHAR + "MqProductConnectionSingleton.destoryall()" + CHAR);
		} catch (Exception e) {
			logger.error(CHAR + "MqProductConnectionSingleton.destoryall() exception for" + CHAR);
		}

		try {
			MqConsumerConnectionSingleton.destoryallDAndShutDown();
			logDebug(CHAR + "MqConsumerConnectionSingleton.destoryall() " + CHAR);
		} catch (Exception e) {
			logger.error(CHAR + "MqConsumerConnectionSingleton.destoryall() exception for " + e.getMessage() + CHAR);
		}

	}

	private static void logDebug(String msg) {
		if (logger.isDebugEnabled()) {
			logger.debug(msg);
		}
	}

}