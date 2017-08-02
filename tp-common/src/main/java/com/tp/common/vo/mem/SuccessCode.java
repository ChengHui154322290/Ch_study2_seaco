package com.tp.common.vo.mem;

public enum SuccessCode {
	
	/** 注册成功*/
	REGISTER_SUCCESS(6000, "注册成功 ");
	
	public Integer code;
	
    public String value;

	private SuccessCode(Integer code, String value) {
		this.code = code;
		this.value = value;
	}

	public static String getValue(Integer code){
		for (SuccessCode c : SuccessCode.values()) {
            if (c.code.intValue() == code.intValue()) {
                return c.value;
            }
        }
		return null;
	}
	
}
