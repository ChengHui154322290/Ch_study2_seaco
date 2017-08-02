package com.tp.model.mmp;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.common.vo.mmp.PointConstant;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 积分日志详情表
  */
public class PointDetail extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1471421712808L;

	/**详情日志ID 数据类型bigint(11)*/
	@Id
	private Long pointDetailId;
	
	/**详情操作标题*/
	private String title;
	
	/**会员ID 数据类型bigint(11)*/
	private Long memberId;
	
	/**积分 数据类型int(8)*/
	private Integer point;
	
	/**积分入账类型(1：入账-获取,2：出帐-使用) 数据类型tinyint(1)*/
	private Integer pointType;
	
	/**原有积分数 数据类型int(10)*/
	private Integer orgTotalPoint;
	
	/**业务类型 数据类型tinyint(2)*/
	private Integer bizType;
	
	/**业务编码 数据类型varchar(32)*/
	private String bizId;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**创建人 数据类型varchar(32)*/
	private String createUser;
	/**渠道CODE*/
	private String channelCode;
	@Virtual
	private PointPackage pointPackage;
	@Virtual
	private List<PointPackageDetail> pointPackageDetailList;
	@Virtual
	private Integer relateBizType;
	
	public String getBizTypeName(){
		return PointConstant.BIZ_TYPE.getCnName(bizType);
	}
	
	public String getPointTypeName(){
		return PointConstant.OPERATE_TYPE.getCode(pointType);
	}
	public Long getPointDetailId(){
		return pointDetailId;
	}
	public Long getMemberId(){
		return memberId;
	}

	public Integer getPoint(){
		return point;
	}
	public Integer getBizType(){
		return bizType;
	}
	public String getBizId(){
		return bizId;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public String getCreateUser(){
		return createUser;
	}
	public void setPointDetailId(Long pointDetailId){
		this.pointDetailId=pointDetailId;
	}
	public void setMemberId(Long memberId){
		this.memberId=memberId;
	}

	public void setPoint(Integer point){
		this.point=point;
	}
	public void setBizType(Integer bizType){
		this.bizType=bizType;
	}
	public void setBizId(String bizId){
		this.bizId=bizId;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setCreateUser(String createUser){
		this.createUser=createUser;
	}
	public PointPackage getPointPackage() {
		return pointPackage;
	}
	public void setPointPackage(PointPackage pointPackage) {
		this.pointPackage = pointPackage;
	}
	public List<PointPackageDetail> getPointPackageDetailList() {
		return pointPackageDetailList;
	}
	public void setPointPackageDetailList(List<PointPackageDetail> pointPackageDetailList) {
		this.pointPackageDetailList = pointPackageDetailList;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getPointType() {
		return pointType;
	}
	public void setPointType(Integer pointType) {
		this.pointType = pointType;
	}
	public Integer getOrgTotalPoint() {
		return orgTotalPoint;
	}
	public void setOrgTotalPoint(Integer orgTotalPoint) {
		this.orgTotalPoint = orgTotalPoint;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public Integer getRelateBizType() {
		return relateBizType;
	}

	public void setRelateBizType(Integer relateBizType) {
		this.relateBizType = relateBizType;
	}
	
}
