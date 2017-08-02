package com.tp.model.app;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 统计相关 公共参数表
  */
public class StatisticPublic extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450431557815L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**终端类型 数据类型varchar(20)*/
	private String oem;
	
	/**设备型号 数据类型varchar(20)*/
	private String deviceModel;
	
	/**终端运行的系统版本 数据类型varchar(20)*/
	private String osVersion;
	
	/**设备分辨率宽度 数据类型varchar(10)*/
	private String screenWidth;
	
	/** 数据类型varchar(10)*/
	private String screenHeight;
	
	/**两种类型
“0”移动应用
“1”移动网页(wap)
 数据类型varchar(2)*/
	private String appType;
	
	/**连接网络类型:  wifi,3g,2g,4g,unknown等 数据类型varchar(255)*/
	private String netType;
	
	/**网络运营商信息 数据类型varchar(20)*/
	private String operator;
	
	/**用户区域信息 例如:”华东” 数据类型varchar(20)*/
	private String regCode;
	
	/**用户省会信息 数据类型varchar(20)*/
	private String provCode;
	
	/**设备ID Windows Phone客户端用该字段作为唯一设备标示符 数据类型varchar(64)*/
	private String deviceId;
	
	/**终端网络设备 MAC地址  MAC和iOS客户端用该字段作为唯一设备标示符 数据类型varchar(16)*/
	private String macAddress;
	
	/** 数据类型bigint(20)*/
	private Long userId;
	
	/**iOS系统上为APP的bundleID，Android系统上为APP的packagename；PPS影音缺省为空 数据类型varchar(50)*/
	private String bundleId;
	
	/** 数据类型varchar(100)*/
	private String appName;
	
	/**推广合作渠道的字段 数据类型varchar(100)*/
	private String partner;
	
	/** 数据类型varchar(100)*/
	private String initPartner;
	
	/**通过开源项目
openUDID计算得到的设备唯一标示符
 数据类型varchar(100)*/
	private String openUdid;
	
	/**Android设备唯一标示符 Android客户端（包括手机和PAD）特有字段，使用该字段作为唯一设备标示符 数据类型varchar(32)*/
	private String uuid;
	
	/**iOS设备型号 数据类型varchar(50)*/
	private String platform;
	
	/**iOS标示设备是否越狱  iOS客户端特有字段，其他终端设备没有,1,越狱；0，非越狱 数据类型varchar(2)*/
	private String isJailBreak;
	
	/**终端设备生产厂家信息  Android和Windows Phone客户端特有字段 数据类型varchar(100)*/
	private String manufacturer;
	
	/** 广告ID IOS 特有字段 数据类型varchar(36)*/
	private String adsId;
	
	/** 数据类型datetime*/
	private Date createTime;
	
	/** 数据类型varchar(255)*/
	private String remark;
	
	/** 数据类型varchar(20)*/
	private String appVersion;
	
	
	public Long getId(){
		return id;
	}
	public String getOem(){
		return oem;
	}
	public String getDeviceModel(){
		return deviceModel;
	}
	public String getOsVersion(){
		return osVersion;
	}
	public String getScreenWidth(){
		return screenWidth;
	}
	public String getScreenHeight(){
		return screenHeight;
	}
	public String getAppType(){
		return appType;
	}
	public String getNetType(){
		return netType;
	}
	public String getOperator(){
		return operator;
	}
	public String getRegCode(){
		return regCode;
	}
	public String getProvCode(){
		return provCode;
	}
	public String getDeviceId(){
		return deviceId;
	}
	public String getMacAddress(){
		return macAddress;
	}
	public Long getUserId(){
		return userId;
	}
	public String getBundleId(){
		return bundleId;
	}
	public String getAppName(){
		return appName;
	}
	public String getPartner(){
		return partner;
	}
	public String getInitPartner(){
		return initPartner;
	}
	public String getOpenUdid(){
		return openUdid;
	}
	public String getUuid(){
		return uuid;
	}
	public String getPlatform(){
		return platform;
	}
	public String getIsJailBreak(){
		return isJailBreak;
	}
	public String getManufacturer(){
		return manufacturer;
	}
	public String getAdsId(){
		return adsId;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public String getRemark(){
		return remark;
	}
	public String getAppVersion(){
		return appVersion;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setOem(String oem){
		this.oem=oem;
	}
	public void setDeviceModel(String deviceModel){
		this.deviceModel=deviceModel;
	}
	public void setOsVersion(String osVersion){
		this.osVersion=osVersion;
	}
	public void setScreenWidth(String screenWidth){
		this.screenWidth=screenWidth;
	}
	public void setScreenHeight(String screenHeight){
		this.screenHeight=screenHeight;
	}
	public void setAppType(String appType){
		this.appType=appType;
	}
	public void setNetType(String netType){
		this.netType=netType;
	}
	public void setOperator(String operator){
		this.operator=operator;
	}
	public void setRegCode(String regCode){
		this.regCode=regCode;
	}
	public void setProvCode(String provCode){
		this.provCode=provCode;
	}
	public void setDeviceId(String deviceId){
		this.deviceId=deviceId;
	}
	public void setMacAddress(String macAddress){
		this.macAddress=macAddress;
	}
	public void setUserId(Long userId){
		this.userId=userId;
	}
	public void setBundleId(String bundleId){
		this.bundleId=bundleId;
	}
	public void setAppName(String appName){
		this.appName=appName;
	}
	public void setPartner(String partner){
		this.partner=partner;
	}
	public void setInitPartner(String initPartner){
		this.initPartner=initPartner;
	}
	public void setOpenUdid(String openUdid){
		this.openUdid=openUdid;
	}
	public void setUuid(String uuid){
		this.uuid=uuid;
	}
	public void setPlatform(String platform){
		this.platform=platform;
	}
	public void setIsJailBreak(String isJailBreak){
		this.isJailBreak=isJailBreak;
	}
	public void setManufacturer(String manufacturer){
		this.manufacturer=manufacturer;
	}
	public void setAdsId(String adsId){
		this.adsId=adsId;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setRemark(String remark){
		this.remark=remark;
	}
	public void setAppVersion(String appVersion){
		this.appVersion=appVersion;
	}
}
