package com.tp.dto.ord;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrderDeliverListDTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2783772534699473822L;
	
	private List<OrderDeliverDTO> deliverList = new ArrayList<OrderDeliverDTO>();
	
//	private List<ExpressModifyDTO> expressModifyList = new ArrayList<ExpressModifyDTO>();

	public List<OrderDeliverDTO> getDeliverList() {
		return deliverList;
	}

	public void setDeliverList(List<OrderDeliverDTO> deliverList) {
		this.deliverList = deliverList;
	}

	@Override
	public String toString() {
		return "OrderDeliverListDTO [deliverList=" + deliverList + "]";
	}	
}
