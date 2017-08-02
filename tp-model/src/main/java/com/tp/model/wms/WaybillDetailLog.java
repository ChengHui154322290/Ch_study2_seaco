package com.tp.model.wms;

import java.io.Serializable;
import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.common.vo.wms.WmsWaybillConstant.WaybillStatus;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 运单号详情日志
  */
public class WaybillDetailLog extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1466146540862L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**运单号 数据类型bigint(20)*/
	private Long waybillNo;
	
	/**订单号 数据类型varchar(50)*/
	private String orderCode;
	
	/**快递企业编码 数据类型varchar(30)*/
	private String logisticsCode;
	
	/**运单号之前状态 数据类型tinyint(1)*/
	private Integer preStatus;
	
	/**运单号最新状态 数据类型tinyint(1)*/
	private Integer curStatus;
	
	/**描述 数据类型varchar(100)*/
	private String content;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**更新时间 数据类型datetime*/
	private Date updateTime;
	
	/**备注 数据类型varchar(255)*/
	private String remark;
	
	public String getCurStatusStr(){
		return WaybillStatus.getDesc(curStatus);
	}
	
	public String getPreStatusStr(){
		return WaybillStatus.getDesc(preStatus);
	}
	
	public Long getId(){
		return id;
	}
	public Long getWaybillNo(){
		return waybillNo;
	}
	public String getOrderCode(){
		return orderCode;
	}
	public String getLogisticsCode(){
		return logisticsCode;
	}
	public Integer getPreStatus(){
		return preStatus;
	}
	public Integer getCurStatus(){
		return curStatus;
	}
	public String getContent(){
		return content;
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
	public void setOrderCode(String orderCode){
		this.orderCode=orderCode;
	}
	public void setLogisticsCode(String logisticsCode){
		this.logisticsCode=logisticsCode;
	}
	public void setPreStatus(Integer preStatus){
		this.preStatus=preStatus;
	}
	public void setCurStatus(Integer curStatus){
		this.curStatus=curStatus;
	}
	public void setContent(String content){
		this.content=content;
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
