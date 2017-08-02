package com.tp.m.vo.order;

import java.util.List;

import com.tp.m.base.BaseVO;
import com.tp.m.to.order.OrderLineTO;


public class OrderVO implements BaseVO{

	private static final long serialVersionUID = -8701960059395209561L;

	private String ordercode;//订单编号
	private String ordertime;//下单时间
	private String orderprice;//订单价格
	private String ordercount;//订单的商品数
	//private String imgurl;
	private String status;
	private String statusdesc;
	private String payway;//支付方式 ：1支付宝2微信
	private String paywaydesc;//支付方式描述
	private String paywaylist;//支付方式CODELIST
	private String linecount;//商品行数量
	private String ordertype;//订单类型
	private String channelid;//渠道ID
	private String iscancancel;//是否能取消0否1是
	private String totalpoint;//使用积分
	private List<OrderLineTO> lines;
	private String address;//收货地址
	private String paytime;//支付时间
	private String memberid;//会员ID
	
	public String getOrdercode() {
		return ordercode;
	}
	public void setOrdercode(String ordercode) {
		this.ordercode = ordercode;
	}
	public String getOrdertime() {
		return ordertime;
	}
	public void setOrdertime(String ordertime) {
		this.ordertime = ordertime;
	}
	public String getOrderprice() {
		return orderprice;
	}
	public void setOrderprice(String orderprice) {
		this.orderprice = orderprice;
	}
	public String getOrdercount() {
		return ordercount;
	}
	public void setOrdercount(String ordercount) {
		this.ordercount = ordercount;
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
	public List<OrderLineTO> getLines() {
		return lines;
	}
	public void setLines(List<OrderLineTO> lines) {
		this.lines = lines;
	}
	public String getPayway() {
		return payway;
	}
	public void setPayway(String payway) {
		this.payway = payway;
	}
	public String getPaywaydesc() {
		return paywaydesc;
	}
	public void setPaywaydesc(String paywaydesc) {
		this.paywaydesc = paywaydesc;
	}
	public String getLinecount() {
		return linecount;
	}
	public void setLinecount(String linecount) {
		this.linecount = linecount;
	}
	public String getOrdertype() {
		return ordertype;
	}
	public void setOrdertype(String ordertype) {
		this.ordertype = ordertype;
	}
	public String getChannelid() {
		return channelid;
	}
	public void setChannelid(String channelid) {
		this.channelid = channelid;
	}
	public String getIscancancel() {
		return iscancancel;
	}
	public void setIscancancel(String iscancancel) {
		this.iscancancel = iscancancel;
	}
	public String getTotalpoint() {
		return totalpoint;
	}
	public void setTotalpoint(String totalpoint) {
		this.totalpoint = totalpoint;
	}
	public String getPaywaylist() {
		return paywaylist;
	}
	public void setPaywaylist(String paywaylist) {
		this.paywaylist = paywaylist;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPaytime() {
		return paytime;
	}
	public void setPaytime(String paytime) {
		this.paytime = paytime;
	}
	public String getMemberid() {
		return memberid;
	}
	public void setMemberid(String memberid) {
		this.memberid = memberid;
	}
}
