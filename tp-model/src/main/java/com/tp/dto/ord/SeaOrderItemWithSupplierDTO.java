/**
 * NewHeight.com Inc.
 * Copyright (c) 2007-2014 All Rights Reserved.
 */
package com.tp.dto.ord;

import java.util.List;

/**
 * <pre>
 * 海淘订单商品，根据供应商拆分
 * </pre>
 * 
 * @author szy
 * @time 2015-3-20 下午4:55:59
 */
public class SeaOrderItemWithSupplierDTO implements BaseDTO {
	private static final long serialVersionUID = -3708310338945368416L;
	/** 供应商ID */
	private Long supplierId;

	/** 供应商运费模版ID */
	private Long freightTempleteId;

	/** 供应商名称 */
	private String supplierName;

	/** 海淘订单商品，根据仓库拆分 */
	private List<SeaOrderItemWithWarehouseDTO> seaOrderItemWithWarehouseList;

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	public Long getFreightTempleteId() {
		return freightTempleteId;
	}

	public void setFreightTempleteId(Long freightTempleteId) {
		this.freightTempleteId = freightTempleteId;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public List<SeaOrderItemWithWarehouseDTO> getSeaOrderItemWithWarehouseList() {
		return seaOrderItemWithWarehouseList;
	}

	public void setSeaOrderItemWithWarehouseList(List<SeaOrderItemWithWarehouseDTO> seaOrderItemWithWarehouseList) {
		this.seaOrderItemWithWarehouseList = seaOrderItemWithWarehouseList;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
