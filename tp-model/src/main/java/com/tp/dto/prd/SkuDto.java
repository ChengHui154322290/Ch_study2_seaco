package com.tp.dto.prd;

import java.io.Serializable;
import java.util.List;

import com.tp.model.prd.ItemDetailSpec;
import com.tp.model.prd.ItemPictures;

public class SkuDto implements Serializable {

	/**
	 * 
	 */

	/**
	 * 
	 */
	private static final long serialVersionUID = -5527265051872875799L;

	/** 主键 */
	private Long id;

	/** 商品ID */
	private Long itemId;

	/** 小类编号+5位流水码 */
	private String spu;

	/** SPU+3位数流水码 */
	private String sku;
	/** 库存 */
	private Integer qutity;
	
	private String showName;
	/**基础价格**/
	private Double basicPrice;
	/**西客商城价格**/
	private Double xgPrice;
	/**sku 对应的数量**/
	private Integer quantity;
	
	private Integer minNum;//起够数量
	
	private Integer maxNum;//最大购买数量
	
	private Long spId;//供应商id
	
	private Long detailId;//prdid编码
	
	private String itemType;//商品类型 1-正常商品，2-服务商品，3-二手商品,4-报废商品 默认1',
	
	private  String mainTitle;//商品主标题
	
	private String subTitle;//商品副标题
	
	private Integer wavesSign;//是否海淘商品  1-否，2-是，默认1
	
	private String listSpec;
	
	private Long freightTemplateId;
	/** 邮费 */
	private String postage;
	
	/** 满x元包邮 */
	private String freePostage;
	
	/** 满减后邮费 */
	private String aftPostage;
	
	/**商品品类Id***/
	private Long categoryId;
	/***供应商编码***/
	private String barCode;
	
	/***品牌id**/
	private Long  brandId;
	
	private String spCode;
	/**规格组信息***/
	private List<ItemDetailSpec> itemDetailSpecList;
	/**图片组信息***/
	private List<ItemPictures> itemPicturesList;
	
	private String spName;
	
	/***上下架状态**/
	private String status;
	
	/**国家ID***/
	private Long countryId;
	
	/***国家图片**/
	private String countryImagePath;
	
	/***配送方式**/
	private String sendType;
	
	/**适用年龄**/
	private Long applyAgeId;
	
	/** 关税税率 */
	private Long tarrifRate;
	
	/***PRDID编码***/
	private String prdid;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public String getSpu() {
		return spu;
	}

	public void setSpu(String spu) {
		this.spu = spu;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public Integer getQutity() {
		return qutity;
	}

	public void setQutity(Integer qutity) {
		this.qutity = qutity;
	}

	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}

	public Double getBasicPrice() {
		return basicPrice;
	}

	public void setBasicPrice(Double basicPrice) {
		this.basicPrice = basicPrice;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getMinNum() {
		return minNum;
	}

	public void setMinNum(Integer minNum) {
		this.minNum = minNum;
	}

	public Integer getMaxNum() {
		return maxNum;
	}

	public void setMaxNum(Integer maxNum) {
		this.maxNum = maxNum;
	}

	public Long getSpId() {
		return spId;
	}

	public void setSpId(Long spId) {
		this.spId = spId;
	}

	public Long getDetailId() {
		return detailId;
	}

	public void setDetailId(Long detailId) {
		this.detailId = detailId;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getMainTitle() {
		return mainTitle;
	}

	public void setMainTitle(String mainTitle) {
		this.mainTitle = mainTitle;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public Integer getWavesSign() {
		return wavesSign;
	}

	public void setWavesSign(Integer wavesSign) {
		this.wavesSign = wavesSign;
	}

	public String getListSpec() {
		return listSpec;
	}

	public void setListSpec(String listSpec) {
		this.listSpec = listSpec;
	}

	public Long getFreightTemplateId() {
		return freightTemplateId;
	}

	public void setFreightTemplateId(Long freightTemplateId) {
		this.freightTemplateId = freightTemplateId;
	}

	public String getPostage() {
		return postage;
	}

	public void setPostage(String postage) {
		this.postage = postage;
	}

	public String getFreePostage() {
		return freePostage;
	}

	public void setFreePostage(String freePostage) {
		this.freePostage = freePostage;
	}

	public String getAftPostage() {
		return aftPostage;
	}

	public void setAftPostage(String aftPostage) {
		this.aftPostage = aftPostage;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	public String getSpCode() {
		return spCode;
	}

	public void setSpCode(String spCode) {
		this.spCode = spCode;
	}

	public List<ItemDetailSpec> getItemDetailSpecList() {
		return itemDetailSpecList;
	}

	public void setItemDetailSpecList(List<ItemDetailSpec> itemDetailSpecList) {
		this.itemDetailSpecList = itemDetailSpecList;
	}

	public List<ItemPictures> getItemPicturesList() {
		return itemPicturesList;
	}

	public void setItemPicturesList(List<ItemPictures> itemPicturesList) {
		this.itemPicturesList = itemPicturesList;
	}

	public String getSpName() {
		return spName;
	}

	public void setSpName(String spName) {
		this.spName = spName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getCountryId() {
		return countryId;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}

	public String getCountryImagePath() {
		return countryImagePath;
	}

	public void setCountryImagePath(String countryImagePath) {
		this.countryImagePath = countryImagePath;
	}

	public String getSendType() {
		return sendType;
	}

	public void setSendType(String sendType) {
		this.sendType = sendType;
	}

	public Long getApplyAgeId() {
		return applyAgeId;
	}

	public void setApplyAgeId(Long applyAgeId) {
		this.applyAgeId = applyAgeId;
	}

	public Long getTarrifRate() {
		return tarrifRate;
	}

	public void setTarrifRate(Long tarrifRate) {
		this.tarrifRate = tarrifRate;
	}

	public String getPrdid() {
		return prdid;
	}

	public void setPrdid(String prdid) {
		this.prdid = prdid;
	}

	public Double getXgPrice() {
		return xgPrice;
	}

	public void setXgPrice(Double xgPrice) {
		this.xgPrice = xgPrice;
	}
	
}
