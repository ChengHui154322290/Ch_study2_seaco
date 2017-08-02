package com.tp.model.ord;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 订单相关申报海关全日志
  */
public class CustomsClearanceLog extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1467967774185L;

	/**主键ID 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**订单编号(子单) 数据类型bigint(20)*/
	private Long orderCode;
	
	/**海关编号 数据类型varchar(30)*/
	private String customsCode;
	
	/**类型1订单申报2运单申报3个人物品申报4清关状态 数据类型tinyint(1)*/
	private Integer type;
	
	/**结果0失败1成功 数据类型tinyint(1)*/
	private Integer result;
	
	/**结果简述 数据类型varchar(500)*/
	private String resultDesc;
	
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
	public Integer getType(){
		return type;
	}
	public Integer getResult(){
		return result;
	}
	public String getResultDesc(){
		return resultDesc;
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
	public void setType(Integer type){
		this.type=type;
	}
	public void setResult(Integer result){
		this.result=result;
	}
	public void setResultDesc(String resultDesc){
		this.resultDesc=resultDesc;
	}
	public void setRemark(String remark){
		this.remark=remark;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
}
