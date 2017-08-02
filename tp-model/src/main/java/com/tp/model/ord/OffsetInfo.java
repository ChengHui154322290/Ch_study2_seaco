package com.tp.model.ord;

import java.io.Serializable;
import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.common.vo.ord.OffsetConstant;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 退款单
  */
public class OffsetInfo extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451468597510L;

	/**补偿ID 数据类型bigint(20)*/
	@Id
	private Long offsetId;
	
	/**补偿编号(21+yymmdd+lpad(补偿ID，0)) 数据类型bigint(20)*/
	private Long offsetCode;
	
	/**子订单编号 数据类型bigint(20)*/
	private Long orderCode;
	
	/**补偿类型 数据类型tinyint(4)*/
	private Integer offsetType;
	
	/**补偿金额 数据类型double(10,2)*/
	private Double offsetAmount;
	
	/**补偿原因 数据类型tinyint(4)*/
	private Integer offsetReason;
	
	/**补偿状态 数据类型tinyint(4)*/
	private Integer offsetStatus;
	
	/**补偿业务类型 数据类型char(10)*/
	private String offsetBizType;
	
	/**收款人银行 数据类型varchar(64)*/
	private String payeeBank;
	
	/**支付方式 数据类型tinyint(1)*/
	private Integer paymentModel;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**创建人 数据类型varchar(32)*/
	private String createUser;
	
	/**更新时间 数据类型datetime*/
	private Date updateTime;
	
	/**更新人 数据类型varchar(32)*/
	private String updateUser;
	
	/**下单人 数据类型varchar(32)*/
	private String orderCreateUser;
	
	/**补偿说明 数据类型varchar(500)*/
	private String remarks;
	
	/**银行账号 数据类型varchar(32)*/
	private String bankAccount;
	
	/**收款人手机 数据类型varchar(16)*/
	private String payeeMobile;
	
	/**收款人 数据类型varchar(32)*/
	private String payee;
	
	/**承担方 数据类型int(11)*/
	private Integer bear;
	
	/**交易流水号 数据类型varchar(32)*/
	private String serialNo;
	
	/**优惠券代码 数据类型varchar(32)*/
	private String couponCode;
	
	/**支付银行 数据类型varchar(64)*/
	private String payBank;
	
	/**支付银行账号 数据类型varchar(32)*/
	private String payBankAccount;
	
	public String getZhOffsetType(){
		return OffsetConstant.OFFSET_TYPE.getCnName(offsetType);
	}
	
	public String getZhOffsetStatus(){
		return OffsetConstant.OFFSET_STATUS.getCnName(offsetStatus);
	}
	
	public String getZhOffsetReason(){
		return OffsetConstant.OFFSET_REASON.getCnName(offsetReason);
	}

	public String getZhBear(){
		return OffsetConstant.OFFSET_BEAR.getCnName(bear);
	}
	
	public String getZhPaymentModel(){
		return OffsetConstant.PAYMENT_MODEL.getCnName(paymentModel);
	}
	public Long getOffsetId(){
		return offsetId;
	}
	public Long getOffsetCode(){
		return offsetCode;
	}
	public Long getOrderCode(){
		return orderCode;
	}
	public Integer getOffsetType(){
		return offsetType;
	}
	public Double getOffsetAmount(){
		return offsetAmount;
	}
	public Integer getOffsetReason(){
		return offsetReason;
	}
	public Integer getOffsetStatus(){
		return offsetStatus;
	}
	public String getOffsetBizType(){
		return offsetBizType;
	}
	public String getPayeeBank(){
		return payeeBank;
	}
	public Integer getPaymentModel(){
		return paymentModel;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public String getCreateUser(){
		return createUser;
	}
	public Date getUpdateTime(){
		return updateTime;
	}
	public String getUpdateUser(){
		return updateUser;
	}
	public String getOrderCreateUser(){
		return orderCreateUser;
	}
	public String getRemarks(){
		return remarks;
	}
	public String getBankAccount(){
		return bankAccount;
	}
	public String getPayeeMobile(){
		return payeeMobile;
	}
	public String getPayee(){
		return payee;
	}
	public Integer getBear(){
		return bear;
	}
	public String getSerialNo(){
		return serialNo;
	}
	public String getCouponCode(){
		return couponCode;
	}
	public String getPayBank(){
		return payBank;
	}
	public String getPayBankAccount(){
		return payBankAccount;
	}
	public void setOffsetId(Long offsetId){
		this.offsetId=offsetId;
	}
	public void setOffsetCode(Long offsetCode){
		this.offsetCode=offsetCode;
	}
	public void setOrderCode(Long orderCode){
		this.orderCode=orderCode;
	}
	public void setOffsetType(Integer offsetType){
		this.offsetType=offsetType;
	}
	public void setOffsetAmount(Double offsetAmount){
		this.offsetAmount=offsetAmount;
	}
	public void setOffsetReason(Integer offsetReason){
		this.offsetReason=offsetReason;
	}
	public void setOffsetStatus(Integer offsetStatus){
		this.offsetStatus=offsetStatus;
	}
	public void setOffsetBizType(String offsetBizType){
		this.offsetBizType=offsetBizType;
	}
	public void setPayeeBank(String payeeBank){
		this.payeeBank=payeeBank;
	}
	public void setPaymentModel(Integer paymentModel){
		this.paymentModel=paymentModel;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setCreateUser(String createUser){
		this.createUser=createUser;
	}
	public void setUpdateTime(Date updateTime){
		this.updateTime=updateTime;
	}
	public void setUpdateUser(String updateUser){
		this.updateUser=updateUser;
	}
	public void setOrderCreateUser(String orderCreateUser){
		this.orderCreateUser=orderCreateUser;
	}
	public void setRemarks(String remarks){
		this.remarks=remarks;
	}
	public void setBankAccount(String bankAccount){
		this.bankAccount=bankAccount;
	}
	public void setPayeeMobile(String payeeMobile){
		this.payeeMobile=payeeMobile;
	}
	public void setPayee(String payee){
		this.payee=payee;
	}
	public void setBear(Integer bear){
		this.bear=bear;
	}
	public void setSerialNo(String serialNo){
		this.serialNo=serialNo;
	}
	public void setCouponCode(String couponCode){
		this.couponCode=couponCode;
	}
	public void setPayBank(String payBank){
		this.payBank=payBank;
	}
	public void setPayBankAccount(String payBankAccount){
		this.payBankAccount=payBankAccount;
	}
}
