/**
 * 
 */
package com.tp.dto.wms;

import java.io.Serializable;

/**
 * @author Administrator
 * 
 *  发货单对应的发货信息
 */
public class StockoutInvoiceDto implements Serializable{
	
	private static final long serialVersionUID = -1744728335347212853L;

	/** 发票ID **/
	private String invoiceId;
	
	/** 发票号码 **/
	private String invoiceNo;
	
	/** 发票代码 **/
	private String invoiceCode;
	
	/** 发票类型:1增值税发票2普通发票 **/
	private Integer invoiceType;
	
	/** 发票抬头 **/
	private String invoiceTitle;
	
	/** 发票金额 **/
	private String invoiceAmount;
	
	/** 发票内容 **/
	private String invoiceContent;

	public String getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}

	public Integer getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(Integer invoiceType) {
		this.invoiceType = invoiceType;
	}

	public String getInvoiceTitle() {
		return invoiceTitle;
	}

	public void setInvoiceTitle(String invoiceTitle) {
		this.invoiceTitle = invoiceTitle;
	}

	public String getInvoiceAmount() {
		return invoiceAmount;
	}

	public void setInvoiceAmount(String invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	public String getInvoiceContent() {
		return invoiceContent;
	}

	public void setInvoiceContent(String invoiceContent) {
		this.invoiceContent = invoiceContent;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getInvoiceCode() {
		return invoiceCode;
	}

	public void setInvoiceCode(String invoiceCode) {
		this.invoiceCode = invoiceCode;
	}
	
}
