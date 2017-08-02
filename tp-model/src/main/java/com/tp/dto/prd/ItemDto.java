package com.tp.dto.prd;

import java.io.Serializable;
import java.util.List;

import com.tp.model.prd.ItemDesc;
import com.tp.model.prd.ItemDetail;
import com.tp.model.prd.ItemInfo;
import com.tp.model.prd.ItemSku;

/**
 * 
 * <pre>
 * 		与后台页面绑定的对象	
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 */
public class ItemDto  implements Serializable {
	
	private static final long serialVersionUID = 2461566538652537293L;
	
	//商品基本信息
	private ItemInfo itemInfo;
	//商品detail信息
	private List<ItemDetail>  itemDetailList;
	//商品SKU
	private List<ItemSku> itemSkuList;
	//商品介绍
	private ItemDesc desc;
	private String skuListJson;
	private String storageListJson;
	private String supplierListJson;
	private String attrListJson;
	private String prdidListJson;
	public ItemInfo getItemInfo() {
		return itemInfo;
	}
	public void setItemInfo(ItemInfo itemInfo) {
		this.itemInfo = itemInfo;
	}
	public List<ItemDetail> getItemDetailList() {
		return itemDetailList;
	}
	public void setItemDetailList(List<ItemDetail> itemDetailList) {
		this.itemDetailList = itemDetailList;
	}
	public List<ItemSku> getItemSkuList() {
		return itemSkuList;
	}
	public void setItemSkuList(List<ItemSku> itemSkuList) {
		this.itemSkuList = itemSkuList;
	}
	public ItemDesc getDesc() {
		return desc;
	}
	public void setDesc(ItemDesc desc) {
		this.desc = desc;
	}
	public String getSkuListJson() {
		return skuListJson;
	}
	public void setSkuListJson(String skuListJson) {
		this.skuListJson = skuListJson;
	}
	public String getStorageListJson() {
		return storageListJson;
	}
	public void setStorageListJson(String storageListJson) {
		this.storageListJson = storageListJson;
	}
	public String getSupplierListJson() {
		return supplierListJson;
	}
	public void setSupplierListJson(String supplierListJson) {
		this.supplierListJson = supplierListJson;
	}
	public String getAttrListJson() {
		return attrListJson;
	}
	public void setAttrListJson(String attrListJson) {
		this.attrListJson = attrListJson;
	}
	public String getPrdidListJson() {
		return prdidListJson;
	}
	public void setPrdidListJson(String prdidListJson) {
		this.prdidListJson = prdidListJson;
	}
	
}
