/**
 * 
 */
package com.tp.dto.stg;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author szy
 * 入库单
 */
public class InputOrderDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2690492546028331305L;

	/** 仓库id 必传*/
	private Long warehouseId;
	
	/** 仓库编号 必传 */
	private String warehouseCode;
	
	/** 采购类型  必传*/
	private String type;
	
	/** 采购单号  必传*/
	private String orderCode;
	
	/** 制单日期 必传 */
	private Date zdrq;
	
	/** 到货日期  必传*/
	private Date dhrq;
	
	/** 批次号  必传*/
	private String zdr;
	
	/** 备注 */
	private String bz;
	
	private List<InputOrderDetailDto> products;

	public Long getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(Long warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
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

	public Date getZdrq() {
		return zdrq;
	}

	public void setZdrq(Date zdrq) {
		this.zdrq = zdrq;
	}

	public Date getDhrq() {
		return dhrq;
	}

	public void setDhrq(Date dhrq) {
		this.dhrq = dhrq;
	}

	public String getZdr() {
		return zdr;
	}

	public void setZdr(String zdr) {
		this.zdr = zdr;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public List<InputOrderDetailDto> getProducts() {
		return products;
	}

	public void setProducts(List<InputOrderDetailDto> products) {
		this.products = products;
	}
	
}
