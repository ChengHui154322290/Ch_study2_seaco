package com.tp.m.vo.wx;

import com.tp.m.base.BaseVO;

/**
 * 通过config接口注入权限验证配置的基本
 * @author zhuss
 * @2016年1月30日 下午4:56:24
 */
public class ConfigVO implements BaseVO{

	private static final long serialVersionUID = 2779853018781335298L;

	private String appid; //公众号的唯一标识
	private String timestamp; //生成签名的时间戳
	private String nonceStr; //生成签名的随机串 16位
	private String signature; //签名
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getNonceStr() {
		return nonceStr;
	}
	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
}
