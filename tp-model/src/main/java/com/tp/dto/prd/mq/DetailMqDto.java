package com.tp.dto.prd.mq;

import java.io.Serializable;
import java.util.List;

import com.tp.model.prd.ItemAttribute;
import com.tp.model.prd.ItemDesc;
import com.tp.model.prd.ItemDescMobile;
import com.tp.model.prd.ItemDetail;
import com.tp.model.prd.ItemDetailSpec;
import com.tp.model.prd.ItemPictures;

/***
 * 
 * <pre>
 * 	规格
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 */
public class DetailMqDto implements Serializable{

	private static final long serialVersionUID = -8335477094075120933L;
	private ItemDetail itemDetail;
	private List<ItemPictures> itemPicturesList;
	private ItemDesc itemDesc;
	private ItemDescMobile itemDescMobile;
	private List<ItemAttribute> itemAttributeList;
	private List<ItemDetailSpec> itemDetailSpecList;
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
	public List<ItemAttribute> getItemAttributeList() {
		return itemAttributeList;
	}
	public void setItemAttributeList(List<ItemAttribute> itemAttributeList) {
		this.itemAttributeList = itemAttributeList;
	}
	public List<ItemDetailSpec> getItemDetailSpecList() {
		return itemDetailSpecList;
	}
	public void setItemDetailSpecList(List<ItemDetailSpec> itemDetailSpecList) {
		this.itemDetailSpecList = itemDetailSpecList;
	}

}
