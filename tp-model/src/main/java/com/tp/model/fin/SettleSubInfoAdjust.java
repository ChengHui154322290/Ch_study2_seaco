package com.tp.model.fin;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 财务调整金额表
  */
public class SettleSubInfoAdjust extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450405991708L;

	/** 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/**结算子项编号 数据类型bigint(20)*/
	private Long settleSubNo;
	
	/**调整金额 数据类型double(10,2)*/
	private Double adjustAmount;
	
	/**调整前 金额 数据类型double(10,2)*/
	private Double adjustAmountBefor;
	
	/**调整后 金额 数据类型double(10,2)*/
	private Double adjustAmountAfter;
	
	/**调整人 数据类型varchar(45)*/
	private String adjustUser;
	
	/**调整说明 数据类型varchar(255)*/
	private String adjustDesc;
	
	/**状体（1：启用 0：禁用） 数据类型tinyint(1)*/
	private Integer status;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**更新时间 数据类型datetime*/
	private Date updateTime;
	
	/**创建操作者id 数据类型varchar(60)*/
	private String createOperatorId;
	
	/**更新操作者id 数据类型varchar(60)*/
	private String updateOperatorId;
	
	/**用户ip 数据类型varchar(80)*/
	private String userIp;
	
	/**服务端ip 数据类型varchar(80)*/
	private String serverIp;
	
	
	public Long getId(){
		return id;
	}
	public Long getSettleSubNo(){
		return settleSubNo;
	}
	public Double getAdjustAmount(){
		return adjustAmount;
	}
	public Double getAdjustAmountBefor(){
		return adjustAmountBefor;
	}
	public Double getAdjustAmountAfter(){
		return adjustAmountAfter;
	}
	public String getAdjustUser(){
		return adjustUser;
	}
	public String getAdjustDesc(){
		return adjustDesc;
	}
	public Integer getStatus(){
		return status;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Date getUpdateTime(){
		return updateTime;
	}
	public String getCreateOperatorId(){
		return createOperatorId;
	}
	public String getUpdateOperatorId(){
		return updateOperatorId;
	}
	public String getUserIp(){
		return userIp;
	}
	public String getServerIp(){
		return serverIp;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setSettleSubNo(Long settleSubNo){
		this.settleSubNo=settleSubNo;
	}
	public void setAdjustAmount(Double adjustAmount){
		this.adjustAmount=adjustAmount;
	}
	public void setAdjustAmountBefor(Double adjustAmountBefor){
		this.adjustAmountBefor=adjustAmountBefor;
	}
	public void setAdjustAmountAfter(Double adjustAmountAfter){
		this.adjustAmountAfter=adjustAmountAfter;
	}
	public void setAdjustUser(String adjustUser){
		this.adjustUser=adjustUser;
	}
	public void setAdjustDesc(String adjustDesc){
		this.adjustDesc=adjustDesc;
	}
	public void setStatus(Integer status){
		this.status=status;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setUpdateTime(Date updateTime){
		this.updateTime=updateTime;
	}
	public void setCreateOperatorId(String createOperatorId){
		this.createOperatorId=createOperatorId;
	}
	public void setUpdateOperatorId(String updateOperatorId){
		this.updateOperatorId=updateOperatorId;
	}
	public void setUserIp(String userIp){
		this.userIp=userIp;
	}
	public void setServerIp(String serverIp){
		this.serverIp=serverIp;
	}
}
