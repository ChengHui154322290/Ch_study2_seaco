package com.tp.mq.util;

public class MqStringUtil {

	public static boolean isNotBlank(String str) {
		return (null != str && !"".equals(str));
	}
	
	public static boolean isBlank(String str) {
		return !isNotBlank(str);
	}

}
