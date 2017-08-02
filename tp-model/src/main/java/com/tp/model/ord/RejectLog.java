package com.tp.model.ord;

import java.io.Serializable;
import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.common.vo.ord.LogTypeConstant;
import com.tp.common.vo.ord.RejectConstant;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 退货单操作日志
  */
public class RejectLog extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451468597558L;

	/**日志ID 数据类型bigint(20)*/
	@Id
	private Long logId;
	
	/**退货单编号 数据类型bigint(20)*/
	private Long rejectCode;
	
	/**子订单编号 数据类型bigint(20)*/
	private Long orderCode;
	
	/**动作类型 数据类型tinyint(4)*/
	private Integer actionType;
	
	/**以前退货单状态 数据类型tinyint(4)*/
	private Integer oldRejectStatus;
	
	/**当前退货单状态 数据类型tinyint(4)*/
	private Integer currentRejectStatus;
	
	/** 数据类型tinyint(4)*/
	private Integer auditStatus;
	
	/**日志内容 数据类型varchar(1000)*/
	private String logContent;
	
	/**凭证图片地址 数据类型varchar(500)*/
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
	
	public String getZhActionType(){
		return RejectConstant.REJECT_LOG_ACTIVE_TYPE.getCnName(actionType);
	}
	
	public String getZhOldRejectStatus(){
		return RejectConstant.REJECT_STATUS.getCnName(oldRejectStatus);
	}
	
	public String getZhCurrentRejectStatus(){
		return RejectConstant.REJECT_STATUS.getCnName(currentRejectStatus);
	}
	
	public String getZhAuditStatus(){
		return RejectConstant.REJECT_AUDIT_STATUS.getCnName(auditStatus);
	}
	
	public String getZhOperatorType(){
		return LogTypeConstant.LOG_TYPE.getCnName(operatorType);
	}
	public Long getLogId(){
		return logId;
	}
	public Long getRejectCode(){
		return rejectCode;
	}
	public Long getOrderCode(){
		return orderCode;
	}
	public Integer getActionType(){
		return actionType;
	}
	public Integer getOldRejectStatus(){
		return oldRejectStatus;
	}
	public Integer getCurrentRejectStatus(){
		return currentRejectStatus;
	}
	public Integer getAuditStatus(){
		return auditStatus;
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
	public void setLogId(Long logId){
		this.logId=logId;
	}
	public void setRejectCode(Long rejectCode){
		this.rejectCode=rejectCode;
	}
	public void setOrderCode(Long orderCode){
		this.orderCode=orderCode;
	}
	public void setActionType(Integer actionType){
		this.actionType=actionType;
	}
	public void setOldRejectStatus(Integer oldRejectStatus){
		this.oldRejectStatus=oldRejectStatus;
	}
	public void setCurrentRejectStatus(Integer currentRejectStatus){
		this.currentRejectStatus=currentRejectStatus;
	}
	public void setAuditStatus(Integer auditStatus){
		this.auditStatus=auditStatus;
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
}
