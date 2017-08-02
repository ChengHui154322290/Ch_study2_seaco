package com.tp.dto.ord.remote;

import java.io.Serializable;

import com.tp.common.vo.prd.ItemConstant;

/**
 * 亿起发传送订单信息
 * @author szy
 *
 */
public class YiQiFaOrderDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 371438177759658386L;
	private String cid;//活动id	是	否	广告主在亿起发平台推广的标识，固定值，来自于广告入口的cid值
	private String wi;//	亿起发下级网站信息	是	否	来自于广告入口的wi值
	private String on;//	订单编号	是	否	广告主网站的订单编号
	private String pn;//	商品编号	是	否	如果是多个商品，请以“|”分开
	private String pna;//	商品名称	否	是	
	private String ct=ItemConstant.COMMISION_TYPE.COMMON.code;//	佣金类型	是	是	
	private String ta;//	商品数量	是	是	
	private String pp;//	商品单价	是	否	
	private String sd;//	下单时间	是	是	格式：yyyy-MM-dd HH:mm:ss，
	private String dt="m";//	区分标识	是	否	移动互联网手机活动，固定值为：m
	private String os;//	订单状态	是	是	
	private String ps;//	支付状态	是	是	
	private String pw;//	支付方式	是	是	
	private String far;//	运费	是	否	
	private String fav ;//	优惠额	是	否	
	private String fac;//	优惠码	是	是	
	private String encoding="utf-8";//	编码方式	否	否	如果不加此参数，默认编码方式为GBK
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getWi() {
		return wi;
	}
	public void setWi(String wi) {
		this.wi = wi;
	}
	public String getOn() {
		return on;
	}
	public void setOn(String on) {
		this.on = on;
	}
	public String getPn() {
		return pn;
	}
	public void setPn(String pn) {
		this.pn = pn;
	}
	public String getPna() {
		return pna;
	}
	public void setPna(String pna) {
		this.pna = pna;
	}
	public String getCt() {
		return ct;
	}
	public void setCt(String ct) {
		this.ct = ct;
	}
	public String getTa() {
		return ta;
	}
	public void setTa(String ta) {
		this.ta = ta;
	}
	public String getPp() {
		return pp;
	}
	public void setPp(String pp) {
		this.pp = pp;
	}
	public String getSd() {
		return sd;
	}
	public void setSd(String sd) {
		this.sd = sd;
	}
	public String getDt() {
		return dt;
	}
	public void setDt(String dt) {
		this.dt = dt;
	}
	public String getOs() {
		return os;
	}
	public void setOs(String os) {
		this.os = os;
	}
	public String getPs() {
		return ps;
	}
	public void setPs(String ps) {
		this.ps = ps;
	}
	public String getPw() {
		return pw;
	}
	public void setPw(String pw) {
		this.pw = pw;
	}
	public String getFar() {
		return far;
	}
	public void setFar(String far) {
		this.far = far;
	}
	public String getFav() {
		return fav;
	}
	public void setFav(String fav) {
		this.fav = fav;
	}
	public String getFac() {
		return fac;
	}
	public void setFac(String fac) {
		this.fac = fac;
	}
	public String getEncoding() {
		return encoding;
	}
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	//wi||下单时间||订单编号||商品编号||商品名称||佣金类型||商品数量||商品金额||订单状态||支付状态||支付方式||运费||优惠金额||优惠券编号或积分卡卡号
	@Override
	public String toString() {
		/**return "wi||"+wi  +"sd||"+sd  +"on||"+on  +"pn||"+pn  +"pna||"+pna  +"ct||"+ ct
			 + "ta||"+ta  +"pp||"+pp  +"os||"+os  +"ps||"+ps  +"pw||"+pw    +"far||"+far
			 + "fav||"+fav+"fac||"+fac;*/
		String ssx = "||";
		return  nd(wi)+ssx  +nd(sd)+ssx  +nd(on)+ssx  +nd(pn)+ssx  +nd(pna)+ssx  +
				nd(ct)+ssx  +nd(ta)+ssx  +nd(pp)+ssx  +nd(os)+ssx  +nd(ps)+ssx   +
				nd(pw)+ssx  +nd(far)+ssx +nd(fav)+ssx +nd(fac);
	}
	
	public String getUrlParam(){
		String equ = "=",and="&";
		StringBuffer params = new StringBuffer();
		return  params.append("cid").append(equ).append(cid).append(and).
				append("wi").append(equ).append(wi).append(and).
				append("on").append(equ).append(on).append(and).
				append("pn").append(equ).append(pn).append(and).
				append("pna").append(equ).append(pna).append(and).
				append("ct").append(equ).append(ct).append(and).
				append("ta").append(equ).append(ta).append(and).
				append("pp").append(equ).append(pp).append(and).
				append("sd").append(equ).append(sd).append(and).
				append("dt").append(equ).append(dt).append(and).
				append("os").append(equ).append(os).append(and).
				append("ps").append(equ).append(ps).append(and).
				append("pw").append(equ).append(pw).append(and).
				append("far").append(equ).append(far).append(and).
				append("fav").append(equ).append(fav).append(and).
				append("fac").append(equ).append(fac).append(and).
				append("encoding").append(equ).append(encoding).
				toString();
	}

	private String nd(String str){
		if(str==null){
			return "";
		}
		return str;
	}
}
