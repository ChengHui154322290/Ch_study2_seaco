package com.tp.m.query.order;

import java.util.List;

import com.tp.m.base.BaseQuery;

/**
 * 订单模块的入参
 * @author zhuss
 * @2016年1月7日 下午4:29:05
 */
public class QueryOrder extends BaseQuery{

	private static final long serialVersionUID = 8952791355447181170L;
	private String aid;//地址ID
	/**接收手机号码*/
	private String receiveTel;
	private String cid;//优惠券ID
	private List<String> cidlist; //优惠券集合
	private String type;
	private String ordercode;
	
	/**用于立即购买**/
	private String sku;
	private String tid;
	private String count;
	private String uuid;
	private String shopMobile;
	private String tpin;

	/**用于团购*/
	private String gid;
	
	//用于合并支付
	private String payway;//支付方式的code
	private String openid; //wap的微信支付必传字段属性
	
	private String usedPointSign;//是否使用积分

	private String channelsource;
	private String clientcode;
	private String distributecode;
	/**积分类型  存放对应的渠道名*/
	private String pointType;

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getAid() {
		return aid;
	}
	public void setAid(String aid) {
		this.aid = aid;
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getOrdercode() {
		return ordercode;
	}
	public void setOrdercode(String ordercode) {
		this.ordercode = ordercode;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getShopMobile() {
		return shopMobile;
	}
	public void setShopMobile(String shopMobile) {
		this.shopMobile = shopMobile;
	}
	public List<String> getCidlist() {
		return cidlist;
	}
	public void setCidlist(List<String> cidlist) {
		this.cidlist = cidlist;
	}

	public String getPayway() {
		return payway;
	}

	public void setPayway(String payway) {
		this.payway = payway;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getTpin() {
		return tpin;
	}

	public void setTpin(String tpin) {
		this.tpin = tpin;
	}

	public String getUsedPointSign() {
		return usedPointSign;
	}

	public void setUsedPointSign(String usedPointSign) {
		this.usedPointSign = usedPointSign;
	}

	public String getChannelsource() {
		return channelsource;
	}

	public void setChannelsource(String channelsource) {
		this.channelsource = channelsource;
	}

	public String getClientcode() {
		return clientcode;
	}

	public void setClientcode(String clientcode) {
		this.clientcode = clientcode;
	}

	public String getDistributecode() {
		return distributecode;
	}

	public void setDistributecode(String distributecode) {
		this.distributecode = distributecode;
	}

	public String getReceiveTel() {
		return receiveTel;
	}

	public void setReceiveTel(String receiveTel) {
		this.receiveTel = receiveTel;
	}

	public String getPointType() {
		return pointType;
	}

	public void setPointType(String pointType) {
		this.pointType = pointType;
	}

	
	
}
