package com.tp.dto.sup.excel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.time.DurationFormatUtils;

import com.tp.common.annotation.excel.poi.ExcelEntity;
import com.tp.common.annotation.excel.poi.ExcelProperty;
import com.tp.dto.prd.excel.ExcelBaseDTO;
import com.tp.model.prd.ItemAttribute;

/**
 * 
 * <pre>
 * 	 excel模板对应的实体类
 * </pre>
 *
 * @author zhs
 * @version 0.0.1
 */
@ExcelEntity
public class ExcelQuotationProductDTO   extends ExcelBaseDTO  implements Serializable  {
	
	@ExcelProperty(value="*报价单索引",required=true)
	private Long quotationInfoIndex;
		
	@ExcelProperty(value="商品sku",required=true)
	private String prdSku;	
//	@ExcelProperty(value="商品barcode",required=true)
//	private String prdBarcode;
	
	//@ExcelProperty(value="建议最低售价",required=true)
	private Double salePrice;

	//@ExcelProperty(value="供货价",required=true)
	private Double supplyPrice;
	
	@ExcelProperty(value="平台使用费%",required=true)
	private Double commissionPercent;

	/**
	 * 商品裸价
	 */
	@ExcelProperty(value="裸价",required=true)
	private Double basePrice;
	/**
	 * 运费
	 */
	@ExcelProperty(value="运费",required=true)
	private Double freight;
	/**
	 * 跨境综合税率
	 */
	@ExcelProperty(value="跨境综合税率",required=true)
	private Double mulTaxRate;
	/**
	 * 行邮税税率
	 */
	@ExcelProperty(value="行邮税税率",required=true)
	private Double tarrifTaxRate;
	/**
	 * 包邮包税代发价
	 */
	@ExcelProperty(value="包邮包税代发价",required=true)
	private Double sumPrice;

	public Double getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(Double basePrice) {
		this.basePrice = basePrice;
	}

	public Double getFreight() {
		return freight;
	}

	public void setFreight(Double freight) {
		this.freight = freight;
	}

	public Double getMulTaxRate() {
		return mulTaxRate;
	}

	public void setMulTaxRate(Double mulTaxRate) {
		this.mulTaxRate = mulTaxRate;
	}

	public Double getTarrifTaxRate() {
		return tarrifTaxRate;
	}

	public void setTarrifTaxRate(Double tarrifTaxRate) {
		this.tarrifTaxRate = tarrifTaxRate;
	}

	public Double getSumPrice() {
		return sumPrice;
	}

	public void setSumPrice(Double sumPrice) {
		this.sumPrice = sumPrice;
	}

	/**
	 * Getter method for property <tt>quotationInfoIndex</tt>.
	 * 
	 * @return property value of supplierName
	 */
	public Long getQuotationInfoIndex() {
		return quotationInfoIndex;
	}

	/**
	 * Setter method for property <tt>quotationInfoIndex</tt>.
	 * 
	 * @param supplierName value to be assigned to property supplierName
	 */
	public void setQuotationInfoIndex(Long quotationInfoIndex) {
		this.quotationInfoIndex = quotationInfoIndex;
	}

	/**
	 * Getter method for property <tt>prdSku</tt>.
	 * 
	 * @return property value of prdSku
	 */
	public String getPrdSku() {
		return prdSku;
	}

	/**
	 * Setter method for property <tt>prdSku</tt>.
	 * 
	 * @param unitId value to be assigned to property prdSku
	 */
	public void setPrdSku(String prdSku) {
		this.prdSku = prdSku;
	}

	/**
	 * Getter method for property <tt>prdBarcode</tt>.
	 * 
	 * @return property value of prdBarcode
	 */
//	public String getPrdBarcode() {
//		return prdBarcode;
//	}

	/**
	 * Setter method for property <tt>prdBarcode</tt>.
	 * 
	 * @param brandName value to be assigned to property prdBarcode
	 */
//	public void setPrdBarcode(String prdBarcode) {
//		this.prdBarcode = prdBarcode;
//	}

	/**
	 * Getter method for property <tt>salePrice</tt>.
	 * 
	 * @return property value of salePrice
	 */
	public Double getSalePrice() {
		return salePrice;
	}

	/**
	 * Setter method for property <tt>salePrice</tt>.
	 * 
	 * @param brandId value to be assigned to property salePrice
	 */
	public void setSalePrice(Double salePrice) {
		this.salePrice = salePrice;
	}

	/**
	 * Getter method for property <tt>supplyPrice</tt>.
	 * 
	 * @return property value of supplyPrice
	 */
	public Double getSupplyPrice() {
		return supplyPrice;
	}

	/**
	 * Setter method for property <tt>supplyPrice</tt>.
	 * 
	 * @param spuRemark value to be assigned to property supplyPrice
	 */
	public void setSupplyPrice(Double supplyPrice) {
		this.supplyPrice = supplyPrice;
	}

	/**
	 * Getter method for property <tt>commissionPercent</tt>.
	 * 
	 * @return property value of commissionPercent
	 */
	public Double getCommissionPercent() {
		return commissionPercent;
	}

	/**
	 * Setter method for property <tt>commissionPercent</tt>.
	 * 
	 * @param skuName value to be assigned to property commissionPercent
	 */
	public void setCommissionPercent(Double commissionPercent) {
		this.commissionPercent = commissionPercent;
	}	
	
}