package com.tp.common.vo.mem;

public enum ReviewStatus {
	
	/** 数据导入成功  **/
	IMPORT_SUCCESS(8, "导入成功"),
	
	/** 数据导入失败  **/
	IMPORT_FAIL(9, "导入成功");

	
	public Integer code;
	
    public String value;

	private ReviewStatus(Integer code, String value) {
		this.code = code;
		this.value = value;
	}

	public static String getValue(Integer code){
		for (ReviewStatus c : ReviewStatus.values()) {
            if (c.code.intValue() == code.intValue()) {
                return c.value;
            }
        }
		return null;
	}
	
}
