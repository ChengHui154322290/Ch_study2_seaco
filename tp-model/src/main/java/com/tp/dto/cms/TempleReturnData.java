/**
 * 
 */
package com.tp.dto.cms;

import java.io.Serializable;
import java.util.Date;

/**
 * 首页促销专题模板的dto实体类
 * 
 * 
 */
public class TempleReturnData implements Serializable {

	private static final long serialVersionUID = -1620952831373453144L;

	/**活动图片**/
	private String img;
	
	/**APP活动图片**/
	private String mobileImage;
	
	/**商品图片：用于单品团的商品图片**/
	private String goodimg;
	
	/**商品名称**/
	private String name;
	
	/**活动类型**/
	private String type;
	
	/**活动卖点：需要改成point**/
	private String description;
	
	/**活动备注**/
	private String remark;
	
	/**活动描述(PC)**/
	private String intro;
	
	/**活动描述(APP)**/
	private String introMobile;
	
	/**活动价**/
	private double price;
	
	private String buy;
	
	/**链接**/
	private String link;
	
	/**原价格**/
	private double olderprice;
	
	/**位置**/
	private Integer positionSort;
	
	/**宽度**/
	private Integer positionSize;
	
	/**是否最新单品**/
	private Integer isLastSinglePro;
	
	/**倒计时**/
	private String deadline;
	
	/**倒计时，显示时,分,秒格式**/
	private String deadlineString;
	
	/**折扣**/
	private String discount;
	
	/**活动id**/
	private Long actityId;
	
	/**活动名称**/
	private String actityName;
	
	/**商品id**/
	private Long productId;
	
	/**sku编码**/
	private String sku;
	
	/**活动开始时间**/
	private Date startTime;
	
	/**活动结束时间**/
	private Date endTime;
	
	/**是否长期活动：0表示长期活动，1表示短期活动**/
	private Integer lastType;
	
	/**根据价格计算出折扣信息**/
	private String discountPrice;
	
	/********************************** 海淘特有字段  ***************************************/
	/** 品牌ID：只有是品牌团的时候才有值 **/
	private Long brandPinId;
	
	/** 品牌名称：只有是品牌团的时候才有值 **/
	private String brandPinName;
	
	/**国家**/
	private String country;
	
	/**国家图片地址**/
	private String countryImg;
	
	/** 增加角标 1是无 2是新品 3是热卖 4是特卖  */
	private Integer superscript;
	
	/***********************************************************************************/
	
	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBuy() {
		return buy;
	}

	public void setBuy(String buy) {
		this.buy = buy;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public double getOlderprice() {
		return olderprice;
	}

	public void setOlderprice(double olderprice) {
		this.olderprice = olderprice;
	}

	public Integer getPositionSort() {
		return positionSort;
	}

	public void setPositionSort(Integer positionSort) {
		this.positionSort = positionSort;
	}

	public Integer getPositionSize() {
		return positionSize;
	}

	public void setPositionSize(Integer positionSize) {
		this.positionSize = positionSize;
	}

	public Integer getIsLastSinglePro() {
		return isLastSinglePro;
	}

	public void setIsLastSinglePro(Integer isLastSinglePro) {
		this.isLastSinglePro = isLastSinglePro;
	}

	public String getDeadline() {
		return deadline;
	}

	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}

	public Long getActityId() {
		return actityId;
	}

	public void setActityId(Long actityId) {
		this.actityId = actityId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
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

	public String getActityName() {
		return actityName;
	}

	public void setActityName(String actityName) {
		this.actityName = actityName;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getGoodimg() {
		return goodimg;
	}

	public void setGoodimg(String goodimg) {
		this.goodimg = goodimg;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getIntroMobile() {
		return introMobile;
	}

	public void setIntroMobile(String introMobile) {
		this.introMobile = introMobile;
	}

	public String getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(String discountPrice) {
		this.discountPrice = discountPrice;
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

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCountryImg() {
		return countryImg;
	}

	public void setCountryImg(String countryImg) {
		this.countryImg = countryImg;
	}

	public String getMobileImage() {
		return mobileImage;
	}

	public void setMobileImage(String mobileImage) {
		this.mobileImage = mobileImage;
	}

	public Integer getSuperscript() {
		return superscript;
	}

	public void setSuperscript(Integer superscript) {
		this.superscript = superscript;
	}

	
}
