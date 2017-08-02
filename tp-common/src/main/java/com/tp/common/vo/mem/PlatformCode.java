package com.tp.common.vo.mem;

public enum PlatformCode {
	
	/** pc **/
	PC(0, "pc"),
	
	/** app **/
	APP(1, "app"),
	
	/** wap **/
	WAP(2, "wap");
	
	
	/** 平台码 **/
	public Integer code;
	
	/** 描述 */
    public String desc;
    

	private PlatformCode(Integer code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static String getDesc(Integer code) {
		if(null == code) 
			return null;
		for (PlatformCode c : PlatformCode.values()) {
            if (c.code.intValue() == code.intValue()) {
                return c.desc;
            }
        }
		return null;
	}
	
}
