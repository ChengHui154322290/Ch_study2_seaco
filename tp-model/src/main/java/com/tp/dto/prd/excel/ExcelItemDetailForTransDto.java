package com.tp.dto.prd.excel;

import com.tp.common.annotation.excel.poi.ExcelEntity;
import com.tp.common.annotation.excel.poi.ExcelProperty;

/***
 * 商品详情信息Dto
 * @author 
 *
 */
@ExcelEntity
public class ExcelItemDetailForTransDto extends ExcelBaseDTO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 */
	/**detailId****/
	@ExcelProperty(value="*id")
	private Long detailId;
	/**sku**/
	@ExcelProperty(value="*sku")
	private String sku;
	/**品牌名称**/
	@ExcelProperty(value="*品牌名称")
	private String brandName;
	/**商品名称 **/
	@ExcelProperty(value="*商品名称")
	private String itemTitle;
	/**商品详情描述**/
	@ExcelProperty(value="商品详情描述")
	private String itemDetailDesc;
	/**品牌故事**/
	@ExcelProperty(value="*品牌故事")
	private String brandStory;
	/**材质**/
	@ExcelProperty(value="材质")
	private String material;
	/**特殊说明**/
	@ExcelProperty(value="特殊说明")
	private String specialInstructions;
	
	public String getBrandStory() {
		return brandStory;
	}

	public void setBrandStory(String brandStory) {
		this.brandStory = brandStory;
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public String getSpecialInstructions() {
		return specialInstructions;
	}

	public void setSpecialInstructions(String specialInstructions) {
		this.specialInstructions = specialInstructions;
	}

	public Long getDetailId() {
		return detailId;
	}

	public void setDetailId(Long detailId) {
		this.detailId = detailId;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getItemTitle() {
		return itemTitle;
	}

	public void setItemTitle(String itemTitle) {
		this.itemTitle = itemTitle;
	}

	public String getItemDetailDesc() {
		return itemDetailDesc;
	}

	public void setItemDetailDesc(String itemDetailDesc) {
		this.itemDetailDesc = itemDetailDesc;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	
	
}
