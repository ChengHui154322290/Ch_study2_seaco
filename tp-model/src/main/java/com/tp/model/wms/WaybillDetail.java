package com.tp.model.wms;

import java.io.Serializable;
import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.common.vo.wms.WmsWaybillConstant.WaybillStatus;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 运单号使用详情表
  */
public class WaybillDetail extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1464753650984L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**运单号 数据类型bigint(20)*/
	private Long waybillNo;
	
	/**运单号申请单号 数据类型bigint(20)*/
	private Long applicationId;
	
	/**快递企业编码 数据类型varchar(30)*/
	private String logisticsCode;
	
	/**快递企业名称 数据类型varchar(30)*/
	private String logisticsName;
	
	/**订单号 数据类型varchar(50)*/
	private String orderCode;
	
	/**运单号使用状态 数据类型tinyint(1)*/
	private Integer status;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**更新时间 数据类型datetime*/
	private Date updateTime;
	
	/**备注 数据类型varchar(255)*/
	private String remark;
	
	public String getStatusStr(){
		return WaybillStatus.getDesc(status);
	}
	
	public Long getId(){
		return id;
	}
	public Long getWaybillNo(){
		return waybillNo;
	}
	public Long getApplicationId(){
		return applicationId;
	}
	public String getLogisticsCode(){
		return logisticsCode;
	}
	public String getLogisticsName(){
		return logisticsName;
	}
	public String getOrderCode(){
		return orderCode;
	}
	public Integer getStatus(){
		return status;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Date getUpdateTime(){
		return updateTime;
	}
	public String getRemark(){
		return remark;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setWaybillNo(Long waybillNo){
		this.waybillNo=waybillNo;
	}
	public void setApplicationId(Long applicationId){
		this.applicationId=applicationId;
	}
	public void setLogisticsCode(String logisticsCode){
		this.logisticsCode=logisticsCode;
	}
	public void setLogisticsName(String logisticsName){
		this.logisticsName=logisticsName;
	}
	public void setOrderCode(String orderCode){
		this.orderCode=orderCode;
	}
	public void setStatus(Integer status){
		this.status=status;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setUpdateTime(Date updateTime){
		this.updateTime=updateTime;
	}
	public void setRemark(String remark){
		this.remark=remark;
	}
}
