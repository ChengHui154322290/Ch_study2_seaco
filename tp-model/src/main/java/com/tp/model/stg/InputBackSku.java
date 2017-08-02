package com.tp.model.stg;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 入库单反馈

  */
public class InputBackSku extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450403690112L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**关联入库反馈id 数据类型bigint(20)*/
	private Long inputBackId;
	
	/**即sku 数据类型varchar(50)*/
	private String skuCode;
	
	/**条形码 数据类型varchar(50)*/
	private String barcode;
	
	/**收货时间 数据类型varchar(50)*/
	private String receivedTime;
	
	/**应实收数量 数据类型double*/
	private Double expectedQty;
	
	/**实际收入数量 数据类型double*/
	private Double receivedQty;
	
	/** 数据类型varchar(50)*/
	private String lotatt01;
	
	/** 数据类型varchar(50)*/
	private String lotatt02;
	
	/** 数据类型varchar(50)*/
	private String lotatt03;
	
	/** 数据类型varchar(50)*/
	private String lotatt04;
	
	/** 数据类型varchar(50)*/
	private String lotatt05;
	
	/** 数据类型varchar(50)*/
	private String lotatt06;
	
	/**批次号 数据类型varchar(50)*/
	private String lotatt07;
	
	/** 数据类型varchar(50)*/
	private String lotatt08;
	
	/** 数据类型varchar(50)*/
	private String lotatt09;
	
	/** 数据类型varchar(50)*/
	private String lotatt10;
	
	/** 数据类型varchar(50)*/
	private String lotatt11;
	
	/** 数据类型varchar(50)*/
	private String lotatt12;
	
	/**记录创建时间 数据类型datetime*/
	private Date createTime;
	
	
	public Long getId(){
		return id;
	}
	public Long getInputBackId(){
		return inputBackId;
	}
	public String getSkuCode(){
		return skuCode;
	}
	public String getBarcode(){
		return barcode;
	}
	public String getReceivedTime(){
		return receivedTime;
	}
	public Double getExpectedQty(){
		return expectedQty;
	}
	public Double getReceivedQty(){
		return receivedQty;
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
	public String getLotatt04(){
		return lotatt04;
	}
	public String getLotatt05(){
		return lotatt05;
	}
	public String getLotatt06(){
		return lotatt06;
	}
	public String getLotatt07(){
		return lotatt07;
	}
	public String getLotatt08(){
		return lotatt08;
	}
	public String getLotatt09(){
		return lotatt09;
	}
	public String getLotatt10(){
		return lotatt10;
	}
	public String getLotatt11(){
		return lotatt11;
	}
	public String getLotatt12(){
		return lotatt12;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setInputBackId(Long inputBackId){
		this.inputBackId=inputBackId;
	}
	public void setSkuCode(String skuCode){
		this.skuCode=skuCode;
	}
	public void setBarcode(String barcode){
		this.barcode=barcode;
	}
	public void setReceivedTime(String receivedTime){
		this.receivedTime=receivedTime;
	}
	public void setExpectedQty(Double expectedQty){
		this.expectedQty=expectedQty;
	}
	public void setReceivedQty(Double receivedQty){
		this.receivedQty=receivedQty;
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
	public void setLotatt04(String lotatt04){
		this.lotatt04=lotatt04;
	}
	public void setLotatt05(String lotatt05){
		this.lotatt05=lotatt05;
	}
	public void setLotatt06(String lotatt06){
		this.lotatt06=lotatt06;
	}
	public void setLotatt07(String lotatt07){
		this.lotatt07=lotatt07;
	}
	public void setLotatt08(String lotatt08){
		this.lotatt08=lotatt08;
	}
	public void setLotatt09(String lotatt09){
		this.lotatt09=lotatt09;
	}
	public void setLotatt10(String lotatt10){
		this.lotatt10=lotatt10;
	}
	public void setLotatt11(String lotatt11){
		this.lotatt11=lotatt11;
	}
	public void setLotatt12(String lotatt12){
		this.lotatt12=lotatt12;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
}
