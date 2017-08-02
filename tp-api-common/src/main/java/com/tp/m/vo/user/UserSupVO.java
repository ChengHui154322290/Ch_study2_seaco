package com.tp.m.vo.user;

import com.tp.m.base.BaseVO;

/**
 * 用户角标数量
 * @author zhuss
 * @2016年1月7日 下午4:10:50
 */
public class UserSupVO implements BaseVO{

	private static final long serialVersionUID = -1908169516093654647L;

	private String unpaycount = "0";//待付款数量
	private String unreceiptcount= "0";//待付款数量
	private String unusecount= "0";//未使用数量
	private String couponcount= "0";//优惠券数量
	private boolean usedThirdShopPoint=false;//是否使用第三方商城积分
	private Double  points;//第三方商城积分
	
	public UserSupVO() {
		super();
	}
	public UserSupVO(String unpaycount, String unreceiptcount,
			String couponcount) {
		super();
		this.unpaycount = unpaycount;
		this.unreceiptcount = unreceiptcount;
		this.couponcount = couponcount;
	}
	public String getUnpaycount() {
		return unpaycount;
	}
	public void setUnpaycount(String unpaycount) {
		this.unpaycount = unpaycount;
	}
	public String getUnreceiptcount() {
		return unreceiptcount;
	}
	public void setUnreceiptcount(String unreceiptcount) {
		this.unreceiptcount = unreceiptcount;
	}
	public String getCouponcount() {
		return couponcount;
	}
	public void setCouponcount(String couponcount) {
		this.couponcount = couponcount;
	}
	public String getUnusecount() {
		return unusecount;
	}
	public void setUnusecount(String unusecount) {
		this.unusecount = unusecount;
	}
	public boolean isUsedThirdShopPoint() {
		return usedThirdShopPoint;
	}
	public void setUsedThirdShopPoint(boolean usedThirdShopPoint) {
		this.usedThirdShopPoint = usedThirdShopPoint;
	}
	public Double getPoints() {
		return points;
	}
	public void setPoints(Double points) {
		this.points = points;
	}
	
}
