package com.tp.dto.prd;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.tp.model.prd.ItemDesc;
import com.tp.model.prd.ItemDescMobile;
import com.tp.model.prd.ItemDetail;
import com.tp.model.prd.ItemPictures;

/**
 * 商品复制
 * @author szy
 *
 */
public class ItemCopyDto implements Serializable {

	private static final long serialVersionUID = 7686423396220832751L;
	
	private Long detailId;
	private ItemDetail itemDetail;
	private List<ItemPictures> itemPicturesList = new ArrayList<ItemPictures>();
	private ItemDesc itemDesc;
	private ItemDescMobile itemDescMobile;
	public Long getDetailId() {
		return detailId;
	}
	public void setDetailId(Long detailId) {
		this.detailId = detailId;
	}
	public ItemDetail getItemDetail() {
		return itemDetail;
	}
	public void setItemDetail(ItemDetail itemDetail) {
		this.itemDetail = itemDetail;
	}
	public List<ItemPictures> getItemPicturesList() {
		return itemPicturesList;
	}
	public void setItemPicturesList(List<ItemPictures> itemPicturesList) {
		this.itemPicturesList = itemPicturesList;
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
}
