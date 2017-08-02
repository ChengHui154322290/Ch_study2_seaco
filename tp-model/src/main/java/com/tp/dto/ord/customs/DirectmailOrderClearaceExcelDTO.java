/**
 * 
 */
package com.tp.dto.ord.customs;

import com.tp.common.annotation.excel.poi.ExcelEntity;
import com.tp.common.annotation.excel.poi.ExcelProperty;
import com.tp.dto.prd.excel.ExcelBaseDTO;

/**
 * @author Administrator
 *
 */
@ExcelEntity
public class DirectmailOrderClearaceExcelDTO extends ExcelBaseDTO{

	private static final long serialVersionUID = -3754170427701474837L;

	@ExcelProperty(value = "*订单号")
	private String orderCode;
	
	@ExcelProperty(value = "*航班号")
	private String voyageNo;
	
	@ExcelProperty(value = "*总提运单号")
	private String billNo;
	
	@ExcelProperty(value = "运输工具编号")
	private String trafNo;

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getVoyageNo() {
		return voyageNo;
	}

	public void setVoyageNo(String voyageNo) {
		this.voyageNo = voyageNo;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getTrafNo() {
		return trafNo;
	}

	public void setTrafNo(String trafNo) {
		this.trafNo = trafNo;
	}
}
