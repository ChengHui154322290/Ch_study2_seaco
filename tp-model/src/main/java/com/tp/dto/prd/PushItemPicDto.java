package com.tp.dto.prd;

import java.io.Serializable;
import java.util.List;

public class PushItemPicDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//商品sku
	private String sku;
	//商品主图片
	private String mainPic;
	//商品子图片
	private List<String> pictures;
	//商详图片
	private List<String> descPics;
	
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public List<String> getPictures() {
		return pictures;
	}
	public void setPictures(List<String> pictures) {
		this.pictures = pictures;
	}
	public String getMainPic() {
		return mainPic;
	}
	public void setMainPic(String mainPic) {
		this.mainPic = mainPic;
	}
	public List<String> getDescPics() {
		return descPics;
	}
	public void setDescPics(List<String> descPics) {
		this.descPics = descPics;
	}
	
	
}
