package com.tp.model.prd;

import java.io.Serializable;
import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.common.annotation.excel.poi.ExcelProperty;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 商品导入明细表
  */
public class ItemImportList extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1452422037905L;

	/**主键 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**item_import_log的主键  数据类型bigint(10)*/
	private Long logId;
	
	/**商家名称 数据类型varchar(100)*/
	private String spName;
	
	/***商家ID 数据类型varchar(50)*/
	private String spId;
	
	/***条形码（商品码） 数据类型varchar(50)*/
	private String barcode;
	
	/***SPU名称 数据类型varchar(255)*/
	private String mainTitle;
	
	/**大类名称 数据类型varchar(100)*/
	private String largeName;
	
	/**中类名称 数据类型varchar(100)*/
	private String mediumName;
	
	/**小类名称 数据类型varchar(100)*/
	private String smallName;
	
	/***大类ID 数据类型varchar(50)*/
	private String largeId;
	
	/***中类ID 数据类型varchar(50)*/
	private String mediumId;
	
	/***小类ID 数据类型varchar(50)*/
	private String smallId;
	
	/**单位 数据类型varchar(100)*/
	private String unitName;
	
	/**单位ID 数据类型varchar(50)*/
	private String unitId;
	
	/**品牌 数据类型varchar(100)*/
	private String brandName;
	
	/***品牌ID 数据类型varchar(50)*/
	private String brandId;
	
	/***SKU名称 数据类型varchar(255)*/
	private String detailMainTitle;
	
	/**销售维度1 数据类型varchar(100)*/
	private String spec1Name;
	
	/**销售维度1ID 数据类型varchar(50)*/
	private String spec1Id;
	
	/**销售维度2 数据类型varchar(100)*/
	private String spec2Name;
	
	/**销售维度2ID 数据类型varchar(50)*/
	private String spec2Id;
	
	/**销售维度3 数据类型varchar(100)*/
	private String spec3Name;
	
	/**销售维度3ID 数据类型varchar(50)*/
	private String spec3Id;
	
	/***商品卖点 数据类型varchar(500)*/
	private String subTitle;
	
	/***商品类型 数据类型varchar(11)*/
	private String itemType;
	
	/***规格 数据类型varchar(500)*/
	private String specifications;
	
	/***无理由退货期限 数据类型varchar(50)*/
	private String returnDays;
	
	/***吊牌价 数据类型varchar(50)*/
	private String basicPrice;
	
	/***运费 数据类型varchar(100)*/
	private String freightTemplateName;
	
	/***运费模板ID 数据类型varchar(50)*/
	private String freightTemplateId;
	
	/***毛重 数据类型varchar(50)*/
	private String weight;
	
	/**生产厂家 数据类型varchar(255)*/
	private String manufacturer;
	
	/***箱规 数据类型varchar(255)*/
	private String cartonSpec;
	
	/**是否有效期管理 数据类型varchar(11)*/
	private String expSign;
	
	/**有效期 数据类型varchar(50)*/
	private String expDays;
	
	/**是否海淘商品 数据类型varchar(11)*/
	private String wavesSign;
	
	/**自定义属性组 数据类型text*/
	private String attibuteCus;
	
	/**导入的excel行号 数据类型bigint(8)*/
	private Long excelIndex;
	
	/**状态:1-为导入成功，2-为导入失败，默认1 数据类型int(1)*/
	private Integer status;
	
	/**导入操作信息 数据类型text*/
	private String opMessage;
	
	/**spu的编码 数据类型varchar(20)*/
	private String spuCode;
	
	/**prdid 数据类型varchar(20)*/
	private String prdid;
	
	/**sku的编码 数据类型varchar(20)*/
	private String skuCode;
	
	/**适用年龄Id：关联base里面的数据字典 数据类型varchar(100)*/
	private String applyAge;
	
	/**适用年龄Id：关联base里面的数据字典 数据类型varchar(50)*/
	private String applyAgeId;
	
	/**产地 数据类型varchar(32)*/
	private String origin;
	
	/**产地ID 数据类型int(8)*/
	private Long originId;
	
	/**行邮税 数据类型varchar(50)*/
	private String incomeTaxTate;
	
	/**行邮税id 数据类型int(8)*/
	private Long incomeTaxTateId;
	
	private String customsRateName;
	private Long customsRateId;
	
	private String exciseRateName;
	private Long exciseRateId;
	
	private String addedvalueRateName;
	private Long addedvalueRateId;
	
	
	/**海关备案信息 数据类型varchar(350)*/
	private String customsBackup;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	
	/**海淘商品配送方式***/
	@Virtual
	private String sendTypeName;
	
	/**海淘商品配送方式***/
	@Virtual
	private String sendType;
	
	/** *货号 */
	@Virtual
	private String articleNumber;

	/** 海关 */
	@Virtual
	private String bondedAreaName;
	
	/** *海关ID */
	@Virtual
	private Integer bondedArea;
	
	public Long getId(){
		return id;
	}
	public Long getLogId(){
		return logId;
	}
	public String getSpName(){
		return spName;
	}
	public String getSpId(){
		return spId;
	}
	public String getBarcode(){
		return barcode;
	}
	public String getMainTitle(){
		return mainTitle;
	}
	public String getLargeName(){
		return largeName;
	}
	public String getMediumName(){
		return mediumName;
	}
	public String getSmallName(){
		return smallName;
	}
	public String getLargeId(){
		return largeId;
	}
	public String getMediumId(){
		return mediumId;
	}
	public String getSmallId(){
		return smallId;
	}
	public String getUnitName(){
		return unitName;
	}
	public String getUnitId(){
		return unitId;
	}
	public String getBrandName(){
		return brandName;
	}
	public String getBrandId(){
		return brandId;
	}
	public String getDetailMainTitle(){
		return detailMainTitle;
	}
	public String getSpec1Name(){
		return spec1Name;
	}
	public String getSpec1Id(){
		return spec1Id;
	}
	public String getSpec2Name(){
		return spec2Name;
	}
	public String getSpec2Id(){
		return spec2Id;
	}
	public String getSpec3Name(){
		return spec3Name;
	}
	public String getSpec3Id(){
		return spec3Id;
	}
	public String getSubTitle(){
		return subTitle;
	}
	public String getItemType(){
		return itemType;
	}
	public String getSpecifications(){
		return specifications;
	}
	public String getReturnDays(){
		return returnDays;
	}
	public String getBasicPrice(){
		return basicPrice;
	}
	public String getFreightTemplateName(){
		return freightTemplateName;
	}
	public String getFreightTemplateId(){
		return freightTemplateId;
	}
	public String getWeight(){
		return weight;
	}
	public String getManufacturer(){
		return manufacturer;
	}
	public String getCartonSpec(){
		return cartonSpec;
	}
	public String getExpSign(){
		return expSign;
	}
	public String getExpDays(){
		return expDays;
	}
	public String getWavesSign(){
		return wavesSign;
	}
	public String getAttibuteCus(){
		return attibuteCus;
	}
	public Long getExcelIndex(){
		return excelIndex;
	}
	public Integer getStatus(){
		return status;
	}
	public String getOpMessage(){
		return opMessage;
	}
	public String getSpuCode(){
		return spuCode;
	}
	public String getPrdid(){
		return prdid;
	}
	public String getSkuCode(){
		return skuCode;
	}
	public String getApplyAge(){
		return applyAge;
	}
	public String getApplyAgeId(){
		return applyAgeId;
	}
	public String getOrigin(){
		return origin;
	}
	public Long getOriginId(){
		return originId;
	}
	public String getIncomeTaxTate(){
		return incomeTaxTate;
	}
	public Long getIncomeTaxTateId(){
		return incomeTaxTateId;
	}
	public String getCustomsBackup(){
		return customsBackup;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setLogId(Long logId){
		this.logId=logId;
	}
	public void setSpName(String spName){
		this.spName=spName;
	}
	public void setSpId(String spId){
		this.spId=spId;
	}
	public void setBarcode(String barcode){
		this.barcode=barcode;
	}
	public void setMainTitle(String mainTitle){
		this.mainTitle=mainTitle;
	}
	public void setLargeName(String largeName){
		this.largeName=largeName;
	}
	public void setMediumName(String mediumName){
		this.mediumName=mediumName;
	}
	public void setSmallName(String smallName){
		this.smallName=smallName;
	}
	public void setLargeId(String largeId){
		this.largeId=largeId;
	}
	public void setMediumId(String mediumId){
		this.mediumId=mediumId;
	}
	public void setSmallId(String smallId){
		this.smallId=smallId;
	}
	public void setUnitName(String unitName){
		this.unitName=unitName;
	}
	public void setUnitId(String unitId){
		this.unitId=unitId;
	}
	public void setBrandName(String brandName){
		this.brandName=brandName;
	}
	public void setBrandId(String brandId){
		this.brandId=brandId;
	}
	public void setDetailMainTitle(String detailMainTitle){
		this.detailMainTitle=detailMainTitle;
	}
	public void setSpec1Name(String spec1Name){
		this.spec1Name=spec1Name;
	}
	public void setSpec1Id(String spec1Id){
		this.spec1Id=spec1Id;
	}
	public void setSpec2Name(String spec2Name){
		this.spec2Name=spec2Name;
	}
	public void setSpec2Id(String spec2Id){
		this.spec2Id=spec2Id;
	}
	public void setSpec3Name(String spec3Name){
		this.spec3Name=spec3Name;
	}
	public void setSpec3Id(String spec3Id){
		this.spec3Id=spec3Id;
	}
	public void setSubTitle(String subTitle){
		this.subTitle=subTitle;
	}
	public void setItemType(String itemType){
		this.itemType=itemType;
	}
	public void setSpecifications(String specifications){
		this.specifications=specifications;
	}
	public void setReturnDays(String returnDays){
		this.returnDays=returnDays;
	}
	public void setBasicPrice(String basicPrice){
		this.basicPrice=basicPrice;
	}
	public void setFreightTemplateName(String freightTemplateName){
		this.freightTemplateName=freightTemplateName;
	}
	public void setFreightTemplateId(String freightTemplateId){
		this.freightTemplateId=freightTemplateId;
	}
	public void setWeight(String weight){
		this.weight=weight;
	}
	public void setManufacturer(String manufacturer){
		this.manufacturer=manufacturer;
	}
	public void setCartonSpec(String cartonSpec){
		this.cartonSpec=cartonSpec;
	}
	public void setExpSign(String expSign){
		this.expSign=expSign;
	}
	public void setExpDays(String expDays){
		this.expDays=expDays;
	}
	public void setWavesSign(String wavesSign){
		this.wavesSign=wavesSign;
	}
	public void setAttibuteCus(String attibuteCus){
		this.attibuteCus=attibuteCus;
	}
	public void setExcelIndex(Long excelIndex){
		this.excelIndex=excelIndex;
	}
	public void setStatus(Integer status){
		this.status=status;
	}
	public void setOpMessage(String opMessage){
		this.opMessage=opMessage;
	}
	public void setSpuCode(String spuCode){
		this.spuCode=spuCode;
	}
	public void setPrdid(String prdid){
		this.prdid=prdid;
	}
	public void setSkuCode(String skuCode){
		this.skuCode=skuCode;
	}
	public void setApplyAge(String applyAge){
		this.applyAge=applyAge;
	}
	public void setApplyAgeId(String applyAgeId){
		this.applyAgeId=applyAgeId;
	}
	public void setOrigin(String origin){
		this.origin=origin;
	}
	public void setOriginId(Long originId){
		this.originId=originId;
	}
	public void setIncomeTaxTate(String incomeTaxTate){
		this.incomeTaxTate=incomeTaxTate;
	}
	public void setIncomeTaxTateId(Long incomeTaxTateId){
		this.incomeTaxTateId=incomeTaxTateId;
	}
	public void setCustomsBackup(String customsBackup){
		this.customsBackup=customsBackup;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public String getSendTypeName() {
		return sendTypeName;
	}
	public void setSendTypeName(String sendTypeName) {
		this.sendTypeName = sendTypeName;
	}
	public String getSendType() {
		return sendType;
	}
	public void setSendType(String sendType) {
		this.sendType = sendType;
	}
	public String getArticleNumber() {
		return articleNumber;
	}
	public void setArticleNumber(String articleNumber) {
		this.articleNumber = articleNumber;
	}
	public String getBondedAreaName() {
		return bondedAreaName;
	}
	public void setBondedAreaName(String bondedAreaName) {
		this.bondedAreaName = bondedAreaName;
	}
	public Integer getBondedArea() {
		return bondedArea;
	}
	public void setBondedArea(Integer bondedArea) {
		this.bondedArea = bondedArea;
	}
	public String getCustomsRateName() {
		return customsRateName;
	}
	public void setCustomsRateName(String customsRateName) {
		this.customsRateName = customsRateName;
	}
	public Long getCustomsRateId() {
		return customsRateId;
	}
	public void setCustomsRateId(Long customsRateId) {
		this.customsRateId = customsRateId;
	}
	public String getExciseRateName() {
		return exciseRateName;
	}
	public void setExciseRateName(String exciseRateName) {
		this.exciseRateName = exciseRateName;
	}
	public Long getExciseRateId() {
		return exciseRateId;
	}
	public void setExciseRateId(Long exciseRateId) {
		this.exciseRateId = exciseRateId;
	}
	public String getAddedvalueRateName() {
		return addedvalueRateName;
	}
	public void setAddedvalueRateName(String addedvalueRateName) {
		this.addedvalueRateName = addedvalueRateName;
	}
	public Long getAddedvalueRateId() {
		return addedvalueRateId;
	}
	public void setAddedvalueRateId(Long addedvalueRateId) {
		this.addedvalueRateId = addedvalueRateId;
	}
}
