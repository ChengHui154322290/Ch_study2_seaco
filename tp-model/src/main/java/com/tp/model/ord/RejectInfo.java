package com.tp.model.ord;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.common.vo.ord.RejectConstant;
import com.tp.model.BaseDO;
import com.tp.model.ord.RejectItem;
import com.tp.model.ord.RejectLog;
/**
  * @author szy
  * 退货单
  */
public class RejectInfo extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451468597516L;

	/**退货单ID 数据类型bigint(20) unsigned*/
	@Id
	private Long rejectId;
	
	/**退货类型 数据类型tinyint(4)*/
	private Integer rejectType;
	
	/**子订单编号 数据类型bigint(20)*/
	private Long orderCode;
	
	/**退货单编号(24+yymmdd+（退货单ID，不足8位前补0）) 数据类型bigint(20)*/
	private Long rejectCode;
	
	/**退款金额 数据类型double(10,2)*/
	private Double refundAmount;
	
	/**返还西客币 数据类型int(8)*/
	private Integer points;
	
	/**补偿金额 数据类型double(10,2)*/
	private Double offsetAmount;
	
	/**退货单状态 数据类型tinyint(4)*/
	private Integer rejectStatus;
	
	/**核审状态：待客服审核-1，待商家审核-2，客服审核通过-3，客服审核不通过-4，商家审核通过-5，商家审核不通过-6 数据类型tinyint(4)*/
	private Integer auditStatus;
	
	/**退货原因 数据类型varchar(100)*/
	private String rejectReason;
	
	/**联系人手机号 数据类型varchar(16)*/
	private String linkMobile;
	
	/**联系人姓名 数据类型varchar(16)*/
	private String linkMan;
	
	/**用户凭证URL 数据类型varchar(500)*/
	private String buyerImgUrl;
	
	/**商家凭证URL 数据类型varchar(500)*/
	private String sellerImgUrl;
	
	/**用户说明 数据类型varchar(500)*/
	private String buyerRemarks;
	
	/**商家说明 数据类型varchar(500)*/
	private String sellerRemarks;
	
	/**备注 数据类型varchar(500)*/
	private String remarks;
	
	/**退款单编号 数据类型bigint(16)*/
	private Long refundCode;
	
	/**补偿单编号 数据类型bigint(16)*/
	private Long offsetCode;
	
	/**供应商备案号 数据类型varchar(32)*/
	private String customCode;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**创建人 数据类型varchar(32)*/
	private String createUser;
	
	/**更新时间 数据类型datetime*/
	private Date updateTime;
	
	/**更新人 数据类型varchar(32)*/
	private String updateUser;
	
	/** 数据类型varchar(32)*/
	private String supplierName;
	
	/** 数据类型bigint(18)*/
	private Long supplierId;
	
	/** 数据类型bigint(18)*/
	private Long userId;
	
	/**寄回地址 数据类型varchar(200)*/
	private String returnAddress;
	
	/**退回货物快递单号 数据类型varchar(64)*/
	private String expressNo;
	
	/**物流公司名称 数据类型varchar(32)*/
	private String companyName;
	
	/**物流公司编号 数据类型varchar(32)*/
	private String companyCode;
	
	/**是否成功推送给快递100平台,0:没有成功，1:成功 数据类型tinyint(1)*/
	private Integer postKuaidi100;
	
	/**推送快递100次数 数据类型tinyint(1)*/
	private Integer postKuaidi100Times;
	
	/**退货商家联系人 数据类型varchar(32)*/
	private String returnContact;
	
	/**退货商家联系电话 数据类型varchar(16)*/
	private String returnMobile;
	
	// 实付行小计
	@Virtual
	private Double subTotal;
	
	// 售后次数
	@Virtual
	private Integer custServCount;


	@Virtual
	private List<RejectItem> rejectItemList = new ArrayList<RejectItem>();
	@Virtual
	private List<RejectLog> rejectLogList = new ArrayList<RejectLog>();
	@Virtual
	private Integer operatorType;
	@Virtual
	private Boolean success;
	@Virtual
	private Long rejectItemId;
	@Virtual
	private String packageNo;
	
	public String getZhRejectStatus() {
		if (rejectStatus != null) {
			return RejectConstant.REJECT_STATUS.getCnName(rejectStatus);
		}
		return null;
	}

	public String getZhAuditStatus() {
		if (auditStatus != null) {
			return RejectConstant.REJECT_AUDIT_STATUS.getCnName(auditStatus);
		}
		return null;
	}
	public Long getRejectId(){
		return rejectId;
	}
	public Integer getRejectType(){
		return rejectType;
	}
	public Long getOrderCode(){
		return orderCode;
	}
	public Long getRejectCode(){
		return rejectCode;
	}
	public Double getRefundAmount(){
		return refundAmount;
	}
	public Double getOffsetAmount(){
		return offsetAmount;
	}
	public Integer getRejectStatus(){
		return rejectStatus;
	}
	public Integer getAuditStatus(){
		return auditStatus;
	}
	public String getRejectReason(){
		return rejectReason;
	}
	public String getLinkMobile(){
		return linkMobile;
	}
	public String getLinkMan(){
		return linkMan;
	}
	public String getBuyerImgUrl(){
		return buyerImgUrl;
	}
	public String getSellerImgUrl(){
		return sellerImgUrl;
	}
	public String getBuyerRemarks(){
		return buyerRemarks;
	}
	public String getSellerRemarks(){
		return sellerRemarks;
	}
	public String getRemarks(){
		return remarks;
	}
	public Long getRefundCode(){
		return refundCode;
	}
	public Long getOffsetCode(){
		return offsetCode;
	}
	public String getCustomCode(){
		return customCode;
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
	public String getSupplierName(){
		return supplierName;
	}
	public Long getSupplierId(){
		return supplierId;
	}
	public Long getUserId(){
		return userId;
	}
	public String getReturnAddress(){
		return returnAddress;
	}
	public String getExpressNo(){
		return expressNo;
	}
	public String getCompanyName(){
		return companyName;
	}
	public String getCompanyCode(){
		return companyCode;
	}
	public Integer getPostKuaidi100(){
		return postKuaidi100;
	}
	public Integer getPostKuaidi100Times(){
		return postKuaidi100Times;
	}
	public String getReturnContact(){
		return returnContact;
	}
	public String getReturnMobile(){
		return returnMobile;
	}
	public void setRejectId(Long rejectId){
		this.rejectId=rejectId;
	}
	public void setRejectType(Integer rejectType){
		this.rejectType=rejectType;
	}
	public void setOrderCode(Long orderCode){
		this.orderCode=orderCode;
	}
	public void setRejectCode(Long rejectCode){
		this.rejectCode=rejectCode;
	}
	public void setRefundAmount(Double refundAmount){
		this.refundAmount=refundAmount;
	}
	public void setOffsetAmount(Double offsetAmount){
		this.offsetAmount=offsetAmount;
	}
	public void setRejectStatus(Integer rejectStatus){
		this.rejectStatus=rejectStatus;
	}
	public void setAuditStatus(Integer auditStatus){
		this.auditStatus=auditStatus;
	}
	public void setRejectReason(String rejectReason){
		this.rejectReason=rejectReason;
	}
	public void setLinkMobile(String linkMobile){
		this.linkMobile=linkMobile;
	}
	public void setLinkMan(String linkMan){
		this.linkMan=linkMan;
	}
	public void setBuyerImgUrl(String buyerImgUrl){
		this.buyerImgUrl=buyerImgUrl;
	}
	public void setSellerImgUrl(String sellerImgUrl){
		this.sellerImgUrl=sellerImgUrl;
	}
	public void setBuyerRemarks(String buyerRemarks){
		this.buyerRemarks=buyerRemarks;
	}
	public void setSellerRemarks(String sellerRemarks){
		this.sellerRemarks=sellerRemarks;
	}
	public void setRemarks(String remarks){
		this.remarks=remarks;
	}
	public void setRefundCode(Long refundCode){
		this.refundCode=refundCode;
	}
	public void setOffsetCode(Long offsetCode){
		this.offsetCode=offsetCode;
	}
	public void setCustomCode(String customCode){
		this.customCode=customCode;
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
	public void setSupplierName(String supplierName){
		this.supplierName=supplierName;
	}
	public void setSupplierId(Long supplierId){
		this.supplierId=supplierId;
	}
	public void setUserId(Long userId){
		this.userId=userId;
	}
	public void setReturnAddress(String returnAddress){
		this.returnAddress=returnAddress;
	}
	public void setExpressNo(String expressNo){
		this.expressNo=expressNo;
	}
	public void setCompanyName(String companyName){
		this.companyName=companyName;
	}
	public void setCompanyCode(String companyCode){
		this.companyCode=companyCode;
	}
	public void setPostKuaidi100(Integer postKuaidi100){
		this.postKuaidi100=postKuaidi100;
	}
	public void setPostKuaidi100Times(Integer postKuaidi100Times){
		this.postKuaidi100Times=postKuaidi100Times;
	}
	public void setReturnContact(String returnContact){
		this.returnContact=returnContact;
	}
	public void setReturnMobile(String returnMobile){
		this.returnMobile=returnMobile;
	}

	public List<RejectItem> getRejectItemList() {
		return rejectItemList;
	}

	public void setRejectItemList(List<RejectItem> rejectItemList) {
		this.rejectItemList = rejectItemList;
	}

	public List<RejectLog> getRejectLogList() {
		return rejectLogList;
	}

	public void setRejectLogList(List<RejectLog> rejectLogList) {
		this.rejectLogList = rejectLogList;
	}

	public Integer getOperatorType() {
		return operatorType;
	}

	public void setOperatorType(Integer operatorType) {
		this.operatorType = operatorType;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public Long getRejectItemId() {
		return rejectItemId;
	}

	public void setRejectItemId(Long rejectItemId) {
		this.rejectItemId = rejectItemId;
	}

	public String getPackageNo() {
		return packageNo;
	}

	public void setPackageNo(String packageNo) {
		this.packageNo = packageNo;
	}

	public Double getSubTotal() {
		return subTotal;
	}
	
	public void  setSubTotal(Double subTotal) {
		this.subTotal = subTotal;
	}		
	
	public Integer getCustServCount() {
		return custServCount;
	}
	
	public void setCustServCount(Integer custServCount) {
		this.custServCount	= custServCount;
	}

	public Integer getPoints() {
		return points==null?0:points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}
}
