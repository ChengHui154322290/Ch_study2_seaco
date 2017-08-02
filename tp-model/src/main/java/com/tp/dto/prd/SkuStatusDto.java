package com.tp.dto.prd;

import java.io.Serializable;

/***
 * 
 * sku 上下架状态 dto
 * @author caihui
 *
 */
public class SkuStatusDto  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 326883460631616338L;

	/***sku信息**/
	private String sku;
	
	/***上下架状态***/
	private  Integer status;

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "SkuStatusDto [sku=" + sku + ", status=" + status + "]";
	}
	
}
