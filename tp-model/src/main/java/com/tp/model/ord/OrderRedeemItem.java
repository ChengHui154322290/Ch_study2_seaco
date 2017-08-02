package com.tp.model.ord;

import java.io.Serializable;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.common.vo.PaymentConstant;
import com.tp.model.BaseDO;
import com.tp.util.DateUtil;
/**
  * @author zhouguofeng
  * 商家线下购买兑换码信息
  */
public class OrderRedeemItem extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1476258241219L;

	/**PK 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**父订单ID 数据类型bigint(20)*/
	private Long parentOrderId;
	
	/**子订单ID 数据类型bigint(20)*/
	private Long orderId;
	
	/**父订单代码 数据类型varchar(64)*/
	private Long parentOrderCode;
	
	/**订单编码 数据类型bigint(20)*/
	private Long orderCode;
	
	/**商品sku编码 数据类型varchar(64)*/
	private String skuCode;
	/**兑换码名称 数据类型varchar(32)*/
	private String redeemName;
	/**兑换码 数据类型varchar(32)*/
	private String redeemCode;
	
	/**供应商id 数据类型bigint(11)*/
	private Long supplierId;
	
	/**店铺名称 数据类型varchar(255)*/
	private String shopName;
	
	/**兑换码金额 数据类型double(10,2)*/
	private Double salesPrice;
	
	/**兑换码状态  1：未使用 0：已使用 3：已到期  4：已退款 数据类型varchar(32)*/
	private Integer redeemCodeState;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**修改时间 数据类型datetime*/
	private Date updateTime;
	
	/**兑换码有效开始日期 数据类型datetime*/
	private Date  effectTimeStart;
	
	/**兑换码有效截止日期 数据类型datetime*/
	private Date   effectTimeEnd ;
	/**核销人*/
	private String updateUser;
	/**仓库ID*/
	private Long warehouseId;
	
	private String spuName;
	
	@Virtual
	/**二维码  */
	private String  qrCode;
	
	public String getCreateTimeCn(){
		return DateUtil.formatDateTime(createTime);
	}
	public String getUpdateTimeCn(){
		return DateUtil.formatDateTime(updateTime);
	}
	
	public String getRedeemCodeStateCn(){
		return PaymentConstant.REDEEM_CODE_STATUS.getCnName(redeemCodeState);
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getParentOrderId() {
		return parentOrderId;
	}
	public void setParentOrderId(Long parentOrderId) {
		this.parentOrderId = parentOrderId;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public Long getParentOrderCode() {
		return parentOrderCode;
	}
	public void setParentOrderCode(Long parentOrderCode) {
		this.parentOrderCode = parentOrderCode;
	}
	public Long getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(Long orderCode) {
		this.orderCode = orderCode;
	}
	public String getSkuCode() {
		return skuCode;
	}
	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	public String getRedeemName() {
		return redeemName;
	}
	public void setRedeemName(String redeemName) {
		this.redeemName = redeemName;
	}
	public String getRedeemCode() {
		return redeemCode;
	}
	public void setRedeemCode(String redeemCode) {
		this.redeemCode = redeemCode;
		if(StringUtils.isNoneBlank(redeemCode)){
			this.redeemCode = redeemCode.replaceAll("\r|\n", "");
		}
		
	}
	public Long getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public Double getSalesPrice() {
		return salesPrice;
	}
	public void setSalesPrice(Double salesPrice) {
		this.salesPrice = salesPrice;
	}
	public Integer getRedeemCodeState() {
		return redeemCodeState;
	}
	public void setRedeemCodeState(Integer redeemCodeState) {
		this.redeemCodeState = redeemCodeState;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Date getEffectTimeStart() {
		return effectTimeStart;
	}
	public void setEffectTimeStart(Date effectTimeStart) {
		this.effectTimeStart = effectTimeStart;
	}
	public Date getEffectTimeEnd() {
		return effectTimeEnd;
	}
	public void setEffectTimeEnd(Date effectTimeEnd) {
		this.effectTimeEnd = effectTimeEnd;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	public Long getWarehouseId() {
		return warehouseId;
	}
	public void setWarehouseId(Long warehouseId) {
		this.warehouseId = warehouseId;
	}
	public String getQrCode() {
		return qrCode;
	}
	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}
	public String getSpuName() {
		return spuName;
	}
	public void setSpuName(String spuName) {
		this.spuName = spuName;
	}
	
}
