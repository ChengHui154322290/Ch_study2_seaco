package com.tp.dto.ord;

/**
 * 拆单DTO
 * 
 * @author szy
 * @version 0.0.1
 */
public class OrderSpliterDTO implements BaseDTO {

	private static final long serialVersionUID = -2821750219115309507L;

	/** 销售类型 */
	private Integer salesType;
	/** 供应商ID */
	private Long supplierId;
	/** 仓库ID */
	private Long warehouseId;

	public OrderSpliterDTO() {
		super();
	}

	/**
	 * 
	 * @param salesType
	 * @param supplierId
	 * @param warehouseId
	 */
	public OrderSpliterDTO(Integer salesType, Long supplierId, Long warehouseId) {
		super();
		this.salesType = salesType;
		this.supplierId = supplierId;
		this.warehouseId = warehouseId;
	}

	public Integer getSalesType() {
		return salesType;
	}

	public void setSalesType(Integer salesType) {
		this.salesType = salesType;
	}

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	public Long getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(Long warehouseId) {
		this.warehouseId = warehouseId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((salesType == null) ? 0 : salesType.hashCode());
		result = prime * result + ((supplierId == null) ? 0 : supplierId.hashCode());
		result = prime * result + ((warehouseId == null) ? 0 : warehouseId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrderSpliterDTO other = (OrderSpliterDTO) obj;
		if (salesType == null) {
			if (other.salesType != null)
				return false;
		} else if (!salesType.equals(other.salesType))
			return false;
		if (supplierId == null) {
			if (other.supplierId != null)
				return false;
		} else if (!supplierId.equals(other.supplierId))
			return false;
		if (warehouseId == null) {
			if (other.warehouseId != null)
				return false;
		} else if (!warehouseId.equals(other.warehouseId))
			return false;
		return true;
	}

}
