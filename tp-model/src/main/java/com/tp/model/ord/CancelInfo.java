package com.tp.model.ord;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.model.BaseDO;
import com.tp.model.ord.CancelItem;
import com.tp.model.ord.CancelLog;
/**
  * @author szy
  * 取消单
  */
public class CancelInfo extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451468597506L;

	/**取消单ID 数据类型bigint(20)*/
	@Id
	private Long cancelId;
	
	/**关联子订单编号 数据类型bigint(16)*/
	private Long orderCode;
	
	/**取消单编号 数据类型bigint(16)*/
	private Long cancelCode;
	
	/**退款金额 数据类型double(10,2)*/
	private Double refundAmount;
	
	/**取消单状态 数据类型tinyint(4)*/
	private Integer cancelStatus;
	
	/**备注 数据类型varchar(500)*/
	private String remarks;
	
	/**退款编号 数据类型bigint(16)*/
	private Long refundCode;
	
	/**供应商ID 数据类型bigint(20)*/
	private Long supplierId;
	
	/**供应商名称 数据类型varchar(32)*/
	private String supplierName;
	
	/**用户ID 数据类型bigint(20)*/
	private Long userId;
	
	/**用户姓名 数据类型varchar(32)*/
	private String userName;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**创建人 数据类型varchar(32)*/
	private String createUser;
	
	/**更新时间 数据类型datetime*/
	private Date updateTime;
	
	/**更新人 数据类型varchar(32)*/
	private String updateUser;
	
	@Virtual
	private List<CancelItem> cancelItemList = new ArrayList<CancelItem>();
	@Virtual
	private List<CancelLog> cancelLogList = new ArrayList<CancelLog>();
	
	public Long getCancelId(){
		return cancelId;
	}
	public Long getOrderCode(){
		return orderCode;
	}
	public Long getCancelCode(){
		return cancelCode;
	}
	public Double getRefundAmount(){
		return refundAmount;
	}
	public Integer getCancelStatus(){
		return cancelStatus;
	}
	public String getRemarks(){
		return remarks;
	}
	public Long getRefundCode(){
		return refundCode;
	}
	public Long getSupplierId(){
		return supplierId;
	}
	public String getSupplierName(){
		return supplierName;
	}
	public Long getUserId(){
		return userId;
	}
	public String getUserName(){
		return userName;
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
	public void setCancelId(Long cancelId){
		this.cancelId=cancelId;
	}
	public void setOrderCode(Long orderCode){
		this.orderCode=orderCode;
	}
	public void setCancelCode(Long cancelCode){
		this.cancelCode=cancelCode;
	}
	public void setRefundAmount(Double refundAmount){
		this.refundAmount=refundAmount;
	}
	public void setCancelStatus(Integer cancelStatus){
		this.cancelStatus=cancelStatus;
	}
	public void setRemarks(String remarks){
		this.remarks=remarks;
	}
	public void setRefundCode(Long refundCode){
		this.refundCode=refundCode;
	}
	public void setSupplierId(Long supplierId){
		this.supplierId=supplierId;
	}
	public void setSupplierName(String supplierName){
		this.supplierName=supplierName;
	}
	public void setUserId(Long userId){
		this.userId=userId;
	}
	public void setUserName(String userName){
		this.userName=userName;
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
	public List<CancelItem> getCancelItemList() {
		return cancelItemList;
	}
	public void setCancelItemList(List<CancelItem> cancelItemList) {
		this.cancelItemList = cancelItemList;
	}
	public List<CancelLog> getCancelLogList() {
		return cancelLogList;
	}
	public void setCancelLogList(List<CancelLog> cancelLogList) {
		this.cancelLogList = cancelLogList;
	}
}
