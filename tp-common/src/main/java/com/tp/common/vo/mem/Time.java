package com.tp.common.vo.mem;

public enum Time {
	
	/**
	 * 一分钟
	 */
	ONE_MINUTE("一分钟",60L),
	
	/**
	 * 十分钟
	 */
	TEN_MINUTE("十分钟",10*ONE_MINUTE.value),
	
	/**
	 * 三十分钟
	 */
	THIRTY_MINUTE("三十分钟",30*ONE_MINUTE.value),
	
	/**
	 * 一小时
	 */
	ONE_HOUR("一小时",60*ONE_MINUTE.value),
	
	/**
	 * 一天
	 */
	ONE_DAY("一天",24*ONE_HOUR.value),
	 
	/**
	 * 一周
	 */
	ONE_WEEK("一周",7*ONE_DAY.value),
	
	/**
	 * 三十天
	 */
	ONE_MONTH("三十天",30*ONE_DAY.value),
	
	/**
	 * 一年
	 */
	ONE_YEAR("一年",365*ONE_DAY.value);
	// 成员变量
     public String key;
     public Long value;

     // 构造方法
     private Time(String key,Long value) {
         this.key = key;
         this.value = value;
     }

     // 普通方法
     public static Long getValue(String key) {
         for (Time c : Time.values()) {
             if (c.key.equals(key)) {
                 return c.value;
             }
         }
         return null;
     }
}
