package com.tp.dto.cms;

import java.io.Serializable;

public class CoordsImagesHrefDTO implements Serializable{
	
	private static final long serialVersionUID = 7621455529024041799L;
	
	/** 位置 */
	private String coords;
	
	/** 链接 */
	private String href;

	public String getCoords() {
		return coords;
	}

	public void setCoords(String coords) {
		this.coords = coords;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}
	
}
