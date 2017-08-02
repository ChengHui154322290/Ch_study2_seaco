package com.tp.m.base;

import java.io.Serializable;

/**
 * 基础入参
 * 
 * @author zhuss
 *
 */
public class BaseQuery implements Serializable {

	private static final long serialVersionUID = 7968757746959715647L;

	private String ip;// 当前请求的IP
	private String oem;// 用于区别终端类型
	private String osversion;// 系统版本
	private String screenwidth;// 设备分辨率宽度
	private String screenheight;// 设备分辨率高度
	private String appversion;// 应用版本
	private String nettype;// 连接的网络类型
	private String regcode;// 用户区域信息
	private String provcode;// 用户省会信息
	private String partner;// 推广合作渠道的字段，如： pps,91zhushou、hiapk
	private String apptype; // WAP;iOS;Android;WX

	private String token;
	private Long userid;
	private String curpage;
	private String curtime; // 当前时间
	private String signature;// 签名

	// 第三方商城CODE
	private String channelcode;
	private Long shopId;// 商城对应的分销ID
	private String scheme;//请求协议HTTP/HTTPS 
	
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getOem() {
		return oem;
	}

	public void setOem(String oem) {
		this.oem = oem;
	}

	public String getOsversion() {
		return osversion;
	}

	public void setOsversion(String osversion) {
		this.osversion = osversion;
	}

	public String getScreenwidth() {
		return screenwidth;
	}

	public void setScreenwidth(String screenwidth) {
		this.screenwidth = screenwidth;
	}

	public String getScreenheight() {
		return screenheight;
	}

	public void setScreenheight(String screenheight) {
		this.screenheight = screenheight;
	}

	public String getAppversion() {
		return appversion;
	}

	public void setAppversion(String appversion) {
		this.appversion = appversion;
	}

	public String getNettype() {
		return nettype;
	}

	public void setNettype(String nettype) {
		this.nettype = nettype;
	}

	public String getRegcode() {
		return regcode;
	}

	public void setRegcode(String regcode) {
		this.regcode = regcode;
	}

	public String getProvcode() {
		return provcode;
	}

	public void setProvcode(String provcode) {
		this.provcode = provcode;
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public String getApptype() {
		return apptype;
	}

	public void setApptype(String apptype) {
		this.apptype = apptype;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public String getCurpage() {
		return curpage;
	}

	public void setCurpage(String curpage) {
		this.curpage = curpage;
	}

	public String getCurtime() {
		return curtime;
	}

	public void setCurtime(String curtime) {
		this.curtime = curtime;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getChannelcode() {
		return channelcode;
	}

	public void setChannelcode(String channelcode) {
		this.channelcode = channelcode;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public String getScheme() {
		return scheme;
	}

	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

}
