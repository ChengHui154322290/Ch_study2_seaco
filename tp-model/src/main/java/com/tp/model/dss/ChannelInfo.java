package com.tp.model.dss;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.common.vo.Constant;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 营销渠道信息表
  */
public class ChannelInfo extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1462266031623L;

	/**营销渠道ID 数据类型int(8)*/
	@Id
	private Integer channelId;
	
	/**渠道代码 数据类型varchar(16)*/
	private String channelCode;
	
	/**渠道名称 数据类型varchar(32)*/
	private String channelName;
	
	/**渠道备注 数据类型varchar(128)*/
	private String remark;
	
	/**渠道备注 数据类型varchar(64)*/
	private String token;
	
	/**父级渠道ID 数据类型int(8)*/
	private Integer parentChannelId;
	
	/**是否已禁用0:否，1是，默认0 数据类型tinyint(1)*/
	private Integer disabled;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**创建人 数据类型varchar(32)*/
	private String createUser;
	
	/**更新时间 数据类型datetime*/
	private Date updateTime;
	
	/**更新时间 数据类型varchar(32)*/
	private String updateUser;
	
	/** 渠道商城名称 */
	private String eshopName;
	
	/** 渠道分享标题 */
	private String shareTitle;
	
	/** 渠道分享内容 */
	private String shareContent;
	
	/** 第三方商城分销类型：0无分销1店铺分销 */
	private Integer companyDssType;
	/**是否已禁用0:否，1是，默认0 数据类型tinyint(1)*/
	/**是否使用第三方商城的积分 1是，默认0  不使用*/
	private String isUsedPoint;
	//对应推广信息
	@Virtual
	private PromoterInfo promoterInfo;
	
	public String getDisabledStr(){
		return Constant.DISABLED.NO.equals(disabled)?"否":"是";
	}
	public Integer getChannelId(){
		return channelId;
	}
	public String getChannelCode(){
		return channelCode;
	}
	public String getChannelName(){
		return channelName;
	}
	public String getRemark(){
		return remark;
	}
	public Integer getParentChannelId(){
		return parentChannelId;
	}
	public Integer getDisabled(){
		return disabled;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public String getCreateUser(){
		return createUser;
	}
	public Date getUpdateTime(){
		return updateTime;
	}
	public String getUpdateUser(){
		return updateUser;
	}
	public void setChannelId(Integer channelId){
		this.channelId=channelId;
	}
	public void setChannelCode(String channelCode){
		this.channelCode=channelCode;
	}
	public void setChannelName(String channelName){
		this.channelName=channelName;
	}
	public void setRemark(String remark){
		this.remark=remark;
	}
	public void setParentChannelId(Integer parentChannelId){
		this.parentChannelId=parentChannelId;
	}
	public void setDisabled(Integer disabled){
		this.disabled=disabled;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setCreateUser(String createUser){
		this.createUser=createUser;
	}
	public void setUpdateTime(Date updateTime){
		this.updateTime=updateTime;
	}
	public void setUpdateUser(String updateUser){
		this.updateUser=updateUser;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getEshopName() {
		return eshopName;
	}
	public void setEshopName(String eshopName) {
		this.eshopName = eshopName;
	}
	public String getShareTitle() {
		return shareTitle;
	}
	public void setShareTitle(String shareTitle) {
		this.shareTitle = shareTitle;
	}
	public String getShareContent() {
		return shareContent;
	}
	public void setShareContent(String shareContent) {
		this.shareContent = shareContent;
	}
	public PromoterInfo getPromoterInfo() {
		return promoterInfo;
	}
	public void setPromoterInfo(PromoterInfo promoterInfo) {
		this.promoterInfo = promoterInfo;
	}
	public Integer getCompanyDssType() {
		return companyDssType;
	}
	public void setCompanyDssType(Integer companyDssType) {
		this.companyDssType = companyDssType;
	}
	public String getIsUsedPoint() {
		return isUsedPoint;
	}
	public void setIsUsedPoint(String isUsedPoint) {
		this.isUsedPoint = isUsedPoint;
	}
	
}
