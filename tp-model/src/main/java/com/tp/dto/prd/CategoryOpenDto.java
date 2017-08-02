package com.tp.dto.prd;

import java.io.Serializable;

public class CategoryOpenDto implements Serializable {

	private static final long serialVersionUID = 4150559064204860201L;
	
	private String bigCateName;

	private String middCateName;

	private String smallCateName;
	
	private  Long smallId;

	public String getBigCateName() {
		return bigCateName;
	}

	public void setBigCateName(String bigCateName) {
		this.bigCateName = bigCateName;
	}

	public String getMiddCateName() {
		return middCateName;
	}

	public void setMiddCateName(String middCateName) {
		this.middCateName = middCateName;
	}

	public String getSmallCateName() {
		return smallCateName;
	}

	public void setSmallCateName(String smallCateName) {
		this.smallCateName = smallCateName;
	}

	public Long getSmallId() {
		return smallId;
	}

	public void setSmallId(Long smallId) {
		this.smallId = smallId;
	}
	
	

}
