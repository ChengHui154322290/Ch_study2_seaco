/**
 * 
 */
package com.tp.model.wms.jdz;

import java.io.Serializable;

/**
 * @author Administrator
 *
 */
public class JdzStockoutOrderDetail implements Serializable{
	
	private static final long serialVersionUID = -5442711747523206423L;

	/**SKU编码*/
	private String sku;
	
	/**数量*/
	private Integer qty;
	
	/**单价*/
	private Double price;
	
	/**平台商品sku*/
	private String nsku;
	
	/**实际金额*/
	private Double actualAmount;
	
	/**优惠金额*/
	private Double discountAmount;
	
	/**应付金额*/
	private Double amountPayable;
	
	/**折扣*/
	private Double discount;
	
	/**账册编号*/
	private String manualId;
	
	/**备用字段2 */
	private String remain2;
	
	/**备用字段3*/
	private String remain3;
	
	/**备用字段4*/
	private String remain4;
	
	/**备用字段5*/
	private String remain5;

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getNsku() {
		return nsku;
	}

	public void setNsku(String nsku) {
		this.nsku = nsku;
	}

	public Double getActualAmount() {
		return actualAmount;
	}

	public void setActualAmount(Double actualAmount) {
		this.actualAmount = actualAmount;
	}

	public Double getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(Double discountAmount) {
		this.discountAmount = discountAmount;
	}

	public Double getAmountPayable() {
		return amountPayable;
	}

	public void setAmountPayable(Double amountPayable) {
		this.amountPayable = amountPayable;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public String getManualId() {
		return manualId;
	}

	public void setManualId(String manualId) {
		this.manualId = manualId;
	}

	public String getRemain2() {
		return remain2;
	}

	public void setRemain2(String remain2) {
		this.remain2 = remain2;
	}

	public String getRemain3() {
		return remain3;
	}

	public void setRemain3(String remain3) {
		this.remain3 = remain3;
	}

	public String getRemain4() {
		return remain4;
	}

	public void setRemain4(String remain4) {
		this.remain4 = remain4;
	}

	public String getRemain5() {
		return remain5;
	}

	public void setRemain5(String remain5) {
		this.remain5 = remain5;
	}
}
