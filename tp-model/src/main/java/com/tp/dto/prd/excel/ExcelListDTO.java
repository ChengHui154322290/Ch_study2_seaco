package com.tp.dto.prd.excel;

import java.io.Serializable;

import com.tp.common.annotation.excel.poi.ExcelEntity;
import com.tp.common.annotation.excel.poi.ExcelProperty;

/**
 * 商品导入明细
 * 
 * @author szy
 */
@ExcelEntity
public class ExcelListDTO extends ExcelBaseDTO  implements Serializable {

	private static final long serialVersionUID = 5350689447621545702L;
	
	/** 主键 */
	private Long id;

	/** item_import_log的主键 */
	private Long logId;

	/** 商家名称 */
	@ExcelProperty(value="商家名称",columnLength =100 )
	private String spName;

	/** *商家ID */
	@ExcelProperty(value="*商家ID",columnLength =50 )
	private String spId;

	/** *条形码（商品码） */
	@ExcelProperty(value="*条形码",columnLength =50)
	private String barcode;

	/** *SPU名称 */
	@ExcelProperty(value="*款号",columnLength =255)
	private String mainTitle;

	/** 大类名称 */
	@ExcelProperty(value="大类名称",columnLength =100)
	private String largeName;

	/** 中类名称 */
	@ExcelProperty(value="中类名称 ",columnLength =100)
	private String mediumName;

	/** 小类名称 */
	@ExcelProperty(value="小类名称",columnLength =100)
	private String smallName;

	/** *大类ID */
	@ExcelProperty(value="*大类ID",columnLength =50)
	private String largeId;

	/** *中类ID */
	@ExcelProperty(value="*中类ID",columnLength =50)
	private String mediumId;

	/** *小类ID */
	@ExcelProperty(value="*小类ID",columnLength =50)
	private String smallId;

	/** 单位 */
	@ExcelProperty(value="单位名称",columnLength =100)
	private String unitName;

	/** 单位ID */
	@ExcelProperty(value="*单位ID",columnLength =50)
	private String unitId;

	/** 品牌 */
	@ExcelProperty(value="品牌名称",columnLength =100)
	private String brandName;

	/** *品牌ID */
	@ExcelProperty(value="*品牌ID",columnLength =50)
	private String brandId;
	



	/** *SKU名称 */
	@ExcelProperty(value="*SKU名称",columnLength =255)
	private String detailMainTitle;

	/** 销售维度1 */
	@ExcelProperty(value="*规格1名称",columnLength =100)
	private String spec1Name;

	/** 销售维度1ID */
	@ExcelProperty(value=" *规格1ID",columnLength =50)
	private String spec1Id;

	/** 销售维度2 */
	@ExcelProperty(value="*规格2名称",columnLength =100)
	private String spec2Name;

	/** 销售维度2ID */
	@ExcelProperty(value=" *规格2ID",columnLength =50)
	private String spec2Id;

	/** 销售维度3 */
	@ExcelProperty(value="*规格3名称",columnLength =100)
	private String spec3Name;

	/** 销售维度3ID */
	@ExcelProperty(value=" *规格3ID",columnLength =50)
	private String spec3Id;
	
	/** *商品卖点 */
	@ExcelProperty(value="*副标题",columnLength=500)
	private String subTitle;

	/** *商品类型 */
	@ExcelProperty(value="*商品类型",columnLength=11)
	private String itemType;

	/** *规格 */
	@ExcelProperty(value="商品规格",columnLength=50)
	private String specifications;

	/** *无理由退货期限 */
	@ExcelProperty(value="*无理由退货期限（天）",columnLength=500)
	private String returnDays;

	/** *吊牌价 */
	@ExcelProperty(value="*市场价（元）",columnLength=50)
	private String basicPrice;

	/** *运费 */
	@ExcelProperty(value="运费名称",columnLength=100)
	private String freightTemplateName;

	/** *运费模板ID */
	@ExcelProperty(value="*运费ID",columnLength=50)
	private String freightTemplateId;

	/** *毛重 */
	@ExcelProperty(value="*毛重（g）",columnLength=50)
	private String weight;

	/** 生产厂家 */
	@ExcelProperty(value="生产厂家",columnLength=255)
	private String manufacturer;

	/** *箱规 */
	@ExcelProperty(value="箱规",columnLength=255)
	private String cartonSpec;

	/** 是否有效期管理 */
	@ExcelProperty(value="*是否有效期管理",columnLength=11)
	private String expSign;

	/** 有效期 */
	@ExcelProperty(value="有效期（月）",columnLength=50)
	private String expDays;

	/** 自定义属性组 */
	@ExcelProperty(value="自定义属性组")
	private String attibuteCus;

	/** 导入的excel行号 */
	private Long excelIndex;

