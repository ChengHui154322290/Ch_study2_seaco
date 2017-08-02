package com.tp.common.vo.supplier.entry;

/**
 * 销售渠道
 */
public enum SalesChannels {
	
	ss("销售渠道",1);	
	private String name;
    private Integer value;

    private SalesChannels(final String name, final Integer value) {
        this.name = name;
        this.value = value;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

}
