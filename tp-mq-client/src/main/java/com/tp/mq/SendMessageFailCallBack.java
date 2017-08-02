package com.tp.mq;

import com.rabbitmq.client.AMQP.BasicProperties;

/**
 * 发送消息失败时回调接口，客户端自己实现
 * @author szy
 *
 */
public interface SendMessageFailCallBack {

	/**
	 * 此接口请不要抛出异常
	 * @param o
	 */
	boolean execute(int replyCode, String replyText, String exchange, String routingKey,
			BasicProperties properties, byte[] body);

}
