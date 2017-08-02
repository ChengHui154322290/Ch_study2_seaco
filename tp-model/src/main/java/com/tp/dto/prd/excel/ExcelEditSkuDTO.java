package com.tp.dto.prd.excel;

import com.tp.common.annotation.excel.poi.ExcelEntity;
import com.tp.common.annotation.excel.poi.ExcelProperty;

@ExcelEntity
public class ExcelEditSkuDTO extends ExcelBaseDTO{
	
	private static final long serialVersionUID = 1898991715016807187L;

	@ExcelProperty(value="*SKU",required=true,itemLatitude=3)
	private String sku;
	
	@ExcelProperty(value="网站标题",itemLatitude=2)
	private String mainTitle;
	
	@ExcelProperty(value="SKU市场价",itemLatitude=2)
	private String basicPrice;

	@ExcelProperty(value="商品详情市场价",itemLatitude=2)
	private String detailBasicPrice;
	
	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getMainTitle() {
		return mainTitle;
	}

	public void setMainTitle(String mainTitle) {
		this.mainTitle = mainTitle;
	}

	public String getBasicPrice() {
		return basicPrice;
	}

	public void setBasicPrice(String basicPrice) {
		this.basicPrice = basicPrice;
	}

	public String getDetailBasicPrice() {
		return detailBasicPrice;
	}

	public void setDetailBasicPrice(String detailBasicPrice) {
		this.detailBasicPrice = detailBasicPrice;
	}	
}
