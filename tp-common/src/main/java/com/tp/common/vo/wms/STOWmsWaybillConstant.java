/**
 * 
 */
package com.tp.common.vo.wms;

/**
 * @author Administrator
 *
 */
public class STOWmsWaybillConstant {
	
	
	/**
	 *  运单类型
	 */
	public enum STOWaybillType{
		SHIPPING(900, 1, "发运"),
		DECLARE(250, 0, "报关");
		
		public Integer code;
		public Integer commonCode;
		public String desc;
		
		private STOWaybillType(Integer code, Integer commonCode, String desc) {
			this.code = code;
			this.commonCode = commonCode;
			this.desc = desc;
		}

		public static Integer getSTOTypeByCommonCode(Integer commonCode){
			for (STOWaybillType type : STOWaybillType.values()) {
				if (type.getCommonCode().equals(commonCode)) {
					return type.getCode();
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

		public Integer getCommonCode() {
			return commonCode;
		}

		public void setCommonCode(Integer commonCode) {
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
	 * 申通交互状态码 
	 */
	public enum ErrorCode{
		SUCCESS("1000", "成功"),
		FAIL("1005", "失败"),
		PART_FAIL("1015", "部分失败"),
		EXCEPTION("1025", "其他异常");
		
		public String code;
		public String desc;
		private ErrorCode(String code, String desc) {
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
