package com.tp.service.wx.manager;

import com.tp.util.Sha1Util;

/**
 * 签名管理
 * @author zhuss
 *
 */
public class SignManager {

	/**
	 * 调用JSAPI的签名
	 * @return
	 */
	public static String signature2Jsapi(String ticket ,String noncestr,Long timestamp,String url){
		StringBuffer sb = new StringBuffer();
		sb.append("jsapi_ticket=").append(ticket).append("&");
		sb.append("noncestr=").append(noncestr).append("&");
		sb.append("timestamp=").append(timestamp).append("&");
		sb.append("url=").append(url);
		return Sha1Util.sha1LowerData(sb.toString());
	}
}
