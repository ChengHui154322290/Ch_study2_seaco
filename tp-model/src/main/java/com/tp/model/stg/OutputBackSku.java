package com.tp.model.stg;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 出库订单反馈sku信息列表

  */
public class OutputBackSku extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450403690114L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**出库反馈id 数据类型bigint(20)*/
	private Long outputBackId;
	
	/**sku编号 数据类型varchar(20)*/
	private String sku;
	
	/**条形码 数据类型varchar(20)*/
	private String barcode;
	
	/**出库数量 数据类型int(11)*/
	private Integer num;
	
	/**批次号 数据类型varchar(50)*/
	private String vendor;
	
	/** 数据类型varchar(50)*/
	private String lotatt01;
	
	/** 数据类型varchar(50)*/
	private String lotatt02;
	
	/** 数据类型varchar(50)*/
	private String lotatt03;
	
	/**记录创建时间 数据类型datetime*/
	private Date createTime;
	
	
	public Long getId(){
		return id;
	}
	public Long getOutputBackId(){
		return outputBackId;
	}
	public String getSku(){
		return sku;
	}
	public String getBarcode(){
		return barcode;
	}
	public Integer getNum(){
		return num;
	}
	public String getVendor(){
		return vendor;
	}
	public String getLotatt01(){
		return lotatt01;
	}
	public String getLotatt02(){
		return lotatt02;
	}
	public String getLotatt03(){
		return lotatt03;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setOutputBackId(Long outputBackId){
		this.outputBackId=outputBackId;
	}
	public void setSku(String sku){
		this.sku=sku;
	}
	public void setBarcode(String barcode){
		this.barcode=barcode;
	}
	public void setNum(Integer num){
		this.num=num;
	}
	public void setVendor(String vendor){
		this.vendor=vendor;
	}
	public void setLotatt01(String lotatt01){
		this.lotatt01=lotatt01;
	}
	public void setLotatt02(String lotatt02){
		this.lotatt02=lotatt02;
	}
	public void setLotatt03(String lotatt03){
		this.lotatt03=lotatt03;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
}
