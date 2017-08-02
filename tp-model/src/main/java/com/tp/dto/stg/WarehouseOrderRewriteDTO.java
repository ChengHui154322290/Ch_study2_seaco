package com.tp.dto.stg;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
/**
 * 仓库预约单回写dto
 * @author szy
 *
 */
public class WarehouseOrderRewriteDTO extends BaseDTO{
	  /**
	 * 
	 */
	private static final long serialVersionUID = -2274113447564015843L;

	/**
     * 仓库预约单id (必填)
     */
    private Long warehouseOrderId;

    /**
     * 订单类型
     */
    private String orderType;

    /**
     * 商品明细会写信息 (必填)
     */
    private List<WarehouseOrderProductRewriteDTO> productList;

	public Long getWarehouseOrderId() {
		return warehouseOrderId;
	}

	public void setWarehouseOrderId(Long warehouseOrderId) {
		this.warehouseOrderId = warehouseOrderId;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public List<WarehouseOrderProductRewriteDTO> getProductList() {
		return productList;
	}

	public void setProductList(List<WarehouseOrderProductRewriteDTO> productList) {
		this.productList = productList;
	}
    
	  /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("warehouseOrderId", this.warehouseOrderId).append("productList", this.productList)
            .append("orderType", this.orderType).toString();
    }

}
