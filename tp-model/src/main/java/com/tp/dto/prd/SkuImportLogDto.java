package com.tp.dto.prd;

import java.io.Serializable;
import java.util.List;

import com.tp.model.prd.ItemImportList;
import com.tp.model.prd.ItemImportLog;

/**
 * 
 * <pre>
 * 		sku导入dto
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 */
public class SkuImportLogDto  implements Serializable{

	
	private static final long serialVersionUID = 2241402829685897580L;

	/** sku导入主表*/
	private ItemImportLog itemImportLog;
	
	/** sku导入子表*/
	private List<ItemImportList> itemImportList;
	
	/** 分页查询的总数量*/
	private Long totalCount = 0l;

	/**
	 * Getter method for property <tt>itemImportLog</tt>.
	 * 
	 * @return property value of itemImportLog
	 */
	public ItemImportLog getItemImportLog() {
		return itemImportLog;
	}

	/**
	 * Setter method for property <tt>itemImportLog</tt>.
	 * 
	 * @param itemImportLog value to be assigned to property itemImportLog
	 */
	public void setItemImportLog(ItemImportLog itemImportLog) {
		this.itemImportLog = itemImportLog;
	}

	/**
	 * Getter method for property <tt>imprtList</tt>.
	 * 
	 * @return property value of imprtList
	 */
	public List<ItemImportList> getImportList() {
		return itemImportList;
	}

	/**
	 * Setter method for property <tt>imprtList</tt>.
	 * 
	 * @param imprtList value to be assigned to property imprtList
	 */
	public void setImportList(List<ItemImportList> itemImportList) {
		this.itemImportList = itemImportList;
	}

	/**
	 * Getter method for property <tt>totalCount</tt>.
	 * 
	 * @return property value of totalCount
	 */
	public Long getTotalCount() {
		return totalCount;
	}

	/**
	 * Setter method for property <tt>totalCount</tt>.
	 * 
	 * @param totalCount value to be assigned to property totalCount
	 */
	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}
	
}
