package com.tp.model.wms;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 订单发票
  */
public class StockoutInvoice extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1464588984275L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**订单编号 数据类型varchar(30)*/
	private String orderCode;
	
	/**发票代码 数据类型varchar(50)*/
	private String invoiceCode;
	
	/**发票号码 数据类型varchar(50)*/
	private String invoiceNo;
	
	/**发票类型：1增值税发票2普通发票 数据类型tinyint(1)*/
	private Integer invoiceType;
	
	/**发票抬头 数据类型varchar(100)*/
	private String invoiceTitle;
	
	/**发票金额 数据类型varchar(20)*/
	private String invoiceAmount;
	
	/**发票内容 数据类型varchar(200)*/
	private String invoiceContent;
	
	/** 数据类型datetime*/
	private Date createTime;
	
	
	public Long getId(){
		return id;
	}
	public String getOrderCode(){
		return orderCode;
	}
	public String getInvoiceCode(){
		return invoiceCode;
	}
	public String getInvoiceNo(){
		return invoiceNo;
	}
	public Integer getInvoiceType(){
		return invoiceType;
	}
	public String getInvoiceTitle(){
		return invoiceTitle;
	}
	public String getInvoiceAmount(){
		return invoiceAmount;
	}
	public String getInvoiceContent(){
		return invoiceContent;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setOrderCode(String orderCode){
		this.orderCode=orderCode;
	}
	public void setInvoiceCode(String invoiceCode){
		this.invoiceCode=invoiceCode;
	}
	public void setInvoiceNo(String invoiceNo){
		this.invoiceNo=invoiceNo;
	}
	public void setInvoiceType(Integer invoiceType){
		this.invoiceType=invoiceType;
	}
	public void setInvoiceTitle(String invoiceTitle){
		this.invoiceTitle=invoiceTitle;
	}
	public void setInvoiceAmount(String invoiceAmount){
		this.invoiceAmount=invoiceAmount;
	}
	public void setInvoiceContent(String invoiceContent){
		this.invoiceContent=invoiceContent;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
}
