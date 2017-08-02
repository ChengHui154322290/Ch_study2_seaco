package com.tp.model.wms;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 
  */
public class StockasnFact extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1467702259992L;

	/**主键 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**入库订单号 数据类型bigint(20)*/
	private Long stockasnId;
	
	/**采购单号 数据类型varchar(45)*/
	private String orderCode;
	
	/**仓库Id 数据类型bigint(20)*/
	private Long warehouseId;
	
	/**仓库CODE
 数据类型varchar(45)*/
	private String warehouseCode;
	
	/**审核人 数据类型varchar(45)*/
	private String auditor;
	
	/**审核时间 数据类型datetime*/
	private Date auditTime;
	
	/**电商编码 数据类型varchar(45)*/
	private String providerCode;
	
	/**货主 数据类型varchar(100)*/
	private String goodsOwner;
	
	/**备注 数据类型varchar(500)*/
	private String remark;
	
	/** 数据类型datetime*/
	private Date createTime;
	
	
	public Long getId(){
		return id;
	}
	public Long getStockasnId(){
		return stockasnId;
	}
	public String getOrderCode(){
		return orderCode;
	}
	public Long getWarehouseId(){
		return warehouseId;
	}
	public String getWarehouseCode(){
		return warehouseCode;
	}
	public String getAuditor(){
		return auditor;
	}
	public Date getAuditTime(){
		return auditTime;
	}
	public String getProviderCode(){
		return providerCode;
	}
	public String getGoodsOwner(){
		return goodsOwner;
	}
	public String getRemark(){
		return remark;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setStockasnId(Long stockasnId){
		this.stockasnId=stockasnId;
	}
	public void setOrderCode(String orderCode){
		this.orderCode=orderCode;
	}
	public void setWarehouseId(Long warehouseId){
		this.warehouseId=warehouseId;
	}
	public void setWarehouseCode(String warehouseCode){
		this.warehouseCode=warehouseCode;
	}
	public void setAuditor(String auditor){
		this.auditor=auditor;
	}
	public void setAuditTime(Date auditTime){
		this.auditTime=auditTime;
	}
	public void setProviderCode(String providerCode){
		this.providerCode=providerCode;
	}
	public void setGoodsOwner(String goodsOwner){
		this.goodsOwner=goodsOwner;
	}
	public void setRemark(String remark){
		this.remark=remark;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
}
