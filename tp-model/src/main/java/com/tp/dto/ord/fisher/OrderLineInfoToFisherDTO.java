/**
 * 
 */
package com.tp.dto.ord.fisher;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author szy
 *
 */
public class OrderLineInfoToFisherDTO implements Serializable  {

	private static final long serialVersionUID = -4092728591854831872L;

	/** 商品编码 */
	private String commodityCode ;
	
	/** 商品名称 */
	private String commodityName ;

	/** 商品条形码 */
	private String commodityBarcode ;
	
	/** 商品规格 */
	private String commoditySpec ;
	
	/** 是否套装的标识 */
	private String isSet ;
	
	/** 商品数量 */
	private Integer qty ;
	
	/** 商品重量 */
	private BigDecimal weight ;
	
	/** 买家留言 */
	private String buyerMessage ;
	
	/** 客服备注 */
	private String remark ;
	
	/**成交单价 */
	private BigDecimal tradePrice ;
	
	/**成交总价 */
	private BigDecimal tradeTotal ;
	
	/**申报单价 */
	private BigDecimal declPrice ;
	
	/**申报总价 */
	private BigDecimal declTotalPrice ;
	
	/** 行邮税号 */
	private String codeTs;

	public String getCommodityCode() {
		return commodityCode;
	}

	public void setCommodityCode(String commodityCode) {
		this.commodityCode = commodityCode;
	}

	public String getCommodityName() {
		return commodityName;
	}

	public void setCommodityName(String commodityName) {
		this.commodityName = commodityName;
	}

	public String getCommodityBarcode() {
		return commodityBarcode;
	}

	public void setCommodityBarcode(String commodityBarcode) {
		this.commodityBarcode = commodityBarcode;
	}

	public String getCommoditySpec() {
		return commoditySpec;
	}

	public void setCommoditySpec(String commoditySpec) {
		this.commoditySpec = commoditySpec;
	}

	public String getIsSet() {
		return isSet;
	}

	public void setIsSet(String isSet) {
		this.isSet = isSet;
	}

	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	public String getBuyerMessage() {
		return buyerMessage;
	}

	public void setBuyerMessage(String buyerMessage) {
		this.buyerMessage = buyerMessage;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public BigDecimal getTradePrice() {
		return tradePrice;
	}

	public void setTradePrice(BigDecimal tradePrice) {
		this.tradePrice = tradePrice;
	}

	public BigDecimal getTradeTotal() {
		return tradeTotal;
	}

	public void setTradeTotal(BigDecimal tradeTotal) {
		this.tradeTotal = tradeTotal;
	}

	public BigDecimal getDeclPrice() {
		return declPrice;
	}

	public void setDeclPrice(BigDecimal declPrice) {
		this.declPrice = declPrice;
	}

	public BigDecimal getDeclTotalPrice() {
		return declTotalPrice;
	}

	public void setDeclTotalPrice(BigDecimal declTotalPrice) {
		this.declTotalPrice = declTotalPrice;
	}

	public String getCodeTs() {
		return codeTs;
	}

	public void setCodeTs(String codeTs) {
		this.codeTs = codeTs;
	}
	

	
	
}
