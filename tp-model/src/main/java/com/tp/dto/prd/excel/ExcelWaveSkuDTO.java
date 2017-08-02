package com.tp.dto.prd.excel;

import java.io.Serializable;
import java.util.List;

import com.tp.common.annotation.excel.poi.ExcelEntity;
import com.tp.common.annotation.excel.poi.ExcelProperty;
import com.tp.model.prd.ItemSkuArt;

/**
 * 
 * 海淘的sku类
 * @author szy
 *
 */
@ExcelEntity
public class ExcelWaveSkuDTO extends ExcelSkuDTO implements Serializable{

	private static final long serialVersionUID = -4695341793770231591L;
	
	
	//产地	*产地ID	行邮税号	*行邮税ID

	
	@ExcelProperty(value="行邮税号")
	private String tarrifRateName;
	
	/** 关税税率 */
	@ExcelProperty(value="*行邮税ID",required=true)
	private Long tarrifRate;
	
	@ExcelProperty(value="关税号")
	private String customsRateName;
	@ExcelProperty(value="*关税ID")
	private String customsRateId;
	
	@ExcelProperty(value="消费税")
	private String exciseRateName;
	@ExcelProperty(value="*消费税ID")
	private String exciseRateId;
	
	@ExcelProperty(value="增值税")
	private String addedvalueRateName;
	@ExcelProperty(value="*增值税ID")
	private String addedvalueRateId;
	

	@ExcelProperty(value="产地")
	private String countryName;

	/***海淘商品产地（国家ID）**/
	@ExcelProperty(value="*产地ID",required=true)
	private Long countryId;
	
	@ExcelProperty(value="海淘配送方式")
	private String sendTypeName;
	
	
	/**海淘商品配送方式***/
	@ExcelProperty(value="*海淘配送方式KEY")
	private String sendType;
	
	/** 货号 */
	//@ExcelProperty(value="*货号",required=true)
	private String articleNumber;

	//@ExcelProperty(value="海关")
	private String bondedAreaName;
	
	/** 海关ID */
	//@ExcelProperty(value="*海关ID",required=true)
	private String bondedArea;

	
	@ExcelProperty(value="海关与货号")
	private String bondedAreaAndArticleNumber;
	
	
	/**商品海关信息*/
	private List<ItemSkuArt> skuArtList;
	/**
	 * @return the tarrifRateName
	 */
	public String getTarrifRateName() {
		return tarrifRateName;
	}

	/**
	 * @param tarrifRateName the tarrifRateName to set
	 */
	public void setTarrifRateName(String tarrifRateName) {
		this.tarrifRateName = tarrifRateName;
	}

	/**
	 * @return the tarrifRate
	 */
	public Long getTarrifRate() {
		return tarrifRate;
	}

	/**
	 * @param tarrifRate the tarrifRate to set
	 */
	public void setTarrifRate(Long tarrifRate) {
		this.tarrifRate = tarrifRate;
	}

	/**
	 * @return the countryName
	 */
	public String getCountryName() {
		return countryName;
	}

	/**
	 * @param countryName the countryName to set
	 */
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	/**
	 * @return the countryId
	 */
	public Long getCountryId() {
		return countryId;
	}

	/**
	 * @param countryId the countryId to set
	 */
	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}

	/**
	 * @return the sendTypeName
	 */
	public String getSendTypeName() {
		return sendTypeName;
	}

	/**
	 * @param sendTypeName the sendTypeName to set
	 */
	public void setSendTypeName(String sendTypeName) {
		this.sendTypeName = sendTypeName;
	}

	/**
	 * @return the sendType
	 */
	public String getSendType() {
		return sendType;
	}

	/**
	 * @param sendType the sendType to set
	 */
	public void setSendType(String sendType) {
		this.sendType = sendType;
	}

	/**
	 * @return the articleNumber
	 */
	public String getArticleNumber() {
		return articleNumber;
	}

	/**
	 * @param articleNumber the articleNumber to set
	 */
	public void setArticleNumber(String articleNumber) {
		this.articleNumber = articleNumber;
	}

	/**
	 * @return the bondedAreaName
	 */
	public String getBondedAreaName() {
		return bondedAreaName;
	}

	/**
	 * @param bondedAreaName the bondedAreaName to set
	 */
	public void setBondedAreaName(String bondedAreaName) {
		this.bondedAreaName = bondedAreaName;
	}

	/**
	 * @return the bondedArea
	 */
	public String getBondedArea() {
		return bondedArea;
	}

	/**
	 * @param bondedArea the bondedArea to set
	 */
	public void setBondedArea(String bondedArea) {
		this.bondedArea = bondedArea;
	}
	

	/**
	 * @return the bondedAreaAndArticleNumber
	 */
	public String getBondedAreaAndArticleNumber() {
		return bondedAreaAndArticleNumber;
	}

	/**
	 * @param bondedAreaAndArticleNumber the bondedAreaAndArticleNumber to set
	 */
	public void setBondedAreaAndArticleNumber(String bondedAreaAndArticleNumber) {
		this.bondedAreaAndArticleNumber = bondedAreaAndArticleNumber;
	}

	/**
	 * @return the skuArtList
	 * @throws Exception 
	 */
	public List<ItemSkuArt> getSkuArtList() throws Exception {
		return skuArtList;
	}

	/**
	 * @param skuArtList the skuArtList to set
	 */
	public void setSkuArtList(List<ItemSkuArt> skuArtList) {
		this.skuArtList = skuArtList;
	}

	public String getCustomsRateName() {
		return customsRateName;
	}

	public void setCustomsRateName(String customsRateName) {
		this.customsRateName = customsRateName;
	}

	public String getCustomsRateId() {
		return customsRateId;
	}

	public void setCustomsRateId(String customsRateId) {
		this.customsRateId = customsRateId;
	}

	public String getExciseRateName() {
		return exciseRateName;
	}

	public void setExciseRateName(String exciseRateName) {
		this.exciseRateName = exciseRateName;
	}

	public String getExciseRateId() {
		return exciseRateId;
	}

	public void setExciseRateId(String exciseRateId) {
		this.exciseRateId = exciseRateId;
	}

	public String getAddedvalueRateName() {
		return addedvalueRateName;
	}

	public void setAddedvalueRateName(String addedvalueRateName) {
		this.addedvalueRateName = addedvalueRateName;
	}

	public String getAddedvalueRateId() {
		return addedvalueRateId;
	}

	public void setAddedvalueRateId(String addedvalueRateId) {
		this.addedvalueRateId = addedvalueRateId;
	}
}
