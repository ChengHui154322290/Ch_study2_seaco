package com.tp.model.ord;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 费舍尔发货日志表
  */
public class FisherDeliveryLog extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451468597509L;

	/**主键ID 数据类型bigint(14)*/
	@Id
	private Long id;
	
	/**子订单编号 数据类型bigint(16)*/
	private Long orderCode;
	
	/**快递公司编码 数据类型varchar(32)*/
	private String companyNo;
	
	/**快递单号 数据类型varchar(32)*/
	private String expressNo;
	
	/**发货时间 数据类型datetime*/
	private Date shippingTime;
	
	/**发货详情 数据类型varchar(1024)*/
	private String shippingDetail;
	
	/**创建时间  数据类型datetime*/
	private Date createTime;
	
	
	public Long getId(){
		return id;
	}
	public Long getOrderCode(){
		return orderCode;
	}
	public String getCompanyNo(){
		return companyNo;
	}
	public String getExpressNo(){
		return expressNo;
	}
	public Date getShippingTime(){
		return shippingTime;
	}
	public String getShippingDetail(){
		return shippingDetail;
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
	public void setCompanyNo(String companyNo){
		this.companyNo=companyNo;
	}
	public void setExpressNo(String expressNo){
		this.expressNo=expressNo;
	}
	public void setShippingTime(Date shippingTime){
		this.shippingTime=shippingTime;
	}
	public void setShippingDetail(String shippingDetail){
		this.shippingDetail=shippingDetail;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
}
