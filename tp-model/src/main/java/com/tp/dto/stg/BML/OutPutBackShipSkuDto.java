package com.tp.dto.stg.BML;


/**
 * 
 * <pre>
 * sku信息
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 */
public class OutPutBackShipSkuDto {
    /**
     * sku code码
     */
	private String skuCode;
	/**
	 * sku的数量
	 */
	private String skuNum;

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public String getSkuNum() {
		return skuNum;
	}

	public void setSkuNum(String skuNum) {
		this.skuNum = skuNum;
	}

}
