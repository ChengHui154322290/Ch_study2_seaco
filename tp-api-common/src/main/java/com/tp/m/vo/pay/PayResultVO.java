package com.tp.m.vo.pay;

import java.util.List;

import com.tp.m.base.BaseVO;
import com.tp.m.vo.groupbuy.OrderRedeemItemVo;

/**
 * 支付结果
 * @author zhuss
 * @2016年1月13日 下午5:07:16
 */
public class PayResultVO implements BaseVO{

	private static final long serialVersionUID = -7218512398034185348L;

	private String status;
	private String statusdesc;
    private List<OrderRedeemItemVo> OrderRedeemItemList;
	
	public PayResultVO() {
		super();
	}
	public PayResultVO(String status, String statusdesc) {
		super();
		this.status = status;
		this.statusdesc = statusdesc;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatusdesc() {
		return statusdesc;
	}
	public void setStatusdesc(String statusdesc) {
		this.statusdesc = statusdesc;
	}
	/**  
	 * orderRedeemItemList.  
	 *  
	 * @return  the orderRedeemItemList  
	 * @since  JDK 1.8  
	 */
	public List<OrderRedeemItemVo> getOrderRedeemItemList() {
		return OrderRedeemItemList;
	}
	/**  
	 * orderRedeemItemList.  
	 *  
	 * @param   orderRedeemItemList    the orderRedeemItemList to set  
	 * @since  JDK 1.8  
	 */
	public void setOrderRedeemItemList(List<OrderRedeemItemVo> orderRedeemItemList) {
		OrderRedeemItemList = orderRedeemItemList;
	}
	
}
