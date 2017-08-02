package com.tp.dto.cms.temple;

import java.io.Serializable;

/**
 * 品牌下感兴趣
 * 2015-1-9
 */
public class Interest  implements Serializable {

	private static final long serialVersionUID = 3104809429783197384L;

	private String imgsrc;
	
	private String imgLink;
	
	private String name;
	
	private String detail;

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
	
	
}
