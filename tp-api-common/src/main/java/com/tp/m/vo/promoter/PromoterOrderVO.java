package com.tp.m.vo.promoter;

import com.tp.m.vo.order.OrderVO;

public class PromoterOrderVO extends OrderVO{
	
	private static final long serialVersionUID = -3863256567924644399L;
	
	private String commision;		//佣金数量

	public String getCommision() {
		return commision;
	}

	public void setCommision(String commision) {
		this.commision = commision;
	}	
}
