package com.tp.query.mmp;

import java.io.Serializable;
import java.util.List;

/**
 * 商品查询促销专题信息接口条件
 * @author szy
 *
 */
public class TopicItemCartQuery implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6759485063924953842L;

	private long topicId; //专题id
	
	private String sku; //sku
	
	private long area;// 所在地区
	
	private int amount;//购买数量
	
	private int platform;//所用平台
	
	private Long memberId; //登录用户的uid
	
	private String uip;// ip
	
	private String mobile;// 收货mobile
	

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	

	public String getUip() {
		return uip;
	}

	public void setUip(String uip) {
		this.uip = uip;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getPlatform() {
		return platform;
	}

	public void setPlatform(int platform) {
		this.platform = platform;
	}

	public long getTopicId() {
		return topicId;
	}

	public void setTopicId(long topicId) {
		this.topicId = topicId;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public long getArea() {
		return area;
	}

	public void setArea(long area) {
		this.area = area;
	}

	

	
	
	
}
