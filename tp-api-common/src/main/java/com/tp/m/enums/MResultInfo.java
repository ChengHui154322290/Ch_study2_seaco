package com.tp.m.enums;

public enum MResultInfo {

	//---------------------------特殊处理的类型信息-----------------------
	ACCOUNT_TIMEOUT("-100","用户失效,请重新登录"),
	BIND_MOBILE_NO("-101","该账户未绑定手机,请绑定手机号"),
	CART_CHECK_ERROR("102","购物车校验失败"),
	NO_PROMOTER("-103","无此分销员信息"),
	USER_AUTH_NULL("-104","用户未认证"),
	USER_AUTH_IMG_NULL("-105","用户未上传身份证照片"),

	//---------------------------系统处理：code为 -10000-----------------------
	SYSTEM_ERROR("-10001","系统异常，请稍后再试"),
	CONN_ERROR("-10002","网络连接异常，请稍后再试"),
	CONN_TIMEOUT("-10003","网络连接超时，请稍后再试"),
	PARAM_ERROR("-10004","参数异常,请检查后再试"),
	SIGNATURE_ERROR("-10005","服务器开小差了"),
	APPTYPE_NULL("-10006","平台不能为空"),
	APPVERSION_NULL("-10007","版本号不能为空"),
	APPVERSION_SIGN_NULL("-10008","验证码已离家出走，去最新版本找它 "),
	//------------------------------查询 结果 统一提示信息-----------------------
	SUCCESS("0","操作成功"),
	FAILED("-1","操作失败"), 
	//------------------------------新增 修改 删除 成功统一提示信息-----------------------
	OPERATION_SUCCESS("0","操作成功"),
	ADD_SUCCESS("0","新增成功"),
	MODIFY_SUCCESS("0","更新成功"),
	DEL_SUCCESS("0","删除成功"),
	LOGIN_SUCCESS("0","登录成功"),
	LOGOUT_SUCCESS("0","退出成功"),
	REGISTER_SUCCESS("0","注册成功"),
	CANCEL_SUCCESS("0","取消成功"),
	BIND_SUCCESS("0","绑定成功"),
	CAPTCHA_SUCCESS("0","验证码已发送"),
	AUTH_SUCCESS("0","认证成功"),
	COMMENT_SUCCESS("0","评论成功"),
	RECEIVE_SUCCESS("0","领取成功"),
	EXCHANGE_SUCCESS("0","兑换成功"),
	EXCHANGE_POINT_SUCCESS("0","你的优惠券已经兑换成西客币，\n请到“我的西客币”查看"),
	CONFIRE_SUCCESS("0","确认成功"),
	SUBMIT_SUCCESS("0","提交成功"),
	SHARE_SUCCESS("0","分享成功"),
	IMAGE_UPLOAD_SUCCESS("0","上传文件成功"),
	//------------------------------新增 修改 删除 失败统一提示信息-----------------------
	OPERATION_FAILED("-20001","操作失败"),
	ADD_FAILED("-20002","新增失败"),
	MODIFY_FAILED("-20003","更新失败"),
	DEL_FAILED("-20004","删除失败"),
	LOGIN_FAILED("-20005","登录失败"),
	LOGOUT_FAILED("-20006","退出失败"),
	REGISTER_FAILED("-20007","注册失败"),
	CANCEL_FAILED("-20008","取消失败"),
	BIND_FAILED("-20009","绑定失败"),
	CAPTCHA_FAILED("-20010","验证码发送失败"),
	AUTH_FAILED("-20011","认证失败"),
	COMMENT_FAILED("-20012","评论失败"),
	RECEIVE_FAILED("-20013","领取失败"),
	EXCHANGE_FAILED("-20014","兑换失败"),
	CONFIRE_FAILED("-20015","确认失败"),
	SUBMIT_FAILED("-20016","提交失败"),
	SHARE_FAILED("-20017","分享失败"),
	IMAGE_UPLOAD_FAILED("-20018","上传文件失败"),
	IMAGE_DECODE_ERROR("-20019","图片处理失败"),
	CACHE_SET_FAILED("-20020","设置缓存失败"),
	CACHE_GET_FAILED("-20021","获取缓存失败"),
	CACHE_DEL_FAILED("-20022","删除缓存失败"),
	LOAD_FAILED("-20023","加载失败"),
	//------------------------------参数错误  统一处理信息-----------------------
	//------缓存处理信息-30000------
	TOKEN_NULL("-30001","TOKEN不能为空"),
	TOKEN_NO_EXIST("-30002","TOKEN不存在"),
	TOKEN_NO_USER("-30003","TOKEN不存在对应的用户信息"),
	CAPTCHA_ERROR("-30004","短信验证码输入错误"),
	//------常用参数验证处理信息-31000------
	TYPE_NOT_IN_SCOPE("-31001","操作类型不在范围内"),
	LOGONNAME_NO_VALID("-31002","请输入正确的用户名"),
	TELEPHONE_NO_VALID("-31003","请输入正确的手机号"),
	EMAIL_NO_VALID("-31004","请输入正确的邮箱"),
	PASSWORD_NO_VALID("-31005","请输入6-12位数字或字母作为密码"),
	CAPTCHA_NO_VALID("-31006","请输入正确的验证码"),
	ID_NO_VALID("-31007","身份证号码输入不合法"),
	TYPE_NULL("-31008","类型不能为空"),
	PLATFORM_NULL("-31009","平台不能为空"),
	PLATFORM_NO_EXIST("-31010","平台不匹配"),
	AREA_ID_NULL("-31011","地区不能为空"),
	CODE_NULL("-31012","CODE不能为空"),
	CODE_NO_VALID("-31013","CODE不能不合法"),
	URL_NULL("-31014","请求URL不能为空"),
	TYPE_ERROR("-31015","请选择正确的操作类型"),
	WX_URL_NULL("-31016","微信API的URL不能为空"),	
	USERAGREEMENT_NOT_AGREED("-31017","没有同意西客商城用户协议"),
	ALIPAY_NOT_VALID("-31018","请输入正确的支付宝账号"),
	CONFIG_URL_NULL("-31014","当前页URL不能为空"),	
	CAPTCHA_TYPE_NULL("-31015","图形验证码类型不在范围内"),	
	//------文件处理信息-32000------
	IMAGE_NULL("-32001","上传图片不能为空"),
	IMAGE_MAX_ERROR("-32002","上传图片不能超过2M"),
	BASE64_ENCRYPT_ERROR("-32003","Base64加密错误"),
	BASE64_DECRYPT_ERROR("-32004","Base64解密错误"),
	AES_ENCRYPT_ERROR("-32005","AES加密错误"),
	AES_DECRYPT_ERROR("-32006","AES解密错误"),
	//------专题商品参数处理信息-33000------
	TOPIC_ID_NULL("-33001","专题不能为空"),
	COUPON_PRODUCT_NULL("-33002","商品信息不能为空"),
	ITEM_NULL("-33003","请选择至少一个商品"),
	ITEM_ID_NULL("-33004","商品不能为空"),
	ITEM_SKU_NULL("-33005","商品不能为空"),
	ITEM_CODE_NULL("-33006","商品不能为空"),
	ITEM_COUNT_NULL("-33007","商品数量不能为空"),
	ITEM_TYPE_NULL("-33008","商品状态不能为空"),
	ITEM_ERROR("-33009","商品异常"),
	//------用户参数信息-34000------
	ACCOUNT_NOT_BIND("-34001","用户未绑定手机,请绑定"),
	TEL_EXIST("-34002","手机号已经存在"),
	TEL_NO_EXIST("-34003","手机号不存在"),
	USER_AUTH("-34004","用户已认证"),
	USER_NO_AUTH("0","用户未认证"),
	NAME_NULL("-34005","姓名不能为空"),
	USER_NO_EXIST("-34006","用户不存在"),
	ADDRESS_ID_NULL("-34007","收货地址不能为空"),
	ADDRESS_NAME_NULL("-34008","收货人不能为空"),
	ADDRESS_PROV_NULL("-34009","省份不能为空"),
	ADDRESS_CITY_NULL("-34010","城市不能为空"),
	ADDRESS_INFO_NULL("-34011","详细信息不能为空"),
	COUPON_CODE_NULL("-34012","优惠券编号不能为空"),
	COMMENT_SERSCORE_NULL("-34013","服务评分不能为空"),
	COUPON_ID_NULL("-34019","优惠券ID不能为空"),
	COMMENT_ITEMSCORE_NULL("-34014","商品评分不能为空"),
	UNION_OPENID_NULL("-34015","微信OPENDI不能为空"),
	USER_AUTH_FRONT_IMG_NULL("-34016","请上传身份证正面照片"),
	USER_AUTH_BACK_IMG_NULL("-34017","请上传身份证反面照片"),

