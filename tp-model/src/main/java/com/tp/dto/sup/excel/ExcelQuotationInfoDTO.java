package com.tp.dto.sup.excel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.time.DurationFormatUtils;

import com.tp.common.annotation.Virtual;
import com.tp.common.annotation.excel.poi.ExcelEntity;
import com.tp.common.annotation.excel.poi.ExcelProperty;
import com.tp.dto.prd.excel.ExcelBaseDTO;
import com.tp.model.prd.ItemAttribute;
import com.tp.model.sup.QuotationProduct;

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
public class ExcelQuotationInfoDTO   extends ExcelBaseDTO implements Serializable {
		
	@ExcelProperty(value="*报价单索引",required=true)
	private Long quotationInfoIndex;
	
//	@ExcelProperty(value="商家名称")
//	private String supplierName;
	@ExcelProperty(value="*商家ID" ,required=true )
	private Long supplierId;
	
//	@ExcelProperty(value="合同编号" )
//	private String contractCode;	
	@ExcelProperty(value="*合同ID",required=true)
	private Long contractId;
	
	@ExcelProperty(value="报价单名称",required=true)
	private String quotationName;
	
//	@ExcelProperty(value="合同类型name",required=true)
//	private String contractTypeName;	
//	@ExcelProperty(value="*合同类型value",required=true)
//	private String contractTypeValue;
	
	@ExcelProperty(value="起始有效日期",required=true)
	private String startDate;	
	@ExcelProperty(value="终止有效日期",required=true)
	private String endDate;
	
	@ExcelProperty(value="备注")
	private String quotationDesc;
		
	@Virtual
	private List<ExcelQuotationProductDTO> quotationProductList = new ArrayList<ExcelQuotationProductDTO>();

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
	 * Getter method for property <tt>supplierName</tt>.
	 * 
	 * @return property value of supplierName
	 */
//	public String getSupplierName() {
//		return supplierName;
//	}

	/**
	 * Setter method for property <tt>supplierName</tt>.
	 * 
	 * @param supplierName value to be assigned to property supplierName
	 */
//	public void setSupplierName(String supplierName) {
//		this.supplierName = supplierName;
//	}

	/**
	 * Getter method for property <tt>supplierId</tt>.
	 * 
	 * @return property value of supplierId
	 */
	public Long getSupplierId() {
		return supplierId;
	}

	/**
	 * Setter method for property <tt>supplierId</tt>.
	 * 
	 * @param supplierId value to be assigned to property supplierId
	 */
	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	/**
	 * Getter method for property <tt>spuName</tt>.
	 * 
	 * @return property value of spuName
	 */
//	public String getContractCode() {
//		return contractCode;
//	}

	/**
	 * Setter method for property <tt>contractCode</tt>.
	 * 
	 * @param spuName value to be assigned to property contractCode
	 */
//	public void setContractCode(String contractCode) {
//		this.contractCode = contractCode;
//	}

	/**
	 * Getter method for property <tt>contractId</tt>.
	 * 
	 * @return property value of contractId
	 */
	public Long getContractId() {
		return contractId;
	}

	/**
	 * Setter method for property <tt>contractId</tt>.
	 * 
	 * @param largeName value to be assigned to property contractId
	 */
	public void setContractId(Long contractId) {
		this.contractId = contractId;
	}

	/**
	 * Getter method for property <tt>quotationName</tt>.
	 * 
	 * @return property value of largId
	 */
	public String getQuotationName() {
		return quotationName;
	}

	/**
	 * Setter method for property <tt>quotationName</tt>.
	 * 
	 * @param largId value to be assigned to property quotationName
	 */
	public void setQuotationName(String quotationName) {
		this.quotationName = quotationName;
	}

	/**
	 * Getter method for property <tt>contractTypeName</tt>.
	 * 
	 * @return property value of contractTypeName
	 */
//	public String getContractTypeName() {
//		return contractTypeName;
//	}

	/**
	 * Setter method for property <tt>contractTypeName</tt>.
	 * 
	 * @param mediumName value to be assigned to property contractTypeName
	 */
//	public void setContractTypeName(String contractTypeName) {
//		this.contractTypeName = contractTypeName;
//	}

	/**
	 * Getter method for property <tt>contractTypeValue</tt>.
	 * 
	 * @return property value of contractTypeValue
	 */
//	public String getContractTypeValue() {
//		return contractTypeValue;
//	}

	/**
	 * Setter method for property <tt>contractTypeValue</tt>.
	 * 
	 * @param mediumId value to be assigned to property contractTypeValue
	 */
//	public void setContractTypeValue(String contractTypeValue) {
//		this.contractTypeValue = contractTypeValue;
//	}

	/**
	 * Getter method for property <tt>startDate</tt>.
	 * 
	 * @return property value of startDate
	 */
	public String getStartDate() {
		return startDate;
	}

	/**
	 * Setter method for property <tt>startDate</tt>.
	 * 
	 * @param smallName value to be assigned to property startDate
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/**
	 * Getter method for property <tt>endDate</tt>.
	 * 
	 * @return property value of endDate
	 */
	public String getEndDate() {
		return endDate;
	}

	/**
	 * Setter method for property <tt>endDate</tt>.
	 * 
	 * @param smallId value to be assigned to property endDate
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/**
	 * Getter method for property <tt>quotationDesc</tt>.
	 * 
	 * @return property value of quotationDesc
	 */
	public String getQuotationDesc() {
		return quotationDesc;
	}

	/**
	 * Setter method for property <tt>quotationDesc</tt>.
	 * 
	 * @param categoryCode value to be assigned to property quotationDesc
	 */
	public void setQuotationDesc(String quotationDesc) {
		this.quotationDesc = quotationDesc;
	}
	
	
	public List<ExcelQuotationProductDTO> getQuotationProductList() {		
		return quotationProductList;
	}
	public void setQuotationProductList(List<ExcelQuotationProductDTO> quotationProductList) {
		this.quotationProductList = quotationProductList;
	}
		
}