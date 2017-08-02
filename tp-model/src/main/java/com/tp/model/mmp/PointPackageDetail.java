package com.tp.model.mmp;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 积分包出入详情表
  */
public class PointPackageDetail extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1471484175762L;

	/**包详情ID 数据类型bigint(14)*/
	@Id
	private Long pointPackageDetailId;
	
	/**积分包ID 数据类型bigint(11)*/
	private Long pointPackageId;
	
	/**积分详情ID 数据类型bigint(11)*/
	private Long pointDetailId;
	
	/**积分 数据类型int(8)*/
	private Integer point;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**创建人 数据类型varchar(32)*/
	private String createUser;
	
	
	public Long getPointPackageDetailId(){
		return pointPackageDetailId;
	}
	public Long getPointPackageId(){
		return pointPackageId;
	}
	public Long getPointDetailId(){
		return pointDetailId;
	}
	public Integer getPoint(){
		return point;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public String getCreateUser(){
		return createUser;
	}
	public void setPointPackageDetailId(Long pointPackageDetailId){
		this.pointPackageDetailId=pointPackageDetailId;
	}
	public void setPointPackageId(Long pointPackageId){
		this.pointPackageId=pointPackageId;
	}
	public void setPointDetailId(Long pointDetailId){
		this.pointDetailId=pointDetailId;
	}
	public void setPoint(Integer point){
		this.point=point;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setCreateUser(String createUser){
		this.createUser=createUser;
	}
}
