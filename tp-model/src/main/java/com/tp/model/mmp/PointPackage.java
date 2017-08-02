package com.tp.model.mmp;

import java.io.Serializable;
import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.common.vo.mmp.PointConstant;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 会员积分打包记录
  */
public class PointPackage extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1471421712809L;

	/**会员积分打包ID 数据类型bigint(11)*/
	@Id
	private Long pointPackageId;
	
	/**会员ID 数据类型bigint(11)*/
	private Long memberId;
	
	/**打包时间（按年打包） 数据类型int(8)*/
	private Integer packageTime;
	
	/**积分包状态  数据类型tinyint(1)*/
	private Integer packageStatus;
	
	/**积分总数 数据类型int(8)*/
	private Integer subTotalPoint;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**创建人 数据类型varchar(32)*/
	private String createUser;
	
	/**更新时间 数据类型datetime*/
	private Date updateTime;
	
	/**更新人 数据类型varchar(32)*/
	private String updateUser;
	
	@Virtual
	private PointMember pointMember;
	@Virtual
	private Integer operateType=PointConstant.OPERATE_TYPE.ADD.type;
	@Virtual
	private Integer bizType;
	/**渠道code*/
	@Virtual
	private String channelCode;
	
	public Long getPointPackageId(){
		return pointPackageId;
	}
	public Long getMemberId(){
		return memberId;
	}
	public Integer getPackageTime(){
		return packageTime;
	}
	public Integer getSubTotalPoint(){
		return subTotalPoint;
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
	public void setPointPackageId(Long pointPackageId){
		this.pointPackageId=pointPackageId;
	}
	public void setMemberId(Long memberId){
		this.memberId=memberId;
	}
	public void setPackageTime(Integer packageTime){
		this.packageTime=packageTime;
	}
	public void setSubTotalPoint(Integer subTotalPoint){
		this.subTotalPoint=subTotalPoint;
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
	public PointMember getPointMember() {
		return pointMember;
	}
	public void setPointMember(PointMember pointMember) {
		this.pointMember = pointMember;
	}
	public Integer getOperateType() {
		return operateType;
	}
	public void setOperateType(Integer operateType) {
		this.operateType = operateType;
	}
	public Integer getPackageStatus() {
		return packageStatus;
	}
	public void setPackageStatus(Integer packageStatus) {
		this.packageStatus = packageStatus;
	}
	public Integer getBizType() {
		return bizType;
	}
	public void setBizType(Integer bizType) {
		this.bizType = bizType;
	}
}
