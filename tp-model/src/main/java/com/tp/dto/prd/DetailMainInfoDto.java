package com.tp.dto.prd;

import java.io.Serializable;

public class DetailMainInfoDto  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5952637512717119384L;
	
	private Long detailId;
	
	
	private String prdid;
	
	
	private String mainTitle;
	
	private String barcode;


	public Long getDetailId() {
		return detailId;
	}


	public void setDetailId(Long detailId) {
		this.detailId = detailId;
	}


	public String getPrdid() {
		return prdid;
	}


	public void setPrdid(String prdid) {
		this.prdid = prdid;
	}


	public String getMainTitle() {
		return mainTitle;
	}


	public void setMainTitle(String mainTitle) {
		this.mainTitle = mainTitle;
	}


	public String getBarcode() {
		return barcode;
	}


	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

}
