package com.tp.dto.cms.temple;

import java.io.Serializable;

/**
 * 今日必海淘
 * @author szy
 * 2015-1-9
 */
public class Hoard  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5292228101810576856L;

	private String srclink;
	
	private String imgsrc;
	
	private String namelink;
	
	/** 活动名称,由原来商品名称全部改成活动名称 **/
	private String name;
	
	private String money;
	
	/** 当前价格 **/
	private Double nowValue;
	
	/** 原价格 **/
	private Double lastValue;
	
	private String nowHoard;
	
	private String seelink;
	
	private String see;
	
	private String pictureSize;
	
	private String deadline;
	
	/**倒计时，显示时,分,秒格式**/
	private String deadlineString;
	
	private Integer isLastSinglePro;
	
	/**商品sku**/
	private String sku;
	
	/**活动id**/
	private Long activityId;
	
	/**活动名称**/
	private String activityName;
	
	/**品牌id**/
	private Long brandId;
	
	/**品牌名称**/
	private String brandName;
	
	/**是否长期活动：0表示长期活动，1表示短期活动**/
	private Integer lastType;
	
	/** 卖点描述 **/
	private String description;
	
	/**根据价格计算出折扣信息**/
	private String discountPrice;

	public String getSrclink() {
		return srclink;
	}

	public void setSrclink(String srclink) {
		this.srclink = srclink;
	}

	public String getImgsrc() {
		return imgsrc;
	}

	public void setImgsrc(String imgsrc) {
		this.imgsrc = imgsrc;
	}

	public String getNamelink() {
		return namelink;
	}

	public void setNamelink(String namelink) {
		this.namelink = namelink;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getNowValue() {
		return nowValue;
	}

	public void setNowValue(Double nowValue) {
		this.nowValue = nowValue;
	}

	public Double getLastValue() {
		return lastValue;
	}

	public void setLastValue(Double lastValue) {
		this.lastValue = lastValue;
	}

	public String getNowHoard() {
		return nowHoard;
	}

	public void setNowHoard(String nowHoard) {
		this.nowHoard = nowHoard;
	}

	public String getSeelink() {
		return seelink;
	}

	public void setSeelink(String seelink) {
		this.seelink = seelink;
	}

	public String getSee() {
		return see;
	}

	public void setSee(String see) {
		this.see = see;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getPictureSize() {
		return pictureSize;
	}

	public void setPictureSize(String pictureSize) {
		this.pictureSize = pictureSize;
	}

	public String getDeadline() {
		return deadline;
	}

	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}

	public Integer getIsLastSinglePro() {
		return isLastSinglePro;
	}

	public void setIsLastSinglePro(Integer isLastSinglePro) {
		this.isLastSinglePro = isLastSinglePro;
	}

	public Long getActivityId() {
		return activityId;
	}

	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public Integer getLastType() {
		return lastType;
	}

	public void setLastType(Integer lastType) {
		this.lastType = lastType;
	}

	public String getDeadlineString() {
		return deadlineString;
	}

	public void setDeadlineString(String deadlineString) {
		this.deadlineString = deadlineString;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(String discountPrice) {
		this.discountPrice = discountPrice;
	}


}
