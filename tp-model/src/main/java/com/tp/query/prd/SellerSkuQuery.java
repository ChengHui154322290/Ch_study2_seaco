package com.tp.query.prd;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.tp.model.BaseDO;

public class SellerSkuQuery extends BaseDO implements Serializable {

	private static final long serialVersionUID = 4119940815672063815L;

	/** 商品主标题 */
	private String detailName;

	/** 单位 */
	private String unitName;

	/** 品牌名称 */
	private String brandName;

	/** 供应商名称 */
	private String supplierName;

	/** 供应商Id List */
	private List<Long> supplierIds;

	/** 创建起始时间 */
	private Date createBeginTime;
	/** 创建查询截止时间 */
	private Date createEndTime;


	public String getDetailName() {
		return detailName;
	}

	public void setDetailName(String detailName) {
		this.detailName = detailName;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public List<Long> getSupplierIds() {
		return supplierIds;
	}

	public void setSupplierIds(List<Long> supplierIds) {
		this.supplierIds = supplierIds;
	}

	public Date getCreateBeginTime() {
		return createBeginTime;
	}

	public void setCreateBeginTime(Date createBeginTime) {
		this.createBeginTime = createBeginTime;
	}

	public Date getCreateEndTime() {
		return createEndTime;
	}

	public void setCreateEndTime(Date createEndTime) {
		this.createEndTime = createEndTime;
	}

}
