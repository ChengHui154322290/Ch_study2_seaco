package com.tp.dto.stg.BML;

/**
 * 
 * <pre>
 * 出库汇总查询
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 */
public class SKUS {
	/**
	 * SKU
	 */
	private String SKU;
	/**
	 * 销售渠道
	 */
	private String IssuePartyID;
	/**
	 * 发运合计数量
	 */
	private String QtyShipped;

	public String getSKU() {
		return SKU;
	}

	public void setSKU(String sKU) {
		SKU = sKU;
	}

	public String getIssuePartyID() {
		return IssuePartyID;
	}

	public void setIssuePartyID(String issuePartyID) {
		IssuePartyID = issuePartyID;
	}

	public String getQtyShipped() {
		return QtyShipped;
	}

	public void setQtyShipped(String qtyShipped) {
		QtyShipped = qtyShipped;
	}
}
