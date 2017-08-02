package com.tp.dto.cms.temple;

import java.io.Serializable;

/**
 * 首页广告模板
 * @author szy
 * 2015-1-6
 */
public class HomePageAdData  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 933130829596368876L;

	/**
	 * 
	 */
//	private static final long serialVersionUID = 1L;

	private String link;
	
	private String imageSrc;

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
	
	
	
}
