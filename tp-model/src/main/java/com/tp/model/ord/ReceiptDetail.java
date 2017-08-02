package com.tp.model.ord;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 发票明细表
  */
public class ReceiptDetail extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451468597514L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**发票代码 数据类型varchar(50)*/
	private String receiptCode;
	
	/**父单号 数据类型varchar(50)*/
	private Long parentOrderCode;
	
	/**发票号码 数据类型varchar(20)*/
	private String receiptNo;
	
	/**发票类型： 普通发票:PT ; 增值税专用发票:ZZ 数据类型varchar(20)*/
	private String type;
	
	/**开票日期 数据类型datetime*/
	private Date receiptTime;
	
	/**发票抬头 数据类型varchar(200)*/
	private String title;
	
	/**发票金额 数据类型double*/
	private Double amount;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	
	public Long getId(){
		return id;
	}
	public String getReceiptCode(){
		return receiptCode;
	}
	public Long getParentOrderCode(){
		return parentOrderCode;
	}
	public String getReceiptNo(){
		return receiptNo;
	}
	public String getType(){
		return type;
	}
	public Date getReceiptTime(){
		return receiptTime;
	}
	public String getTitle(){
		return title;
	}
	public Double getAmount(){
		return amount;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setReceiptCode(String receiptCode){
		this.receiptCode=receiptCode;
	}
	public void setParentOrderCode(Long parentOrderCode){
		this.parentOrderCode=parentOrderCode;
	}
	public void setReceiptNo(String receiptNo){
		this.receiptNo=receiptNo;
	}
	public void setType(String type){
		this.type=type;
	}
	public void setReceiptTime(Date receiptTime){
		this.receiptTime=receiptTime;
	}
	public void setTitle(String title){
		this.title=title;
	}
	public void setAmount(Double amount){
		this.amount=amount;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
}
