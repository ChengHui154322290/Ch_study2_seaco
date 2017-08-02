package com.tp.common.vo.ord;

/**
 * 子订单 常量
 * 
 * @author szy
 */
public class SubOrderConstant {
	// /** 订单类型（1：一般订单（入仓）。2：平台（不入仓））。3：海淘*/
	public final static int GENERAL_ORDER = 1;
	public final static int PLATFORM_ORDER = 2;
	public final static int SEA_ORDER = 3;

	/** 逻辑删除 - 是 */
	public final static Integer DELETED_TRUE = 1;
	/** 逻辑删除 - 否 */
	public final static Integer DELETED_FALSE = 2;
	
	/**
	 * 海关审核状态
	 * 
	 * @author szy
	 * @version 0.0.1
	 */
	public enum PutStatus {
		
		/** 0 - 等待推送 */
		NEW(0, "等待推送"),
		/** 1 - 已推送 */
		PUT(1, "已推送"),
		/** 2 - 推送失败 */
		PUT_FAIL(2, "推送失败"),
		/** 3 - 审核成功 */
		VERIFY_SUCCUESS(3, "审核成功"),
		/** 4 - 审核失败 */
		VERIFY_FAIL(4, "审核失败");
		
		public Integer code;
		public String cnName;
		
		PutStatus(Integer code, String cnName) {
			this.code = code;
			this.cnName = cnName;
		}
		
		public static String getCnName(Integer code) {
			for (PutStatus entry : PutStatus.values()) {
				if (entry.code.equals(code)) {
					return entry.cnName;
				}
			}
			return null;
		}

		public Integer getCode() {
			return code;
		}

		public void setCode(Integer code) {
			this.code = code;
		}

		public String getCnName() {
			return cnName;
		}

		public void setCnName(String cnName) {
			this.cnName = cnName;
		}
		
	}
	
	/**
	 * 订单推送仓库状态
	 * 
	 * @author szy
	 * @version 0.0.1
	 */
	public enum PutWarehouseStatus {	
		/** 0 - 等待推送 */
		NEW(0, "等待推送"),
		/** 1 - 已推送 */
		PUT(1, "已推送"),
		/** 2 - 推送失败 */
		PUT_FAIL(2, "推送失败");
		
		public Integer code;
		public String cnName;
		
		PutWarehouseStatus(Integer code, String cnName) {
			this.code = code;
			this.cnName = cnName;
		}
		
		public static String getCnName(Integer code) {
			for (PutStatus entry : PutStatus.values()) {
				if (entry.code.equals(code)) {
					return entry.cnName;
				}
			}
			return null;
		}

		public Integer getCode() {
			return code;
		}

		public void setCode(Integer code) {
			this.code = code;
		}

		public String getCnName() {
			return cnName;
		}

		public void setCnName(String cnName) {
			this.cnName = cnName;
		}
		
	}
	
}