	/** 状态:1-为导入成功，2-为导入失败，默认1 */
	private Integer status;

	/** 导入操作信息 */
	private String opMessage;

	/** 适用年龄 */
	@ExcelProperty(value="适用年龄",columnLength=100)
	private String applyAge;
	
	/** 适用年龄 */
	@ExcelProperty(value="*适用年龄ID",columnLength=50)
	private String applyAgeId;

	/**
	 * 设置 主键
	 * 
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 设置 item_import_log的主键
	 * 
	 * @param logId
	 */
	public void setLogId(Long logId) {
		this.logId = logId;
	}

	/**
	 * 设置 商家名称
	 * 
	 * @param spName
	 */
	public void setSpName(String spName) {
		this.spName = spName;
	}

	/**
	 * 设置 *商家ID
	 * 
	 * @param spId
	 */
	public void setSpId(String spId) {
		this.spId = spId;
	}

	/**
	 * 设置 *条形码（商品码）
	 * 
	 * @param barcode
	 */
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	/**
	 * 设置 *SPU名称
	 * 
	 * @param mainTitle
	 */
	public void setMainTitle(String mainTitle) {
		this.mainTitle = mainTitle;
	}

	/**
	 * 设置 大类名称
	 * 
	 * @param largeName
	 */
	public void setLargeName(String largeName) {
		this.largeName = largeName;
	}

	/**
	 * 设置 中类名称
	 * 
	 * @param mediumName
	 */
	public void setMediumName(String mediumName) {
		this.mediumName = mediumName;
	}

	/**
	 * 设置 小类名称
	 * 
	 * @param smallName
	 */
	public void setSmallName(String smallName) {
		this.smallName = smallName;
	}

	/**
	 * 设置 *大类ID
	 * 
	 * @param largeId
	 */
	public void setLargeId(String largeId) {
		this.largeId = largeId;
	}

	/**
	 * 设置 *中类ID
	 * 
	 * @param mediumId
	 */
	public void setMediumId(String mediumId) {
		this.mediumId = mediumId;
	}

	/**
	 * 设置 *小类ID
	 * 
	 * @param smallId
	 */
	public void setSmallId(String smallId) {
		this.smallId = smallId;
	}

