package com.tp.common.vo.ord;

 /**
 * 退货单 常量
 * @author szy
 */
public interface RejectConstant{

	/**
	 * 退货状态
	 * @author szy
	 *
	 */
	public enum REJECT_STATUS{
		
		/**0,"申请退货"*/
		APPLYING(0,"申请退货"),
		/**1,"退货中"*/
		REJECTING(1,"退货中"),
		/**2,"退款中"*/
		REFUNDING(2,"退款中"),
		/**3,"退货完成"*/
		REJECTED(3,"退货完成"),
		/**4,"取消退货"*/
		CANCELED(4,"取消退货"),
		/**5,"退货失败"*/
		REJECTFAIL(5,"退货失败"),
		/**6,"待确认退货"*/
		CONFIRMING(6,"待确认退货");
		
		public Integer code;
		public String cnName;
		
		REJECT_STATUS(Integer code,String cnName){
			this.code = code;
			this.cnName = cnName;
		}
		
		public static String getCnName(Integer code){
			if(null!=code){
				for(REJECT_STATUS entry:REJECT_STATUS.values()){
					if(entry.code.intValue()==code){
						return entry.cnName;
					}
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
	 * 退货审核状态
	 * 核审状态：待客服审核-1，待商家审核-2，客服审核通过-3，客服审核不通过-4，商家审核通过-5，商家审核不通过-6
	 * @author szy
	 *
	 */
	public enum REJECT_AUDIT_STATUS{
		
		/**待客服审核-1*/
		XG_AUDITING(1,"待客服审核"),
		/**客服审核通过-3*/
		XG_AUDITED(3,"客服审核通过"),
		/**客服审核不通过-4*/
		XG_FIAL(4,"客服审核不通过"),
		
		/**待商家审核-2*/
		SELLER_AUDITING(2,"待商家审核"),
		/**商家审核通过-5*/
		SELLER_AUDITED(5,"商家审核通过"),
		/**商家审核不通过-6*/
		SELLER_FIAL(6,"商家审核不通过"),		
		/**客服终审通过-7*/
		END_XG_AUDITED(7,"客服终审通过"),
		/**客服终审不通过-8*/
		END_XG_FIAL(8,"客服终审不通过"),
		/**强制审核通过 -9*/
		FORCE_AUDITED(9,"强制审核通过");
		
		
		public Integer code;
		public String cnName;
		
		REJECT_AUDIT_STATUS(Integer code,String cnName){
			this.code = code;
			this.cnName = cnName;
		}
		
		public static String getCnName(Integer code){
			if(null!=code){
				for(REJECT_AUDIT_STATUS entry:REJECT_AUDIT_STATUS.values()){
					if(entry.code.intValue()==code){
						return entry.cnName;
					}
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
	
	public enum REJECT_LOG_ACTIVE_TYPE{
		APPLY(1,"申请"),
		USER_AUDIT(2,"审核"),
		SELLER_AUDIT(3,"商家审核"),
		SENT_BACK(4,"寄回商品"),
		REFUND(5,"退款"),
		CANCEL(6,"取消");
		
		public Integer code;
		public String cnName;
		
		REJECT_LOG_ACTIVE_TYPE(Integer code,String cnName){
			this.code = code;
			this.cnName = cnName;
		}
		
		public static String getCnName(Integer code){
			if(null!=code){
				for(REJECT_LOG_ACTIVE_TYPE entry:REJECT_LOG_ACTIVE_TYPE.values()){
					if(entry.code.intValue()==code){
						return entry.cnName;
					}
				}
			}
			return null;
		}
	}
	
	/**
	 * 退货类型
	 * @author szy
	 *
	 */
	public enum REJECT_TYPE{
		REJECT(1,"拒收"),
		SEND_BACK(2,"退货"),
		REPLACE(3,"换货");
		
		public Integer code;
		public String cnName;
		
		REJECT_TYPE(Integer code,String cnName){
			this.code = code;
			this.cnName = cnName;
		}
		
		public static String getCnName(Integer code){
			if(null!=code){
				for(REJECT_TYPE entry:REJECT_TYPE.values()){
					if(entry.code.intValue()==code){
						return entry.cnName;
					}
				}
			}
			return null;
		}
	}
	
	
	/**
	 * 退货原因
	 * @author zhs
	 *
	 */
	public enum REJECT_REASON{
		REASON_IN_7_DAYS("1","7天无理由"),
		REASON_GOODS_BROKEN ("2","收到商品破损"),
		REASON_WRONG_DELIVER ("3","商品发错"),
		REASON_WRONG_DESC("4","收到商品与描述不符"),
		REASON_BAD_QUALITY ("5","商品质量有问题"),
		REASON_OTHER ("6","其他"),
		REASON_PRIVATE_REASON("7","个人原因退货"),
		REASON_NOT_RECEIVED("8","一直未收到货");

		public String code;
		public String cnName;
		
		REJECT_REASON(String code,String cnName){
			this.code = code;
			this.cnName = cnName;
		}
		
		public static String getCnName(String code){
			if(null!=code){
				for(REJECT_REASON entry:REJECT_REASON.values()){
					if(entry.code.equals(code) ){
						return entry.cnName;
					}
				}
			}
			return null;
		}
	}
	
}
