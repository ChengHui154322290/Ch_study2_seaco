package com.tp.model.stg;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 标杆系统入库单反馈

  */
public class InputBack extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450403690112L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**仓库id 数据类型bigint(20)*/
	private Long warehouseId;
	
	/**客户采购单号 数据类型varchar(50)*/
	private String customerOrderNo;
	
	/**仓库编号对应第三方仓库 数据类型varchar(50)*/
	private String warehouseCode;
	
	/**仓内入库流水号 数据类型varchar(50)*/
	private String asnno;
	
	/**预计到货时间 数据类型datetime*/
	private Date expectedArriveTime;
	
	/**记录创建时间 数据类型datetime*/
	private Date createTime;
	
	
	public Long getId(){
		return id;
	}
	public Long getWarehouseId(){
		return warehouseId;
	}
	public String getCustomerOrderNo(){
		return customerOrderNo;
	}
	public String getWarehouseCode(){
		return warehouseCode;
	}
	public String getAsnno(){
		return asnno;
	}
	public Date getExpectedArriveTime(){
		return expectedArriveTime;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setWarehouseId(Long warehouseId){
		this.warehouseId=warehouseId;
	}
	public void setCustomerOrderNo(String customerOrderNo){
		this.customerOrderNo=customerOrderNo;
	}
	public void setWarehouseCode(String warehouseCode){
		this.warehouseCode=warehouseCode;
	}
	public void setAsnno(String asnno){
		this.asnno=asnno;
	}
	public void setExpectedArriveTime(Date expectedArriveTime){
		this.expectedArriveTime=expectedArriveTime;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
}
