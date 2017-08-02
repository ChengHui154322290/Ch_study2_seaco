package com.tp.result.bse;

import java.io.Serializable;
import java.util.List;

import com.tp.model.bse.Spec;
import com.tp.model.bse.SpecGroup;

public class SpecGroupResult implements Serializable {

	private static final long serialVersionUID = 3931706864807139183L;
	private SpecGroup specGroup;
	private List<Spec> specDoList;
    

	public SpecGroup getSpecGroup() {
		return specGroup;
	}

	public void setSpecGroup(SpecGroup specGroup) {
		this.specGroup = specGroup;
	}

	public List<Spec> getSpecDoList() {
		return specDoList;
	}

	public void setSpecDoList(List<Spec> specDoList) {
		this.specDoList = specDoList;
	}
}
