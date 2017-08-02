package com.tp.dto.sup.excel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.model.sup.QuotationProduct;

/**
 * excel导出报价单dto
 * 
 * @author zhs
 * @version 0.0.1
 */
public class ExcelQuotationExportDTO implements Serializable {

	
	/**报价单编号 数据类型varchar(100)*/
	private String quotationCode;
	
	/**报价单名称 数据类型varchar(80)*/
	private String quotationName;
		
	/**供应商名称 数据类型varchar(80)*/
	private String supplierName;
		
	/**合同编号 数据类型varchar(80)*/
	private String contractCode;
		
	/**合同类型 数据类型varchar(45)*/
	private String contractType;
		
	/**有效期-开始日期 数据类型String*/
	private String startDate;
	
	/**有效期-结束日期 数据类型String*/
	private String endDate;
		
	/**商品编号 数据类型varchar(100)*/
	private String productCode;
	
	/**商品名称 数据类型varchar(255)*/
	private String productName;
		
	/**品牌名称 数据类型varchar(80)*/
	private String brandName;
		
	/**大类名称 数据类型varchar(80)*/
	private String bigName;
		
	/**中类名称 数据类型varchar(80)*/
	private String midName;
		
	/**小类名称 数据类型varchar(80)*/
	private String smallName;
			
	/** 数据类型varchar(100)*/
	private String sku;
	
	/**条形码 数据类型varchar(100)*/
	private String barCode;
		
	/**商品单位 数据类型varchar(45)*/
	private String productUnit;
	
	/**标准价 数据类型double*/
	private Double standardPrice;
	
	/**销售价格 数据类型double*/
	private Double salePrice;
	
	/**供货价格 数据类型double*/
	private Double supplyPrice;
	
	/**佣金比例 数据类型double*/
	private Double commissionPercent;
	
	/**箱规 数据类型varchar(255)*/
	private String boxProp;
	
	/**商品规格 数据类型varchar(255)*/
	private String productProp;

	/**
	 * 商品裸价
	 */
	private Double basePrice;
	/**
	 * 运费
	 */
	private Double freight;
	/**
	 * 跨境综合税率
	 */
	private Double mulTaxRate;
	/**
	 * 行邮税税率
	 */
	private Double tarrifTaxRate;
	/**
	 * 包邮包税代发价
	 */
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

	public String getProductCode(){
		return productCode;
	}
	public String getBrandName(){
		return brandName;
	}
	public String getBigName(){
		return bigName;
	}
	public String getMidName(){
		return midName;
	}
	public String getSmallName(){
		return smallName;
	}
	public String getSku(){
		return sku;
	}
	public String getBarCode(){
		return barCode;
	}
	public String getProductName(){
		return productName;
	}
	public String getProductUnit(){
		return productUnit;
	}
	public Double getStandardPrice(){
		return standardPrice;
	}
	public Double getSalePrice(){
		return salePrice;
	}
	public Double getSupplyPrice(){
		return supplyPrice;
	}
	public Double getCommissionPercent(){
		return commissionPercent;
	}
	public String getBoxProp(){
		return boxProp;
	}
	public String getProductProp(){
		return productProp;
	}
	public void setProductCode(String productCode){
		this.productCode=productCode;
	}
	public void setBrandName(String brandName){
		this.brandName=brandName;
	}
	public void setBigName(String bigName){
		this.bigName=bigName;
	}
	public void setMidName(String midName){
		this.midName=midName;
	}
	public void setSmallName(String smallName){
		this.smallName=smallName;
	}
	public void setSku(String sku){
		this.sku=sku;
	}
	public void setBarCode(String barCode){
		this.barCode=barCode;
	}
	public void setProductName(String productName){
		this.productName=productName;
	}
	public void setProductUnit(String productUnit){
		this.productUnit=productUnit;
	}
	public void setStandardPrice(Double standardPrice){
		this.standardPrice=standardPrice;
	}
	public void setSalePrice(Double salePrice){
		this.salePrice=salePrice;
	}
	public void setSupplyPrice(Double supplyPrice){
		this.supplyPrice=supplyPrice;
	}
	public void setCommissionPercent(Double commissionPercent){
		this.commissionPercent=commissionPercent;
	}
	public void setBoxProp(String boxProp){
		this.boxProp=boxProp;
	}
	public void setProductProp(String productProp){
		this.productProp=productProp;
	}	
	public String getQuotationCode(){
		return quotationCode;
	}
	public String getQuotationName(){
		return quotationName;
	}
	public String getSupplierName(){
		return supplierName;
	}
	public String getContractCode(){
		return contractCode;
	}
	public String getContractType(){
		return contractType;
	}
	public String getStartDate(){
		return startDate;
	}
	public String getEndDate(){
		return endDate;
	}
	public void setQuotationCode(String quotationCode){
		this.quotationCode=quotationCode;
	}
	public void setQuotationName(String quotationName){
		this.quotationName=quotationName;
	}
	public void setSupplierName(String supplierName){
		this.supplierName=supplierName;
	}
	public void setContractCode(String contractCode){
		this.contractCode=contractCode;
	}
	public void setContractType(String contractType){
		this.contractType=contractType;
	}
	public void setStartDate(String startDate){
		this.startDate=startDate;
	}
	public void setEndDate(String endDate){
		this.endDate=endDate;
	}
	
}
