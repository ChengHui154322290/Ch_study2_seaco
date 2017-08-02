package com.tp.mq;

/**
 * 接收消息(主题订阅消息及点对点都可以)回调接口,实现此接口的类必须是线程安全的
 * @author szy
 *
 */
public interface MqMessageCallBack {

	/**
	 * 方法执行成功 返回true 失败返回false 注意实现此方法上打下重要的业务日志,业务系统必须能够处理重复消息
	 * @param o
	 */
	boolean execute(Object o);

}
