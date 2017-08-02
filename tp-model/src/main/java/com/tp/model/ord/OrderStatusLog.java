package com.tp.model.ord;

import java.io.Serializable;
import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.common.vo.OrderConstant.ORDER_STATUS;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 订单状态日志表
  */
public class OrderStatusLog extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451468597514L;

	/**PK 数据类型bigint(11)*/
	@Id
	private Long id;
	
	/**订单编号 数据类型varchar(20)*/
	private Long parentOrderCode;
	
	/**子订单编号 数据类型varchar(32)*/
	private Long orderCode;
	
	/**类型（1.跟踪 2.监控） 数据类型tinyint(3)*/
	private Integer type;
	
	/**日志名称 数据类型varchar(32)*/
	private String name;
	
	/**先前状态 数据类型tinyint(3)*/
	private Integer preStatus;
	
	/**当前状态 数据类型tinyint(3)*/
	private Integer currStatus;
	
	/**日志内容 数据类型varchar(1000)*/
	private String content;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**创建人ID 数据类型bigint(20)*/
	private Long createUserId;
	
	/** 数据类型varchar(32)*/
	private String createUserName;
	
	/**创建日志类型 数据类型tinyint(4)*/
	private Integer createUserType;
	
	
	public Long getId(){
		return id;
	}
	public Long getParentOrderCode(){
		return parentOrderCode;
	}
	public Long getOrderCode(){
		return orderCode;
	}
	public Integer getType(){
		return type;
	}
	public String getName(){
		return name;
	}
	public Integer getPreStatus(){
		return preStatus;
	}
	public Integer getCurrStatus(){
		return currStatus;
	}
	public String getContent(){
		return content;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Long getCreateUserId(){
		return createUserId;
	}
	public String getCreateUserName(){
		return createUserName;
	}
	public Integer getCreateUserType(){
		return createUserType;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setParentOrderCode(Long parentOrderCode){
		this.parentOrderCode=parentOrderCode;
	}
	public void setOrderCode(Long orderCode){
		this.orderCode=orderCode;
	}
	public void setType(Integer type){
		this.type=type;
	}
	public void setName(String name){
		this.name=name;
	}
	public void setPreStatus(Integer preStatus){
		this.preStatus=preStatus;
	}
	public void setCurrStatus(Integer currStatus){
		this.currStatus=currStatus;
	}
	public void setContent(String content){
		this.content=content;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setCreateUserId(Long createUserId){
		this.createUserId=createUserId;
	}
	public void setCreateUserName(String createUserName){
		this.createUserName=createUserName;
	}
	public void setCreateUserType(Integer createUserType){
		this.createUserType=createUserType;
	}
	public String getPreStatusStr() {
		return ORDER_STATUS.getCnName(getPreStatus());
	}

	public String getCurrStatusStr() {
		return ORDER_STATUS.getCnName(getCurrStatus());
	}
}
