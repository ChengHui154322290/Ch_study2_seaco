package com.tp.model.ord;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 取消海淘单海关申报回执流水日志
  */
public class CancelCustomsReceiptLog extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1466047015330L;

	/**主键ID 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**订单编号(子单) 数据类型bigint(20)*/
	private Long orderCode;
	
	/**海关编号 数据类型varchar(30)*/
	private String customsCode;
	
	/**审批结果（21撤销成功22撤销失败） 数据类型int(1)*/
	private Integer approveResult;
	
	/**海关审批意见 数据类型varchar(512)*/
	private String approveComment;
	
	/**回执原始数据 数据类型varchar(2048)*/
	private String resMsg;
	
	/**备注 数据类型varchar(255)*/
	private String remark;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	
	public Long getId(){
		return id;
	}
	public Long getOrderCode(){
		return orderCode;
	}
	public String getCustomsCode(){
		return customsCode;
	}
	public Integer getApproveResult(){
		return approveResult;
	}
	public String getApproveComment(){
		return approveComment;
	}
	public String getResMsg(){
		return resMsg;
	}
	public String getRemark(){
		return remark;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setOrderCode(Long orderCode){
		this.orderCode=orderCode;
	}
	public void setCustomsCode(String customsCode){
		this.customsCode=customsCode;
	}
	public void setApproveResult(Integer approveResult){
		this.approveResult=approveResult;
	}
	public void setApproveComment(String approveComment){
		this.approveComment=approveComment;
	}
	public void setResMsg(String resMsg){
		this.resMsg=resMsg;
	}
	public void setRemark(String remark){
		this.remark=remark;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
}
