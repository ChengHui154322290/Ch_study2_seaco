package com.tp.result.bse;

import java.io.Serializable;
import java.util.List;

import com.tp.model.bse.Attribute;
import com.tp.model.bse.AttributeValue;

public class AttributeResult implements Serializable{
	
	private static final long serialVersionUID = 2175242992481785624L;
	
	private Attribute attribute;
	private List<AttributeValue> attributeValues;
	
	public Attribute getAttribute() {
		return attribute;
	}
	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}
	public List<AttributeValue> getAttributeValues() {
		return attributeValues;
	}
	public void setAttributeValues(List<AttributeValue> attributeValue) {
		this.attributeValues = attributeValue;
	}
}
