package com.tp.model.dss;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.common.vo.DssConstant;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 提现日志表
  */
public class WithdrawLog extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1456900594220L;

	/**提现日志编码 数据类型bigint(14)*/
	@Id
	private Long withdrawLogId;
	
	/**提现明细编号 数据类型bigint(14)*/
	private Long withdrawDetailId;
	
	/**提现明细编码 数据类型bigint(16)*/
	private Long withdrawDetailCode;
	
	/**操作类型 数据类型tinyint(2)*/
	private Integer activeType;
	
	/**操作时旧状态 数据类型tinyint(4)*/
	private Integer oldStatus;
	
	/**操作后状态 数据类型tinyint(4)*/
	private Integer currentStatus;
	
	/**备注 数据类型varchar(512)*/
	private String remark;
	
	/**创建者 数据类型varchar(32)*/
	private String createUser;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	public String getActiveTypeCn(){
		return DssConstant.WITHDRAW_LOG_TYPE.getCnName(activeType);
	}
	public String getOldStatusCn(){
		return DssConstant.WITHDRAW_STATUS.getCnName(oldStatus);
	}
	public String getCurrentStatusCn(){
		return DssConstant.WITHDRAW_STATUS.getCnName(currentStatus);
	}
	public Long getWithdrawLogId(){
		return withdrawLogId;
	}
	public Long getWithdrawDetailId(){
		return withdrawDetailId;
	}
	public Long getWithdrawDetailCode(){
		return withdrawDetailCode;
	}
	public Integer getActiveType(){
		return activeType;
	}
	public Integer getOldStatus(){
		return oldStatus;
	}
	public Integer getCurrentStatus(){
		return currentStatus;
	}
	public String getRemark(){
		return remark;
	}
	public String getCreateUser(){
		return createUser;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public void setWithdrawLogId(Long withdrawLogId){
		this.withdrawLogId=withdrawLogId;
	}
	public void setWithdrawDetailId(Long withdrawDetailId){
		this.withdrawDetailId=withdrawDetailId;
	}
	public void setWithdrawDetailCode(Long withdrawDetailCode){
		this.withdrawDetailCode=withdrawDetailCode;
	}
	public void setActiveType(Integer activeType){
		this.activeType=activeType;
	}
	public void setOldStatus(Integer oldStatus){
		this.oldStatus=oldStatus;
	}
	public void setCurrentStatus(Integer currentStatus){
		this.currentStatus=currentStatus;
	}
	public void setRemark(String remark){
		this.remark=remark;
	}
	public void setCreateUser(String createUser){
		this.createUser=createUser;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
}
