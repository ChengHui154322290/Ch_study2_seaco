package com.tp.model.sup;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 
  */
public class QuotationPriceLog extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1463112309480L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/** 数据类型bigint(20)*/
	private Long quotationProductId;
	
	/** 数据类型int(2)*/
	private Integer type;
	
	/** 数据类型double(11,2)*/
	private Double price;
	
	/** 数据类型varchar(255)*/
	private String createUser;
	
	/** 数据类型datetime*/
	private Date startDate;
	
	/** 数据类型datetime*/
	private Date endDate;
	
	
	public Long getId(){
		return id;
	}
	public Long getQuotationProductId(){
		return quotationProductId;
	}
	public Integer getType(){
		return type;
	}
	public Double getPrice(){
		return price;
	}
	public String getCreateUser(){
		return createUser;
	}
	public Date getStartDate(){
		return startDate;
	}
	public Date getEndDate(){
		return endDate;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setQuotationProductId(Long quotationProductId){
		this.quotationProductId=quotationProductId;
	}
	public void setType(Integer type){
		this.type=type;
	}
	public void setPrice(Double price){
		this.price=price;
	}
	public void setCreateUser(String createUser){
		this.createUser=createUser;
	}
	public void setStartDate(Date startDate){
		this.startDate=startDate;
	}
	public void setEndDate(Date endDate){
		this.endDate=endDate;
	}
}
