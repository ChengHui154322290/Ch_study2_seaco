package com.tp.dto.prd;

import java.io.Serializable;
import java.util.List;

public class ItemDetailOpenDto implements Serializable {
	private static final long serialVersionUID = 2730378252867208107L;

	/** 商品信息 */
	private InfoOpenDto infoOpenDto;

	private DetailOpenDto detailOpenDto;

	/** 图片信息 */
	private List<PicturesOpenDto> listOfPictures;

	/** 客户端富文本框描述 */
	private String descDetail;

	/** 手机端富文本框描述 */
	private String descMobileDetail;
	/**sku信息***/
	private SkuDto skuDto;

	public InfoOpenDto getInfoOpenDto() {
		return infoOpenDto;
	}

	public void setInfoOpenDto(InfoOpenDto infoOpenDto) {
		this.infoOpenDto = infoOpenDto;
	}

	public DetailOpenDto getDetailOpenDto() {
		return detailOpenDto;
	}

	public void setDetailOpenDto(DetailOpenDto detailOpenDto) {
		this.detailOpenDto = detailOpenDto;
	}

	public List<PicturesOpenDto> getListOfPictures() {
		return listOfPictures;
	}

	public void setListOfPictures(List<PicturesOpenDto> listOfPictures) {
		this.listOfPictures = listOfPictures;
	}

	public String getDescDetail() {
		return descDetail;
	}

	public void setDescDetail(String descDetail) {
		this.descDetail = descDetail;
	}

	public String getDescMobileDetail() {
		return descMobileDetail;
	}

	public void setDescMobileDetail(String descMobileDetail) {
		this.descMobileDetail = descMobileDetail;
	}

	public SkuDto getSkuDto() {
		return skuDto;
	}
	public void setSkuDto(SkuDto skuDto) {
		this.skuDto = skuDto;
	}
}
