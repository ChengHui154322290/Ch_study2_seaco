package com.tp.common.vo;

/**
 * 售后服务常量 
 * @author szy
 *
 */
public final class CrmConstant {

	/**退款类型选项REFUND_TYPE_OPTION_KEY*/
	public static final String REFUND_TYPE_OPTION_KEY = "2015001";
	
	/**
	 * 退款状态
	 * @author szy
	 *
	 */
	public enum REFUND_STATUS{
		/**1,"等待审核"*/
		CREATE(1,"等待审核"),/**2,"审核中"*/
		AUDIT(2,"审核中"),/**3,"退款成功"*/
		SUCCESS(3,"退款成功"),/**4,"退款失败"*/
		FAIL(4,"退款失败"),
		CANCEL(0,"取消退款");
		
		public Integer code;
		public String cnName;
		
		REFUND_STATUS(Integer code,String cnName){
			this.code = code;
			this.cnName = cnName;
		}
		
		public static String getCnName(Integer code){
			for(REFUND_STATUS entry:REFUND_STATUS.values()){
				if(entry.getCode().equals(code)){
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
	 * 退款类型
	 * @author szy
	 *
	 */
	public enum REFUND_TYPE{
		CANCEL("","");
		public String code;
		public String cnName;
		
		REFUND_TYPE(String code,String cnName){
			this.code = code;
			this.cnName = cnName;
		}

		public static String getCnName(String code){
			for(REFUND_TYPE entry:REFUND_TYPE.values()){
				if(entry.getCode().equals(code)){
					return entry.cnName;
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

		public String getCnName() {
			return cnName;
		}

		public void setCnName(String cnName) {
			this.cnName = cnName;
		}
	}

}
