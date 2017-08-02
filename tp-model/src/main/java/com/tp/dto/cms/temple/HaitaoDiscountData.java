package com.tp.dto.cms.temple;

import java.io.Serializable;
import java.util.Date;

/**
 * 前面今日特卖模板
 * 2015-1-7
 */
public class HaitaoDiscountData  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 图片 **/
	private String leftImage;
	
	private String rightImage;
	
	/** 活动名称 **/
	private String brand;
	
	/** 活动折扣 **/
	private String rate;
	
	/** 倒计时 **/
	private String deadline;
	
	/**倒计时，显示时,分,秒格式**/
	private String deadlineString;
	
	/** 链接 **/
	private String link;
	
	/** 活动ID **/
	private Long brandId;
	
	/** 品牌ID：只有是品牌团的时候才有值 **/
	private Long brandPinId;
	
	/** 品牌名称：只有是品牌团的时候才有值 **/
	private String brandPinName;
	
	/** 开始时间 **/
	private Date startTime;
	
	/** 结束时间 **/
	private Date endTime;
	
	/**是否长期活动：0表示长期活动，1表示短期活动**/
	private Integer lastType;
	
	/**国家**/
	private String country;

	public String getLeftImage() {
		return leftImage;
	}

	public void setLeftImage(String leftImage) {
		this.leftImage = leftImage;
	}

	public String getRightImage() {
		return rightImage;
	}

	public void setRightImage(String rightImage) {
		this.rightImage = rightImage;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public String getDeadline() {
		return deadline;
	}

	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
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

	public Integer getLastType() {
		return lastType;
	}

	public void setLastType(Integer lastType) {
		this.lastType = lastType;
	}

	public Long getBrandPinId() {
		return brandPinId;
	}

	public void setBrandPinId(Long brandPinId) {
		this.brandPinId = brandPinId;
	}

	public String getBrandPinName() {
		return brandPinName;
	}

	public void setBrandPinName(String brandPinName) {
		this.brandPinName = brandPinName;
	}

	public String getDeadlineString() {
		return deadlineString;
	}

	public void setDeadlineString(String deadlineString) {
		this.deadlineString = deadlineString;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}


	
	
}
