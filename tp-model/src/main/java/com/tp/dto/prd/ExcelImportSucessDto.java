package com.tp.dto.prd;

import java.io.Serializable;
/**
 * 
 * <pre>
 *  excel导入成功后返回的商品信息
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 */
public class ExcelImportSucessDto implements Serializable {
	
	private static final long serialVersionUID = -8078470261335037846L;

	/**商品spu编码*/
	private String spuCode;
	
	/**商品prdid编码*/
	private String prdidCode;
	
	/**商品sku编码*/
	private String skuCode;

	/**
	 * Getter method for property <tt>spuCode</tt>.
	 * 
	 * @return property value of spuCode
	 */
	public String getSpuCode() {
		return spuCode;
	}

	/**
	 * Setter method for property <tt>spuCode</tt>.
	 * 
	 * @param spuCode value to be assigned to property spuCode
	 */
	public void setSpuCode(String spuCode) {
		this.spuCode = spuCode;
	}

	/**
	 * Getter method for property <tt>prdidCode</tt>.
	 * 
	 * @return property value of prdidCode
	 */
	public String getPrdidCode() {
		return prdidCode;
	}

	/**
	 * Setter method for property <tt>prdidCode</tt>.
	 * 
	 * @param prdidCode value to be assigned to property prdidCode
	 */
	public void setPrdidCode(String prdidCode) {
		this.prdidCode = prdidCode;
	}

	/**
	 * Getter method for property <tt>skuCode</tt>.
	 * 
	 * @return property value of skuCode
	 */
	public String getSkuCode() {
		return skuCode;
	}

	/**
	 * Setter method for property <tt>skuCode</tt>.
	 * 
	 * @param skuCode value to be assigned to property skuCode
	 */
	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
}
