package com.tp.dto.prd;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.tp.model.prd.ItemSku;
import com.tp.model.prd.ItemSkuArt;


public class PushItemInfoAndDetailDto  implements Serializable{

	private static final long serialVersionUID = -235916799741025052L;
	//spu名称
	private String spuName;
	//小类id
	private Long smallId;
	//品牌id
	private Long brandId;
	//品牌名称
	private String brandName;
	//单位id
    private Long unitId;
    //备注
    private String remark;
	//规格组信息
    private List<Map<String, Long>> specInfos;
//    //规格组信息描述
//    private String  specInfosDesc;
    //商品描述
    private String itemDesc;
    //手机商品描述
    private String descMobile;
    //商品属性
    private List<Map<String,String>> itemAttr;
    //商品图片
    private List<String> itemPictures;
    // 市场价
    private Double basicPrice;
    // 报关信息
    private ItemSkuArt customsInfo;
    
    
    //商品属性
    private Map<String,String> itemAttrMap;
    //规格信息
    private Map<Long, Long> specInfosMap;
    
    //规格,规格组描述
    private Map<String, String> specInfosDescMap;
    //商品详细信息
    private PushItemDetailDto itemDetail;
    //图片信息
    private List<String> picListPath;
    //itemSkuList
    private List<ItemSku> itemSkus;
    
	public List<ItemSku> getItemSkus() {
		return itemSkus;
	}
	public void setItemSkus(List<ItemSku> itemSkus) {
		this.itemSkus = itemSkus;
	}
	public Long getBrandId() {
		return brandId;
	}
	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}
	public String getSpuName() {
		return spuName;
	}
	public void setSpuName(String spuName) {
		this.spuName = spuName;
	}
	public Long getSmallId() {
		return smallId;
	}
	public void setSmallId(Long smallId) {
		this.smallId = smallId;
	}
	public Long getUnitId() {
		return unitId;
	}
	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public List<Map<String, Long>> getSpecInfos() {
		return specInfos;
	}
	public void setSpecInfos(List<Map<String, Long>> specInfos) {
		this.specInfos = specInfos;
	}
	public String getItemDesc() {
		return itemDesc;
	}
	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}
	public List<Map<String,String>> getItemAttr() {
		return itemAttr;
	}
	public void setItemAttr(List<Map<String,String>> itemAttr) {
		this.itemAttr = itemAttr;
	}
	public List<String> getItemPictures() {
		return itemPictures;
	}
	public void setItemPictures(List<String> itemPictures) {
		this.itemPictures = itemPictures;
	}
	public Double getBasicPrice() {
		return basicPrice;
	}
	public void setBasicPrice(Double basicPrice) {
		this.basicPrice = basicPrice;
	}
	public Map<String, String> getItemAttrMap() {
		return itemAttrMap;
	}
	public void setItemAttrMap(Map<String, String> itemAttrMap) {
		this.itemAttrMap = itemAttrMap;
	}
	public Map<Long, Long> getSpecInfosMap() {
		return specInfosMap;
	}
	public void setSpecInfosMap(Map<Long, Long> specInfosMap) {
		this.specInfosMap = specInfosMap;
	}
	public PushItemDetailDto getItemDetail() {
		return itemDetail;
	}
	public void setItemDetail(PushItemDetailDto itemDetail) {
		this.itemDetail = itemDetail;
	}
	public List<String> getPicListPath() {
		return picListPath;
	}
	public void setPicListPath(List<String> picListPath) {
		this.picListPath = picListPath;
	}
	public String getDescMobile() {
		return descMobile;
	}
	public void setDescMobile(String descMobile) {
		this.descMobile = descMobile;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
//	public String getSpecInfosDesc() {
//		return specInfosDesc;
//	}
//	public void setSpecInfosDesc(String specInfosDesc) {
//		this.specInfosDesc = specInfosDesc;
//	}
	public Map<String, String> getSpecInfosDescMap() {
		return specInfosDescMap;
	}
	public void setSpecInfosDescMap(Map<String, String> specInfosDescMap) {
		this.specInfosDescMap = specInfosDescMap;
	}
	public ItemSkuArt getCustomsInfo() {
		return customsInfo;
	}
	public void setCustomsInfo(ItemSkuArt customsInfo) {
		this.customsInfo = customsInfo;
	}
	
	/**商品编码（第三方推送）**/
	private String goodsCode;
	/**货号（第三方推送）**/
	private String articleCode;
	
	public String getGoodsCode() {
		return goodsCode;
	}

	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}

	public String getArticleCode() {
		return articleCode;
	}

	public void setArticleCode(String articleCode) {
		this.articleCode = articleCode;
	}
}
