package com.tp.common.vo.mem;

public enum MemberToken {
	BIND_EMAIL("bind_email","bind_email");
	 
	// 成员变量
     public String key;
     public String value;

     // 构造方法
     private MemberToken(String key,String value) {
         this.key = key;
         this.value = value;
     }

     // 普通方法
     public static String getValue(String key) {
         for (MemberToken c : MemberToken.values()) {
             if (c.key.equals(key)) {
                 return c.value;
             }
         }
         return null;
     }
}
