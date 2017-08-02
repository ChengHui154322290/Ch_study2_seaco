/**
 * 
 */
package com.tp.dto.stg;

import java.io.Serializable;

import com.tp.common.vo.StorageConstant.App;

/**
 * @author szy
 *	占用（冻结）库存
 */
public class OccupyInventoryDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1666692627724815654L;
	/** 请求来自应用	必传*/
	App app;
	/** 业务id 活动id	必传*/
	String bizId;
	/** 商品sku	必传*/
	String sku;
	/** 占用库存数量	必传，大于0*/
	Integer inventory;
	/** 子订单号 */
	Long orderCode;
	/** 是否预占库存  必传*/
	boolean isPreOccupy;
	/** 仓库ID  必传*/
	Long warehouseId; 
	
	public App getApp() {
		return app;
	}
	public void setApp(App app) {
		this.app = app;
	}
	public String getBizId() {
		return bizId;
	}
	public void setBizId(String bizId) {
		this.bizId = bizId;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public Integer getInventory() {
		return inventory;
	}
	public void setInventory(Integer inventory) {
		this.inventory = inventory;
	}
	public Long getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(Long orderCode) {
		this.orderCode = orderCode;
	}
	public boolean isPreOccupy() {
		return isPreOccupy;
	}
	public void setPreOccupy(boolean isPreOccupy) {
		this.isPreOccupy = isPreOccupy;
	}
	public Long getWarehouseId() {
		return warehouseId;
	}
	public void setWarehouseId(Long warehouseId) {
		this.warehouseId = warehouseId;
	}
}
