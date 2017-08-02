package com.tp.model.ord;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 快递100订阅记录，即推送快递单号给快递100平台
  */
public class Kuaidi100Subscribe extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451468597510L;

	/**主键ID 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**子订单编号 数据类型bigint(20)*/
	private Long orderCode;
	
	/**快递单号 数据类型varchar(32)*/
	private String packageNo;
	
	/**退货单号 数据类型bigint(20)*/
	private Long rejectCode;
	
	/**物流订单类型:0:订购，1:退货 数据类型tinyint(1)*/
	private Integer deliveryOrderType;
	
	/**物流公司ID 数据类型varchar(32)*/
	private String companyId;
	
	/**出发城市中文描述 数据类型varchar(64)*/
	private String startCity;
	
	/**到达城市中文描述 数据类型varchar(64)*/
	private String endCity;
	
	/**请求参数完整记录 数据类型varchar(512)*/
	private String reqData;
	
	/**响应结果 数据类型varchar(8)*/
	private String resResult;
	
	/**响应结果码 数据类型varchar(8)*/
	private String resReturnCode;
	
	/**响应结果描述 数据类型varchar(128)*/
	private String resReturnMessage;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	
	public Long getId(){
		return id;
	}
	public Long getOrderCode(){
		return orderCode;
	}
	public String getPackageNo(){
		return packageNo;
	}
	public Long getRejectCode(){
		return rejectCode;
	}
	public Integer getDeliveryOrderType(){
		return deliveryOrderType;
	}
	public String getCompanyId(){
		return companyId;
	}
	public String getStartCity(){
		return startCity;
	}
	public String getEndCity(){
		return endCity;
	}
	public String getReqData(){
		return reqData;
	}
	public String getResResult(){
		return resResult;
	}
	public String getResReturnCode(){
		return resReturnCode;
	}
	public String getResReturnMessage(){
		return resReturnMessage;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setOrderCode(Long orderCode){
		this.orderCode=orderCode;
	}
	public void setPackageNo(String packageNo){
		this.packageNo=packageNo;
	}
	public void setRejectCode(Long rejectCode){
		this.rejectCode=rejectCode;
	}
	public void setDeliveryOrderType(Integer deliveryOrderType){
		this.deliveryOrderType=deliveryOrderType;
	}
	public void setCompanyId(String companyId){
		this.companyId=companyId;
	}
	public void setStartCity(String startCity){
		this.startCity=startCity;
	}
	public void setEndCity(String endCity){
		this.endCity=endCity;
	}
	public void setReqData(String reqData){
		this.reqData=reqData;
	}
	public void setResResult(String resResult){
		this.resResult=resResult;
	}
	public void setResReturnCode(String resReturnCode){
		this.resReturnCode=resReturnCode;
	}
	public void setResReturnMessage(String resReturnMessage){
		this.resReturnMessage=resReturnMessage;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
}
