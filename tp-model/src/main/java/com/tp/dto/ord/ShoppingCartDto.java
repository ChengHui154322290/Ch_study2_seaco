package com.tp.dto.ord;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.tp.common.vo.Constant;
import com.tp.common.vo.OrderConstant;
import com.tp.model.ord.CartItemInfo;
/**
 * 购物车
 * @author szy
 *
 */
public class ShoppingCartDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3126948591678840234L;

	/**会员ID*/
	private Long memberId;
	/**商品列表*/
	private List<CartItemInfo> cartItemInfoList = new ArrayList<CartItemInfo>();
	/**失效商品列表*/
	private List<CartItemInfo> deleteCartItemList = new ArrayList<CartItemInfo>();
	/**拆分成预子订单*/
	private List<PreOrderDto> preSubOrderList = new ArrayList<PreOrderDto>();
	/**应付总金额 */
	private Double payableAmount = Constant.ZERO;
	/**实付商品总金额*/
	private Double actuallyAmount = Constant.ZERO;
	/**总运费*/
	private Double freight = Constant.ZERO;
	/**税金*/
	private Double taxes = Constant.ZERO;
	/**合计支付金额*/
	private Double summation = Constant.ZERO;
	/**下单IP*/
	private String ip;
	/**平台类型*/
	private Integer platformType;
	/**下单时的唯一码*/
	private String token;
	/**大区*/
	private Long areaId;
	/**优惠金额*/
	private Double discountTotal = Constant.ZERO;

	private Integer buyNow = Constant.TF.NO;
	
	/**团购id*/
	private Long groupId;
	/**卡券*/
	private Long promoterId;
	
	private Long shopId;
	/**店铺ID*/
	private Long shopPromoterId;
	/**扫码ID*/
	private Long scanPromoterId;
		
	private String channelCode = OrderConstant.CHANNEL_CODE.xg.name();
	/** 民生uuid **/
	private String uuid;
	/** 民生银行 民生 推荐用户OPENID **/
	private String tpin;
	
	/**原始商品总额*/
	private Double orginItemAmount;
	/**原始税费总额*/
	private Double orginTaxFee;
	/**原始运费总额*/
	private Double orginFreight;
	
	
	/**总积分数*/
	private Integer totalPoint;

	/**使用积分数*/
	private Integer usedPoint;
	
	/**使用积分标识*/
	private Boolean usedPointSign=Boolean.FALSE;
	
	/**返佣（惠惠保商城用）*/
	private Double  returnMoney;
	/**
	 * 获取购买商品的总数
	 * @return
	 */
	public Integer getQuantityCount(){
		Integer count = 0;
		if(CollectionUtils.isNotEmpty(cartItemInfoList)){
			for(CartItemInfo cartItemInfo:cartItemInfoList){
				count+=cartItemInfo.getQuantity();
			}
		}
		return count;
	}
	
	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public List<CartItemInfo> getCartItemInfoList() {
		return cartItemInfoList;
	}

	public void setCartItemInfoList(List<CartItemInfo> cartItemInfoList) {
		this.cartItemInfoList = cartItemInfoList;
	}

	public Double getPayableAmount() {
		return payableAmount;
	}

	public void setPayableAmount(Double payableAmount) {
		this.payableAmount = payableAmount;
	}

	public Double getActuallyAmount() {
		return actuallyAmount;
	}

	public void setActuallyAmount(Double actuallyAmount) {
		this.actuallyAmount = actuallyAmount;
	}

	public Double getFreight() {
		return freight;
	}

	public void setFreight(Double freight) {
		this.freight = freight;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getPlatformType() {
		return platformType;
	}

	public void setPlatformType(Integer platformType) {
		this.platformType = platformType;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	public List<CartItemInfo> getDeleteCartItemList() {
		return deleteCartItemList;
	}

	public void setDeleteCartItemList(List<CartItemInfo> deleteCartItemList) {
		this.deleteCartItemList = deleteCartItemList;
	}

	public List<PreOrderDto> getPreSubOrderList() {
		return preSubOrderList;
	}

	public void setPreSubOrderList(List<PreOrderDto> preSubOrderList) {
		this.preSubOrderList = preSubOrderList;
	}

	public Double getTaxes() {
		return taxes;
	}

	public void setTaxes(Double taxes) {
		this.taxes = taxes;
	}

	public Double getSummation() {
		return summation;
	}

	public void setSummation(Double summation) {
		this.summation = summation;
	}

	public Integer getBuyNow() {
		return buyNow;
	}

	public void setBuyNow(Integer buyNow) {
		this.buyNow = buyNow;
	}

	public Double getDiscountTotal() {
		return discountTotal;
	}

	public void setDiscountTotal(Double discountTotal) {
		this.discountTotal = discountTotal;
	}

	public Long getShopPromoterId() {
		return shopPromoterId;
	}

	public void setShopPromoterId(Long shopPromoterId) {
		this.shopPromoterId = shopPromoterId;
	}
	
	public Long getScanPromoterId() {
		return scanPromoterId;
	}

	public void setScanPromoterId(Long scanPromoterId) {
		this.scanPromoterId = scanPromoterId;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Long getPromoterId() {
		return promoterId;
	}

	public void setPromoterId(Long promoterId) {
		this.promoterId = promoterId;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getTpin() {
		return tpin;
	}

	public void setTpin(String tpin) {
		this.tpin = tpin;
	}

	public Double getOrginItemAmount() {
		return orginItemAmount;
	}

	public void setOrginItemAmount(Double orginItemAmount) {
		this.orginItemAmount = orginItemAmount;
	}

	public Double getOrginTaxFee() {
		return orginTaxFee;
	}

	public void setOrginTaxFee(Double orginTaxFee) {
		this.orginTaxFee = orginTaxFee;
	}

	public Double getOrginFreight() {
		return orginFreight;
	}

	public void setOrginFreight(Double orginFreight) {
		this.orginFreight = orginFreight;
	}
	
	public Integer getTotalPoint() {
		if(totalPoint==null){
			return 0;
		}
		return totalPoint;
	}

	public void setTotalPoint(Integer totalPoint) {
		this.totalPoint = totalPoint;
	}

	public Integer getUsedPoint() {
		return usedPoint;
	}

	public void setUsedPoint(Integer usedPoint) {
		this.usedPoint = usedPoint;
	}

	public Boolean getUsedPointSign() {
		return usedPointSign;    
	}

	public void setUsedPointSign(Boolean usedPointSign) {
		this.usedPointSign = usedPointSign;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}   
	public Double getReturnMoney() {
		return returnMoney;
	}

	public void setReturnMoney(Double returnMoney) {
		this.returnMoney = returnMoney;
	}
}
