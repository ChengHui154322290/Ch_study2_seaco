package com.tp.result.bse;

import java.io.Serializable;

import com.tp.model.bse.Spec;

public class SpecDTO implements Serializable{

	private static final long serialVersionUID = -4354771639401216006L;
	/**规格*/
	private Spec specDO;
	
	/**规格排序*/
	private int sort;

	public Spec getSpecDO() {
		return specDO;
	}

	public void setSpecDO(Spec specDO) {
		this.specDO = specDO;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}
	
	
}
