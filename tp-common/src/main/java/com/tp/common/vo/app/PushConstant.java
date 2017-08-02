package com.tp.common.vo.app;

/**
 * 消息推送常量
 * @author zhuss
 * @2016年4月7日 下午2:43:06
 */
public interface PushConstant {
	
	public static final String APP_PUSH_PLATFORM_ALL = "APP";
	
	public static final Integer DEFAULT_APP_PUSH_PAGE_SIZE = 20;
	public static final Integer DEFAULT_APP_PUSH_PAGE_ID = 1;
	
	public enum ERROR_CODE{
		SUCCESS(200,"success"),
		FAILED(400,"failed"),
		APP_PUSH_CONNECT_ERROR(-1,"个推服务器连接失败"),
		APP_CREATE_TEMPLATE_ERROR(-1, "创建消息模版失败"),
		PUSH_TYPE_NULL(-1,"推送类型不合法"),
		PUSH_IOS_LINK_ERROR(-1,"IOS平台暂不支持超链"),
		PUSH_CLIENTID_NULL(-1,"推送单体的clientId不能为空");
		public int code;
		public String message;
		ERROR_CODE(int code,String message){
			this.code = code;
			this.message = message;
		}
	}
	
	//推送平台
	public enum PUSH_PLATFORM {
		APP("app", "APP"),IOS("ios", "IOS"), ANDROID("android", "ANDROID");
		private String code;
		private String desc;

		PUSH_PLATFORM(String code, String desc) {
			this.code = code;
			this.desc = desc;
		}
		public String getCode() {
			return code;
		}

		public String getDesc() {
			return desc;
		}
	}
	
	//推送状态
	public enum PUSH_STATUS {
		NO(1, "未推送"), YES(2, "已推送");
		private int code;
		private String desc;

		PUSH_STATUS(int code, String desc) {
			this.code = code;
			this.desc = desc;
		}
		public static String getDesc(Integer code){
			for(PUSH_STATUS entry:PUSH_STATUS.values()){
				if(entry.code ==code){
					return entry.desc;
				}
			}
			return null;
		}
		public int getCode() {
			return code;
		}
		public String getDesc() {
			return desc;
		}
	}
	
	// 推送目标
	public enum PUSH_TARGET {
		SINGLE(1, "单体推送"), APP(2, "全体推送");
		private int code;
		private String desc;

		PUSH_TARGET(int code, String desc) {
			this.code = code;
			this.desc = desc;
		}
		
		public static String getDesc(Integer code){
			for(PUSH_TARGET entry:PUSH_TARGET.values()){
				if(entry.code ==code){
					return entry.desc;
				}
			}
			return null;
		}

		public int getCode() {
			return code;
		}
		public String getDesc() {
			return desc;
		}
	}
	
	/**
	 *  推送类型
	 * @author szy
	 *
	 */
	public enum PUSH_TYPE {
		/**“0”表示超链接*/
		LINK(0, "超链"), /**“1”表示专场id*/
		TOPIC(1, "专场"), /**“2”专场id和商品sku*/
		PRODUCT(2, "商品"),/**“3”表示秒杀
		seckill(3,"秒杀"),*//**“4”表示购物车*/
		shoppingCart(4,"购物车"),/**“5”表示登陆*/
		login(5,"登录"),/**“6”表示注册*/
		register(6,"注册"),/**“7”表示优惠券*/
		coupon(7,"优惠券"),/**“8”表示红包*/
		redbag(8,"红包"),/**“9”表示订单*/
		order(9,"订单"),/**10 - 首页*/
		homepage(10,"首页"),/**11 - 个人中心*/
		ownonly(11,"个人中心");

		private int code;
		private String desc;

		PUSH_TYPE(int code, String desc) {
			this.code = code;
			this.desc = desc;
		}
		
		public static String getDesc(Integer code){
			for(PUSH_TYPE entry:PUSH_TYPE.values()){
				if(entry.code ==code){
					return entry.desc;
				}
			}
			return null;
		}

		public int getCode() {
			return code;
		}

		public String getDesc() {
			return desc;
		}
	}
	
	// 推送状态
	public enum PUSH_WAY {
		NO(1, "即时"), YES(2, "定时");
		private int code;
		private String desc;

		PUSH_WAY(int code, String desc) {
				this.code = code;
				this.desc = desc;
			}

		public static String getDesc(Integer code) {
			for (PUSH_WAY entry : PUSH_WAY.values()) {
				if (entry.code == code) {
					return entry.desc;
				}
			}
			return null;
		}

		public int getCode() {
			return code;
		}

		public String getDesc() {
			return desc;
		}
	}
}
