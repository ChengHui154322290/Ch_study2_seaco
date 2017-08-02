package com.tp.m.vo.groupbuy;

import java.util.Date;

/**
  * @author zhouguofeng
  * 商家线下购买兑换码信息
  */
public class OrderRedeemItemVo  {

	/**PK */
	private Long id;
	
	/**父订单ID */
	private Long parentOrderId;
	
	/**子订单ID */
	private Long orderId;
	
	/**父订单代码 */
	private String parentOrderCode;
	
	/**订单编码 */
	private Long orderCode;
	
	/**商品sku编码 */
	private String skuCode;
	/**兑换码名称 */
	private String redeemName;
	/**兑换码 */
	private String redeemCode;
	
	/**供应商id*/
	private Long supplierId;
	
	/**店铺名称 */
	private String shopName;
	
	/**兑换码金额 */
	private Double salesPrice;
	
	/**兑换码状态  1：未使用 0：已使用 3：已到期  4：已退款 */
	private Integer redeemCodeState;
	
	/**创建时间*/
	private Date createTime;
	
	/**修改时间*/
	private Date updateTime;
	
	/**兑换码有效开始日期*/
	private Date  effectTimeStart;
	
	/**兑换码有效截止日期*/
	private Date   effectTimeEnd ;
	/**核销人*/
	private String updateUser;
	/**仓库ID*/
	private Long warehouseId;
	private String spuName;
	/**二维码  */
	private String  qrCode;
	
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
	public String getParentOrderCode() {
		return parentOrderCode;
	}
	public void setParentOrderCode(String parentOrderCode) {
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