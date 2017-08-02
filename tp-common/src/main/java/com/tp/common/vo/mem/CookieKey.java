package com.tp.common.vo.mem;

public enum CookieKey {
	ACCOUNT(0,"account"),
	SHOW_SECURITY_CODE(1, "isShowSecurityCode"),
	LOGIN_UID(2, "uid"),
	BIND_MOBILE_REMIND(3,"bind_mobile_remind");
	 
	// 成员变量
    private Integer key;
    private String value;

    // 构造方法
    private CookieKey(Integer key,String value) {
       this.key = key;
       this.value = value;
    }

     // 普通方法
     public static String getValue(int key) {
         for (CookieKey c : CookieKey.values()) {
             if (c.getKey() == key) {
                 return c.getValue();
             }
         }
         return null;
     }

	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
