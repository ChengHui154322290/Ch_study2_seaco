package com.tp.query.bse;

import java.io.Serializable;
import java.util.List;

import com.tp.model.bse.Spec;
import com.tp.model.bse.SpecGroup;

public class SpecGroupQuery implements Serializable {

	private static final long serialVersionUID = 714941808399235463L;

	private SpecGroup specGroupDO;

	private List<Spec> list;

	public final SpecGroup getSpecGroupDO() {
		return specGroupDO;
	}

	public final void setSpecGroupDO(SpecGroup specGroupDO) {
		this.specGroupDO = specGroupDO;
	}

	public final List<Spec> getList() {
		return list;
	}

	public final void setList(List<Spec> list) {
		this.list = list;
	}

}
