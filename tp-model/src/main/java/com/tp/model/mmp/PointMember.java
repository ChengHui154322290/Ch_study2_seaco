package com.tp.model.mmp;

import java.io.Serializable;
import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.common.vo.mmp.PointConstant;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 会员积分记录表
  */
public class PointMember extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1471421712809L;

	/**会员积分记录ID 数据类型bigint(11)*/
	@Id
	private Long pointMemId;
	
	/**会员ID 数据类型bigint(11)*/
	private Long memberId;
	
	/**累计积分 数据类型bigint(14)*/
	private Long accumulatePoint;
	
	/**会员总积分 数据类型int(11)*/
	private Integer totalPoint;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**创建人 数据类型varchar(32)*/
	private String createUser;
	
	/**更新时间 数据类型datetime*/
	private Date updateTime;
	
	/**更新人 数据类型varchar(32)*/
	private String updateUser;
	
	/**入账类型 +-*/
	@Virtual
	private Integer operateType=PointConstant.OPERATE_TYPE.ADD.type;
	
	public Long getPointMemId(){
		return pointMemId;
	}
	public Long getMemberId(){
		return memberId;
	}
	public Long getAccumulatePoint(){
		return accumulatePoint;
	}
	public Integer getTotalPoint(){
		return totalPoint;
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
	public void setPointMemId(Long pointMemId){
		this.pointMemId=pointMemId;
	}
	public void setMemberId(Long memberId){
		this.memberId=memberId;
	}
	public void setAccumulatePoint(Long accumulatePoint){
		this.accumulatePoint=accumulatePoint;
	}
	public void setTotalPoint(Integer totalPoint){
		this.totalPoint=totalPoint;
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
	public Integer getOperateType() {
		return operateType;
	}
	public void setOperateType(Integer operateType) {
		this.operateType = operateType;
	}
}
