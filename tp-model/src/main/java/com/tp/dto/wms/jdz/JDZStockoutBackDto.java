/**
 * 
 */
package com.tp.dto.wms.jdz;

import java.io.Serializable;
import java.util.List;

/**
 * @author Administrator
 * 公共仓出库单反馈
 */
public class JDZStockoutBackDto implements Serializable{

	private static final long serialVersionUID = -2266863845889579766L;
	
	/** 订单号 **/
	private String orderCode;
	
	/** 仓库编号 */
	private String warehouseCode; //WMSCode
	
	/** 物流编号 **/
	private String logisticsCompanyCode;
	
	/** 物流名称 */
	private String logisticsCompanyName;
	
	/** 快递编号 */
	private String expressNo;
	
	/** 审核时间 */
	private String auditTime;
	
	/** 审核人 */
	private String auditor;
	
	/** 包裹重量 */
	private String weight;
	
	/** 厂商编码 */
	private String providerCode;
	
	/** 账册编号 */
	private String goodsOwner;
	
	/** 商品详情 */
	List<JDZStockoutBackItemDto> orderOutDetails;

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	public String getLogisticsCompanyCode() {
		return logisticsCompanyCode;
	}

	public void setLogisticsCompanyCode(String logisticsCompanyCode) {
		this.logisticsCompanyCode = logisticsCompanyCode;
	}

	public String getLogisticsCompanyName() {
		return logisticsCompanyName;
	}

	public void setLogisticsCompanyName(String logisticsCompanyName) {
		this.logisticsCompanyName = logisticsCompanyName;
	}

	public String getExpressNo() {
		return expressNo;
	}

	public void setExpressNo(String expressNo) {
		this.expressNo = expressNo;
	}

	public String getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(String auditTime) {
		this.auditTime = auditTime;
	}

	public String getAuditor() {
		return auditor;
	}

	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getProviderCode() {
		return providerCode;
	}

	public void setProviderCode(String providerCode) {
		this.providerCode = providerCode;
	}

	public String getGoodsOwner() {
		return goodsOwner;
	}

	public void setGoodsOwner(String goodsOwner) {
		this.goodsOwner = goodsOwner;
	}

	public List<JDZStockoutBackItemDto> getOrderOutDetails() {
		return orderOutDetails;
	}

	public void setOrderOutDetails(List<JDZStockoutBackItemDto> orderOutDetails) {
		this.orderOutDetails = orderOutDetails;
	}
}
