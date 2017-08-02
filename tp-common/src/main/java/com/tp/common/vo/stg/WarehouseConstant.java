/**
 * 
 */
package com.tp.common.vo.stg;

/**
 * @author Administrator
 * 仓库常量
 */
public class WarehouseConstant {
	
	//WMS code
	public enum WMSCode{
		/** 标杆仓库 */
		BML_KSWH("BML_KSWH","BML_KSWH","标杆仓库", "上海市"),
		/** 杭州保税区公共仓 **/
		JDZ_HZ("3316W6K001", "3316W6K001", "杭州保税区公共仓", "浙江省杭州市"),	//仓储企业是杭州通隆
		/** 杭州保税区申通自营仓 **/
		STO_HZ("3316W60020", "3316W60020", "杭州保税区自营仓(申通)", "浙江省杭州市");	//仓储企业是西客物流
		public String code;
		public String foreignCode;
		public String desc;
		public String address;
		
		private WMSCode(String code, String foreignCode, String desc, String address) {
			this.code = code;
			this.foreignCode = foreignCode;
			this.desc = desc;
			this.address = address;
		}

		public static WMSCode getWarehouseCodeBycode(String code){
			for (WMSCode warehouseCode : WMSCode.values()) {
				if (warehouseCode.code.equals(code)) {
					return warehouseCode;
				}
			}
			return null;
		}
		
		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getForeignCode() {
			return foreignCode;
		}

		public void setForeignCode(String foreignCode) {
			this.foreignCode = foreignCode;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}
	}
	
}
