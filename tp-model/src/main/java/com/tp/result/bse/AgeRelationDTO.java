package com.tp.result.bse;

import java.io.Serializable;

public class AgeRelationDTO implements Serializable {

	private static final long serialVersionUID = -207372502131534887L;

	/** key */
	private Integer relationKey;

	/** 截止年龄段对应的key */
	private Integer endRelationKey;

	/** 孕月描述 */
	private String ageMonDesc;

	/** 月code */
	private String ageMonCode;

	public Integer getRelationKey() {
		return relationKey;
	}

	public void setRelationKey(Integer relationKey) {
		this.relationKey = relationKey;
	}

	public String getAgeMonDesc() {
		return ageMonDesc;
	}

	public void setAgeMonDesc(String ageMonDesc) {
		this.ageMonDesc = ageMonDesc;
	}

	public Integer getEndRelationKey() {
		return endRelationKey;
	}

	public void setEndRelationKey(Integer endRelationKey) {
		this.endRelationKey = endRelationKey;
	}

	public String getAgeMonCode() {
		return ageMonCode;
	}

	public void setAgeMonCode(String ageMonCode) {
		this.ageMonCode = ageMonCode;
	}

}
