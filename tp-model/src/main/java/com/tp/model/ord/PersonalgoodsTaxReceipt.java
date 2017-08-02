package com.tp.model.ord;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 个人物品申报税费回执
  */
public class PersonalgoodsTaxReceipt extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1465203515899L;

	/**主键ID 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**订单编号 数据类型bigint(20)*/
	private Long orderCode;
	
	/**申报单号(海关生成的编号) 数据类型varchar(30)*/
	private String personalgoodsNo;
	
	/**运单编号 数据类型varchar(255)*/
	private String expressNo;
	
	/**税费是否应征，0免征1应征 数据类型tinyint(1)*/
	private Integer isTax;
	
	/**税额 数据类型double(10,2)*/
	private Double taxAmount;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	
	public Long getId(){
		return id;
	}
	public Long getOrderCode(){
		return orderCode;
	}
	public String getPersonalgoodsNo(){
		return personalgoodsNo;
	}
	public String getExpressNo(){
		return expressNo;
	}
	public Integer getIsTax(){
		return isTax;
	}
	public Double getTaxAmount(){
		return taxAmount;
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
	public void setPersonalgoodsNo(String personalgoodsNo){
		this.personalgoodsNo=personalgoodsNo;
	}
	public void setExpressNo(String expressNo){
		this.expressNo=expressNo;
	}
	public void setIsTax(Integer isTax){
		this.isTax=isTax;
	}
	public void setTaxAmount(Double taxAmount){
		this.taxAmount=taxAmount;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
}
