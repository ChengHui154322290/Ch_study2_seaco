package com.tp.common.vo.ord;

 /**
 * 补偿单 常量
 * @author szy
 */
public interface OffsetConstant{
	
	
	
	/**
	 * 补偿方式
	 */
	public enum OFFSET_TYPE{
		MONEY(1,"现金"),
		/*INTEGRAL(2,"积分"),*/
		COUPON(3,"券");
		
		public Integer code;
		public String cnName;
		
		public static String getCnName(Integer code){
			if(code==null){
				return null;
			}
			for(OFFSET_TYPE entry:OFFSET_TYPE.values()){
				if(entry.code.intValue() == code.intValue()){
					return entry.cnName;
				}
			}
			return null;
		}
		
		OFFSET_TYPE(Integer code,String cnName){
			this.code = code;
			this.cnName = cnName;
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
	 * 补偿单审核状态
	 */
	public enum OFFSET_STATUS{
		APPLY(1,"审核中"),
		AUDIT(2,"客服领导已审核"),
		AUDITED(3,"财务审核打款"),
		FAIL(4,"审核不通过"),
		FINAL_FAIL(5,"财务审核不通过");
		
		public Integer code;
		public String cnName;
		
		public static String getCnName(Integer code){
			if(code==null){
				return null;
			}
			for(OFFSET_STATUS entry:OFFSET_STATUS.values()){
				if(entry.code.intValue() == code.intValue()){
					return entry.cnName;
				}
			}
			return null;
		}
		
		OFFSET_STATUS(Integer code,String cnName){
			this.code = code;
			this.cnName = cnName;
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
	 * 补偿单日志类型
	 */
	public enum OFFSET_ACTION_TYPE{
		
		/**申请*/
		APPLY(1,"申请"),
		/**审核*/
		AUDIT(2,"审核");
		
		public Integer code;
		public String cName;
		OFFSET_ACTION_TYPE(Integer code, String name){
			this.code = code;
			this.cName = name;
		}
		public static String getCnName(Integer code){
			if(code==null){
				return null;
			}
			for(OFFSET_ACTION_TYPE entry:OFFSET_ACTION_TYPE.values()){
				if(entry.code.intValue() == code.intValue()){
					return entry.cName;
				}
			}
			return null;
		}
	}
	
	public enum OFFSET_REASON{
		
		POSTAGE(1,"邮费"),
		GOODS_FAIL(2,"商品破损"),
		OTHER(3,"其它");
		
		public Integer code;
		public String cnName;
		
		public static String getCnName(Integer code){
			if(code==null){
				return null;
			}
			for(OFFSET_REASON entry:OFFSET_REASON.values()){
				if(entry.code.intValue() == code.intValue()){
					return entry.cnName;
				}
			}
			return null;
		}
		
		OFFSET_REASON(Integer code,String cnName){
			this.code = code;
			this.cnName = cnName;
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
	
	public enum OFFSET_BEAR{
		
		XGGJ(1,"西客商城"),
		SELLER(2,"商家");
		
		public Integer code;
		public String cnName;
		
		public static String getCnName(Integer code){
			if(code==null){
				return null;
			}
			for(OFFSET_BEAR entry:OFFSET_BEAR.values()){
				if(entry.code.intValue() == code.intValue()){
					return entry.cnName;
				}
			}
			return null;
		}
		
		OFFSET_BEAR(Integer code,String cnName){
			this.code = code;
			this.cnName = cnName;
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
	 * 支付方式
	 * @author szy
	 *
	 */
	public enum PAYMENT_MODEL{
		/**1-银行（借记卡）*/
		UNPAY(1,"银行（借记卡）"),/**2-支付宝）*/
		ALIPAY(2,"支付宝"),/**3-微信*/
		WEIXINGPAY(3,"微信");
		
		public Integer code;
		public String cnName;
		
		public static String getCnName(Integer code){
			if(code==null){
				return null;
			}
			for(PAYMENT_MODEL entry:PAYMENT_MODEL.values()){
				if(entry.code.intValue() == code.intValue()){
					return entry.cnName;
				}
			}
			return null;
		}
		
		PAYMENT_MODEL(Integer code,String cnName){
			this.code = code;
			this.cnName = cnName;
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
