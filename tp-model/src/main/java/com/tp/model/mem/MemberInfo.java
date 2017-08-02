package com.tp.model.mem;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 
  */
public class MemberInfo extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451281300756L;

	/** 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/**手机号 数据类型varchar(50)*/
	private String mobile;
	
	/**邮箱 数据类型varchar(50)*/
	private String email;
	
	/**登录显示名 数据类型varchar(50)*/
	private String nickName;
	
	/**登录密码 数据类型varchar(50)*/
	private String password;
	/** 随机salt */
	private String salt;
	
	/**性别 数据类型tinyint(1)*/
	private Boolean sex;
	
	/**是否手机验证 数据类型tinyint(1)*/
	private Integer isMobileVerified;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**ip地址 数据类型varchar(20)*/
	private String ip;
	
	/**修改时间 数据类型datetime*/
	private Date modifyTime;
	
	/**平台来源  0:pc 1:app 2:wap 3:ios 4:BTM  5:wx 数据类型tinyint(1)*/
	private Integer platForm;
	
	/**状态1:正常 0:不可用 数据类型tinyint(4)*/
	private Boolean state;
	
	private Integer source;
	
	/**推广员ID 类型bigint(11)*/
	private Long promoterId;
	/**店铺ID 类型bigint(11)*/
	private Long shopPromoterId;
	/**扫码推广员ID 类型bigint(14)*/
	private Long scanPromoterId;
	
	/** 登录用户名(自定义用户名) **/
	private String userName;
	/**注册渠道*/
	private String channelCode;
	//广告来源
	private String advertFrom;
	/**推荐用户openId*/
	private String tpin;

	public String getTpin() {
		return tpin;
	}

	public void setTpin(String tpin) {
		this.tpin = tpin;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public Long getId(){
		return id;
	}
	public String getMobile(){
		return mobile;
	}
	public String getEmail(){
		return email;
	}
	public String getNickName(){
		return nickName;
	}
	public String getPassword(){
		return password;
	}
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	public Boolean getSex(){
		return sex;
	}
	public Integer getIsMobileVerified(){
		return isMobileVerified;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public String getIp(){
		return ip;
	}
	public Date getModifyTime(){
		return modifyTime;
	}
	public Integer getPlatForm(){
		return platForm;
	}
	public Boolean getState(){
		return state;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setMobile(String mobile){
		this.mobile=mobile;
	}
	public void setEmail(String email){
		this.email=email;
	}
	public void setNickName(String nickName){
		this.nickName=nickName;
	}
	public void setPassword(String password){
		this.password=password;
	}
	public void setSex(Boolean sex){
		this.sex=sex;
	}
	public void setIsMobileVerified(Integer isMobileVerified){
		this.isMobileVerified=isMobileVerified;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setIp(String ip){
		this.ip=ip;
	}
	public void setModifyTime(Date modifyTime){
		this.modifyTime=modifyTime;
	}
	public void setPlatForm(Integer platForm){
		this.platForm=platForm;
	}
	public void setState(Boolean state){
		this.state=state;
	}
	public Integer getSource() {
		return source;
	}
	public void setSource(Integer source) {
		this.source = source;
	}
	public Long getPromoterId() {
		return promoterId;
	}
	public void setPromoterId(Long promoterId) {
		this.promoterId = promoterId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getShopPromoterId() {
		return shopPromoterId;
	}

	public void setShopPromoterId(Long shopPromoterId) {
		this.shopPromoterId = shopPromoterId;
	}

	public Long getScanPromoterId() {
		return scanPromoterId;
	}

	public void setScanPromoterId(Long scanPromoterId) {
		this.scanPromoterId = scanPromoterId;
	}

	public String getAdvertFrom() {
		return advertFrom;
	}

	public void setAdvertFrom(String advertFrom) {
		this.advertFrom = advertFrom;
	}
		
}
