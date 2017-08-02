/**
 * 
 */
package com.tp.common.vo.sys;

/**
 * @author Administrator
 * 系统日志
 */
public class CommonLogConstant {
	
	//系统内RESTful日志类型
	public enum RestLogType{
		D_HZ_ORDER("D_HZ_ORDER", "订单推送(浙江电子口岸)"),
		D_HZ_CLEARANCE("D_HZ_CLEARANCE", "清关单推送(浙江电子口岸)"),
		D_HZ_CANCELORDER("D_HZ_CANCELORDER", "订单删除(浙江电子口岸)"),
		D_STO_WAYBILLINFO("D_STO_WAYBILLINFO", "运单推送(申通快递)"),
		R_STO_APPLYWAYBILL("R_STO_APPLYWAYBILL", "运单号申请(申通快递)"),
		D_STO_STOCKOUT("D_STO_STOCKOUT", "出库单推送(曌通)"),
		D_JDZ_STOCKOUT("D_JDZ_STOCKOUT", "出库单推送(公共仓)"),
		D_JDZ_STOCKASN("D_JDZ_STOCKASN", "入库预约单(公共仓)");
		
		public static String getLogTypeDescByCode(String code){
			for (RestLogType type : RestLogType.values()) {
				if (type.getCode().equals(code)) {
					return type.desc;
				}
			}
			return null;
		}
		
		public String code;
		public String desc;
		
		private RestLogType(String code, String desc) {
			this.code = code;
			this.desc = desc;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}
	}
	
	
	//请求类型
	public enum RequestMethod{
		WEBSERVICE("WEBSERVICE", "webservice"),
		N_HTTP_POST("N_HTTP_POST", "normal http post"),
		N_HTTP_GET("N_HTTP_GET", "normal http get");
		
		public static String getRequestTypeDescByCode(String code){
			for (RequestMethod type : RequestMethod.values()) {
				if (type.code.equals(code)) {
					return type.desc;
				}
			}
			return null;
		}
		
		public String code;
		public String desc;
		
		private RequestMethod(String code, String desc) {
			this.code = code;
			this.desc = desc;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
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
