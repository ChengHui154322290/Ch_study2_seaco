package com.tp.common.vo.wms;

import com.tp.common.vo.bse.ClearanceChannelsEnum;

public class JDZWmsConstant {

	public static final String JDZ_PASSWORD = "zjport_tropjdz";

	public static final ClearanceChannelsEnum CHNNEL_HZ = ClearanceChannelsEnum.HANGZHOU;
	
	/**
	 * 公共仓的物流信息 
	 */
	public enum ExpressCompany{
		STO("STO", "shentong", "申通快递");
		
		public String jdzCode;
		public String commonCode;
		public String name;
		
		private ExpressCompany(String jdzCode, String commonCode, String name) {
			this.jdzCode = jdzCode;
			this.commonCode = commonCode;
			this.name = name;
		}
		
		public static String getJdzCodeByCommonCode(String commonCode){
			for (ExpressCompany ec : ExpressCompany.values()) {
				if (ec.getCommonCode().equalsIgnoreCase(commonCode)) {
					return ec.getJdzCode();
				}
			}
			return null;
		}
		
		public static String getJdzNameByCommonCode(String commonCode){
			for (ExpressCompany ec : ExpressCompany.values()) {
				if (ec.getCommonCode().equalsIgnoreCase(commonCode)) {
					return ec.getName();
				}
			}
			return null;
		}
		
		public static ExpressCompany getExpressCompanyByJDZCode(String jdzCode){
			for (ExpressCompany ec : ExpressCompany.values()) {
				if (ec.getJdzCode().equalsIgnoreCase(jdzCode)) {
					return ec;
				}
			}
			return null;
		}
		
		public String getJdzCode() {
			return jdzCode;
		}

		public void setJdzCode(String jdzCode) {
			this.jdzCode = jdzCode;
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
