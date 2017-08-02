package com.tp.common.vo.wx;

public class RequestUrlConstant {

	/** 基础接口 **/
	//基础调用凭证（GET）
	public static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	//调用JSAPI凭证（GET）
	public static final String JSAPI_TICKET_URL ="https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
	
	/** 用户管理接口 **/
	//用户列表（GET）
	public static final String USER_LIST_URL = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN";
	//网页授权第一步获取CODE的URL
	public static final String OAUTH2_CODE_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
	//通过code换取网页授权凭证（GET）
	public static final String OAUTH2_ACCESS_TOKEN_URL="https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
	//弹出授权页面，同意授权后获取用户信息，此处access_token和基础的access_token不同（GET）
	public static final String OAUTH2_GET_USERINFO_URL ="https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
	
	/** 菜单管理接口 **/
	// 菜单创建（POST） 限100（次/天）
	public final static String MENU_CREATE_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
	// 菜单查询（GET）
	public final static String MENU_GET_URL = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN";
	// 菜单删除（GET）
	public final static String MENU_DELETE_URL = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";
	
	/** 账户管理接口 **/
	// 二维码（POST）
	public final static String QRCODE_CREATE_URL = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=ACCESS_TOKEN";
	// 显示二维码图片（GET）
	public final static String QRCODE_SHOW_URL = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=TICKET";
	//LOGo图片地址
	public final static String LOGO_IMAGE_PATH="/logo/logo.png";
}
