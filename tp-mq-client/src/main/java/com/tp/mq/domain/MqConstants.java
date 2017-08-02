package com.tp.mq.domain;

/**
 * @author szy
 *
 */
public interface MqConstants {

	String EXCHANGE_TYPE_FANOUT = "fanout";
	
	String EXCHANGE_TYPE_DIRECT = "direct";
	
	String EXCHANGE_TYPE_TOPIC = "topic";
	
	String EXCHANGE_TYPE_HEADERS = "headers";
	
	String SEND_MESSAGE_TYPE_P2P = "p2p";
	
	String SEND_MESSAGE_TYPE_PUB_SUB = "pub_sub";

}