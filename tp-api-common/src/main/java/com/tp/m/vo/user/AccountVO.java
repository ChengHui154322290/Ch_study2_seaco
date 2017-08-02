package com.tp.m.vo.user;

import com.tp.m.base.BaseVO;
import com.tp.m.vo.promoter.PromoterInfoMobileVO;


/**
 * 账户返回对象
 * @author zhuss
 * @2016年1月3日 下午5:31:15
 */
public class AccountVO implements BaseVO{

	private static final long serialVersionUID = 5364318244658799106L;

	private String token;//用户凭证
	private String tel;//手机号
	private String name;//昵称
	private String source = "0";//来源:0普通 1微信公众号2微信联合登录3QQ联合登录
	private String isneedbindtel; //是否需要绑定手机 0否 1是
	private String promoterinfo;
	private String headimg;//头像
	private String showfastshoporder="0";//是否显示速购店铺订单
	private String showfastorder="0";//是否显示速购配送订单

	private String address ;//地址
	private String contact;//联系人
	
	private String channelCode;
	
	private PromoterInfoMobileVO promoterinfomobile;
	
	public AccountVO() {
		super();
	}
	public AccountVO(String token, String tel, String name) {
		super();
		this.token = token;
		this.tel = tel;
		this.name = name;
	}
	
	
	
	public PromoterInfoMobileVO getPromoterinfomobile() {
		return promoterinfomobile;
	}
	public void setPromoterinfomobile(PromoterInfoMobileVO promoterinfomobile) {
		this.promoterinfomobile = promoterinfomobile;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getIsneedbindtel() {
		return isneedbindtel;
	}
	public void setIsneedbindtel(String isneedbindtel) {
		this.isneedbindtel = isneedbindtel;
	}
	public String getPromoterinfo() {
		return promoterinfo;
	}
	public void setPromoterinfo(String promoterinfo) {
		this.promoterinfo = promoterinfo;
	}
	public String getHeadimg() {
		return headimg;
	}
	public void setHeadimg(String headimg) {
		this.headimg = headimg;
	}
	public String getShowfastshoporder() {
		return showfastshoporder;
	}
	public void setShowfastshoporder(String showfastshoporder) {
		this.showfastshoporder = showfastshoporder;
	}
	public String getShowfastorder() {
		return showfastorder;
	}
	public void setShowfastorder(String showfastorder) {
		this.showfastorder = showfastorder;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getChannelCode() {
		return channelCode;
	}
	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}
	
}
