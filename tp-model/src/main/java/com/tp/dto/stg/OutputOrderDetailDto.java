package com.tp.dto.stg;

import java.io.Serializable;

import com.tp.common.vo.StorageConstant.App;

/**
 * 出库订单明细
 * @author szy
 * 2015年1月22日 下午3:20:14
 *
 */
public class OutputOrderDetailDto implements Serializable{

	private static final long serialVersionUID = 7055592680894586513L;
	/** 商品sku 必传*/
	private String sku;
	/** 商品在仓库中显示名称  必传*/
	private String itemName;
	/** app 枚举*/
	private App app;
	/** 活动id */
	private String bizId;
	
	/** 商品数量 必传 */
	private Integer itemCount;
	/** 条形码 */
	private String barcode;
	/** 商品总金额 */
	private Double itemValue;
	/** 供应商名称 */
	private String batchNo;


	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Integer getItemCount() {
		return itemCount;
	}

	public void setItemCount(Integer itemCount) {
		this.itemCount = itemCount;
	}

	public Double getItemValue() {
		return itemValue;
	}

	public void setItemValue(Double itemValue) {
		this.itemValue = itemValue;
	}


	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
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

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	
}
