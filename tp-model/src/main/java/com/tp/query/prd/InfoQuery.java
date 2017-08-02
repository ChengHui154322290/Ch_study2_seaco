package com.tp.query.prd;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * <pre>
 * 	 info 修改类
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 */
public class InfoQuery implements Serializable {
	
	private static final long serialVersionUID = 4379284053018869035L;

	/***info id**/
	private Long infoId;
	
	/***spu名称**/
	private String mainTitle;
	
	/***备注**/
	private String remark;
	
	/** 关联的品牌 */
	private Long brandId;

	/** 大类ID */
	private Long largeId;

	/** 中类ID */
	private Long mediumId;

	/** 小类ID */
	private Long smallId;

	/** 单位 */
	private Long unitId;
	
	/** 修改时间 */
	private Date modifyTime;

	/** 修改人 */
	private Long modifyUserId;

	/**
	 * Getter method for property <tt>infoId</tt>.
	 * 
	 * @return property value of infoId
	 */
	public Long getInfoId() {
		return infoId;
	}

	/**
	 * Setter method for property <tt>infoId</tt>.
	 * 
	 * @param infoId value to be assigned to property infoId
	 */
	public void setInfoId(Long infoId) {
		this.infoId = infoId;
	}

	/**
	 * Getter method for property <tt>mainTitle</tt>.
	 * 
	 * @return property value of mainTitle
	 */
	public String getMainTitle() {
		return mainTitle;
	}

	/**
	 * Setter method for property <tt>mainTitle</tt>.
	 * 
	 * @param mainTitle value to be assigned to property mainTitle
	 */
	public void setMainTitle(String mainTitle) {
		this.mainTitle = mainTitle;
	}

	/**
	 * Getter method for property <tt>remark</tt>.
	 * 
	 * @return property value of remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * Setter method for property <tt>remark</tt>.
	 * 
	 * @param remark value to be assigned to property remark
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @return the brandId
	 */
	public Long getBrandId() {
		return brandId;
	}

	/**
	 * @param brandId the brandId to set
	 */
	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	/**
	 * @return the largeId
	 */
	public Long getLargeId() {
		return largeId;
	}

	/**
	 * @param largeId the largeId to set
	 */
	public void setLargeId(Long largeId) {
		this.largeId = largeId;
	}

	/**
	 * @return the mediumId
	 */
	public Long getMediumId() {
		return mediumId;
	}

	/**
	 * @param mediumId the mediumId to set
	 */
	public void setMediumId(Long mediumId) {
		this.mediumId = mediumId;
	}

	/**
	 * @return the smallId
	 */
	public Long getSmallId() {
		return smallId;
	}

	/**
	 * @param smallId the smallId to set
	 */
	public void setSmallId(Long smallId) {
		this.smallId = smallId;
	}

	/**
	 * @return the unitId
	 */
	public Long getUnitId() {
		return unitId;
	}

	/**
	 * @param unitId the unitId to set
	 */
	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}

	/**
	 * @return the modifyTime
	 */
	public Date getModifyTime() {
		return modifyTime;
	}

	/**
	 * @param modifyTime the modifyTime to set
	 */
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	/**
	 * @return the modifyUserId
	 */
	public Long getModifyUserId() {
		return modifyUserId;
	}

	/**
	 * @param modifyUserId the modifyUserId to set
	 */
	public void setModifyUserId(Long modifyUserId) {
		this.modifyUserId = modifyUserId;
	}
	
}
