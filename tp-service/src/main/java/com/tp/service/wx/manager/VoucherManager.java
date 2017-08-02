package com.tp.service.wx.manager;

import com.alibaba.fastjson.JSONObject;
import com.tp.common.util.RequestUtil;
import com.tp.common.vo.wx.RequestUrlConstant;

/**
 * 凭证管理器
 * @author zhuss
 *
 */
public class VoucherManager extends BaseManager{
	
	// 获取微信的调用凭证ACCESS_TOKEN
	public static String getAccessToken(String appId,String appSecret) {
		String url = RequestUrlConstant.ACCESS_TOKEN_URL.replace("APPID", appId)
				.replace("APPSECRET", appSecret).trim();
		JSONObject jsonObject = RequestUtil.httpsRequest(url, "GET", null);
		handleError(jsonObject,"获取微信的调用凭证ACCESSTOKEN");
		return jsonObject.getString("access_token");
	}

	// 获取JSAPI的凭证Ticket
	public static String getTicket(String token) {
		String url = RequestUrlConstant.JSAPI_TICKET_URL.replace("ACCESS_TOKEN", token);
		JSONObject jsonObject = RequestUtil.httpsRequest(url, "GET", null);
		handleError(jsonObject,"获取JSAPI的凭证Ticket");
		return jsonObject.getString("ticket");
	}
}