	//------订单参数信息-35000------
	ORDER_CODE_NULL("-35001","订单号不能为空"),
	ORDER_TYPE_NULL("-35002","订单类型不能为空"),
	CHANNEL_NULL("-35003","渠道不能为空"),
	PAYWAY_ERROR("-35004","请选择一种支付方式"),
	PAYINFO_NULL("-35005","订单异常,无法支付"),
	PAYINFO_USER_NOT_MATCH("-35006","订单用户和登录用户不匹配"),
	PAYINFO_ORDER_CANCEL("-35007","订单已取消,支付失败"),
	PAYINFO_PAYING("-35008","订单支付中,不允许重复支付"),
	OPENID_NULL("-35009","请在微信客户端支付"),
	PAYINFO_PAYED("-35010","订单已支付,不允许重复支付"),
	RETURN_REASON_NULL("-35011","退货原因不能为空"),
	RETURN_COUNT_NULL("-35012","退货数量不能为空"),
	RETURN_LINKNAME_NULL("-35013","联系人不能为空"),
	RETURN_ID_NULL("-35014","请至少选择一条售后订单"),
	RETURN_LOGCODE_NULL("-35015","物流单号不能为空"),
	RETURN_COMPANY_NULL("-35016","物流公司不能为空"),
	RETURN_ERROR_NO_ITEM("-35017","无效的售后申请,请至少选择一件商品"),
	PAYWAY_NULL("-35018","请选择至少一种支付方式"),
	//-------------推广员信息-36000------------
	PROMOTER_TYPE_NULL("-36001", "推广员类型不能为空"),
	PROMOTER_QUERY_LEVEL_CODE_NULL("-36002", "推广员层级不能为空"),
	CAPTCHA_NO_NULL("-36003", "验证码不能为空"),
	NICKNAME_NO_NULL("-36004", "昵称不能为空"),
	PROMOTER_NOT_EXIST("-36005", "推广员不存在"),
	UPDATE_PROMOTERINFO_FAILED("-36006", "更新推广员信息失败"),
	DO_NOT_HAVE_PROMOTERINFO_FOR_UPDATE("-36007", "没有可更改的推广员信息"),
	NOT_ENOUGH_SURPLUS("-36008", "账户全额不足"),
	PROMOTER_ID_IS_NULL("-36009", "推广员ID不能为空"),
	PROMOTERNAME_IS_BLANK("-36010", "推广员姓名不能为空"),
	CREDENTIALTYPE_IS_NULL("-36011", "推广员证件类型不能为空"),
	CREDENTIALCODE_IS_BLANK("-36012", "推广员证件号码不能为空"),
	BANKNAME_IS_BLANK("-36013", "推广员银行名称不能为空"),
	BANKACCOUNT_IS_BLANK("-36014", "账号不能为空"),
	DO_NOT_HAVE_WITHDRAWING("-36015", "未读取到提现详情"),
	NO_SONPROMOTER("-36016", "无此下级分销员"),
	SONPROMOTER_ID_IS_NULL("-36017", "下级分销员ID不能为空"),
	CURWITHDRAW_IS_NULL("-36018", "提现金额不能为空"),
	CURWITHDRAW_IS_ZERO("-36019", "提现金额不能为零"),
	BANK_OR_ALIPAY_IS_BLANK("-36020", "银行账号和支付宝均为空"),
	BANKNAME_IS_WRONG("-36021", "不能使用此银行名称"),
	ORDER_LIST_EMPTY("-36022","限时优惠，下单支付成功就开通店铺。"),
	CURWITHDRAW_LESS_THAN_ZERO("-36023", "提现金额应大于零"),

