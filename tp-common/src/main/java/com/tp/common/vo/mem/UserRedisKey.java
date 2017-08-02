package com.tp.common.vo.mem;

public enum UserRedisKey {
	
	FAVORITEPROMOTION(0, "favoritePromotion");
	 
	// 成员变量
     public Integer key;
     public String value;

     // 构造方法
     private UserRedisKey(Integer key,String value) {
         this.key = key;
         this.value = value;
     }

     // 普通方法
     public static String getValue(int key) {
         for (UserRedisKey c : UserRedisKey.values()) {
             if (c.key == key) {
                 return c.value;
             }
         }
         return null;
     }

	
}
