package com.tp.model.ord;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.common.vo.OrderConstant.ORDER_STATUS;
import com.tp.common.vo.OrderConstant.OrderIsDeleted;
import com.tp.common.vo.OrderConstant.OrderIsReceipt;
import com.tp.common.vo.OrderConstant.OrderPayType;
import com.tp.enums.common.PlatformEnum;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 订单表
  */
public class OrderInfo extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451468597512L;

	/**pk 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**订单编号(10+yymmdd+LENGTH(0,id)) 数据类型bigint(20)*/
	private Long parentOrderCode;
	
	/**订单状态（新订单-1,待付款-2,待转移-3,待发货-4,待收货-5,已收货-6,已取消-0,退款完成-100） 数据类型tinyint(3)*/
	private Integer orderStatus;
	
	/**会员ID 数据类型bigint(20)*/
	private Long memberId;
	
	/**支付流水号 数据类型varchar(30)*/
	private String payCode;
	
	/**支付途径（1.支付宝扫码 2.微信支付 3.支付宝 4.工商银行 5.招商银行。。。） 数据类型tinyint(3)*/
	private Long payWay;
	
	/**支付方式（1：在线支付。2：货到付款） 数据类型tinyint(3)*/
	private Integer payType;
	
	/**实付总金额  数据类型double(10,2)*/
	private Double total;
	
	/**应付总金额   数据类型double(10,2)*/
	private Double originalTotal;
	
	/**税费总金额  数据类型double(10,2)*/
	private Double taxTotal;
	
	/**商品总金额  数据类型double(10,2)*/
	private Double itemTotal;
	
	/**整单总运费 数据类型double(10,2)*/
	private Double freight;
	
	/**优惠总金额 数据类型double(10,2)*/
	private Double discountTotal;
	
	/**积分总数 数据类型int(8)*/
	private Integer totalPoint;
	
	/**是否开发票(1.是 0.否) 数据类型tinyint(1)*/
	private Integer isReceipt;
	
	/**大区ID（华北、华东等等） 数据类型bigint(20)*/
	private Long areaId;
	
	/**订单备注 数据类型varchar(255)*/
	private String remark;
	
	/**是否删除（1.删除 2. 不删除） 数据类型tinyint(1)*/
	private Integer deleted;
	
	/**支付成功时间 数据类型datetime*/
	private Date payTime;
	
	/**订单来源（1：MOBILE。2：PC） 数据类型tinyint(3)*/
	private Integer source;
	
	/**下单IP地址 数据类型varchar(32)*/
	private String ip;
	
	/**用户账户名 数据类型varchar(50)*/
	private String accountName;
	
	/**下单时间 数据类型datetime*/
	private Date createTime;
	
	/**订单修改时间 数据类型datetime*/
	private Date updateTime;
	
	/** 数据类型varchar(32)*/
	private String createUser;
	
	/** 数据类型varchar(45)*/
	private String batchNum;
	
	/** 数据类型varchar(32)*/
	private String updateUser;
	
	/**推广员ID 数据类型bigint(14)*/
	private Long promoterId;
	
	/**扫码ID 数据类型bigint(14)*/
	private Long scanPromoterId;
		
	/** 渠道代码 **/
	private String channelCode;
	/** 民生银行uuid **/
	private String uuid;
	/** 民生银行 民生 推荐用户OPENID **/
	private String tpin;
	
	/**分销员ID 数据类型bigint(14)*/
	private Long shopPromoterId;
	
	/**接收手机号码*/
	private String receiveTel;
	
	/**是否合并支付 数据类型tinyint(2)*/
	private Integer mergePay;
	
	@Virtual
	/** 整单商品总数量 */
	private Integer quantity;
	@Virtual
	private String payWayStr;
	@Virtual
	private String payWayCodeListString;
	@Virtual
	/**返佣（惠惠保商城用）*/
	private Double  returnMoney;
	public Long getScanPromoterId() {
		return scanPromoterId;
	}
	public void setScanPromoterId(Long scanPromoterId) {
		this.scanPromoterId = scanPromoterId;
	}
	public Double getDiscount() {
		return getOriginalTotal() - getTotal();
	}
	public String getStatusStr() {
		return ORDER_STATUS.getCnName(getOrderStatus());
	}
	public String getPayTypeStr() {
		if (null == getPayType()) {
			return StringUtils.EMPTY;
		}
		return OrderPayType.getCnName(getPayType());
	}
	public String getIsReceiptStr() {
		return OrderIsReceipt.getCnName(getIsReceipt());
	}
	public String getDeletedStr() {
		return OrderIsDeleted.getCnName(getDeleted());
	}
	public String getSourceStr() {
		return PlatformEnum.getDescByCode(getSource());
	}
	
	public Double getPayTotal() {
		return getTotal();
	}
	
	public Long getId(){
		return id;
	}
	public Long getParentOrderCode(){
		return parentOrderCode;
	}
	public Integer getOrderStatus(){
		return orderStatus;
	}
	public Long getMemberId(){
		return memberId;
	}
	public String getPayCode(){
		return payCode;
	}
	public Long getPayWay(){
		return payWay;
	}
	public Integer getPayType(){
		return payType;
	}
	public Double getTotal(){
		return total;
	}
	public Double getOriginalTotal(){
		return originalTotal;
	}
	public Double getFreight(){
		return freight;
	}
	public Integer getIsReceipt(){
		return isReceipt;
	}
	public Long getAreaId(){
		return areaId;
	}
	public String getRemark(){
		return remark;
	}
	public Integer getDeleted(){
		return deleted;
	}
	public Date getPayTime(){
		return payTime;
	}
	public Integer getSource(){
		return source;
	}
	public String getIp(){
		return ip;
	}
	public String getAccountName(){
		return accountName;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Date getUpdateTime(){
		return updateTime;
	}
	public String getCreateUser(){
		return createUser;
	}
	public String getBatchNum(){
		return batchNum;
	}
	public String getUpdateUser(){
		return updateUser;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setParentOrderCode(Long parentOrderCode){
		this.parentOrderCode=parentOrderCode;
	}
	public void setOrderStatus(Integer orderStatus){
		this.orderStatus=orderStatus;
	}
	public void setMemberId(Long memberId){
		this.memberId=memberId;
	}
	public void setPayCode(String payCode){
		this.payCode=payCode;
	}
	public void setPayWay(Long payWay){
		this.payWay=payWay;
	}
	public void setPayType(Integer payType){
		this.payType=payType;
	}
	public void setTotal(Double total){
		this.total=total;
	}
	public void setOriginalTotal(Double originalTotal){
		this.originalTotal=originalTotal;
	}
	public void setFreight(Double freight){
		this.freight=freight;
	}
	public void setIsReceipt(Integer isReceipt){
		this.isReceipt=isReceipt;
	}
	public void setAreaId(Long areaId){
		this.areaId=areaId;
	}
	public void setRemark(String remark){
		this.remark=remark;
	}
	public void setDeleted(Integer deleted){
		this.deleted=deleted;
	}
	public void setPayTime(Date payTime){
		this.payTime=payTime;
	}
	public void setSource(Integer source){
		this.source=source;
	}
	public void setIp(String ip){
		this.ip=ip;
	}
	public void setAccountName(String accountName){
		this.accountName=accountName;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setUpdateTime(Date updateTime){
		this.updateTime=updateTime;
	}
	public void setCreateUser(String createUser){
		this.createUser=createUser;
	}
	public void setBatchNum(String batchNum){
		this.batchNum=batchNum;
	}
	public void setUpdateUser(String updateUser){
		this.updateUser=updateUser;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public String getPayWayStr() {
		return payWayStr;
	}
	public void setPayWayStr(String payWayStr) {
		this.payWayStr = payWayStr;
	}
	public Long getPromoterId() {
		return promoterId;
	}
	public void setPromoterId(Long promoterId) {
		this.promoterId = promoterId;
	}
	public Long getShopPromoterId() {
		return shopPromoterId;
	}
	public void setShopPromoterId(Long shopPromoterId) {
		this.shopPromoterId = shopPromoterId;
	}
	public Double getTaxTotal() {
		return taxTotal;
	}
	public void setTaxTotal(Double taxTotal) {
		this.taxTotal = taxTotal;
	}
	public Double getItemTotal() {
		return itemTotal;
	}
	public void setItemTotal(Double itemTotal) {
		this.itemTotal = itemTotal;
	}
	public Double getDiscountTotal() {
		return discountTotal;
	}
	public void setDiscountTotal(Double discountTotal) {
		this.discountTotal = discountTotal;
	}
	public Integer getMergePay() {
		return mergePay;
	}
	public void setMergePay(Integer mergePay) {
		this.mergePay = mergePay;
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
	public Integer getTotalPoint() {
		if(totalPoint==null){
			return 0;
		}
		return totalPoint;
	}
	public void setTotalPoint(Integer totalPoint) {
		this.totalPoint = totalPoint;
	}
	public String getPayWayCodeListString() {
		return payWayCodeListString;
	}
	public void setPayWayCodeListString(String payWayCodeListString) {
		this.payWayCodeListString = payWayCodeListString;
	}
	public String getReceiveTel() {
		return receiveTel;
	}
	public void setReceiveTel(String receiveTel) {
		this.receiveTel = receiveTel;
	}
	public Double getReturnMoney() {
		return returnMoney;
	}
	public void setReturnMoney(Double returnMoney) {
		this.returnMoney = returnMoney;
	}
	
	
}
