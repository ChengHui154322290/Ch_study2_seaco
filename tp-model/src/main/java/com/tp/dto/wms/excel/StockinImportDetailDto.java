package com.tp.dto.wms.excel;

import java.io.Serializable;

import com.tp.common.annotation.excel.poi.ExcelEntity;
import com.tp.common.annotation.excel.poi.ExcelProperty;
import com.tp.dto.prd.excel.ExcelBaseDTO;
import com.tp.dto.prd.excel.ExcelListDTO;

/**
 * 入库单导入类
 *
 */
@ExcelEntity
public class StockinImportDetailDto extends ExcelBaseDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4173519741781229344L;

	@ExcelProperty(value="*导入TOKEN")
	private String uploadToken;
	@ExcelProperty(value="*采购单号")
	private String purchaseCode;
	@ExcelProperty(value="*仓库编号")
	private String warehouseCode;
	@ExcelProperty(value="仓库ID")
	private String warehouseId;
	@ExcelProperty(value="*商品SKU")
	private String skuCode;
	@ExcelProperty(value="*备案料号")
	private String articleNumber;
	@ExcelProperty(value="*实际入库数量")
	private String factAmount;
	@ExcelProperty(value="计划入库数量")
	private String planAmount;
	@ExcelProperty(value="实际入库时间")
	private String stockinTime;
	@ExcelProperty(value="操作人")
	private String operator;
	@ExcelProperty(value="操作时间")
	private String operateTime;
	
	
	public String getUploadToken() {
		return uploadToken;
	}
	public void setUploadToken(String uploadToken) {
		this.uploadToken = uploadToken;
	}
	public String getPurchaseCode() {
		return purchaseCode;
	}
	public void setPurchaseCode(String purchaseCode) {
		this.purchaseCode = purchaseCode;
	}
	public String getWarehouseCode() {
		return warehouseCode;
	}
	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}
	public String getWarehouseId() {
		return warehouseId;
	}
	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}
	public String getSkuCode() {
		return skuCode;
	}
	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	public String getArticleNumber() {
		return articleNumber;
	}
	public void setArticleNumber(String articleNumber) {
		this.articleNumber = articleNumber;
	}
	public String getFactAmount() {
		return factAmount;
	}
	public void setFactAmount(String factAmount) {
		this.factAmount = factAmount;
	}
	public String getPlanAmount() {
		return planAmount;
	}
	public void setPlanAmount(String planAmount) {
		this.planAmount = planAmount;
	}
	public String getStockinTime() {
		return stockinTime;
	}
	public void setStockinTime(String stockinTime) {
		this.stockinTime = stockinTime;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getOperateTime() {
		return operateTime;
	}
	public void setOperateTime(String operateTime) {
		this.operateTime = operateTime;
	}
	
}
