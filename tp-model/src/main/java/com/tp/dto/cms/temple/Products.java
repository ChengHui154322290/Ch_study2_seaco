package com.tp.dto.cms.temple;

import java.io.Serializable;
import java.util.Date;

/**
 * 品牌下产品
 * @author szy
 * 2015-1-9
 */
public class Products  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5292228101810576856L;

	private String srclink;
	
	private String imgsrc;
	
	private String namelink;
	
	private String name;
	
	private String money;
	
	private Double nowValue;
	
	private Double lastValue;
	
	private String nowHoard;
	
	private String seelink;
	
	private String see;
	
	private String type; //ruball 已抢光  over已下架 outof暂时缺货  chance还有机会  normal 正常  noStart 未开售 editing 编辑中
	
	/**sku**/
	private String sku;
	
	/**活动id**/
	private String topicid;
	
	/**开始时间**/
	private Date startDate;
	
	/**结束时间**/
	private Date endDate;
	
	/**开始时间(转成秒)**/
	private Long startDateSeckend;
	
	/**结束时间(转成秒)**/
	private Long endDateSeckend;
	
	/**根据价格计算出折扣信息**/
	private String discountPrice;

	/**佣金 for dss**/	
	private Double commision;

	private Integer salesCount;

	public Integer getSalesCount() {
		return salesCount;
	}

	public void setSalesCount(Integer salesCount) {
		this.salesCount = salesCount;
	}

	public Double getCommision() {
		return commision;
	}

	public void setCommision(Double commision) {
		this.commision = commision;
	}

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Long getStartDateSeckend() {
		return startDateSeckend;
	}

	public void setStartDateSeckend(Long startDateSeckend) {
		this.startDateSeckend = startDateSeckend;
	}

	public Long getEndDateSeckend() {
		return endDateSeckend;
	}

	public void setEndDateSeckend(Long endDateSeckend) {
		this.endDateSeckend = endDateSeckend;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(String discountPrice) {
		this.discountPrice = discountPrice;
	}

	public String getTopicid() {
		return topicid;
	}

	public void setTopicid(String topicid) {
		this.topicid = topicid;
	}
	
	
}
