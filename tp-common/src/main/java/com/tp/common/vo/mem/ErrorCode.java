package com.tp.common.vo.mem;


public enum ErrorCode {
	
	/** 系统异常*/
	SYSTEM_ERROR(-1, "系统异常"),
	
	/** 用户未登录*/
	USER_UN_LOGIN(-999, "用户未登录"),
	
	/** 收货地址对多可以添加5个*/
	CONSIGNEE_ADDRESS_MAX_FIVE(4001, "收货地址最多可添加" + MemberConstant.ConsigneeAddress.MaxCount + "个"),
	
	/** Required parameter is missing.*/
	PARAM_MISSING(4002, "缺少必要参数"),
	
	/** Check code error.*/
	CHECK_CODE_ERROR(4003, "验证码错误"),
	
	/** Mobile code error.*/
	MOBILE_CODE_ERROR(4004, "手机验证码错误"),
	
	/** Login fail.*/
	LOGIN_FAIL(4005, "用户名或密码错误"),
	
	/** This email is registered before.*/
	EMAIL_REGISTERED(4006, "该邮箱已被注册"),
	
	/** This mobile number is registered before.*/
	MOBILE_REGISTERED(4007, "该手机号码已被注册."),
	
	/** The user does not exist*/
	USER_NOT_EXISTS(4008, "该用户不存在"),
	
	/** 秒后才可以重新发送短信,60秒内只能发送一次*/
	SEND_MSG_UN_ONE_SECOND(4009, "秒后才可以重新发送短信,一分钟内只能发送一次"),
	
	/** 用户名或者密码错误*/
	USERNAME_OR_PASS_WRONG(4010, "用户名或者密码错误"),
	
	/** 用户名为空*/
	USERNAME_MISSING(4011, "用户名为空"),
	
	/** 密码为空*/
	PASSWORD_MISSING(4012, "密码为空"),
	
	/** 手机验证码错误*/
	SMS_CODE_ERROR(4013, "验证码错误"),
	
	/** 手机号为空*/
	SMS_MOBILE_IS_NULL(4014, "手机号为空"),
	
	/** 手机短信类型为空 */
	SMS_MOBILE_TYPE_IS_NULL(4015, "短信类型为空"),
	
	/** 密码错误 */
	PASSWORD_ERROR(4016, "密码错误"),
	
	/** 手机验证码为空 */
	SMS_CODE_IS_NULL(4017, "验证码为空"),
	
	/** 用户名或密码为空 */
	USERNAME_OR_PASSWORD_IS_NULL(4018, "用户名或密码为空 "),
	
	/** 注册失败*/
	REGISTER_FAIL(4019, "注册失败 "),
	
	/** 收货人为空*/
	CONSIGNEE_USERNAME_IS_NULL(4020, "收货人为空 "),
	
	/** 收货人联系电话为空*/
	CONSIGNEE_MOBILE_IS_NULL(4021, "收货人联系电话为空 "),
	
	/** 收货地址-省份为空*/
	CONSIGNEE_PROVINCE_IS_NULL(4022, "收货地址-省份为空"),
	
	/** 收货地址-市区为空*/
	CONSIGNEE_CITY_IS_NULL(4023, "收货地址-城市为空 "),
	
	/** 收货地址-区/县为空*/
	CONSIGNEE_COUNTY_IS_NULL(4024, "收货地址-区/县为空 "),
	
	/** 收货地址-街道为空*/
	CONSIGNEE_STREET_IS_NULL(4025, "收货地址-街道为空 "),
	
	/** 收货地址,用户id为空*/
	CONSIGNEE_USERID_IS_NULL(4026, "收货地址,用户id为空 "),
	
	/** 收货地址-详细地址为空 */
	CONSIGNEE_ADDRESS_IS_NULL(4027, "收货地址-详细地址为空 "),
	
	/** 密码修改失败 **/
	UPDATE_PASSWORD_FAIL(4028, "密码修改失败"),
	
	/** 用户id为空  **/
	USER_ID_IS_NULL(4029, "缺少user id"),
	
	/** 短信发送失败 **/
	SEND_SMS_FAIL(4030, "短信发送失败"),
	
	/** 该手机号已绑定 **/
	MOBILE_BIND_BEFORE(4031, "该手机号已绑定"),
	
	/** 收货地址-修改失败*/
	CONSIGNEE_UPDATE_FAIL(4032, "收货地址修改失败 "),
	
	/** 您已经关注了该活动*/
	PROMOTION_HAS_FAVORITE(4033, "您已经关注了该活动 "),
	
	/** 关注人id为空*/
	PROMOTION_USER_IS_NULL(4034, "关注人id为空 "),
	
