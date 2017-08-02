package com.tp.dto.mmp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.tp.model.bse.Spec;
import com.tp.model.mmp.TopicItem;
import com.tp.model.mmp.TopicItemChange;
import com.tp.model.prd.ItemSku;
import com.tp.model.stg.Warehouse;

/**
 * 商品查询专题信息
 * 
 * @author szy
 *
 */
public class TopicItemInfoDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6613355546178923769L;
	
	/** id 主键 */
	private Long id;

	/** 供应商Id */
	private Long supplierId;

	/** 供应商名 */
	private String supplierName;

	/** 商品名 */
	private String name;

	/** 排序 */
	private Integer sortIndex;

	/** 商品颜色 */
	private String itemColor;

	/** 商品尺码 */
	private String itemSize;

	/** 限购总量 */
	private Integer limitTotal;

	/** 销售数量 */
	private Integer saledAmount;

	/** 仓库名 */
	private String stockLocation;

	/** 仓库id */
	private Long stockLocationId;

	private Integer putSign;
	
	/** 库存数量(总库存中可申请库存) */
	private Integer stockAmout;
	
	/** 专场内商品剩余库存 */
	private Integer remainStock;

	/** sku 信息 **/
	private String sku;
	
	/** item sku status **/
	private Integer itemStatus; 
	
	/** 条码 */
	private String barCode;

	/** 商品id **/
	private Long itemId;

	/** 专题id **/
	private Long topicId;

	/** 专题名称 **/
	private String topicName;

	/** 限购数量 **/
	private Integer limitAmount;

	/** 促销价格 **/
	private Double topicPrice;

	/** 本商品促销图片 **/
	private String topicImage;

	/** 专题状态 **/
	private Integer topicStatus;

	/** 专题持续类型 **/
	private Integer lastingType;

	/** 专题开始时间 **/
	private Date startTime;

	/** 专题结束时间 **/
	private Date endTime;

	/** 备注 */
	private String remark;

	/** 商品规格参数 */
	private String itemSpec;

	/** 位图大小 */
	private Integer pictureSize;

	/** 品牌id */
	private Long brandId;

	/** 分类id */
	private Long categoryId;

	/** 有无库存 */
	private Integer stock;

	/** 销售价格 */
	private Double salePrice;

	/** 图片全路径 */
	private String imageFullPath;

	/** spu */
	private String spu;

	/** 数据输入源 */
	private Integer inputSource;

	/** 锁定状态 */
	private Integer lockStatus;

	/** 数据操作类型 */
	private Integer operStatus;

	/** 变更单对应的活动Id */
	private Long changeTopicItemId;

	/** 变更专题id **/
	private Long topicChangeId;

	/** 通关渠道 */
	private Long bondedArea;

	/** 仓库类型 */
	private Integer whType;

	/** 国家序号 */
	private Long countryId;

	/** 国家名称 */
	private String countryName;

	/** 获得detailId */
	private Long detailId;

	/** 购买方式 1-普通 2-立即购买 */
	private Integer purchaseMethod;
	
	private Boolean validate;
	
	private Boolean locked;
	
	/** 库存数量**/
	private int stockAmount;
	
	/** 活动商品标签 **/
	private String itemTags;
	
	/** 活动预占库存标示：0否1是 */
	private Integer topicInventoryFlag;
	
	/** 仓库列表 */
	private List<Warehouse> warehouses = new ArrayList<Warehouse>();
	public List<Warehouse> getWarehouses() {
		return warehouses;
	}

	public void setWarehouses(List<Warehouse> warehouses) {
		this.warehouses = warehouses;
	}

	public TopicItemInfoDTO() {

	}

	public Integer getTopicInventoryFlag() {
		return topicInventoryFlag;
	}

	public void setTopicInventoryFlag(Integer topicInventoryFlag) {
		this.topicInventoryFlag = topicInventoryFlag;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public Long getTopicId() {
		return topicId;
	}

	public void setTopicId(Long topicId) {
		this.topicId = topicId;
	}

	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	public Integer getLimitAmount() {
		return limitAmount;
	}

	public void setLimitAmount(Integer limitAmount) {
		this.limitAmount = limitAmount;
	}

	public Double getTopicPrice() {
		return topicPrice;
	}

	public void setTopicPrice(Double topicPrice) {
		this.topicPrice = topicPrice;
	}

	public String getTopicImage() {
		return topicImage;
	}

	public void setTopicImage(String topicImage) {
		this.topicImage = topicImage;
	}

	public Integer getTopicStatus() {
		return topicStatus;
	}

	public void setTopicStatus(Integer topicStatus) {
		this.topicStatus = topicStatus;
	}

	public Integer getLastingType() {
		return lastingType;
	}

	public void setLastingType(Integer lastingType) {
		this.lastingType = lastingType;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the supplierId
	 */
	public Long getSupplierId() {
		return supplierId;
	}

	/**
	 * @param supplierId
	 *            the supplierId to set
	 */
	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the sortIndex
	 */
	public Integer getSortIndex() {
		return sortIndex;
	}

	/**
	 * @param sortIndex
	 *            the sortIndex to set
	 */
	public void setSortIndex(Integer sortIndex) {
		this.sortIndex = sortIndex;
	}

	/**
	 * @return the itemColor
	 */
	public String getItemColor() {
		return itemColor;
	}

	/**
	 * @param itemColor
	 *            the itemColor to set
	 */
	public void setItemColor(String itemColor) {
		this.itemColor = itemColor;
	}

	/**
	 * @return the itemSize
	 */
	public String getItemSize() {
		return itemSize;
	}

	/**
	 * @param itemSize
	 *            the itemSize to set
	 */
	public void setItemSize(String itemSize) {
		this.itemSize = itemSize;
	}

	/**
	 * @return the limitTotal
	 */
	public Integer getLimitTotal() {
		return limitTotal;
	}

	/**
	 * @param limitTotal
	 *            the limitTotal to set
	 */
	public void setLimitTotal(Integer limitTotal) {
		this.limitTotal = limitTotal;
	}

	/**
	 * @return the saledAmount
	 */
	public Integer getSaledAmount() {
		return saledAmount;
	}

	/**
	 * @param saledAmount
	 *            the saledAmount to set
	 */
	public void setSaledAmount(Integer saledAmount) {
		this.saledAmount = saledAmount;
	}

	/**
	 * @return the stockLocation
	 */
	public String getStockLocation() {
		return stockLocation;
	}

	/**
	 * @param stockLocation
	 *            the stockLocation to set
	 */
	public void setStockLocation(String stockLocation) {
		this.stockLocation = stockLocation;
	}

	/**
	 * @return the stockAmout
	 */
	public Integer getStockAmout() {
		return stockAmout;
	}

	/**
	 * @param stockAmout
	 *            the stockAmout to set
	 */
	public void setStockAmout(Integer stockAmout) {
		this.stockAmout = stockAmout;
	}

	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark
	 *            the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @return the itemSpec
	 */
	public String getItemSpec() {
		return itemSpec;
	}

	/**
	 * @param itemSpec
	 *            the itemSpec to set
	 */
	public void setItemSpec(String itemSpec) {
		this.itemSpec = itemSpec;
	}

	/**
	 * @return the stockLocationId
	 */
	public Long getStockLocationId() {
		return stockLocationId;
	}

	/**
	 * @param stockLocationId
	 *            the stockLocationId to set
	 */
	public void setStockLocationId(Long stockLocationId) {
		this.stockLocationId = stockLocationId;
	}

	/**
	 * @return the supplierName
	 */
	public String getSupplierName() {
		return supplierName;
	}

	/**
	 * @param supplierName
	 *            the supplierName to set
	 */
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	/**
	 * @return the barCode
	 */
	public String getBarCode() {
		return barCode;
	}

	/**
	 * @param barCode
	 *            the barCode to set
	 */
	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	/**
	 * @return the pictureSize
	 */
	public Integer getPictureSize() {
		return pictureSize;
	}

	/**
	 * @param pictureSize
	 *            the pictureSize to set
	 */
	public void setPictureSize(Integer pictureSize) {
		this.pictureSize = pictureSize;
	}

	/**
	 * @return the brandId
	 */
	public Long getBrandId() {
		return brandId;
	}

	/**
	 * @param brandId
	 *            the brandId to set
	 */
	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	/**
	 * @return the categoryId
	 */
	public Long getCategoryId() {
		return categoryId;
	}

	/**
	 * @param categoryId
	 *            the categoryId to set
	 */
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	/**
	 * @return the stock
	 */
	public Integer getStock() {
		return stock;
	}

	/**
	 * @param stock
	 *            the stock to set
	 */
	public void setStock(Integer stock) {
		this.stock = stock;
	}

	/**
	 * @return the salePrice
	 */
	public Double getSalePrice() {
		return salePrice;
	}

	/**
	 * @param salePrice
	 *            the salePrice to set
	 */
	public void setSalePrice(Double salePrice) {
		this.salePrice = salePrice;
	}

	/**
	 * @return the imageFullPath
	 */
	public String getImageFullPath() {
		return imageFullPath;
	}

	/**
	 * @param imageFullPath
	 *            the imageFullPath to set
	 */
	public void setImageFullPath(String imageFullPath) {
		this.imageFullPath = imageFullPath;
	}

	/**
	 * @return the spu
	 */
	public String getSpu() {
		return spu;
	}

	/**
	 * @param spu
	 *            the spu to set
	 */
	public void setSpu(String spu) {
		this.spu = spu;
	}

	/**
	 * 设置Sku信息 basicPrice
	 * 
	 * @param skuDO
	 */
	public void setSKUInfo(ItemSku skuDO) {
		if (null != skuDO) {
			this.brandId = skuDO.getBrandId();
			this.salePrice = skuDO.getBasicPrice();
			this.supplierId = skuDO.getSpId();
			this.supplierName = skuDO.getSpName();
			this.sku = skuDO.getSku();
			this.spu = skuDO.getSpu();
			this.barCode = skuDO.getBarcode();
			this.itemId = skuDO.getItemId();
			// 相当于Detail表中的MainTitle
			this.name = skuDO.getDetailName();
		}
	}

	public void setItemSpecInfo(List<Spec> specs) {
		if (null != specs) {
			List<String> specValue = new ArrayList<String>();
			for (Spec spec : specs) {
				if (!StringUtils.isBlank(spec.getSpec())) {
					specValue.add(spec.getSpec());
				}
			}
			this.itemSpec = StringUtils.join(specValue, "<br/>");
		}
	}

	public void setWarehouseInfo(Warehouse warehouse, Integer remainAmount) {
		if (null != warehouse) {
			this.stockLocation = warehouse.getName();
			this.stockLocationId = warehouse.getId();
			this.stockAmout = remainAmount;
		}
	}

	/**
	 * @return the inputSource
	 */
	public Integer getInputSource() {
		return inputSource;
	}

	/**
	 * @param inputSource
	 *            the inputSource to set
	 */
	public void setInputSource(Integer inputSource) {
		this.inputSource = inputSource;
	}

	/**
	 * @return the operStatus
	 */
	public Integer getOperStatus() {
		return operStatus;
	}

	/**
	 * @param operStatus
	 *            the operStatus to set
	 */
	public void setOperStatus(Integer operStatus) {
		this.operStatus = operStatus;
	}

	/**
	 * @return the changeTopicItemId
	 */
	public Long getChangeTopicItemId() {
		return changeTopicItemId;
	}

	/**
	 * @param changeTopicItemId
	 *            the changeTopicItemId to set
	 */
	public void setChangeTopicItemId(Long changeTopicItemId) {
		this.changeTopicItemId = changeTopicItemId;
	}

	/**
	 * @return the topicChangeId
	 */
	public Long getTopicChangeId() {
		return topicChangeId;
	}

	/**
	 * @param topicChangeId
	 *            the topicChangeId to set
	 */
	public void setTopicChangeId(Long topicChangeId) {
		this.topicChangeId = topicChangeId;
	}

	/**
	 * @return the lockStatus
	 */
	public Integer getLockStatus() {
		return lockStatus;
	}

	/**
	 * @param lockStatus
	 *            the lockStatus to set
	 */
	public void setLockStatus(Integer lockStatus) {
		this.lockStatus = lockStatus;
	}

	/**
	 * @return the bondedArea
	 */
	public Long getBondedArea() {
		return bondedArea;
	}

	/**
	 * @param bondedArea
	 *            the bondedArea to set
	 */
	public void setBondedArea(Long bondedArea) {
		this.bondedArea = bondedArea;
	}

	/**
	 * @return the whType
	 */
	public Integer getWhType() {
		return whType;
	}

	/**
	 * @param whType
	 *            the whType to set
	 */
	public void setWhType(Integer whType) {
		this.whType = whType;
	}

	/**
	 * @return the countryId
	 */
	public Long getCountryId() {
		return countryId;
	}

	/**
	 * @param countryId
	 *            the countryId to set
	 */
	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}

	/**
	 * @return the countryName
	 */
	public String getCountryName() {
		return countryName;
	}

	/**
	 * @param countryName
	 *            the countryName to set
	 */
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	/**
	 * @return the detailId
	 */
	public Long getDetailId() {
		return detailId;
	}

	/**
	 * @param detailId
	 *            the detailId to set
	 */
	public void setDetailId(Long detailId) {
		this.detailId = detailId;
	}

	/**
	 * @return the purchaseMethod
	 */
	public Integer getPurchaseMethod() {
		return purchaseMethod;
	}

	/**
	 * @param purchaseMethod
	 *            the purchaseMethod to set
	 */
	public void setPurchaseMethod(Integer purchaseMethod) {
		this.purchaseMethod = purchaseMethod;
	}

	public TopicItem getTopicItem() {
		TopicItem topicItem = new TopicItem();
		topicItem.setBarCode(this.barCode);
		topicItem.setId(this.id);
		// 目前不用了
		// topicItem.setItemColor(this.itemColor);
		// topicItem.setItemSize(this.itemSize);
		topicItem.setItemId(this.itemId);
		topicItem.setItemSpec(this.itemSpec);
		topicItem.setLimitAmount(this.limitAmount);
		topicItem.setLimitTotal(this.limitTotal);
		topicItem.setSaledAmount(this.saledAmount);
		topicItem.setName(this.name);
		topicItem.setRemark(this.remark);
		topicItem.setSku(this.sku);
		topicItem.setSpu(this.spu);
		topicItem.setSortIndex(this.sortIndex);
		topicItem.setStockAmount(this.stockAmout);
		topicItem.setStockLocation(this.stockLocation);
		topicItem.setStockLocationId(this.stockLocationId);
		topicItem.setPutSign(this.putSign);
		topicItem.setSupplierId(this.supplierId);
		topicItem.setSupplierName(this.supplierName);
		topicItem.setTopicId(this.topicId);
		topicItem.setTopicImage(this.topicImage);
		topicItem.setTopicPrice(this.topicPrice);
		topicItem.setPictureSize(this.pictureSize);
		topicItem.setBrandId(this.brandId);
		topicItem.setCategoryId(this.categoryId);
		topicItem.setStock(this.stock);
		topicItem.setSalePrice(this.salePrice);
		topicItem.setInputSource(this.inputSource);
		topicItem.setLockStatus(this.lockStatus);
		topicItem.setBondedArea(this.bondedArea);
		topicItem.setWhType(this.whType);
		topicItem.setCountryId(this.countryId);
		topicItem.setCountryName(this.countryName);
		topicItem.setPurchaseMethod(this.purchaseMethod);
		topicItem.setItemTags(this.getItemTags());
		return topicItem;
	}

	public void setTopicItem(TopicItem topicItem) {
		this.barCode = topicItem.getBarCode();
		this.id = topicItem.getId();
		// 目前不用了
		// topicItem.setItemColor(this.itemColor);
		// topicItem.setItemSize(this.itemSize);
		this.itemId = topicItem.getItemId();
		this.itemSpec = topicItem.getItemSpec();
		this.limitAmount = topicItem.getLimitAmount();
		this.limitTotal = topicItem.getLimitTotal();
		this.saledAmount = topicItem.getSaledAmount();
		this.name = topicItem.getName();
		this.remark = topicItem.getRemark();
		this.sku = topicItem.getSku();
		this.spu = topicItem.getSpu();
		this.sortIndex = topicItem.getSortIndex();
		this.stockAmout = topicItem.getStockAmount();
		this.stockLocation = topicItem.getStockLocation();
		this.stockLocationId = topicItem.getStockLocationId();
		this.supplierId = topicItem.getSupplierId();
		this.supplierName = topicItem.getSupplierName();
		this.topicId = topicItem.getTopicId();
		this.topicImage = topicItem.getTopicImage();
		this.topicPrice = topicItem.getTopicPrice();
		this.pictureSize = topicItem.getPictureSize();
		this.brandId = topicItem.getBrandId();
		this.categoryId = topicItem.getCategoryId();
		this.stock = topicItem.getStock();
		this.salePrice = topicItem.getSalePrice();
		this.lockStatus = topicItem.getLockStatus();
		this.inputSource = topicItem.getInputSource();
		this.bondedArea = topicItem.getBondedArea();
		this.whType = topicItem.getWhType();
		this.countryId = topicItem.getCountryId();
		this.countryName = topicItem.getCountryName();
		this.purchaseMethod = topicItem.getPurchaseMethod();
		this.itemTags = topicItem.getItemTags();
	}

	public TopicItemChange getTopicItemChange() {
		TopicItemChange topicItemChange = new TopicItemChange();
		topicItemChange.setBarCode(this.barCode);
		topicItemChange.setId(this.id);
		// 目前不用了
		// topicItem.setItemColor(this.itemColor);
		// topicItem.setItemSize(this.itemSize);
		topicItemChange.setItemId(this.itemId);
		topicItemChange.setItemSpec(this.itemSpec);
		topicItemChange.setLimitAmount(this.limitAmount);
		topicItemChange.setLimitTotal(this.limitTotal);
		topicItemChange.setSaledAmount(this.saledAmount);
		topicItemChange.setName(this.name);
		topicItemChange.setRemark(this.remark);
		topicItemChange.setSku(this.sku);
		topicItemChange.setSpu(this.spu);
		topicItemChange.setSortIndex(this.sortIndex);
		topicItemChange.setStockAmount(this.stockAmout);
		topicItemChange.setStockLocation(this.stockLocation);
		topicItemChange.setStockLocationId(this.stockLocationId);
		topicItemChange.setPutSign(this.putSign);
		topicItemChange.setSupplierId(this.supplierId);
		topicItemChange.setSupplierName(this.supplierName);
		topicItemChange.setTopicChangeId(this.topicChangeId);
		topicItemChange.setTopicImage(this.topicImage);
		topicItemChange.setTopicPrice(this.topicPrice);
		topicItemChange.setPictureSize(this.pictureSize);
		topicItemChange.setBrandId(this.brandId);
		topicItemChange.setCategoryId(this.categoryId);
		topicItemChange.setStock(this.stock);
		topicItemChange.setSalePrice(this.salePrice);
		topicItemChange.setInputSource(this.inputSource);
		topicItemChange.setOperStatus(this.operStatus);
		topicItemChange.setChangeTopicItemId(this.changeTopicItemId);
		topicItemChange.setBondedArea(this.bondedArea);
		topicItemChange.setWhType(this.whType);
		topicItemChange.setCountryId(this.countryId);
		topicItemChange.setCountryName(this.countryName);
		topicItemChange.setPurchaseMethod(this.purchaseMethod);
		topicItemChange.setItemTags(this.getItemTags());
		return topicItemChange;
	}

	public void setTopicItemChange(TopicItemChange topicItemChange) {
		this.barCode = topicItemChange.getBarCode();
		this.id = topicItemChange.getId();
		// 目前不用了
		// topicItem.setItemColor(this.itemColor);
		// topicItem.setItemSize(this.itemSize);
		this.itemId = topicItemChange.getItemId();
		this.itemSpec = topicItemChange.getItemSpec();
		this.limitAmount = topicItemChange.getLimitAmount();
		this.limitTotal = topicItemChange.getLimitTotal();
		this.saledAmount = topicItemChange.getSaledAmount();
		this.name = topicItemChange.getName().replaceAll("\"","&quot;");
		this.remark = topicItemChange.getRemark();
		this.sku = topicItemChange.getSku();
		this.spu = topicItemChange.getSpu();
		this.sortIndex = topicItemChange.getSortIndex();
		this.stockAmout = topicItemChange.getStockAmount();
		this.stockLocation = topicItemChange.getStockLocation();
		this.stockLocationId = topicItemChange.getStockLocationId();
		this.supplierId = topicItemChange.getSupplierId();
		this.supplierName = topicItemChange.getSupplierName();
		this.topicChangeId = topicItemChange.getTopicChangeId();
		this.topicImage = topicItemChange.getTopicImage();
		this.topicPrice = topicItemChange.getTopicPrice();
		this.pictureSize = topicItemChange.getPictureSize();
		this.brandId = topicItemChange.getBrandId();
		this.categoryId = topicItemChange.getCategoryId();
		this.stock = topicItemChange.getStock();
		this.salePrice = topicItemChange.getSalePrice();
		this.inputSource = topicItemChange.getInputSource();
		this.operStatus = topicItemChange.getOperStatus();
		this.changeTopicItemId = topicItemChange.getChangeTopicItemId();
		this.bondedArea = topicItemChange.getBondedArea();
		this.whType = topicItemChange.getWhType();
		this.countryId = topicItemChange.getCountryId();
		this.countryName = topicItemChange.getCountryName();
		this.purchaseMethod = topicItemChange.getPurchaseMethod();
		this.itemTags = topicItemChange.getItemTags();
	}

	public Boolean getValidate() {
		return validate;
	}

	public void setValidate(Boolean validate) {
		this.validate = validate;
	}

	public Boolean getLocked() {
		return locked;
	}

	public void setLocked(Boolean locked) {
		this.locked = locked;
	}

	public int getStockAmount() {
		return stockAmount;
	}

	public void setStockAmount(int stockAmount) {
		this.stockAmount = stockAmount;
	}

	public String getItemTags() {
		return itemTags;
	}

	public void setItemTags(String itemTags) {
		this.itemTags = itemTags;
	}

	public Integer getItemStatus() {
		return itemStatus;
	}

	public void setItemStatus(Integer itemStatus) {
		this.itemStatus = itemStatus;
	}

	public Integer getPutSign() {
		return putSign;
	}

	public void setPutSign(Integer putSign) {
		this.putSign = putSign;
	}

	public Integer getRemainStock() {
		return remainStock;
	}

	public void setRemainStock(Integer remainStock) {
		this.remainStock = remainStock;
	}
}
