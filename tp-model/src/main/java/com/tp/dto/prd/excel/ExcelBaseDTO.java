package com.tp.dto.prd.excel;

import java.io.Serializable;
import java.util.Date;
/**
 * 
 * <pre>
 * 
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 */
public class ExcelBaseDTO  implements Serializable {
	
	
	/** 创建时间 */
	private Date createTime;
	
	/** excel 验证成功与否  默认1  
	 * 1-验证成功 
	 * 2-验证失败 prdid里面的barcode重复了，spu，prdid不需要插入，也不修改 
	 * sku里面的barcode与供应商都重复了 整行excel数据不需要插
	 * 
	 * 
	*/
	private int excelOpStatus = 1;
	
	/** excel的索引 */
	private Long excelIndex;
	
	/** excel 验证消息*/
	private String excelOpmessage;
	
	
	/** 是否海淘商品,1-否，2-是，默认1 */
	private int waveFlag;

	/**
	 * Getter method for property <tt>createTime</tt>.
	 * 
	 * @return property value of createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * Setter method for property <tt>createTime</tt>.
	 * 
	 * @param createTime value to be assigned to property createTime
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * Getter method for property <tt>excelOpStatus</tt>.
	 * 
	 * @return property value of excelOpStatus
	 */
	public int getExcelOpStatus() {
		return excelOpStatus;
	}

	/**
	 * Setter method for property <tt>excelOpStatus</tt>.
	 * 
	 * @param excelOpStatus value to be assigned to property excelOpStatus
	 */
	public void setExcelOpStatus(int excelOpStatus) {
		this.excelOpStatus = excelOpStatus;
	}

	/**
	 * Getter method for property <tt>excelIndex</tt>.
	 * 
	 * @return property value of excelIndex
	 */
	public Long getExcelIndex() {
		return excelIndex;
	}

	/**
	 * Setter method for property <tt>excelIndex</tt>.
	 * 
	 * @param excelIndex value to be assigned to property excelIndex
	 */
	public void setExcelIndex(Long excelIndex) {
		this.excelIndex = excelIndex;
	}

	/**
	 * Getter method for property <tt>excelOpmessage</tt>.
	 * 
	 * @return property value of excelOpmessage
	 */
	public String getExcelOpmessage() {
		return excelOpmessage;
	}

	/**
	 * Setter method for property <tt>excelOpmessage</tt>.
	 * 
	 * @param excelOpmessage value to be assigned to property excelOpmessage
	 */
	public void setExcelOpmessage(String excelOpmessage) {
		this.excelOpmessage = excelOpmessage;
	}

	/**
	 * @return the waveFlag
	 */
	public int getWaveFlag() {
		return waveFlag;
	}

	/**
	 * @param waveFlag the waveFlag to set
	 */
	public void setWaveFlag(int waveFlag) {
		this.waveFlag = waveFlag;
	}

}
