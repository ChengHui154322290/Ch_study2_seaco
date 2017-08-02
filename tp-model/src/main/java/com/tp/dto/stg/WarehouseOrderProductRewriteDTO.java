package com.tp.dto.stg;

import org.apache.commons.lang.builder.ToStringBuilder;
/**
 * 仓库预约商品回写dto
 * @author szy
 *
 */
public class WarehouseOrderProductRewriteDTO extends BaseDTO{
	   /**
	 * 
	 */
	private static final long serialVersionUID = -4379675167717925466L;

	/**
     * 批次号 （必填）
     */
    private String batchNumber;

    /**
     * 入(出)库数量 (必填)
     */
    private Long storageCount;

    /**
     * skuCode (必填)
     */
    private String skuCode;

    public Long getStorageCount() {
        return storageCount;
    }

    public void setStorageCount(final Long storageCount) {
        this.storageCount = storageCount;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(final String skuCode) {
        this.skuCode = skuCode;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(final String batchNumber) {
        this.batchNumber = batchNumber;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("batchNumber", this.batchNumber).append("skuCode", this.skuCode).append("storageCount", this.storageCount)
            .toString();
    }

}
