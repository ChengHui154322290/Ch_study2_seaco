/**
 * 
 */
package com.tp.m.vo.promoter;

import com.tp.m.base.BaseVO;

/**
 * @author Administrator
 *
 */
public class PromoterInfoMobileVO implements BaseVO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2630621635146660435L;
	
	String promoterid;
	String nickname;
	String weixin;
	String qq;
	String mobile;
	String email;
	String name;
	String credentialtype;
	String credentialcode;
	String bankname;
	String bankaccount;
	String alipay;
	String pageshow;
	String iscoupondss;
	String isshopdss;
	String isscandss;
	String shopmobile;
	String shopnickname;
	/**是否一级扫码推广员 0不是1是*/
	private String isTopScandss;
	
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getWeixin() {
		return weixin;
	}
	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCredentialtype() {
		return credentialtype;
	}
	public void setCredentialtype(String credentialtype) {
		this.credentialtype = credentialtype;
	}
	public String getCredentialcode() {
		return credentialcode;
	}
	public void setCredentialcode(String credentialcode) {
		this.credentialcode = credentialcode;
	}
	public String getBankname() {
		return bankname;
	}
	public void setBankname(String bankname) {
		this.bankname = bankname;
	}
	public String getBankaccount() {
		return bankaccount;
	}
	public void setBankaccount(String bankaccount) {
		this.bankaccount = bankaccount;
	}
	public String getAlipay() {
		return alipay;
	}
	public void setAlipay(String alipay) {
		this.alipay = alipay;
	}
	public String getPageshow() {
		return pageshow;
	}
	public void setPageshow(String pageshow) {
		this.pageshow = pageshow;
	}
	public String getIscoupondss() {
		return iscoupondss;
	}
	public void setIscoupondss(String iscoupondss) {
		this.iscoupondss = iscoupondss;
	}
	public String getIsshopdss() {
		return isshopdss;
	}
	public void setIsshopdss(String isshopdss) {
		this.isshopdss = isshopdss;
	}	
	public String getIsscandss() {
		return isscandss;
	}
	public void setIsscandss(String isscandss) {
		this.isscandss = isscandss;
	}
	public String getShopmobile() {
		return shopmobile;
	}
	public void setShopmobile(String shopmobile) {
		this.shopmobile = shopmobile;
	}
	public String getShopnickname() {
		return shopnickname;
	}
	public void setShopnickname(String shopnickname) {
		this.shopnickname = shopnickname;
	}
	public String getPromoterid() {
		return promoterid;
	}
	public void setPromoterid(String promoterid) {
		this.promoterid = promoterid;
	}
	public String getIsTopScandss() {
		return isTopScandss;
	}
	public void setIsTopScandss(String isTopScandss) {
		this.isTopScandss = isTopScandss;
	}	
	
}
