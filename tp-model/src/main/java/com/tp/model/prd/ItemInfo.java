package com.tp.model.prd;

import java.io.Serializable;
import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 商品基础信息
  */
public class ItemInfo extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450873698778L;

	/**主键 数据类型bigint(18)*/
	@Id
	private Long id;
	
	/**供应商ID  bigint(18)*/
	private Long supplierId;
	
	/**小类编号+4位流水码 数据类型varchar(11)*/
	private String spu;
	
	/**spu名称 数据类型varchar(255)*/
	private String mainTitle;
	
	/**关联的品牌 数据类型int(11)*/
	private Long brandId;
	
	/**大类ID 数据类型int(11)*/
	private Long largeId;
	
	/**中类ID 数据类型int(11)*/
	private Long mediumId;
	
	/**小类ID 数据类型int(11)*/
	private Long smallId;
	
	/**单位 数据类型int(11)*/
	private Long unitId;
	
	/**导入层级*/
	private String bindLevel;
	/**备注 数据类型varchar(200)*/
	private String remark;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**创建人 数据类型varchar(32)*/
	private String createUser;
	
	/**修改时间 数据类型datetime*/
	private Date updateTime;
	
	/**修改人 数据类型varchar(32)*/
	private String updateUser;
	
	/** 小类的编码 不做数据的存储*/
	@Virtual
	private String smallCode;
	
	/**大类名称 不做数据的存储,冗余查询 */
	@Virtual
	private String largeName;
	
	/**中类名称 不做数据的存储,冗余查询*/
	@Virtual
	private String mediumName;
	
	/**小类名称 不做数据的存储,冗余查询*/
	@Virtual
	private String smallName;
	
	/**品牌名称 不做数据的存储,冗余查询*/
	@Virtual
	private String brandName;
	
	/**单位名称 不做数据的存储,冗余查询*/
	@Virtual
	private String unitName;
	
	/** 生产厂家 */
	@Virtual
	private String manufacturer;

	/** 商品类型：1-正常商品，2-服务商品，3-二手商品,默认1 */
	@Virtual
	private Integer itemType;

	/** 基础价格（原价） */
	@Virtual
	private Double basicPrice;

	/** 西客商城价格 */
	@Virtual
	private Double xgPrice;

	/** 发布状态：1-未上架，2-已上架，3-作废 */
	@Virtual
	private Integer itemStatus;
	
	public Long getId(){
		return id;
	}
	public String getSpu(){
		return spu;
	}
	public String getMainTitle(){
		return mainTitle;
	}
	public Long getBrandId(){
		return brandId;
	}
	public Long getLargeId(){
		return largeId;
	}
	public Long getMediumId(){
		return mediumId;
	}
	public Long getSmallId(){
		return smallId;
	}
	public Long getUnitId(){
		return unitId;
	}
	public String getRemark(){
		return remark;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public String getCreateUser(){
		return createUser;
	}
	public Date getUpdateTime(){
		return updateTime;
	}
	public String getUpdateUser(){
		return updateUser;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setSpu(String spu){
		this.spu=spu;
	}
	public void setMainTitle(String mainTitle){
		this.mainTitle=mainTitle;
	}
	public void setBrandId(Long brandId){
		this.brandId=brandId;
	}
	public void setLargeId(Long largeId){
		this.largeId=largeId;
	}
	public void setMediumId(Long mediumId){
		this.mediumId=mediumId;
	}
	public void setSmallId(Long smallId){
		this.smallId=smallId;
	}
	public void setUnitId(Long unitId){
		this.unitId=unitId;
	}
	public void setRemark(String remark){
		this.remark=remark;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setCreateUser(String createUser){
		this.createUser=createUser;
	}
	public void setUpdateTime(Date updateTime){
		this.updateTime=updateTime;
	}
	public void setUpdateUser(String updateUser){
		this.updateUser=updateUser;
	}
	public String getSmallCode() {
		return smallCode;
	}
	public void setSmallCode(String smallCode) {
		this.smallCode = smallCode;
	}
	public String getLargeName() {
		return largeName;
	}
	public void setLargeName(String largeName) {
		this.largeName = largeName;
	}
	public String getMediumName() {
		return mediumName;
	}
	public void setMediumName(String mediumName) {
		this.mediumName = mediumName;
	}
	public String getSmallName() {
		return smallName;
	}
	public void setSmallName(String smallName) {
		this.smallName = smallName;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public Integer getItemType() {
		return itemType;
	}
	public void setItemType(Integer itemType) {
		this.itemType = itemType;
	}
	public Double getBasicPrice() {
		return basicPrice;
	}
	public void setBasicPrice(Double basicPrice) {
		this.basicPrice = basicPrice;
	}
	public Integer getItemStatus() {
		return itemStatus;
	}
	public void setItemStatus(Integer itemStatus) {
		this.itemStatus = itemStatus;
	}
	public Long getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}
	public String getBindLevel() {
		return bindLevel;
	}
	public void setBindLevel(String bindLevel) {
		this.bindLevel = bindLevel;
	}
	public Double getXgPrice() {
		return xgPrice;
	}
	public void setXgPrice(Double xgPrice) {
		this.xgPrice = xgPrice;
	}
}
