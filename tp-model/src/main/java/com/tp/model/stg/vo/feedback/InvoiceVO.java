package com.tp.model.stg.vo.feedback;

import java.io.Serializable;
import java.util.Date;

import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.tp.util.EnhanceDateConverter;

/**
 * 
 * @author szy
 * 解析发票反馈信息
 *
 */
public class InvoiceVO  implements Serializable{
	
	private static final long serialVersionUID = 2663144514839158455L;

	/** 订单编号 */
	private String orderNo;
	
	/** 发票代码 */
	private String invoiceCode;

	/** 发票号码 */
	private String invoiceNo;

	/** 发票类型： 普通发票:PT ; 增值税专用发票:ZZ */
	private String invoiceType;

	/** 开票日期 */
	@XStreamConverter(value = EnhanceDateConverter.class, strings = {"yyyy-MM-dd HH:mm:ss" })
	private Date invoiceTime;

	/** 发票抬头 */
	private String title;

	/** 发票金额 */
	private Double amount;

	/**
	 * 设置 订单编号
	 * 
	 * @param orderNo
	 */
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	/**
	 * 设置 发票编号
	 * 
	 * @param invoiceNo
	 */
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	/**
	 * 设置 发票类型： 普通发票:PT ; 增值税专用发票:ZZ
	 * 
	 * @param invoiceType
	 */
	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

	/**
	 * 设置 开票日期
	 * 
	 * @param invoiceTime
	 */
	public void setInvoiceTime(Date invoiceTime) {
		this.invoiceTime = invoiceTime;
	}

	/**
	 * 设置 发票抬头
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 设置 发票金额
	 * 
	 * @param amount
	 */
	public void setAmount(Double amount) {
		this.amount = amount;
	}

	/**
	 * 获取 订单编号
	 * 
	 * @return orderNo
	 */
	public String getOrderNo() {
		return orderNo;
	}

	/**
	 * 获取 发票编号
	 * 
	 * @return invoiceNo
	 */
	public String getInvoiceNo() {
		return invoiceNo;
	}

	/**
	 * 获取 发票类型： 普通发票:PT ; 增值税专用发票:ZZ
	 * 
	 * @return invoiceType
	 */
	public String getInvoiceType() {
		return invoiceType;
	}

	/**
	 * 获取 开票日期
	 * 
	 * @return invoiceTime
	 */
	public Date getInvoiceTime() {
		return invoiceTime;
	}

	/**
	 * 获取 发票抬头
	 * 
	 * @return title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * 获取 发票金额
	 * 
	 * @return amount
	 */
	public Double getAmount() {
		return amount;
	}

	public String getInvoiceCode() {
		return invoiceCode;
	}

	public void setInvoiceCode(String invoiceCode) {
		this.invoiceCode = invoiceCode;
	}

	@Override
	public String toString() {
		return "InvoiceVO ["
				+ (orderNo != null ? "orderNo=" + orderNo + ", " : "")
				+ (invoiceCode != null ? "invoiceCode=" + invoiceCode + ", "
						: "")
				+ (invoiceNo != null ? "invoiceNo=" + invoiceNo + ", " : "")
				+ (invoiceType != null ? "invoiceType=" + invoiceType + ", "
						: "")
				+ (invoiceTime != null ? "invoiceTime=" + invoiceTime + ", "
						: "") + (title != null ? "title=" + title + ", " : "")
				+ (amount != null ? "amount=" + amount : "") + "]";
	}
	
}
