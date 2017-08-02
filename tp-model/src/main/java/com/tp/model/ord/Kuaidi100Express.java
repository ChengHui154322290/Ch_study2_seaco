package com.tp.model.ord;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 快递100推送的快递日志记录
  */
public class Kuaidi100Express extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451468597509L;

	/**主键ID 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**子订单编号 数据类型varchar(64)*/
	private Long orderCode;
	
	/**快递单号 数据类型varchar(64)*/
	private String packageNo;
	
	/**退货单号 数据类型varchar(64)*/
	private Long rejectCode;
	
	/**物流订单类型:0:订购，1:退货 数据类型tinyint(1)*/
	private Integer deliveryOrderType;
	
	/**监控状态 数据类型varchar(16)*/
	private String monitorStatus;
	
	/**监控状态相关消息 数据类型varchar(32)*/
	private String monitorMessage;
	
	/**状态 数据类型varchar(32)*/
	private String status;
	
	/**快递公司ID 数据类型varchar(64)*/
	private String companyId;
	
	/**是否签收标记 数据类型varchar(4)*/
	private String isCheck;
	
	/**内容 数据类型varchar(128)*/
	private String dataContext;
	
	/**时间，原始格式 数据类型varchar(32)*/
	private String dataTime;
	
	/**格式化后时间 数据类型varchar(32)*/
	private String dataFtime;
	
	/**推送的具体内容 数据类型text*/
	private String postData;
	
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
	public String getMonitorStatus(){
		return monitorStatus;
	}
	public String getMonitorMessage(){
		return monitorMessage;
	}
	public String getStatus(){
		return status;
	}
	public String getCompanyId(){
		return companyId;
	}
	public String getIsCheck(){
		return isCheck;
	}
	public String getDataContext(){
		return dataContext;
	}
	public String getDataTime(){
		return dataTime;
	}
	public String getDataFtime(){
		return dataFtime;
	}
	public String getPostData(){
		return postData;
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
	public void setMonitorStatus(String monitorStatus){
		this.monitorStatus=monitorStatus;
	}
	public void setMonitorMessage(String monitorMessage){
		this.monitorMessage=monitorMessage;
	}
	public void setStatus(String status){
		this.status=status;
	}
	public void setCompanyId(String companyId){
		this.companyId=companyId;
	}
	public void setIsCheck(String isCheck){
		this.isCheck=isCheck;
	}
	public void setDataContext(String dataContext){
		this.dataContext=dataContext;
	}
	public void setDataTime(String dataTime){
		this.dataTime=dataTime;
	}
	public void setDataFtime(String dataFtime){
		this.dataFtime=dataFtime;
	}
	public void setPostData(String postData){
		this.postData=postData;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
}
