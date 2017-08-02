/**
 * 
 */
package com.tp.dto.mmp;

import java.io.Serializable;

import com.tp.dto.mmp.enums.InnerBizType;
import com.tp.dto.mmp.enums.InventoryOperType;

/**
 * @author szy
 *
 */
public class TopicInventoryExchangeDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4326552185272232669L;

	private Long topicId;

	private Long topicItemId;

	private String sku;

	private Integer amount;

	private Long supplierId;

	private Long warehouseId;

	private Integer status;

	private Long operatorId;

	private String operatorName;
	
	private InnerBizType bizType;
	
	private InventoryOperType operType;
	
	private Long topicItemChangeId;
	
	private Long topicChangeId;
	
	private Integer topicInventoryFlag; //活动是否预占库存：0否1是
	
	/**
	 * @return the topicId
	 */
	public Long getTopicId() {
		return topicId;
	}

	/**
	 * @param topicId
	 *            the topicId to set
	 */
	public void setTopicId(Long topicId) {
		this.topicId = topicId;
	}

	/**
	 * @return the sku
	 */
	public String getSku() {
		return sku;
	}

	/**
	 * @param sku
	 *            the sku to set
	 */
	public void setSku(String sku) {
		this.sku = sku;
	}

	/**
	 * @return the amount
	 */
	public Integer getAmount() {
		return amount;
	}

	/**
	 * @param amount
	 *            the amount to set
	 */
	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	/**
	 * @return the supplierId
	 */
	public Long getSupplierId() {
		return supplierId;
	}

	/**
	 * @param supplierId
	 *            the supplierId to set
	 */
	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	/**
	 * @return the warehouseId
	 */
	public Long getWarehouseId() {
		return warehouseId;
	}

	/**
	 * @param warehouseId
	 *            the warehouseId to set
	 */
	public void setWarehouseId(Long warehouseId) {
		this.warehouseId = warehouseId;
	}

	/**
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * @return the topicItemId
	 */
	public Long getTopicItemId() {
		return topicItemId;
	}

	/**
	 * @param topicItemId
	 *            the topicItemId to set
	 */
	public void setTopicItemId(Long topicItemId) {
		this.topicItemId = topicItemId;
	}

	/**
	 * @return the operatorId
	 */
	public Long getOperatorId() {
		return operatorId;
	}

	/**
	 * @param operatorId
	 *            the operatorId to set
	 */
	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}

	/**
	 * @return the operatorName
	 */
	public String getOperatorName() {
		return operatorName;
	}

	/**
	 * @param operatorName
	 *            the operatorName to set
	 */
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	/**
	 * @return the bizType
	 */
	public InnerBizType getBizType() {
		return bizType;
	}

	/**
	 * @param bizType the bizType to set
	 */
	public void setBizType(InnerBizType bizType) {
		this.bizType = bizType;
	}

	/**
	 * @return the operType
	 */
	public InventoryOperType getOperType() {
		return operType;
	}

	/**
	 * @param operType the operType to set
	 */
	public void setOperType(InventoryOperType operType) {
		this.operType = operType;
	}

	/**
	 * @return the topicItemChangeId
	 */
	public Long getTopicItemChangeId() {
		return topicItemChangeId;
	}

	/**
	 * @param topicItemChangeId the topicItemChangeId to set
	 */
	public void setTopicItemChangeId(Long topicItemChangeId) {
		this.topicItemChangeId = topicItemChangeId;
	}

	/**
	 * @return the topicChangeId
	 */
	public Long getTopicChangeId() {
		return topicChangeId;
	}

	/**
	 * @param topicChangeId the topicChangeId to set
	 */
	public void setTopicChangeId(Long topicChangeId) {
		this.topicChangeId = topicChangeId;
	}

	public Integer getTopicInventoryFlag() {
		return topicInventoryFlag;
	}

	public void setTopicInventoryFlag(Integer topicInventoryFlag) {
		this.topicInventoryFlag = topicInventoryFlag;
	}
	
	
	
}
