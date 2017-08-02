/**
 * 
 */
package com.tp.m.vo.promoter;

import com.tp.m.base.BaseVO;

/**
 * @author Administrator
 *
 */
public class PromoterTopicItemVO implements BaseVO{
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 4056106961663921534L;

	
	/** 主题ID */
	private  Long topicid;
	
	/** 商品名称 */
	private String name;
	
	/** 商品 */
	private String sku;
	
	/** 销售价格 */	
	private Double saleprice;
	
	/** 活动价格 */
	private Double topicprice;

	/** 商品上下架情况 */
	private Integer onshelves;
	
	/** 商品活动图片 */
	private String imgurl;
	
	/** 佣金 */
	private Double commission;

	
	/** 分享链接 */
	private String shareurl;

	

	public String getShareurl() {
		return shareurl;
	}

	public void setShareurl(String shareurl) {
		this.shareurl = shareurl;
	}

	public Long getTopicid() {
		return topicid;
	}

	public void setTopicid(Long topicid) {
		this.topicid = topicid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public Double getSaleprice() {
		return saleprice;
	}

	public void setSaleprice(Double saleprice) {
		this.saleprice = saleprice;
	}

	public Double getTopicprice() {
		return topicprice;
	}

	public void setTopicprice(Double topicprice) {
		this.topicprice = topicprice;
	}

	public Integer getOnshelves() {
		return onshelves;
	}

	public void setOnshelves(Integer onshelves) {
		this.onshelves = onshelves;
	}

	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	public Double getCommission() {
		return commission;
	}

	public void setCommission(Double commission) {
		this.commission = commission;
	}
					
}