	NOT_HAVE_TOPIC("-36101", "活动专题不存在"),
	NOT_HAVE_TOPICITEM("-36102", "活动专题商品不存在"),
	TOPICID_IS_NULL("-36103", "专题ID不能为空"),
	SHELF_TYPE_IS_NULL("-36104", "上架类型不能为空"),
	ON_SHELF_IS_NULL("-36105", "上架状态不能为空"),
	WRONG_SHELF_TYPE("-36106", "上下架类型参数错误"),
	WRONG_SHELF_STATUS("-36107", "上下架状态参数错误"),
	QUERY_TOPIC_FAILED("-36108", "查询分销专题失败"),
	WRONG_TOPIC_TYPE("-36109", "专题类型错误"),
	TOPIC_TYPE_IS_NULL("-36110", "专题类型为空"),
	TOPIC_CAN_NOT_SET_ON_SHELF("-36111", "此专题不能进行上下架设置"),
	PROMOTER_SCAN_IS_EXIST("-36112", "该手机号码已是扫码推广员"),
	PROMOTER_NAME_SCAN_IS_EXIST("-36113", "名称已存在，不能重复申请"),
	CHANNEL_INFO_NOT_EXIST("-36114", "渠道信息不存在"),

	/**西客币支付**/

	SEAGOOR_PAY_SYSTEM_ERROR("5000","系统错误"),
	SEAGOOR_PAY_PARAM_ERROR("5100","参数错误"),
	SEAGOOR_PAY_SIGN_ERROR ("5120","签名错误"),
	SEAGOOR_PAY_PAY_CODE_EXPIRED("5130","支付码已过期"),
	SEAGOOR_PAY_MERCHANT_NOT_EXIST("5210","商户不存在"),
	SEAGOOR_PAY_ORDER_PAID("5220","订单已支付"),
	SEAGOOR_PAY_ORDER_CODE_USED("5230","订单号重复"),
	SEAGOOR_PAY_ORDER_NOT_EXIST("5240","支付单不存在"),
	SEAGOOR_PAY_ORDER_NOT_PAID("5250","未支付的支付单"),
	SEAGOOR_PAY_QUERY_ORDER_FIELD("5260","查询支付信息失败"),
	SEAGOOR_PAY_REFUND_ORDER_OVER_TIME("5270","订单超过一年,退款失败"),
	SEAGOOR_PAY_BALANCE_NOT_ENOUGH("5310","用户余额不足"),
	SEAGOOR_PAY_REFUND_CHECK_POINT_ERROR("5320","退款错误"),
	SEAGOOR_PAY_REFUND_NOT_ENOUGH("5330","累计退款金额超过支付金额"),
	SEAGOOR_PAY_REFUND_ORDER_CODE_USED("5340","退款单号重复"),
	SEAGOOR_PAY_REFUND_QUERY_FAILED("5350","查询退款记录失败"),


	;
	
	/** 错误码 */
	public String code;
	
	/** 错误描述 */
    public String message;

	private MResultInfo(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public static String getMessage(String code){
		for (MResultInfo c : MResultInfo.values()) {
            if (c.code .equals(code)) {
                return c.message;
            }
        }
		return null;
	}
}
