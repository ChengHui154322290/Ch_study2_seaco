package com.tp.common.vo.mmp;

/**
 * 积分常量
 * @author szy
 *
 */
public final class PointConstant {

	/**
	 * 积分日志记录操作类型
	 * @author szy
	 *
	 */
	public enum OPERATE_TYPE{
		ADD(1,"+"),
		MINUS(2,"-");
		public String code;
		public Integer type;
		OPERATE_TYPE(Integer type,String code){
			this.type = type;
			this.code=code;
		}
		
		public static String getCode(Integer type){
			for(OPERATE_TYPE operateType:OPERATE_TYPE.values()){
				if(operateType.type.equals(type)){
					return operateType.code;
				}
			}
			return null;
		}
	}
	
	/**
	 * 积分日志业务类型
	 * @author szy
	 *
	 */
	public enum BIZ_TYPE{
		/**1-优惠券*/
		COUPON(1,"优惠券","优惠券兑换"),/**2-订单*/
		ORDER(2,"订单","购物使用"),/**3-订单取消*/
		CANCEL(3,"订单","取消订单返还"),/**4-退货*/
		REFUNED(4,"退货","退货收到"),/**5-兑换*/
		EXCHANGE(5,"兑换","兑换使用"),/**6-过期*/
		DISCARD(6,"过期","已失效"),/**7-注册*/
		MEMBER_REGISTER(7,"注册","会员注册"),/**8-登录*/
		MEMBER_LOGIN(8,"登录","会员登录"),/**9-签到*/
		MEMBER_SIGN(9,"日期","会员签到"),
		SEAGOOR_PAY(10,"支付","线下支付"),
		SEAGOOR_PAY_REFUND(11,"退款","线下支付退款"),/**12-推广提现到积分*/
		WITHDRAW_POINT(12,"提现","推广提现"),/**13-店铺提现到积分*/
		WITHDRAW_POINT_SHOP(13,"提现","店铺提现")
		;
		public Integer code;
		public String cnName;
		
		public String title;
		
		BIZ_TYPE(Integer code,String cnName,String title){
			this.code = code;
			this.cnName = cnName;
			this.title = title;
		}
		
		public static String getCnName(Integer code){
			for(BIZ_TYPE bizType:BIZ_TYPE.values()){
				if(bizType.code.equals(code)){
					return bizType.cnName;
				}
			}
			return null;
		}
		
		public static String getTitle(Integer code){
			for(BIZ_TYPE bizType:BIZ_TYPE.values()){
				if(bizType.code.equals(code)){
					return bizType.title;
				}
			}
			return null;
		}
	}
	
	public enum PACKAGE_STATUS{
		USABLE(0,"未过期"),
		EXPIRED(1,"已过期");
		public Integer code;
		public String cnName;
		
		PACKAGE_STATUS(Integer code,String cnName){
			this.code = code;
			this.cnName = cnName;
		}
		
		public static String getCnName(Integer code){
			for(PACKAGE_STATUS packageStatus:PACKAGE_STATUS.values()){
				if(packageStatus.code.equals(code)){
					return packageStatus.cnName;
				}
			}
			return null;
		}
	}
	
	public enum CHANNEL_CODE{
		XG("xg","西客币"),
		HB("hhb","惠币");
		public String code;
		public String cnName;
		
		CHANNEL_CODE(String code,String cnName){
			this.code = code;
			this.cnName = cnName;
		}
		
		public static String getCnName(Integer code){
			for(PACKAGE_STATUS packageStatus:PACKAGE_STATUS.values()){
				if(packageStatus.code.equals(code)){
					return packageStatus.cnName;
				}
			}
			return null;
		}
	}
	
}
