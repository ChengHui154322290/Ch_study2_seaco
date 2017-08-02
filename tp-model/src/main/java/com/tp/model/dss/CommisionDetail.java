package com.tp.model.dss;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.common.vo.DssConstant;
import com.tp.common.vo.OrderConstant;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 佣金明细
  */
public class CommisionDetail extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1457493844975L;

	/**佣金明细编号 数据类型bigint(14)*/
	@Id
	private Long commisionDetailId;
	
	/**佣金操作类型：0:入，1：冲，默认0 数据类型tinyint(1)*/
	private Integer operateType;
	
	/**业务编号 数据类型bigint(16)*/
	private Long bizCode;
	
	/**业务类型 数据类型tinyint(2)*/
	private Integer bizType;
	
	/**提佣金额 数据类型double(8,2)*/
	private Double bizAmount;
	
	/**订单总金额 数据类型double(8,2)*/
	private Double orderAmount;
	
	/**订单收货日期 数据类型datetime*/
	private Date orderReceiptTime;
	
	/**卡券金额 数据类型double(8,2)*/
	private Double couponAmount;
	
	/**佣金 数据类型double(8,2)*/
	private Double commision;
	
	/**佣金比率 数据类型float(4,2)*/
	private Float commisionRate;
	
	/**订单编号 数据类型bigint(16)*/
	private Long orderCode;
	
	/**订单状态(关联到订单的状态) 数据类型tinyint(1)*/
	private Integer orderStatus;
	
	/**汇总状态(0：未汇总，1：已汇总，默认为0) 数据类型tinyint(1)*/
	private Integer collectStatus;
	
	/**是否间接提佣 (0:不是，1：是，默认为0) 数据类型tinyint(1)*/
	private Integer indirect;
	
	/**订单提佣者 数据类型bigint(12)*/
	private Long promoterId;
	
	/**提佣者类型 数据类型tinyint(1)*/
	private Integer promoterType;
	
	/**贡献者*/
	private Long giver;
	
	/**购买者账号 数据类型bigint(14)*/
	private Long memberId;
	
	/**创建者 数据类型varchar(32)*/
	private String createUser;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**更新者 数据类型varchar(32)*/
	private String updateUser;
	
	/**更新时间 数据类型datetime*/
	private Date updateTime;
	
	@Virtual
	private String memberAccount;
	@Virtual
	private List<Long> allchildPromoterids;
	/**开始时间*/
	@Virtual
	private Date createBeginTime;
	/**结束时间*/
	@Virtual
	private Date createEndTime;
	
	public String getOperateTypeCn(){
		return DssConstant.ACCOUNT_TYPE.getCnName(operateType);
	}
	
	public String getBizTypeCn(){
		return DssConstant.BUSSINESS_TYPE.getCnName(bizType);
	}
	
	public String getOrderStatusCn(){
		return OrderConstant.ORDER_STATUS.getCnName(orderStatus);
	}
	
	public String getCollectStatusCn(){
		return DssConstant.COLLECT_STATUS.getCnName(collectStatus);
	}
	
	public String getIndirectCn(){
		return DssConstant.INDIRECT_TYPE.getCnName(indirect);
	}
	public Long getCommisionDetailId(){
		return commisionDetailId;
	}
	public Integer getOperateType(){
		return operateType;
	}
	public Long getBizCode(){
		return bizCode;
	}
	public Integer getBizType(){
		return bizType;
	}
	public Double getBizAmount(){
		return bizAmount;
	}
	public Double getOrderAmount(){
		return orderAmount;
	}
	public Date getOrderReceiptTime(){
		return orderReceiptTime;
	}
	public Double getCouponAmount(){
		return couponAmount;
	}
	public Double getCommision(){
		return commision;
	}
	public Float getCommisionRate(){
		return commisionRate;
	}
	public Integer getOrderStatus(){
		return orderStatus;
	}
	public Integer getCollectStatus(){
		return collectStatus;
	}
	public Long getPromoterId(){
		return promoterId;
	}
	public Long getMemberId(){
		return memberId;
	}
	public String getCreateUser(){
		return createUser;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public String getUpdateUser(){
		return updateUser;
	}
	public Date getUpdateTime(){
		return updateTime;
	}
	public void setCommisionDetailId(Long commisionDetailId){
		this.commisionDetailId=commisionDetailId;
	}
	public void setOperateType(Integer operateType){
		this.operateType=operateType;
	}
	public void setBizCode(Long bizCode){
		this.bizCode=bizCode;
	}
	public void setBizType(Integer bizType){
		this.bizType=bizType;
	}
	public void setBizAmount(Double bizAmount){
		this.bizAmount=bizAmount;
	}
	public void setOrderAmount(Double orderAmount){
		this.orderAmount=orderAmount;
	}
	public void setOrderReceiptTime(Date orderReceiptTime){
		this.orderReceiptTime=orderReceiptTime;
	}
	public void setCouponAmount(Double couponAmount){
		this.couponAmount=couponAmount;
	}
	public void setCommision(Double commision){
		this.commision=commision;
	}
	public void setCommisionRate(Float commisionRate){
		this.commisionRate=commisionRate;
	}
	public void setOrderStatus(Integer orderStatus){
		this.orderStatus=orderStatus;
	}
	public void setCollectStatus(Integer collectStatus){
		this.collectStatus=collectStatus;
	}
	public void setPromoterId(Long promoterId){
		this.promoterId=promoterId;
	}
	public void setMemberId(Long memberId){
		this.memberId=memberId;
	}
	public void setCreateUser(String createUser){
		this.createUser=createUser;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setUpdateUser(String updateUser){
		this.updateUser=updateUser;
	}
	public void setUpdateTime(Date updateTime){
		this.updateTime=updateTime;
	}
	public Long getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(Long orderCode) {
		this.orderCode = orderCode;
	}

	public Integer getPromoterType() {
		return promoterType;
	}

	public void setPromoterType(Integer promoterType) {
		this.promoterType = promoterType;
	}

	public String getMemberAccount() {
		return memberAccount;
	}

	public void setMemberAccount(String memberAccount) {
		this.memberAccount = memberAccount;
	}

	public Integer getIndirect() {
		return indirect;
	}

	public void setIndirect(Integer indirect) {
		this.indirect = indirect;
	}

	public Long getGiver() {
		return giver;
	}

	public void setGiver(Long giver) {
		this.giver = giver;
	}

	public List<Long> getAllchildPromoterids() {
		return allchildPromoterids;
	}

	public void setAllchildPromoterids(List<Long> allchildPromoterids) {
		this.allchildPromoterids = allchildPromoterids;
	}

	public Date getCreateBeginTime() {
		return createBeginTime;
	}

	public void setCreateBeginTime(Date createBeginTime) {
		this.createBeginTime = createBeginTime;
	}

	public Date getCreateEndTime() {
		return createEndTime;
	}

	public void setCreateEndTime(Date createEndTime) {
		this.createEndTime = createEndTime;
	}

	
}
