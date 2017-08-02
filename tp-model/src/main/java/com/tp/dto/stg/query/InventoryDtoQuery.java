package com.tp.dto.stg.query;

import java.io.Serializable;


/**
 * 根据sku和仓库id查询库存及可用库存信息
 * @author szy
 * 2015年1月18日 下午2:40:57
 *
 */
public class InventoryDtoQuery implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -124712979072434452L;

	private String sku;
	
	private Long warehouseId;

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public Long getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(Long warehouseId) {
		this.warehouseId = warehouseId;
	}
	
}
