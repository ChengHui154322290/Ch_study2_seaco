package com.tp.model.ord;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.common.vo.OrderConstant;
import com.tp.common.vo.ord.RefundConstant;
import com.tp.model.BaseDO;
import com.tp.model.ord.RefundLog;
/**
  * @author szy
  * 退款单
  */
public class RefundInfo extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451468597515L;

	/**退款ID 数据类型bigint(20)*/
	@Id
	private Long refundId;
	
	/**退款编号(20+yy+mm+dd+lpad(退款id,0)) 数据类型bigint(20)*/
	private Long refundCode;
	
	/**子订单编号 数据类型bigint(20)*/
	private Long orderCode;
	
	/**退款类型 数据类型tinyint(4)*/
	private Integer refundType;
	
	/**退款金额 数据类型double(10,2)*/
	private Double refundAmount;
	
	/**退款状态 数据类型tinyint(4)*/
	private Integer refundStatus;
	
	/**备注 数据类型varchar(500)*/
	private String remarks;
	
	/**退款业务类型 数据类型tinyint(4)*/
	private Integer refundBizType;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**创建人 数据类型varchar(32)*/
	private String createUser;
	
	/**更新时间 数据类型datetime*/
	private Date updateTime;
	
	/**更新人 数据类型varchar(32)*/
	private String updateUser;
	
	/**支付网关ID 数据类型bigint(20)*/
	private Long gatewayId;
	
	@Virtual
	private List<RefundLog> refundLogList = new ArrayList<RefundLog>();
	
	@Virtual
	private List itemList = new ArrayList();
	
	public String getSkuNameList(){
		StringBuffer sb = new StringBuffer();
		if(CollectionUtils.isNotEmpty(itemList)){
			for(Object object:itemList){
				if(object instanceof CancelItem){
					CancelItem cancelItem = (CancelItem)object;
					sb.append(cancelItem.getItemName()).append(" \r\n");
				}else if(object instanceof RejectItem){
					RejectItem rejectItem = (RejectItem)object;
					sb.append(rejectItem.getItemName()).append(" \r\n");
				}
			}
		}
		return sb.toString();
	}
	public String getSkuQuantityList(){
		StringBuffer sb = new StringBuffer();
		if(CollectionUtils.isNotEmpty(itemList)){
			for(Object object:itemList){
				if(object instanceof CancelItem){
					CancelItem cancelItem = (CancelItem)object;
					sb.append(cancelItem.getItemRefundQuantity()).append(" \r\n");
				}else if(object instanceof RejectItem){
					RejectItem rejectItem = (RejectItem)object;
					sb.append(rejectItem.getItemRefundQuantity()).append(" \r\n");
				}
			}
		}
		return sb.toString();
	}
	public String getSkuCodeList(){
		StringBuffer sb = new StringBuffer();
		if(CollectionUtils.isNotEmpty(itemList)){
			for(Object object:itemList){
				if(object instanceof CancelItem){
					CancelItem cancelItem = (CancelItem)object;
					sb.append(cancelItem.getItemSkuCode()).append(" \r\n");
				}else if(object instanceof RejectItem){
					RejectItem rejectItem = (RejectItem)object;
					sb.append(rejectItem.getItemSkuCode()).append(" \r\n");
				}
			}
		}
		return sb.toString();
	}
	public String getGatewayName(){
		return OrderConstant.OrderPayWay.getCnName(gatewayId.intValue());
	}
	public String getZhRefundStatus(){
		return RefundConstant.REFUND_STATUS.getCnName(refundStatus);
	}
	
	public String getZhRefundType(){
		return RefundConstant.REFUND_TYPE.getCnName(refundType);
	}
	public Long getRefundId(){
		return refundId;
	}
	public Long getRefundCode(){
		return refundCode;
	}
	public Long getOrderCode(){
		return orderCode;
	}
	public Integer getRefundType(){
		return refundType;
	}
	public Double getRefundAmount(){
		return refundAmount;
	}
	public Integer getRefundStatus(){
		return refundStatus;
	}
	public String getRemarks(){
		return remarks;
	}
	public Integer getRefundBizType(){
		return refundBizType;
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
	public Long getGatewayId(){
		return gatewayId;
	}
	public void setRefundId(Long refundId){
		this.refundId=refundId;
	}
	public void setRefundCode(Long refundCode){
		this.refundCode=refundCode;
	}
	public void setOrderCode(Long orderCode){
		this.orderCode=orderCode;
	}
	public void setRefundType(Integer refundType){
		this.refundType=refundType;
	}
	public void setRefundAmount(Double refundAmount){
		this.refundAmount=refundAmount;
	}
	public void setRefundStatus(Integer refundStatus){
		this.refundStatus=refundStatus;
	}
	public void setRemarks(String remarks){
		this.remarks=remarks;
	}
	public void setRefundBizType(Integer refundBizType){
		this.refundBizType=refundBizType;
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
	public void setGatewayId(Long gatewayId){
		this.gatewayId=gatewayId;
	}
	public List<RefundLog> getRefundLogList() {
		return refundLogList;
	}
	public void setRefundLogList(List<RefundLog> refundLogList) {
		this.refundLogList = refundLogList;
	}
	public List getItemList() {
		return itemList;
	}
	public void setItemList(List itemList) {
		this.itemList = itemList;
	}
}
