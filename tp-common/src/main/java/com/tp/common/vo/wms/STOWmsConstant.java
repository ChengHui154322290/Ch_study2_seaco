/**
 * 
 */
package com.tp.common.vo.wms;

/**
 * @author Administrator
 *
 */
public class STOWmsConstant {
	/**
	 * 公共仓的物流信息 
	 */
	public enum ExpressCompany{
		STO("STO", "shentong", "申通快递");
		
		public String stoCode;
		public String commonCode;
		public String name;
		
		private ExpressCompany(String stoCode, String commonCode, String name) {
			this.stoCode = stoCode;
			this.commonCode = commonCode;
			this.name = name;
		}
		
		public static String getStoCodeByCommonCode(String commonCode){
			for (ExpressCompany ec : ExpressCompany.values()) {
				if (ec.getCommonCode().equalsIgnoreCase(commonCode)) {
					return ec.getStoCode();
				}
			}
			return null;
		}
		
		public static String getStoNameByCommonCode(String commonCode){
			for (ExpressCompany ec : ExpressCompany.values()) {
				if (ec.getCommonCode().equalsIgnoreCase(commonCode)) {
					return ec.getName();
				}
			}
			return null;
		}
		
		public static ExpressCompany getExpressCompanyBySTOCode(String stoCode){
			for (ExpressCompany ec : ExpressCompany.values()) {
				if (ec.getStoCode().equalsIgnoreCase(stoCode)) {
					return ec;
				}
			}
			return null;
		}

		public String getStoCode() {
			return stoCode;
		}

		public void setStoCode(String stoCode) {
			this.stoCode = stoCode;
		}

		public String getCommonCode() {
			return commonCode;
		}

		public void setCommonCode(String commonCode) {
			this.commonCode = commonCode;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
}
