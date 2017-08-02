/**
 * 
 */
package com.tp.model.wms.jdz;

import com.tp.common.vo.wms.JDZWmsConstant;
import com.tp.util.Base64;
import com.tp.util.MD5Util;

/**
 * @author Administrator
 *
 */
public class JdzRequestUser {

	 /** 应用登录key */
	private String appkey;
	
	 /**  登录密码   */
	private String secret;

	public String getAppkey() {
		return appkey;
	}

	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}
	
	public static String encryptSecret(String secret) {
		StringBuilder sb = new StringBuilder();
		String captcha = new MD5Util().getMD5ofStr(JDZWmsConstant.JDZ_PASSWORD);
		sb.append(secret).append(captcha);
		return Base64.encode(sb.toString().getBytes());
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(!( obj instanceof  JdzRequestUser)) return  false;
		JdzRequestUser target = (JdzRequestUser) obj;
		if(target == null || target.getAppkey()==null || target.getSecret() == null) return false;
		return this.getSecret().equalsIgnoreCase(target.getSecret()) && this.getAppkey().equalsIgnoreCase(target.getAppkey());


	}
}
