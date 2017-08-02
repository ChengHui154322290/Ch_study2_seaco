package com.tp.dto.mem;

import java.io.Serializable;

/**
 * 收货地址K - V
 * @author zhuss
 * @2016年4月13日 下午5:24:40
 */
public class ConsigneeAddressKVDTO implements Serializable{

	private static final long serialVersionUID = -4238138140622962801L;

	private Long id;
	
	private String value;
	
	

	public ConsigneeAddressKVDTO() {
		super();
	}

	public ConsigneeAddressKVDTO(Long id, String value) {
		super();
		this.id = id;
		this.value = value;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
