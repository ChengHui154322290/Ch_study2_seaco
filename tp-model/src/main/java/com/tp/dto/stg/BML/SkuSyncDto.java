/**
 * 
 */
package com.tp.dto.stg.BML;

/**
 * @author szy
 * sku同步dto
 */
public class SkuSyncDto {
	private String skucode;
	
	private String name;
	
	private String desc;
	
	private String brand;
	
	private String ALTERNATESKU1;
	
	private String ALTERNATESKU2;

	public String getSkucode() {
		return skucode;
	}

	public void setSkucode(String skucode) {
		this.skucode = skucode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getALTERNATESKU1() {
		return ALTERNATESKU1;
	}

	public void setALTERNATESKU1(String aLTERNATESKU1) {
		ALTERNATESKU1 = aLTERNATESKU1;
	}

	public String getALTERNATESKU2() {
		return ALTERNATESKU2;
	}

	public void setALTERNATESKU2(String aLTERNATESKU2) {
		ALTERNATESKU2 = aLTERNATESKU2;
	}
	
	
}
