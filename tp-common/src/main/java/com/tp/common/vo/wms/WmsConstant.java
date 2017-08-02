/**
 * 
 */
package com.tp.common.vo.wms;

/**
 * @author Administrator
 *
 */
public class WmsConstant {
	
	public static final int MAX_PUT_TIMES = 3;
	
	/**
	 * 出库单类型 
	 */
	public enum StockoutOrderType{
		SO("SO", "订单出库"),
		PR("PR", "退货出库");
		
		public String code;
		public String name;
		private StockoutOrderType(String code, String name) {
			this.code = code;
			this.name = name;
		}
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	}
	
	/** 
	 * 快递公司(与系统内的快递对应) 
	 * 因数量较少故单独取出
	 */
	public enum ExpressCompany{
		STO("shentong", "申通快递");
		
		public String commonCode;
		public String desc;
		
		private ExpressCompany(String commonCode, String desc) {
			this.commonCode = commonCode;
			this.desc = desc;
		}

		public static ExpressCompany getCompanyByCommonCode(String code){
			for (ExpressCompany company : ExpressCompany.values()) {
				if (company.commonCode.equals(code)) {
					return company;
				}
			}
			return null;
		}
		
		public String getCommonCode() {
			return commonCode;
		}

		public void setCommonCode(String commonCode) {
			this.commonCode = commonCode;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}
	}
	
	
	/**
	 * 推送第三方WMS状态 
	 */
	public enum PutStatus{
		SUCCESS(1, "成功"),
		FAIL(0, "失败");
		
		public Integer code;
		public String desc;
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
	 * 发票 
	 */
	public enum InvoiceNeed{
		YES(0, "需要发票"),
		NO(1, "不需要发票");
		
		public Integer code;
		public String desc;
		
		private InvoiceNeed(Integer code, String desc) {
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
	 * 货到付款 
	 */
	public enum DeliveryPay{
		YES(0, "货到付款"),
		NO(1, "不是货到付款");
		
		public Integer code;
		public String desc;
		
		private DeliveryPay(Integer code, String desc) {
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
	 * 邮费是否到付 
	 */
	public enum PostagePay{
		YES(0, "邮费到付"),
		NO(1, "不是邮费到付");
		
		public Integer code;
		public String desc;
		
		private PostagePay(Integer code, String desc) {
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
