package com.tp.m.vo.order;

import java.util.List;

import com.tp.m.vo.groupbuy.OrderRedeemItemVo;

public class OrderDetailVO extends OrderVO{
	private static final long serialVersionUID = 3560609727126939561L;
	private String address;//收货人地址
	private String tel;//收货人手机号
	private String name;//收货人姓名
	private String disprice;//优惠金额
	private String baseprice;//实付金额
	private String leftMoney;//可退金额（线下团购用）
	private String freight;//运费
	private String taxes;//税金
	/**接收手机号码*/
	private String receiveTel;
    private List<OrderRedeemItemVo> orderRedeemItemList;//兑换码
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDisprice() {
		return disprice;
	}
	public void setDisprice(String disprice) {
		this.disprice = disprice;
	}
	public String getBaseprice() {
		return baseprice;
	}
	public void setBaseprice(String baseprice) {
		this.baseprice = baseprice;
	}
	public String getFreight() {
		return freight;
	}
	public void setFreight(String freight) {
		this.freight = freight;
	}
	public String getTaxes() {
		return taxes;
	}
	public void setTaxes(String taxes) {
		this.taxes = taxes;
	}
	public List<OrderRedeemItemVo> getOrderRedeemItemList() {
		return orderRedeemItemList;
	}
	public void setOrderRedeemItemList(List<OrderRedeemItemVo> orderRedeemItemList) {
		this.orderRedeemItemList = orderRedeemItemList;
	}
	public String getLeftMoney() {
		return leftMoney;
	}
	public void setLeftMoney(String leftMoney) {
		this.leftMoney = leftMoney;
	}
	public String getReceiveTel() {
		return receiveTel;
	}
	public void setReceiveTel(String receiveTel) {
		this.receiveTel = receiveTel;
	}
    
	
}
