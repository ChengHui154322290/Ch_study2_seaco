package com.tp.result.bse;

import java.io.Serializable;
import java.util.List;

import com.tp.model.bse.Category;

public class CategoryResult implements Serializable {

	private static final long serialVersionUID = -8975067292768320968L;

	private Category category;
	private List<AttributeResult> attributesAndValues;

	public final Category getCategory() {
		return category;
	}

	public final void setCategory(Category category) {
		this.category = category;
	}

	public List<AttributeResult> getAttributesAndValues() {
		return attributesAndValues;
	}

	public void setAttributesAndValues(List<AttributeResult> attributesAndValues) {
		this.attributesAndValues = attributesAndValues;
	}

}
