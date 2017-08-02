/**
 * 
 */
package com.tp.dto.stg.BML;

import java.util.List;

/**
 * @author szy
 *  入库单
 */
public class RequestPurchaseInfo {
	/** 仓库编号	 */
	private String warehouseid;
	/** 采购类型 */
	private String type;
	/** 采购单号 */
	private String orderCode;
	/** 客户id */
	private String customerId;
	/** 制单日期*/
	private String ZDRQ;
	/** 到货日期*/
	private String DHRQ;
	/** 制单人，这里用作供应商名称  供应商名称|供应商id*/
	private String ZDR;
	/** 备注 */
	private String BZ;
	/** 入库具体商品*/
	private List<ProductInfo> products;
	public String getWarehouseid() {
		return warehouseid;
	}
	public void setWarehouseid(String warehouseid) {
		this.warehouseid = warehouseid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getZDRQ() {
		return ZDRQ;
	}
	public void setZDRQ(String zDRQ) {
		ZDRQ = zDRQ;
	}
	public String getDHRQ() {
		return DHRQ;
	}
	public void setDHRQ(String dHRQ) {
		DHRQ = dHRQ;
	}
	public String getZDR() {
		return ZDR;
	}
	public void setZDR(String zDR) {
		ZDR = zDR;
	}
	public String getBZ() {
		return BZ;
	}
	public void setBZ(String bZ) {
		BZ = bZ;
	}
	public List<ProductInfo> getProducts() {
		return products;
	}
	public void setProducts(List<ProductInfo> products) {
		this.products = products;
	}
	
	
}
