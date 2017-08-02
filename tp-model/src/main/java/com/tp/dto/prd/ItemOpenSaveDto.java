package com.tp.dto.prd;

import java.io.Serializable;
import java.util.List;

import com.tp.model.prd.ItemAttribute;
import com.tp.model.prd.ItemDesc;
import com.tp.model.prd.ItemDescMobile;
import com.tp.model.prd.ItemDetail;
import com.tp.model.prd.ItemDetailSpec;
import com.tp.model.prd.ItemInfo;
import com.tp.model.prd.ItemPictures;
import com.tp.model.prd.ItemSku;
import com.tp.model.prd.ItemSkuArt;
/**
 * 商品信息
 * @author szy
 *
 */
public class ItemOpenSaveDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4656431725804992444L;
	private ItemDetail itemDetail;
	private ItemDesc  itemDesc;
	private ItemDescMobile itemDescMobile;
	private List<ItemAttribute> itemAttributeList;
	private List<ItemDetailSpec> itemDetailSpecList;
	private List<ItemPictures> itemPicturesList;
	private ItemSku itemSku;
	private ItemInfo itemInfo;
	private ItemSkuArt itemSkuArt;

	public ItemDetail getItemDetail() {
		return itemDetail;
	}

	public void setItemDetail(ItemDetail itemDetail) {
		this.itemDetail = itemDetail;
	}

	public ItemDesc getItemDesc() {
		return itemDesc;
	}

	public void setItemDesc(ItemDesc itemDesc) {
		this.itemDesc = itemDesc;
	}

	public ItemDescMobile getItemDescMobile() {
		return itemDescMobile;
	}

	public void setItemDescMobile(ItemDescMobile itemDescMobile) {
		this.itemDescMobile = itemDescMobile;
	}

	public List<ItemAttribute> getAttributeList() {
		return itemAttributeList;
	}

	public void setAttributeList(List<ItemAttribute> itemAttributeList) {
		this.itemAttributeList = itemAttributeList;
	}

	public List<ItemDetailSpec> getListDetailSpec() {
		return itemDetailSpecList;
	}

	public void setListDetailSpec(List<ItemDetailSpec> itemDetailSpecList) {
		this.itemDetailSpecList = itemDetailSpecList;
	}

	public ItemSku getItemSku() {
		return itemSku;
	}

	public void setItemSku(ItemSku itemSku) {
		this.itemSku = itemSku;
	}

	public List<ItemPictures> getListPic() {
		return itemPicturesList;
	}

	public void setListPic(List<ItemPictures> itemPicturesList) {
		this.itemPicturesList = itemPicturesList;
	}

	public ItemInfo getItemInfo() {
		return itemInfo;
	}

	public void setItemInfo(ItemInfo itemInfo) {
		this.itemInfo = itemInfo;
	}

	public ItemSkuArt getItemSkuArt() {
		return itemSkuArt;
	}

	public void setItemSkuArt(ItemSkuArt itemSkuArt) {
		this.itemSkuArt = itemSkuArt;
	}
}
