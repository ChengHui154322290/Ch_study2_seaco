package com.tp.common.vo.mem;

public enum RemoteErrorCode {
	/** 系统错误 */
	SYSTEM_ERROR (0, "系统错误"),
	
	/** 未付款订单取消错误 */
	CHECK_CANCEL_ERROR(2008, "未付款订单取消错误"),
	
	/** 库存回滚调用接口错误 */
	ROLLBACK_STOCK_ERROR(2010, "库存回滚调用接口错误");
	
	/** 通用错误码 **/
	public Integer code;
	
	/** 错误描述 */
    public String value;

	private RemoteErrorCode(Integer code, String value) {
		this.code = code;
		this.value = value;
	}

	public static String getValue(Integer code){
		for (RemoteErrorCode c : RemoteErrorCode.values()) {
            if (c.code.intValue() == code.intValue()) {
                return c.value;
            }
        }
		return null;
	}
	
	
	public static void main(String[] args) {
		System.out.println(getValue(2008));
	}
}
