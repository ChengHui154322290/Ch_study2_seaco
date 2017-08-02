package com.tp.m.vo.coupon;

import java.util.List;

import com.tp.m.base.BaseVO;

/**
 * 优惠券对象
 * @author zhuss
 * @2016年1月5日 上午10:46:19
 */
public class CouponVO implements BaseVO{

	private static final long serialVersionUID = -7999439305359675441L;

	private String cid;//优惠券ID
	private String couponUserId;//优惠券用户ID
	private String couponcode;//优惠券编号
	private String name;//优惠券名称
	private String price;//金额
	/**优惠券需满金额 数据类型int(10)*/
	private Integer needOverMon;
	private String validtime;//有效期间 
	private String rule;//规则
	private String status;//状态 
	private String type;//优惠券类型  0:满减券  1:现金券
	private List<String> scope;//适用范围
	private String topicId;//专场ID
	private Long categoryId;//大类id
	private Long categoryMiddleId;//商品品类 中类ID
	private Long categorySmallId;//商品小类ID
	private Long brandId;//品牌id
	private String brandName;//品牌名称
	private String skuCode;//sku编码
	private String skuName;//sku名称
	private String operate;//操作  1：领取  2：使用 3：已领完
	private String useScope;//使用范围 1:商品      2： 专场  3:品牌   4：类别
	/**优惠券图片*/
	private String couponImagePath;
	/*0:不兑换  1：允许兑换*/
	private String exchangeXgMoney;
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getCouponcode() {
		return couponcode;
	}
	public void setCouponcode(String couponcode) {
		this.couponcode = couponcode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getValidtime() {
		return validtime;
	}
	public void setValidtime(String validtime) {
		this.validtime = validtime;
	}
	public String getRule() {
		return rule;
	}
	public void setRule(String rule) {
		this.rule = rule;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<String> getScope() {
		return scope;
	}
	public void setScope(List<String> scope) {
		this.scope = scope;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getExchangeXgMoney() {
		return exchangeXgMoney;
	}
	public void setExchangeXgMoney(String exchangeXgMoney) {
		this.exchangeXgMoney = exchangeXgMoney;
	}
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public Long getCategoryMiddleId() {
		return categoryMiddleId;
	}
	public void setCategoryMiddleId(Long categoryMiddleId) {
		this.categoryMiddleId = categoryMiddleId;
	}
	public Long getCategorySmallId() {
		return categorySmallId;
	}
	public void setCategorySmallId(Long categorySmallId) {
		this.categorySmallId = categorySmallId;
	}
	public Long getBrandId() {
		return brandId;
	}
	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getSkuName() {
		return skuName;
	}
	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}
	public String getSkuCode() {
		return skuCode;
	}
	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	public String getOperate() {
		return operate;
	}
	public void setOperate(String operate) {
		this.operate = operate;
	}
	public String getUseScope() {
		return useScope;
	}
	public void setUseScope(String useScope) {
		this.useScope = useScope;
	}
	public String getTopicId() {
		return topicId;
	}
	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}
	public Integer getNeedOverMon() {
		return needOverMon;
	}
	public void setNeedOverMon(Integer needOverMon) {
		this.needOverMon = needOverMon;
	}
	public String getCouponImagePath() {
		return couponImagePath;
	}
	public void setCouponImagePath(String couponImagePath) {
		this.couponImagePath = couponImagePath;
	}
	public String getCouponUserId() {
		return couponUserId;
	}
	public void setCouponUserId(String couponUserId) {
		this.couponUserId = couponUserId;
	}
	
}
