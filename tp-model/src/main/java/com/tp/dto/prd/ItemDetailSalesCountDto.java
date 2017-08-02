package com.tp.dto.prd;

import java.io.Serializable;
import java.util.Date;

import com.tp.model.BaseDO;

public class ItemDetailSalesCountDto extends BaseDO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7688089816208116029L;
	
	private Long id;
	
	/**detailId**/
	private Long detailId;
	
	/***prdid code**/
	private String prdid;

	/***默认销售数量**/
	private Long defaultSalesCount;
	
	/**实际销售数量***/
	private Long relSalesCount;
	
	/**创建用户ID**/
	private Long createUserId;
	/**修改用户Id***/
	private Long updateUserId;
	
	/**创建时间***/
	private Date createTime;
	
	/**修改时间**/
	private Date updateTime;
	
	/**商品名称***/
	private  String mainTitle;
	
	/**条形码*****/
	private String barcode;
	
	/****最后更新基数时间***/
	private Date updateDefaultCountTime;
	
	
	private Long [] exportIds;
	
	private String lastUpdateUser;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getDetailId() {
		return detailId;
	}

	public void setDetailId(Long detailId) {
		this.detailId = detailId;
	}

	public String getPrdid() {
		return prdid;
	}

	public void setPrdid(String prdid) {
		this.prdid = prdid;
	}

	public Long getDefaultSalesCount() {
		return defaultSalesCount;
	}

	public void setDefaultSalesCount(Long defaultSalesCount) {
		this.defaultSalesCount = defaultSalesCount;
	}

	public Long getRelSalesCount() {
		return relSalesCount;
	}

	public void setRelSalesCount(Long relSalesCount) {
		this.relSalesCount = relSalesCount;
	}

	public Long getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}

	public Long getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(Long updateUserId) {
		this.updateUserId = updateUserId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getMainTitle() {
		return mainTitle;
	}

	public void setMainTitle(String mainTitle) {
		this.mainTitle = mainTitle;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public Date getUpdateDefaultCountTime() {
		return updateDefaultCountTime;
	}

	public void setUpdateDefaultCountTime(Date updateDefaultCountTime) {
		this.updateDefaultCountTime = updateDefaultCountTime;
	}

	public Long[] getExportIds() {
		return exportIds;
	}

	public void setExportIds(Long[] exportIds) {
		this.exportIds = exportIds;
	}

	public String getLastUpdateUser() {
		return lastUpdateUser;
	}

	public void setLastUpdateUser(String lastUpdateUser) {
		this.lastUpdateUser = lastUpdateUser;
	}
	
	
}
