package com.tp.dto.prd;

import java.io.Serializable;
import java.util.List;

import com.tp.model.prd.ItemAttribute;
import com.tp.model.prd.ItemDesc;
import com.tp.model.prd.ItemDescMobile;
import com.tp.model.prd.ItemDetail;
import com.tp.model.prd.ItemDetailSpec;

/**
 * 
 * <pre>
 * 		与后台页面绑定的对象	
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 */
public class DetailDto  implements Serializable {
	
	private static final long serialVersionUID = 2461566538652537293L;
	
	/** 商品detail信息 */
	private ItemDetail itemDetail;
	
	private List<DetailSkuDto> detailSkuList;
	/** web的详情 */
	private ItemDesc itemDesc;
	
	/** 手机预览的详情 */
	private ItemDescMobile itemDescMobile;
	
	/** 图片  */
	private List<String> picList;
	
	/** 商品属性 */
	private List<ItemAttribute> attrList;
	private List<ItemAttribute> attrItemList;
	/**服务商品属性*/
	private List<ItemAttribute> dummyAttrList;
	
	/** 是否已经有西客商城商家 : 1 ：有，0：没有，默认0 */
	private int hasXgSeller = 0 ;
	
	/** 商品销售规格*/
	private List<ItemDetailSpec> ItemDetailSpecList ;

	public List<ItemAttribute> getDummyAttrList() {
		return dummyAttrList;
	}

	public void setDummyAttrList(List<ItemAttribute> dummyAttrList) {
		this.dummyAttrList = dummyAttrList;
	}

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

	public List<String> getPicList() {
		return picList;
	}

	public void setPicList(List<String> picList) {
		this.picList = picList;
	}

	public List<ItemDetailSpec> getItemDetailSpecList() {
		return ItemDetailSpecList;
	}

	public void setItemDetailSpecList(List<ItemDetailSpec> itemDetailSpecList) {
		ItemDetailSpecList = itemDetailSpecList;
	}

	public List<DetailSkuDto> getDetailSkuList() {
		return detailSkuList;
	}

	public void setDetailSkuList(List<DetailSkuDto> detailSkuList) {
		this.detailSkuList = detailSkuList;
	}

	public List<ItemAttribute> getAttrList() {
		return attrList;
	}

	public void setAttrList(List<ItemAttribute> attrList) {
		this.attrList = attrList;
	}

	public List<ItemAttribute> getAttrItemList() {
		return attrItemList;
	}

	public void setAttrItemList(List<ItemAttribute> attrItemList) {
		this.attrItemList = attrItemList;
	}

	public int getHasXgSeller() {
		return hasXgSeller;
	}

	public void setHasXgSeller(int hasXgSeller) {
		this.hasXgSeller = hasXgSeller;
	}
	
}
