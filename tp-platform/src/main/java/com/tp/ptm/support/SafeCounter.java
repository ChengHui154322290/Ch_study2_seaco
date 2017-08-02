package com.tp.ptm.support;
//package com.meitun.platform.support;
//
//import java.util.Date;
//
///**
// * 线程安全的计数器
// * 
// * @author 项硕
// * @version 2015年5月15日 下午10:09:38
// */
//public final class SafeCounter {
//
//	private volatile long startTime;	// 起始时间毫秒数
//	private volatile int value;	// 计数值
//	private final long seconds;	// 间隔秒数
//	private final int times;	// 限制次数
//	
//	/**
//	 * 根据{seconds}秒之内允许{times}访问构造计数器
//	 * 
//	 * @param seconds
//	 * @param times
//	 */
//	public SafeCounter(long seconds, int times) {
//		this.seconds = seconds;
//		this.times = times;
//		this.startTime = new Date().getTime();
//		this.value = 0;
//	}
//
//	public synchronized boolean verifyFrequency() {
//		if (value > times) {
//			if (new Date().getTime() - startTime < seconds * 1000) {
//				
//			}
//		}
//	}
//}
