package com.tp.result.stg;

import java.io.Serializable;

/**
 * 
 * <pre>
 * 库存查询
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 */
public class StockResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -573317598833859753L;
	/**
	 * sku
	 */
	private String SKU;
	/**
	 * 仓库批号
	 */
	private String lotNumber;
	/**
	 * 库存总数
	 */
	private String qty;
	/**
	 * 库存分配数
	 */
	private String qtyAllocated;
	/**
	 * 库存冻结数
	 */
	private String qtyOnHold;


	public String getSKU() {
		return SKU;
	}

	public void setSKU(String sKU) {
		SKU = sKU;
	}
	
	public String getSku(){
		return SKU;
	}

	public String getLotNumber() {
		return lotNumber;
	}

	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
	}

	public String getQty() {
		return qty;
	}

	public void setQty(String qty) {
		this.qty = qty;
	}

	public String getQtyAllocated() {
		return qtyAllocated;
	}

	public void setQtyAllocated(String qtyAllocated) {
		this.qtyAllocated = qtyAllocated;
	}

	public String getQtyOnHold() {
		return qtyOnHold;
	}

	public void setQtyOnHold(String qtyOnHold) {
		this.qtyOnHold = qtyOnHold;
	}

}
