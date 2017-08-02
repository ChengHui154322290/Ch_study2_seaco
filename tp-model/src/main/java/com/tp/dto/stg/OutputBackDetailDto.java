package com.tp.dto.stg;

import java.io.Serializable;

public class OutputBackDetailDto implements Serializable{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -4787398241627273434L;

	/** sku编号 */
	private String sku;

	/** 出库数量 */
	private Integer num;

	/**  */
	private String lotatt01;

	/**  */
	private String lotatt02;

	/**  */
	private String lotatt03;

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public String getLotatt01() {
		return lotatt01;
	}

	public void setLotatt01(String lotatt01) {
		this.lotatt01 = lotatt01;
	}

	public String getLotatt02() {
		return lotatt02;
	}

	public void setLotatt02(String lotatt02) {
		this.lotatt02 = lotatt02;
	}

	public String getLotatt03() {
		return lotatt03;
	}

	public void setLotatt03(String lotatt03) {
		this.lotatt03 = lotatt03;
	}
	
}
