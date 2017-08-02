package com.tp.common.vo.ord;

/**
 * 订单发票 常量
 * 
 * @author szy
 */
public interface OrderReceiptConstant {

	/** 是否需要发票：0-不需要发票，1-需要发票 */
	public enum NEED_INVOICE{
		FALSE(0,"不需要"),
		TRUE(1,"需要");
		
		public Integer code;
		public String cnName;
		
		NEED_INVOICE(Integer code,String cnName){
			this.code = code;
			this.cnName = cnName;
		}
	}
	int INVOICE_UNREQUIRE = 0;// 0-不需要发票
	int INVOICE_REQUIRE = 1;// 1-需要发票

	/**
	 * 发票类型
	 * 
	 * @author szy
	 * @version 0.0.1
	 */
	public enum ReceiptType {

		/** 1 - 普通纸质 */
		PAPER(1, "普通纸质"),
		/** 2 - 电子票 */
		ELECTRON(2, "电子票"),
		/** 3 - 增值税发票 */
		VAT(3, "增值税发票");

		public Integer code;
		public String cnName;

		ReceiptType(Integer code, String cnName) {
			this.code = code;
			this.cnName = cnName;
		}

		public static String getCnName(Integer code) {
			for (ReceiptType entry : ReceiptType.values()) {
				if (entry.code.equals(code)) {
					return entry.cnName;
				}
			}
			return null;
		}
	}

	/**
	 * 发票抬头类型
	 * 
	 * @author szy
	 * @version 0.0.1
	 */
	public enum ReceiptTitleType {

		/** 1 - 个人 */
		PERSON(1, "个人"),
		/** 2 - 公司 */
		CORPORATION(2, "公司");

		public Integer code;
		public String cnName;

		ReceiptTitleType(Integer code, String cnName) {
			this.code = code;
			this.cnName = cnName;
		}

		public static String getCnName(Integer code) {
			for (ReceiptTitleType entry : ReceiptTitleType.values()) {
				if (entry.code.equals(code)) {
					return entry.cnName;
				}
			}
			return null;
		}
	}

	/**
	 * 发票内容类型
	 * 
	 * @author szy
	 * @version 0.0.1
	 */
	public enum ReceiptContentType {

		/** 1 - 明细 */
		DETAIL(1, "明细");

		public Integer code;
		public String cnName;

		ReceiptContentType(Integer code, String cnName) {
			this.code = code;
			this.cnName = cnName;
		}

		public static String getCnName(Integer code) {
			for (ReceiptContentType entry : ReceiptContentType.values()) {
				if (entry.code.equals(code)) {
					return entry.cnName;
				}
			}
			return null;
		}
	}
}
