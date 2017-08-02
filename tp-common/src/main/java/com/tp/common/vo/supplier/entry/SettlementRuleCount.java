package com.tp.common.vo.supplier.entry;

public enum SettlementRuleCount {
	two("二","two");
	
	private String name;
	private String value;
	
	public String getName() {
		return name;
	}
	public String getValue() {
		return value;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setValue(String value) {
		this.value = value;
	}
	private SettlementRuleCount(final String name,final String value) {
		this.name = name;
		this.value = value;
	}

}
