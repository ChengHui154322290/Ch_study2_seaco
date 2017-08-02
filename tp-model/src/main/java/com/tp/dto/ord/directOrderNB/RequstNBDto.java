package com.tp.dto.ord.directOrderNB;

import java.io.Serializable;

public class RequstNBDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DirectOrderNBDto Body ;

	public DirectOrderNBDto getBody() {
		return Body;
	}

	public void setBody(DirectOrderNBDto body) {
		Body = body;
	}

	
	
}
