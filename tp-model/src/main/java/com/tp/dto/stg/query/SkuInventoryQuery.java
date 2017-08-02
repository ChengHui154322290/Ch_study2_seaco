package com.tp.dto.stg.query;

import java.io.Serializable;

import com.tp.common.vo.StorageConstant.App;

/**
 * 商品库存查询
 * @author szy
 * 2015年1月16日 下午4:14:17
 *
 */
public class SkuInventoryQuery implements Serializable{
	
	private static final long serialVersionUID = -3819672166158343069L;
	
	private App app;
	/** 活动id */
	private String bizId;
	/** 活动商品 */
	private String sku;
	/** 购买数量*/
	private Integer quantity;
	/** 业务是否预占库存 */
	private boolean isBizPreOccupy;
	/** 仓库ID */
	private Long warehouseId;

	public String getAppName(){
		return app.getName();
	}
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

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public boolean isBizPreOccupy() {
		return isBizPreOccupy;
	}

	public void setBizPreOccupy(boolean isBizPreOccupy) {
		this.isBizPreOccupy = isBizPreOccupy;
	}
	public Long getWarehouseId() {
		return warehouseId;
	}
	public void setWarehouseId(Long warehouseId) {
		this.warehouseId = warehouseId;
	}
}
