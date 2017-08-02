package com.tp.dto.prd;

import java.io.Serializable;
import java.util.List;

import com.tp.model.prd.ItemDetailImport;
import com.tp.model.prd.ItemImportLog;

public class ItemDetailImportLogDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 导入主表*/
	private ItemImportLog itemImportLog;
	
	/** 导入子表*/
	private List<ItemDetailImport> importList;

	public ItemImportLog getItemImportLog() {
		return itemImportLog;
	}

	public void setItemImportLog(ItemImportLog itemImportLog) {
		this.itemImportLog = itemImportLog;
	}

	public List<ItemDetailImport> getImportList() {
		return importList;
	}

	public void setImportList(List<ItemDetailImport> importList) {
		this.importList = importList;
	}
	
	
	
}
