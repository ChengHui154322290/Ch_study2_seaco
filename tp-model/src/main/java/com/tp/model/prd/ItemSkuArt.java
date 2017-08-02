package com.tp.model.prd;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 商品通关信息表
  */
public class ItemSkuArt extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450873698779L;

	/**主键 数据类型bigint(18)*/
	@Id
	private Long id;
	
	/**sku号 数据类型varchar(20)*/
	private String sku;
	
	/**货号 数据类型varchar(20)*/
	private String articleNumber;
	
	/**通关渠道 数据类型bigint(18)*/
	private Long bondedArea;
	
	/**sku_id 数据类型bigint(20)*/
	private Long skuId;
	
	/**hs编码 数据类型varchar(200)*/
	private String hsCode;

	/** 海关备案的商品计量第一申报单位Code **/
	private String itemFirstUnitCode;
	
	/** 海关备案的商品计量第一申报单位 **/
	private String itemFirstUnit;
	
	/** 第一单位对应数量 **/
	private Double itemFirstUnitCount;
	
	/** 海关备案的商品计量第二申报单位Code **/
	private String itemSecondUnitCode;
	
	/** 海关备案的商品计量第二申报单位 **/
	private String itemSecondUnit;
	
	/** 第二单位对应数量 **/
	private Double itemSecondUnitCount;
	
	/** 海关备案的商品价 **/
	private Double itemPrice;
	
	/** 国检备案号 **/
	private String itemRecordNo; 
	
	/**商品报关名称 数据类型varchar(200)*/
	private String itemDeclareName;
	
	/**商品特征 数据类型varchar(500)*/
	private String itemFeature;
	
	/** 数据类型datetime*/
	private Date createTime;
	
	
	public Long getId(){
		return id;
	}
	public String getSku(){
		return sku;
	}
	public String getArticleNumber(){
		return articleNumber;
	}
	public Long getBondedArea(){
		return bondedArea;
	}
	public Long getSkuId(){
		return skuId;
	}
	public String getHsCode(){
		return hsCode;
	}
	public String getItemDeclareName(){
		return itemDeclareName;
	}
	public String getItemFeature(){
		return itemFeature;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setSku(String sku){
		this.sku=sku;
	}
	public void setArticleNumber(String articleNumber){
		this.articleNumber=articleNumber;
	}
	public void setBondedArea(Long bondedArea){
		this.bondedArea=bondedArea;
	}
	public void setSkuId(Long skuId){
		this.skuId=skuId;
	}
	public void setHsCode(String hsCode){
		this.hsCode=hsCode;
	}
	public void setItemDeclareName(String itemDeclareName){
		this.itemDeclareName=itemDeclareName;
	}
	public void setItemFeature(String itemFeature){
		this.itemFeature=itemFeature;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public Double getItemPrice() {
		return itemPrice;
	}
	public void setItemPrice(Double itemPrice) {
		this.itemPrice = itemPrice;
	}
	public String getItemRecordNo() {
		return itemRecordNo;
	}
	public void setItemRecordNo(String itemRecordNo) {
		this.itemRecordNo = itemRecordNo;
	}
	public String getItemFirstUnit() {
		return itemFirstUnit;
	}
	public void setItemFirstUnit(String itemFirstUnit) {
		this.itemFirstUnit = itemFirstUnit;
	}
	public String getItemSecondUnit() {
		return itemSecondUnit;
	}
	public void setItemSecondUnit(String itemSecondUnit) {
		this.itemSecondUnit = itemSecondUnit;
	}
	public String getItemFirstUnitCode() {
		return itemFirstUnitCode;
	}
	public void setItemFirstUnitCode(String itemFirstUnitCode) {
		this.itemFirstUnitCode = itemFirstUnitCode;
	}
	public String getItemSecondUnitCode() {
		return itemSecondUnitCode;
	}
	public void setItemSecondUnitCode(String itemSecondUnitCode) {
		this.itemSecondUnitCode = itemSecondUnitCode;
	}
	public Double getItemFirstUnitCount() {
		return itemFirstUnitCount;
	}
	public void setItemFirstUnitCount(Double itemFirstUnitCount) {
		this.itemFirstUnitCount = itemFirstUnitCount;
	}
	public Double getItemSecondUnitCount() {
		return itemSecondUnitCount;
	}
	public void setItemSecondUnitCount(Double itemSecondUnitCount) {
		this.itemSecondUnitCount = itemSecondUnitCount;
	}
}
