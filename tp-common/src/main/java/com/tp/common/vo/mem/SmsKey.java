package com.tp.common.vo.mem;

public enum SmsKey {
	REGISTER(0,"register"),
	UPT_PWD(1, "updatePwd"),
	BIND_PHONE(2, "bindPhone"),
	QUICK_REGISTER(3, "quickRegister");
	 
	// 成员变量
    private Integer key;
    private String value;

    // 构造方法
    private SmsKey(Integer key,String value) {
       this.key = key;
       this.value = value;
    }

     // 普通方法
     public static String getValue(int key) {
         for (SmsKey c : SmsKey.values()) {
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