	/**
	 * 设置 单位
	 * 
	 * @param unitName
	 */
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	/**
	 * 设置 单位ID
	 * 
	 * @param unitId
	 */
	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	/**
	 * 设置 品牌
	 * 
	 * @param brandName
	 */
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	/**
	 * 设置 *品牌ID
	 * 
	 * @param brandId
	 */
	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}

	/**
	 * 设置 *SKU名称
	 * 
	 * @param detailMainTitle
	 */
	public void setDetailMainTitle(String detailMainTitle) {
		this.detailMainTitle = detailMainTitle;
	}

	/**
	 * 设置 销售维度1
	 * 
	 * @param spec1Name
	 */
	public void setSpec1Name(String spec1Name) {
		this.spec1Name = spec1Name;
	}

	/**
	 * 设置 销售维度1ID
	 * 
	 * @param spec1Id
	 */
	public void setSpec1Id(String spec1Id) {
		this.spec1Id = spec1Id;
	}

	/**
	 * 设置 销售维度2
	 * 
	 * @param spec2Name
	 */
	public void setSpec2Name(String spec2Name) {
		this.spec2Name = spec2Name;
	}

	/**
	 * 设置 销售维度2ID
	 * 
	 * @param spec2Id
	 */
	public void setSpec2Id(String spec2Id) {
		this.spec2Id = spec2Id;
	}

	/**
	 * 设置 销售维度3
	 * 
	 * @param spec3Name
	 */
	public void setSpec3Name(String spec3Name) {
		this.spec3Name = spec3Name;
	}

	/**
	 * 设置 销售维度3ID
	 * 
	 * @param spec3Id
	 */
	public void setSpec3Id(String spec3Id) {
		this.spec3Id = spec3Id;
	}

	/**
	 * 设置 *商品卖点
	 * 
	 * @param subTitle
	 */
	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	/**
	 * 设置 *商品类型
	 * 
	 * @param itemType
	 */
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	/**
	 * 设置 *规格
	 * 
	 * @param specifications
	 */
	public void setSpecifications(String specifications) {
		this.specifications = specifications;
	}

	/**
	 * 设置 *无理由退货期限
	 * 
	 * @param returnDays
	 */
	public void setReturnDays(String returnDays) {
		this.returnDays = returnDays;
	}

	/**
	 * 设置 *吊牌价
	 * 
	 * @param basicPrice
	 */
	public void setBasicPrice(String basicPrice) {
		this.basicPrice = basicPrice;
	}

	/**
	 * 设置 *运费
	 * 
	 * @param freightTemplateName
	 */
	public void setFreightTemplateName(String freightTemplateName) {
		this.freightTemplateName = freightTemplateName;
	}

	/**
	 * 设置 *运费模板ID
	 * 
	 * @param freightTemplateId
	 */
	public void setFreightTemplateId(String freightTemplateId) {
		this.freightTemplateId = freightTemplateId;
	}

	/**
	 * 设置 *毛重
	 * 
	 * @param weight
	 */
	public void setWeight(String weight) {
		this.weight = weight;
	}

	/**
	 * 设置 生产厂家
	 * 
	 * @param manufacturer
	 */
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	/**
	 * 设置 *箱规
	 * 
	 * @param cartonSpec
	 */
	public void setCartonSpec(String cartonSpec) {
		this.cartonSpec = cartonSpec;
	}

	/**
	 * 设置 是否有效期管理
	 * 
	 * @param expSign
	 */
	public void setExpSign(String expSign) {
		this.expSign = expSign;
	}

	/**
	 * 设置 有效期
	 * 
	 * @param expDays
	 */
	public void setExpDays(String expDays) {
		this.expDays = expDays;
	}

	

	/**
	 * 设置 自定义属性组
	 * 
	 * @param attibuteCus
	 */
	public void setAttibuteCus(String attibuteCus) {
		this.attibuteCus = attibuteCus;
	}

	/**
	 * 设置 导入的excel行号
	 * 
	 * @param excelIndex
	 */
	public void setExcelIndex(Long excelIndex) {
		this.excelIndex = excelIndex;
	}

	/**
	 * 设置 状态:1-为导入成功，2-为导入失败，默认1
	 * 
	 * @param status
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * 设置 导入操作信息
	 * 
	 * @param opMessage
	 */
	public void setOpMessage(String opMessage) {
		this.opMessage = opMessage;
	}

	/**
	 * 获取 主键
	 * 
	 * @return id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * 获取 item_import_log的主键
	 * 
	 * @return logId
	 */
	public Long getLogId() {
		return logId;
	}

	/**
	 * 获取 商家名称
	 * 
	 * @return spName
	 */
	public String getSpName() {
		return spName;
	}

	/**
	 * 获取 *商家ID
	 * 
	 * @return spId
	 */
	public String getSpId() {
		return spId;
	}

	/**
	 * 获取 *条形码（商品码）
	 * 
	 * @return barcode
	 */
	public String getBarcode() {
		return barcode;
	}

	/**
	 * 获取 *SPU名称
	 * 
	 * @return mainTitle
	 */
	public String getMainTitle() {
		return mainTitle;
	}

	/**
	 * 获取 大类名称
	 * 
	 * @return largeName
	 */
	public String getLargeName() {
		return largeName;
	}

	/**
	 * 获取 中类名称
	 * 
	 * @return mediumName
	 */
	public String getMediumName() {
		return mediumName;
	}

	/**
	 * 获取 小类名称
	 * 
	 * @return smallName
	 */
	public String getSmallName() {
		return smallName;
	}

	/**
	 * 获取 *大类ID
	 * 
	 * @return largeId
	 */
	public String getLargeId() {
		return largeId;
	}

	/**
	 * 获取 *中类ID
	 * 
	 * @return mediumId
	 */
	public String getMediumId() {
		return mediumId;
	}

	/**
	 * 获取 *小类ID
	 * 
	 * @return smallId
	 */
	public String getSmallId() {
		return smallId;
	}

	/**
	 * 获取 单位
	 * 
	 * @return unitName
	 */
	public String getUnitName() {
		return unitName;
	}

	/**
	 * 获取 单位ID
	 * 
	 * @return unitId
	 */
	public String getUnitId() {
		return unitId;
	}

	/**
	 * 获取 品牌
	 * 
	 * @return brandName
	 */
	public String getBrandName() {
		return brandName;
	}

	/**
	 * 获取 *品牌ID
	 * 
	 * @return brandId
	 */
	public String getBrandId() {
		return brandId;
	}

	/**
	 * 获取 *SKU名称
	 * 
	 * @return detailMainTitle
	 */
	public String getDetailMainTitle() {
		return detailMainTitle;
	}

	/**
	 * 获取 销售维度1
	 * 
	 * @return spec1Name
	 */
	public String getSpec1Name() {
		return spec1Name;
	}

	/**
	 * 获取 销售维度1ID
	 * 
	 * @return spec1Id
	 */
	public String getSpec1Id() {
		return spec1Id;
	}

	/**
	 * 获取 销售维度2
	 * 
	 * @return spec2Name
	 */
	public String getSpec2Name() {
		return spec2Name;
	}

	/**
	 * 获取 销售维度2ID
	 * 
	 * @return spec2Id
	 */
	public String getSpec2Id() {
		return spec2Id;
	}

	/**
	 * 获取 销售维度3
	 * 
	 * @return spec3Name
	 */
	public String getSpec3Name() {
		return spec3Name;
	}

	/**
	 * 获取 销售维度3ID
	 * 
	 * @return spec3Id
	 */
	public String getSpec3Id() {
		return spec3Id;
	}

	/**
	 * 获取 *商品卖点
	 * 
	 * @return subTitle
	 */
	public String getSubTitle() {
		return subTitle;
	}

	/**
	 * 获取 *商品类型
	 * 
	 * @return itemType
	 */
	public String getItemType() {
		return itemType;
	}

	/**
	 * 获取 *规格
	 * 
	 * @return specifications
	 */
	public String getSpecifications() {
		return specifications;
	}

	/**
	 * 获取 *无理由退货期限
	 * 
	 * @return returnDays
	 */
	public String getReturnDays() {
		return returnDays;
	}

	/**
	 * 获取 *吊牌价
	 * 
	 * @return basicPrice
	 */
	public String getBasicPrice() {
		return basicPrice;
	}

	/**
	 * 获取 *运费
	 * 
	 * @return freightTemplateName
	 */
	public String getFreightTemplateName() {
		return freightTemplateName;
	}

	/**
	 * 获取 *运费模板ID
	 * 
	 * @return freightTemplateId
	 */
	public String getFreightTemplateId() {
		return freightTemplateId;
	}

	/**
	 * 获取 *毛重
	 * 
	 * @return weight
	 */
	public String getWeight() {
		return weight;
	}

	/**
	 * 获取 生产厂家
	 * 
	 * @return manufacturer
	 */
	public String getManufacturer() {
		return manufacturer;
	}

	/**
	 * 获取 *箱规
	 * 
	 * @return cartonSpec
	 */
	public String getCartonSpec() {
		return cartonSpec;
	}

	/**
	 * 获取 是否有效期管理
	 * 
	 * @return expSign
	 */
	public String getExpSign() {
		return expSign;
	}

	/**
	 * 获取 有效期
	 * 
	 * @return expDays
	 */
	public String getExpDays() {
		return expDays;
	}

	

	/**
	 * 获取 自定义属性组
	 * 
	 * @return attibuteCus
	 */
	public String getAttibuteCus() {
		return attibuteCus;
	}

	/**
	 * 获取 导入的excel行号
	 * 
	 * @return excelIndex
	 */
	public Long getExcelIndex() {
		return excelIndex;
	}

	/**
	 * 获取 状态:1-为导入成功，2-为导入失败，默认1
	 * 
	 * @return status
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * 获取 导入操作信息
	 * 
	 * @return opMessage
	 */
	public String getOpMessage() {
		return opMessage;
	}
	
	

	/**
	 * @return the applyAge
	 */
	public String getApplyAge() {
		return applyAge;
	}

	/**
	 * @param applyAge the applyAge to set
	 */
	public void setApplyAge(String applyAge) {
		this.applyAge = applyAge;
	}

	/**
	 * @return the applyAgeId
	 */
	public String getApplyAgeId() {
		return applyAgeId;
	}

	/**
	 * @param applyAgeId the applyAgeId to set
	 */
	public void setApplyAgeId(String applyAgeId) {
		this.applyAgeId = applyAgeId;
	}

	/** 
	 * @return
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ExcelListDomain [id=" + id + ", logId=" + logId + ", spName="
				+ spName + ", spId=" + spId + ", barcode=" + barcode
				+ ", mainTitle=" + mainTitle + ", largeName=" + largeName
				+ ", mediumName=" + mediumName + ", smallName=" + smallName
				+ ", largeId=" + largeId + ", mediumId=" + mediumId
				+ ", smallId=" + smallId + ", unitName=" + unitName
				+ ", unitId=" + unitId + ", brandName=" + brandName
				+ ", brandId=" + brandId + ", detailMainTitle="
				+ detailMainTitle + ", spec1Name=" + spec1Name + ", spec1Id="
				+ spec1Id + ", spec2Name=" + spec2Name + ", spec2Id=" + spec2Id
				+ ", spec3Name=" + spec3Name + ", spec3Id=" + spec3Id
				+ ", subTitle=" + subTitle + ", itemType=" + itemType
				+ ", specifications=" + specifications + ", returnDays="
				+ returnDays + ", basicPrice=" + basicPrice
				+ ", freightTemplateName=" + freightTemplateName
				+ ", freightTemplateId=" + freightTemplateId + ", weight="
				+ weight + ", manufacturer=" + manufacturer + ", cartonSpec="
				+ cartonSpec + ", expSign=" + expSign + ", expDays=" + expDays
				+ ", attibuteCus=" + attibuteCus
				+ ", excelIndex=" + excelIndex + ", status=" + status
				+ ", opMessage=" + opMessage + "]";
	}

}