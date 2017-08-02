package com.tp.seller.constant;

/**
 * 验证码.
 */
public enum SessionKey {

    LOGIN(0, "loginSecurityCode"),

    REGISTER(1, "registerSecurityCode"),

    VALIDATECODE(2, "validateCode"),

    SMSCODE(3, "smsCode"),

    SMSPWDCODE(5, "smsPwdCode"),

    BINDEMAIL(6, "bindEmail"),

    OTHER(999, "other.unSelect"),

    APP_UPDATE_PASSWORD(0, "app_upt_sms_code"),

    APP_REGISTER(1, "app_register_sms_code");

    // 成员变量
    private Integer key;
    private String value;

    // 构造方法
    private SessionKey(final Integer key, final String value) {
        this.key = key;
        this.value = value;
    }

    // 普通方法
    public static String getValue(final int key) {
        for (final SessionKey c : SessionKey.values()) {
            if (c.getKey() == key) {
                return c.getValue();
            }
        }
        return null;
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(final Integer key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }
}
