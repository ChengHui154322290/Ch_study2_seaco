package com.tp.result.prd;

import com.tp.model.prd.ItemDetailSpec;

/**
 * 规格信息封装，增加组名、规格名称
 * @author szy
 * 2014年12月30日 下午3:56:32
 *
 */
public class DetailSpecResult extends ItemDetailSpec{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7867434765920922293L;

	private String specName;
	
	private String groupName;

	public String getSpecName() {
		return specName;
	}

	public void setSpecName(String specName) {
		this.specName = specName;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	
}
