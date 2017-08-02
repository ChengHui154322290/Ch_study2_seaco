package com.tp.model.stg;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 订单发票
  */
public class OutputOrderInvoice extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450403690114L;

	/**主键 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**订单编号 数据类型varchar(20)*/
	private String orderNo;
	
	/**发票代码 数据类型varchar(50)*/
	private String invoiceCode;
	
	/**发票号码 数据类型varchar(20)*/
	private String invoiceNo;
	
	/**发票类型： 普通发票:PT ; 增值税专用发票:ZZ 数据类型varchar(20)*/
	private String invoiceType;
	
	/**开票日期 数据类型datetime*/
	private Date invoiceTime;
	
	/**发票抬头 数据类型varchar(100)*/
	private String title;
	
	/**发票金额 数据类型double*/
	private Double amount;
	
	
	public Long getId(){
		return id;
	}
	public String getOrderNo(){
		return orderNo;
	}
	public String getInvoiceCode(){
		return invoiceCode;
	}
	public String getInvoiceNo(){
		return invoiceNo;
	}
	public String getInvoiceType(){
		return invoiceType;
	}
	public Date getInvoiceTime(){
		return invoiceTime;
	}
	public String getTitle(){
		return title;
	}
	public Double getAmount(){
		return amount;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setOrderNo(String orderNo){
		this.orderNo=orderNo;
	}
	public void setInvoiceCode(String invoiceCode){
		this.invoiceCode=invoiceCode;
	}
	public void setInvoiceNo(String invoiceNo){
		this.invoiceNo=invoiceNo;
	}
	public void setInvoiceType(String invoiceType){
		this.invoiceType=invoiceType;
	}
	public void setInvoiceTime(Date invoiceTime){
		this.invoiceTime=invoiceTime;
	}
	public void setTitle(String title){
		this.title=title;
	}
	public void setAmount(Double amount){
		this.amount=amount;
	}
}
