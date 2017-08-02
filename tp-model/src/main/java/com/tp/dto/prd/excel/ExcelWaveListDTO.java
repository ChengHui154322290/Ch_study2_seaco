package com.tp.dto.prd.excel;

import java.io.Serializable;

import com.tp.common.annotation.excel.poi.ExcelEntity;
import com.tp.common.annotation.excel.poi.ExcelProperty;

/**
 * 海淘商品日志类
 * @author szy
 *
 */
@ExcelEntity
public class ExcelWaveListDTO extends ExcelListDTO implements Serializable{

	private static final long serialVersionUID = -3262591719189215135L;
	//海淘相关的属性
	//关税税率	*关税税率ID	国家	*国家ID	海淘配送方式	*海淘配送方式KEY	海关	*海关ID	*货号
	
	@ExcelProperty(value="行邮税号")
	private String tarrifRateName;
	
	/** 行邮税率 */
	@ExcelProperty(value="*行邮税ID")
	private String tarrifRate;
	
	
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
	@ExcelProperty(value="*产地ID")
	private String countryId;
	
	@ExcelProperty(value="海淘配送方式")
	private String sendTypeName;
	
	
	/**海淘商品配送方式***/
	@ExcelProperty(value="*海淘配送方式KEY")
	private String sendType;
	
	/** 货号 */
	@ExcelProperty(value="*货号")
	private String articleNumber;

	@ExcelProperty(value="海关")
	private String bondedAreaName;
	
	/** 海关ID */
	@ExcelProperty(value="*海关ID")
	private String bondedArea;
	
	@ExcelProperty(value="*海关与货号")
	private String bondedAreaAndArticleNumber;

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
	public String getCountryId() {
		return countryId;
	}

	/**
	 * @param countryId the countryId to set
	 */
	public void setCountryId(String countryId) {
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
	public String getTarrifRate() {
		return tarrifRate;
	}

	/**
	 * @param tarrifRate the tarrifRate to set
	 */
	public void setTarrifRate(String tarrifRate) {
		this.tarrifRate = tarrifRate;
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

	public String getCustomsRateName() {
		return customsRateName;
	}

	public void setCustomsRateName(String customsRateName) {
		this.customsRateName = customsRateName;
	}


	public String getExciseRateName() {
		return exciseRateName;
	}

	public void setExciseRateName(String exciseRateName) {
		this.exciseRateName = exciseRateName;
	}


	public String getAddedvalueRateName() {
		return addedvalueRateName;
	}

	public void setAddedvalueRateName(String addedvalueRateName) {
		this.addedvalueRateName = addedvalueRateName;
	}

	public String getCustomsRateId() {
		return customsRateId;
	}

	public void setCustomsRateId(String customsRateId) {
		this.customsRateId = customsRateId;
	}

	public String getExciseRateId() {
		return exciseRateId;
	}

	public void setExciseRateId(String exciseRateId) {
		this.exciseRateId = exciseRateId;
	}

	public String getAddedvalueRateId() {
		return addedvalueRateId;
	}

	public void setAddedvalueRateId(String addedvalueRateId) {
		this.addedvalueRateId = addedvalueRateId;
	}

	
}
