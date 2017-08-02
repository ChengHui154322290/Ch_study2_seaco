package com.tp.dto.prd;

import java.io.Serializable;

public class PicturesOpenDto implements Serializable {

	private static final long serialVersionUID = 3372514552706867938L;
	/** 商品id 外键 */
	private Long itemId;
	/**  */
	private Long detailId;

	/** 图片路径 */
	private String picture;

	/** 是否主图 默认为0-否 1-是 */
	private Integer main;

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public Long getDetailId() {
		return detailId;
	}

	public void setDetailId(Long detailId) {
		this.detailId = detailId;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public Integer getMain() {
		return main;
	}

	public void setMain(Integer main) {
		this.main = main;
	}

}
