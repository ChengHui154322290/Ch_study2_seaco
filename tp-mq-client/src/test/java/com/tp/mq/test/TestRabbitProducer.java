package com.tp.mq.test;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.tp.mq.RabbitMqProducer;
import com.tp.mq.exception.MqClientException;
import com.tp.mq.util.MqProductConnectionSingleton;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:spring.xml" })
public class TestRabbitProducer {

	private final static String TASK_QUEUE_NAME = "order_task_queue_p2p";

	private final static String TOPIC_TASK_QUEUE_NAME = "order_task_queue_topic_1";

	@Autowired
	RabbitMqProducer rabbitMqProducer;

	@Test
	public void testP2p() throws InterruptedException {//发送点对点消息
		for (int i = 0; i < 2; i++) {
			String message = getP2pMessage() + i;
			try {
				rabbitMqProducer.sendP2PMessage(TASK_QUEUE_NAME, message);
			} catch (MqClientException e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void testPubSub() throws InterruptedException {//发送主题 订阅消息
//		String message = getTopicMessage();
//		try {
//			rabbitMqProducer.sendTopicMessage(TOPIC_TASK_QUEUE_NAME, message);
//		} catch (MqClientException e) {
//			e.printStackTrace();
//		}
//		System.out.println("-------");
	}

	@After
	public void test() {
		 MqProductConnectionSingleton.destoryall();
		System.out.println("----After running ---");
	}

	private static String getTopicMessage() {
		return "rabbitmq-cient send topic message , hello world !";
	}

	private static String getP2pMessage() {//
		return "welcome china ========!";
	}

}
