/**
 * 
 */
package com.tp.dto.wms.sto;

import java.io.Serializable;
import java.util.List;

/**
 * @author Administrator
 *
 */
public class STOStocksyncInfoDto implements Serializable{
	
	private static final long serialVersionUID = -1445620770827198391L;
	/** 库存列表 */
	private List<STOStocksyncInfoItemDto> inventoryList;

	public List<STOStocksyncInfoItemDto> getInventoryList() {
		return inventoryList;
	}

	public void setInventoryList(List<STOStocksyncInfoItemDto> inventoryList) {
		this.inventoryList = inventoryList;
	}
	
}
