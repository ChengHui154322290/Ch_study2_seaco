package com.tp.model.ord;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 取消单操作日志
  */
public class CancelLog extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451468597509L;

	/**日志ID 数据类型bigint(20)*/
	@Id
	private Long logId;
	
	/**取消单ID 数据类型bigint(20)*/
	private Long cancelId;
	
	/**取消单代码 数据类型bigint(20)*/
	private Long cancelCode;
	
	/**子订单编号 数据类型bigint(20)*/
	private Long orderCode;
	
	/**动作类型 数据类型tinyint(4)*/
	private Integer actionType;
	
	/**以前取消单状态 数据类型tinyint(4)*/
	private Integer oldCancelStatus;
	
	/**当前取消单状态 数据类型tinyint(4)*/
	private Integer currentCancelStatus;
	
	/**日志内容 数据类型varchar(1000)*/
	private String logContent;
	
	/**凭证图片地址 数据类型varchar(200)*/
	private String imgUrls;
	
	/**操作者账号 数据类型varchar(64)*/
	private String operatorName;
	
	/**操作者用户ID 数据类型varchar(32)*/
	private String operatorUserId;
	
	/**操作者类型（0-用户,1-客服,2-商家,3-供应商,4-系统） 数据类型tinyint(4)*/
	private Integer operatorType;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**创建人 数据类型varchar(32)*/
	private String createUser;
	
	/**更新时间 数据类型datetime*/
	private Date updateTime;
	
	/**更新人 数据类型varchar(32)*/
	private String updateUser;
	
	
	public Long getLogId(){
		return logId;
	}
	public Long getCancelId(){
		return cancelId;
	}
	public Long getCancelCode(){
		return cancelCode;
	}
	public Long getOrderCode(){
		return orderCode;
	}
	public Integer getActionType(){
		return actionType;
	}
	public Integer getOldCancelStatus(){
		return oldCancelStatus;
	}
	public Integer getCurrentCancelStatus(){
		return currentCancelStatus;
	}
	public String getLogContent(){
		return logContent;
	}
	public String getImgUrls(){
		return imgUrls;
	}
	public String getOperatorName(){
		return operatorName;
	}
	public String getOperatorUserId(){
		return operatorUserId;
	}
	public Integer getOperatorType(){
		return operatorType;
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
	public void setLogId(Long logId){
		this.logId=logId;
	}
	public void setCancelId(Long cancelId){
		this.cancelId=cancelId;
	}
	public void setCancelCode(Long cancelCode){
		this.cancelCode=cancelCode;
	}
	public void setOrderCode(Long orderCode){
		this.orderCode=orderCode;
	}
	public void setActionType(Integer actionType){
		this.actionType=actionType;
	}
	public void setOldCancelStatus(Integer oldCancelStatus){
		this.oldCancelStatus=oldCancelStatus;
	}
	public void setCurrentCancelStatus(Integer currentCancelStatus){
		this.currentCancelStatus=currentCancelStatus;
	}
	public void setLogContent(String logContent){
		this.logContent=logContent;
	}
	public void setImgUrls(String imgUrls){
		this.imgUrls=imgUrls;
	}
	public void setOperatorName(String operatorName){
		this.operatorName=operatorName;
	}
	public void setOperatorUserId(String operatorUserId){
		this.operatorUserId=operatorUserId;
	}
	public void setOperatorType(Integer operatorType){
		this.operatorType=operatorType;
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
}
