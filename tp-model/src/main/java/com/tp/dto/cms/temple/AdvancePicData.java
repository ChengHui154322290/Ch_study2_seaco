package com.tp.dto.cms.temple;

import java.io.Serializable;
import java.util.Date;

/**
 * 明日预告数据
 * @author szy
 * 2015-1-7
 */
public class AdvancePicData  implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 2157590083883944310L;

	/** 图片链接 **/
	private String link;
	
	/** 图片地址 **/
	private String imageSrc;
	
	/** APP图片:切换后image,新 **/
	private String mobileImage;
	
	/** 名称 **/
	private String name;
	
	/** 专场id **/
	private Long activityId;
	
	/** 专场开始时间 **/
	private Date startdate;
	
	/** 专场结束时间 **/
	private Date enddate;
	
	/** 折扣 **/
	private String rate;
	
	/** 是否已关注:0表示已关注，1标识未关注 **/
	private boolean attentionStatus;
	
	/**是否长期活动：0表示长期活动，1表示短期活动**/
	private Integer lastType;
	
	/** sku **/
	private String skuCode;

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getImageSrc() {
		return imageSrc;
	}

	public void setImageSrc(String imageSrc) {
		this.imageSrc = imageSrc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getActivityId() {
		return activityId;
	}

	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}

	public Date getStartdate() {
		return startdate;
	}

	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}

	public Date getEnddate() {
		return enddate;
	}

	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public boolean getAttentionStatus() {
		return attentionStatus;
	}

	public void setAttentionStatus(boolean attentionStatus) {
		this.attentionStatus = attentionStatus;
	}

	public Integer getLastType() {
		return lastType;
	}

	public void setLastType(Integer lastType) {
		this.lastType = lastType;
	}

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public String getMobileImage() {
		return mobileImage;
	}

	public void setMobileImage(String mobileImage) {
		this.mobileImage = mobileImage;
	}
	
	
	
}
