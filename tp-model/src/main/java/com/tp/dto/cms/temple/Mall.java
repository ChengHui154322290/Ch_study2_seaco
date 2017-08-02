package com.tp.dto.cms.temple;

import java.io.Serializable;

/**
 * 新版商城大牌精选和每日专场
 * 2015-4-22 5:54:34
 */
public class Mall  implements Serializable {

	private static final long serialVersionUID = 3104809429783197384L;

	private String imgsrc;
	
	private String imgLink;
	
	private String name;
	
	private String rate;
	
	private String detail;
	
	/** 卖点 **/
	private String topicPoint;

	public String getImgsrc() {
		return imgsrc;
	}

	public void setImgsrc(String imgsrc) {
		this.imgsrc = imgsrc;
	}

	public String getImgLink() {
		return imgLink;
	}

	public void setImgLink(String imgLink) {
		this.imgLink = imgLink;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public String getTopicPoint() {
		return topicPoint;
	}

	public void setTopicPoint(String topicPoint) {
		this.topicPoint = topicPoint;
	}
	
	
}
