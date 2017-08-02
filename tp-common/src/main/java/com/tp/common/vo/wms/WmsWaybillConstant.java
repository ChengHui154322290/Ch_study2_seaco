package com.tp.common.vo.wms;

/**
 * 运单常量 
 */
public class WmsWaybillConstant {

	/** 单次申请运单号最大数量 **/
	public static final int MAX_APPLY_WAYBILL_COUNT = 1000;
	/** 单次申请运单号数量 **/
	public static final int SINGLE_APPLY_WAYBILL_COUNT = 10;
	/** 可用运单号最小数量 **/
	public static final int MIN_WAYBILL_COUNT = 2;
	
	/** 最大推送失败次数 **/
	public static final int MAX_PUT_TIMES = 3;
	
	/** 运单类型 */
	public enum WaybillType{
		SHIPPING(1, "发运"),
		DECLARE(0, "报关");
		
		public Integer code;
		public String desc;
		
		public static String getDescByType(Integer code){
			for (WaybillType type : WaybillType.values()) {
				if (type.getCode().equals(code)) {
					return type.desc;
				}
			}
			return null;
		}
		
		private WaybillType(Integer code, String desc) {
			this.code = code;
			this.desc = desc;
		}

		public Integer getCode() {
			return code;
		}

		public void setCode(Integer code) {
			this.code = code;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}
	}
	
	/**
	 * 推送状态 
	 */
	public enum PutStatus{
		SUCCESS(0,  "成功"),
		FAIL(1, "失败");
		
		public Integer code;
		public String desc;
		
		public static String getDescByStatus(Integer code){
			for (PutStatus status : PutStatus.values()) {
				if (status.code.equals(code)) {
					return status.desc;
				}
			}
			return null;
		}
		
		private PutStatus(Integer code, String desc) {
			this.code = code;
			this.desc = desc;
		}

		public Integer getCode() {
			return code;
		}

		public void setCode(Integer code) {
			this.code = code;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}
	}
	
	/**
	 * 运单号使用状态 
	 */
	public enum WaybillStatus{
		UNUSE(0, "未使用"),
		USED(1, "已使用"),
		ABANDONED(2, "已作废");
		
		
		public Integer code;
		public String desc;
		
		public static String getDesc(Integer code){
			for (WaybillStatus status : WaybillStatus.values()) {
				if (status.code.equals(code)) {
					return status.desc;
				}
			}
			return null;
		}
		
		private WaybillStatus(Integer code, String desc) {
			this.code = code;
			this.desc = desc;
		}

		public Integer getCode() {
			return code;
		}

		public void setCode(Integer code) {
			this.code = code;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}
	}
	
}
