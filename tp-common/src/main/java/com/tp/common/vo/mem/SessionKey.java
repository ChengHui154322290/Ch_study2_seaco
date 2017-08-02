package com.tp.common.vo.mem;

public enum SessionKey {
	
	LOGIN(0, "loginSecurityCode"),
	REGISTER(1, "registerSecurityCode"),
	VALIDATECODE(2, "validateCode"),
	SMSCODE(3, "smsCode"),
	SMSPWDCODE(5, "smsPwdCode"),
	BINDEMAIL(6, "bindEmail"),
	OTHER(999, "other.unSelect"),
	APP_UPDATE_PASSWORD(0, "app_upt_sms_code"),
	APP_REGISTER(1, "app_register_sms_code"),
	APP_BINDPHONE(7, "app_bind_phone_sms_code"),
	REGISTER_DSS(8, "register_dss_sms_code"),
	UNION_BINDMOBILE(9, "union_bindmbl_sms_code"),
	RECEIVE_COUPON(10, "app_receive_coupon_sms_code"),
	MODIFY_MOBILE(11,"modify_mobile");
	
	// 成员变量
     public Integer key;
     public String value;

     // 构造方法
     private SessionKey(Integer key,String value) {
         this.key = key;
         this.value = value;
     }

     // 普通方法
     public static String getValue(int key) {
         for (SessionKey c : SessionKey.values()) {
             if (c.key == key) {
                 return c.value;
             }
         }
         return null;
     }

	
}