	/** 关注活动id为空*/
	PROMOTION_ID_IS_NULL(4035, "关注活动id为空"),
	
	/** 关注人手机为空*/
	PROMOTION_MOBILE_IS_NULL(4036, "关注人手机为空"),
	
	/** 定时短信,发送时间不能为空*/
	SEND_SMS_SEND_TIME_IS_NULL(4037, "定时短信,发送时间不能为空"),
	
	/** 用户已存在 **/
	USER_EXISTS(4038, "该用户已存在"),
	
	/** 注册成功*/
	REGISTER_SUCCESS(4039, "注册成功 "),
	
	/** 商品pid为空*/
	ITEM_REVIEW_PID_IS_NULL(4040, "商品pid为空 "),
	
	/** 请填写心得*/
	ITEM_REVIEW_CONTENT_IS_NULL(4041, "请填写心得 "),
	
	/** 订单编号为空*/
	ITEM_REVIEW_ORDER_CODE_IS_NULL(4042, "订单编号为空"),
	
	/** 用户id为空*/
	ITEM_REVIEW_USER_ID_IS_NULL(4043, "用户id为空 "),
	
	/** 该词语为违禁词*/
	THIS_WORDS_IS_FORBIDDEN_WORDS(4044, "该词语为违禁词"),
	
	/** 关注活动id为空*/
	PROMOTION_ID_IS_INVALID(4045, "非法活动id"),
	
	/** 商品已经被收藏*/
	PRODUCT_HASH_COLLECTION(4046, "商品已经被收藏"),
	
	/** 商品不存在*/
	PRODUCT_IS_NOT_EXIST(4047, "商品不存在"),
	
	/** skuCode为空 */
	SKU_CODE_IS_NULL(4048, "商品信息异常"),
	
	/** 活动id为空 */
	TOPIC_ID_IS_NULL(4049, "活动信息异常 "),
	
	/** 品牌已经被收藏*/
	BRAND_HASH_COLLECTION(4050, "品牌已经被收藏"),
	
	/** 品牌不存在*/
	BRAND_IS_NOT_EXIST(4051, "品牌不存在"),
	
	/** brandId为空 */
	BRAND_ID_IS_NULL(4052, "品牌信息异常"),
	
	/** 收藏商品id为空 */
	USERSINGLE_ID_IS_NULL(4053, "收藏商品id为空 "),
	
	/** 收藏品牌为空 */
	USERBRAND_ID_IS_NULL(4054, "收藏品牌为空 "),
	
	/** 收藏品牌为空 */
	TOPIC_IS_NOT_EXIST(4055, "活动不存在"),
	
	/** 手机号码为空 */
	MOBILE_IS_NULL(4056, "手机号码为空"),
	
	/** 非常抱歉,没有进行中的达人计划! */
	NO_ACTIVE_MASTER_PLAN(4057, "非常抱歉,当前没有正在进行中的达人计划!"),
	
	/** skucode为null*/
	SKUCODE_IS_NULL(4058, "skuCode为空"),
	
	/** 推荐人账号为空*/
	REFERRER_MISSING(4059, "推荐人账号为空"),
	
	/** 推荐人账号不存在*/
	REFERRER_INVALID(4060, "推荐人账号不存在"),
	
	/**已经评论*/
	ITEM_REVIEW_HAS_REVIEW(4061, "您已经评论过该商品 "),
	
	/** 推荐人账号已达推荐上限*/
	REFERRER_COUNT_INVALID(4062, "亲，该推荐账户已经邀请了20个小伙伴哟！"),
	
	/** 后才可以重新发送短信,同种类型的操作一天只能发送5次短信*/
	SEND_MSG_UN_ONE_DAY(4063, "后才可以重新发送短信,同种类型的操作一天只能发送5次短信"),
	
	/** 操作正在进行*/
	OPERATOR_IS_DO(4073, "操作正在进行"),
	/** 收货地址-身份证验证不通过 */
	CONSIGNEE_ADDRESS_IDCARD_ERROR(5016, "身份证验证不通过 "),
	
	/** 收货人姓名长度不能超过20个字符*/
	CONSIGNEE_ADDRESS_NAME_LENGHT_ERROR(5015, "收货人姓名长度不能超过20个字符");
	
	/** 通用错误码 **/
	public Integer code;
	
	/** 错误描述 */
    public String value;

	private ErrorCode(Integer code, String value) {
		this.code = code;
		this.value = value;
	}

	public static String getValue(Integer code){
		for (ErrorCode c : ErrorCode.values()) {
            if (c.code.intValue() == code.intValue()) {
                return c.value;
            }
        }
		return null;
	}
	
}
