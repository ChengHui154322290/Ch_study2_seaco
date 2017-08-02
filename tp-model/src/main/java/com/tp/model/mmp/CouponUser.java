package com.tp.model.mmp;

import java.io.Serializable;
import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.dto.mmp.enums.CouponUserStatus;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 发放优惠券
  */
public class CouponUser extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451440579105L;

	/**id 数据类型bigint(18)*/
	@Id
	private Long id;
	
	/**优惠券批次id 数据类型bigint(18)*/
	private Long batchId;
	
	/**状态 0 可用，1 已使用 2 已过期  3:作废 4： 删除 数据类型int(1)*/
	private Integer status;
	
	/**用邮箱登录的用户邮箱 数据类型varchar(150)*/
	private String toUserLogin;
	
	/**接受者的手机号 数据类型varchar(50)*/
	private String toUserMobile;
	
	/**优惠券批次（暂时不用此字段） 数据类型int(11)*/
	private Integer batchNum;
	
	/**优惠券编码 数据类型varchar(255)*/
	private String number;
	
	/**来源类型 1-达人粉丝计划 数据类型int(1)*/
	private Integer source;
	
	/**来源名称,由发起方传入 数据类型varchar(100)*/
	private String sourceName;
	
	/**参考编号,由发起方传入 数据类型varchar(50)*/
	private String refCode;
	
	/**被发放的用户id 数据类型bigint(18)*/
	private Long toUserId;
	
	/**发放的用户id 数据类型bigint(18)*/
	private Long createUserId;
	
	/**发放者 数据类型varchar(50)*/
	private String createUserName;
	
	/**发放时间 数据类型datetime*/
	private Date createTime;
	
	/**修改时间 数据类型datetime*/
	private Date updateTime;
	
	/**优惠券类型  0 : 满减券  1：现金券 数据类型int(1)*/
	private Integer couponType;
	
	/** 数据类型datetime*/
	private Date couponUseStime;
	
	/** 数据类型datetime*/
	private Date couponUseEtime;
	
	@Virtual
	private Coupon coupon;
	/**推广员编码*/
	private Long promoterId;
	
	public String getStatusCn(){
		return CouponUserStatus.getCnName(status);
	}
	public Long getId(){
		return id;
	}
	public Long getBatchId(){
		return batchId;
	}
	public Integer getStatus(){
		return status;
	}
	public String getToUserLogin(){
		return toUserLogin;
	}
	public String getToUserMobile(){
		return toUserMobile;
	}
	public Integer getBatchNum(){
		return batchNum;
	}
	public String getNumber(){
		return number;
	}
	public Integer getSource(){
		return source;
	}
	public String getSourceName(){
		return sourceName;
	}
	public String getRefCode(){
		return refCode;
	}
	public Long getToUserId(){
		return toUserId;
	}
	public Long getCreateUserId(){
		return createUserId;
	}
	public String getCreateUserName(){
		return createUserName;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Date getUpdateTime(){
		return updateTime;
	}
	public Integer getCouponType(){
		return couponType;
	}
	public Date getCouponUseStime(){
		return couponUseStime;
	}
	public Date getCouponUseEtime(){
		return couponUseEtime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setBatchId(Long batchId){
		this.batchId=batchId;
	}
	public void setStatus(Integer status){
		this.status=status;
	}
	public void setToUserLogin(String toUserLogin){
		this.toUserLogin=toUserLogin;
	}
	public void setToUserMobile(String toUserMobile){
		this.toUserMobile=toUserMobile;
	}
	public void setBatchNum(Integer batchNum){
		this.batchNum=batchNum;
	}
	public void setNumber(String number){
		this.number=number;
	}
	public void setSource(Integer source){
		this.source=source;
	}
	public void setSourceName(String sourceName){
		this.sourceName=sourceName;
	}
	public void setRefCode(String refCode){
		this.refCode=refCode;
	}
	public void setToUserId(Long toUserId){
		this.toUserId=toUserId;
	}
	public void setCreateUserId(Long createUserId){
		this.createUserId=createUserId;
	}
	public void setCreateUserName(String createUserName){
		this.createUserName=createUserName;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setUpdateTime(Date updateTime){
		this.updateTime=updateTime;
	}
	public void setCouponType(Integer couponType){
		this.couponType=couponType;
	}
	public void setCouponUseStime(Date couponUseStime){
		this.couponUseStime=couponUseStime;
	}
	public void setCouponUseEtime(Date couponUseEtime){
		this.couponUseEtime=couponUseEtime;
	}
	public Coupon getCoupon() {
		return coupon;
	}
	public void setCoupon(Coupon coupon) {
		this.coupon = coupon;
	}
	public Long getPromoterId() {
		return promoterId;
	}
	public void setPromoterId(Long promoterId) {
		this.promoterId = promoterId;
	}
}
