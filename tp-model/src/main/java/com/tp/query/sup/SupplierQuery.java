package com.tp.query.sup;

import com.tp.model.BaseDO;

/**
 * 通关渠道查询条件
 * 
 * @author szy
 *
 */
public class SupplierQuery extends BaseDO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2288311506282260377L;

	/**供应商id*/
	private Long supplierId;
	
	/**通关渠道id*/
	private Long customsChannelId;

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	public Long getCustomsChannelId() {
		return customsChannelId;
	}

	public void setCustomsChannelId(Long customsChannelId) {
		this.customsChannelId = customsChannelId;
	}

}
