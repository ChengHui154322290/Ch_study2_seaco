package com.tp.model.stg.vo.feedback;

import java.io.Serializable;
import java.util.Date;

import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.tp.util.EnhanceDateConverter;


public class DetailVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1562354743252833315L;
	/** 商品sku */
	private String SkuCode;
	/** 条形码 */
	private String barcode;

	@XStreamConverter(value = EnhanceDateConverter.class, strings = {"yyyy-MM-dd HH:mm:ss" })
	private Date ReceivedTime;

	private double ExpectedQty;

	private double ReceivedQty;

	private String Lotatt01;

	private String Lotatt02;

	private String Lotatt03;

	private String Lotatt04;

	private String Lotatt05;

	private String Lotatt06;

	/** 批次号 */
	private String Lotatt07;

	private String Lotatt08;

	private String Lotatt09;

	private String Lotatt10;

	private String Lotatt11;

	private String Lotatt12;

	public String getSkuCode() {
		return SkuCode;
	}

	public void setSkuCode(String skuCode) {
		SkuCode = skuCode;
	}

	public Date getReceivedTime() {
		return ReceivedTime;
	}

	public void setReceivedTime(Date receivedTime) {
		ReceivedTime = receivedTime;
	}

	public double getExpectedQty() {
		return ExpectedQty;
	}

	public void setExpectedQty(double expectedQty) {
		ExpectedQty = expectedQty;
	}

	public double getReceivedQty() {
		return ReceivedQty;
	}

	public void setReceivedQty(double receivedQty) {
		ReceivedQty = receivedQty;
	}

	public String getLotatt01() {
		return Lotatt01;
	}

	public void setLotatt01(String lotatt01) {
		Lotatt01 = lotatt01;
	}

	public String getLotatt02() {
		return Lotatt02;
	}

	public void setLotatt02(String lotatt02) {
		Lotatt02 = lotatt02;
	}

	public String getLotatt03() {
		return Lotatt03;
	}

	public void setLotatt03(String lotatt03) {
		Lotatt03 = lotatt03;
	}

	public String getLotatt04() {
		return Lotatt04;
	}

	public void setLotatt04(String lotatt04) {
		Lotatt04 = lotatt04;
	}

	public String getLotatt05() {
		return Lotatt05;
	}

	public void setLotatt05(String lotatt05) {
		Lotatt05 = lotatt05;
	}

	public String getLotatt06() {
		return Lotatt06;
	}

	public void setLotatt06(String lotatt06) {
		Lotatt06 = lotatt06;
	}

	public String getLotatt07() {
		return Lotatt07;
	}

	public void setLotatt07(String lotatt07) {
		Lotatt07 = lotatt07;
	}

	public String getLotatt08() {
		return Lotatt08;
	}

	public void setLotatt08(String lotatt08) {
		Lotatt08 = lotatt08;
	}

	public String getLotatt09() {
		return Lotatt09;
	}

	public void setLotatt09(String lotatt09) {
		Lotatt09 = lotatt09;
	}

	public String getLotatt10() {
		return Lotatt10;
	}

	public void setLotatt10(String lotatt10) {
		Lotatt10 = lotatt10;
	}

	public String getLotatt11() {
		return Lotatt11;
	}

	public void setLotatt11(String lotatt11) {
		Lotatt11 = lotatt11;
	}

	public String getLotatt12() {
		return Lotatt12;
	}

	public void setLotatt12(String lotatt12) {
		Lotatt12 = lotatt12;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

}
