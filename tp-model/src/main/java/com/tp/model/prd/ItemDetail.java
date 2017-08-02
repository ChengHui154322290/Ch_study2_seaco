package com.tp.model.prd;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 商品基础信息prdid维度
  */
public class ItemDetail extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450873698777L;

	/**主键 数据类型bigint(18)*/
	@Id
	private Long id;
	
	/**供应商id*/
	private Long supplierId;
	
	/**绑定层级*/
	private String bindLevel;
	/**商品ID 数据类型bigint(20)*/
	private Long itemId;
	
	/**编号 spu+2位随机码 数据类型varchar(50)*/
	private String prdid;
	
	/**小类编号+4位流水码 冗余字段 数据类型varchar(11)*/
	private String spu;
	
	/**商品名称 数据类型varchar(60)*/
	private String itemTitle;
	
	/**产品前台展示名称 数据类型varchar(60)*/
	private String mainTitle;
	
	/**存储在仓库中的名称 数据类型varchar(60)*/
	private String storageTitle;
	
	/**卖点描述 数据类型varchar(100)*/
	private String subTitle;
	
	/**条码(全局唯一) 数据类型varchar(20)*/
	private String barcode;
	
	/**生产厂家 数据类型varchar(255)*/
	private String manufacturer;
	
	/**商品类型：1-正常商品，2-服务商品，3-二手商品,4-报废商品 默认1 数据类型int(1)*/
	private Integer itemType;
	
	/**市场价 数据类型double(11,2)*/
	private Double basicPrice;
	
	/**关税税率 数据类型bigint(20)*/
	private Long tarrifRate;
	
	/**销项税率 数据类型bigint(20)*/
	private Long saleRate;
	
	/**进项税率 数据类型bigint(20)*/
	private Long purRate;
	
	/**运费模板 数据类型int(11)*/
	private Long freightTemplateId;
	
	/**是否海淘商品,1-否，2-是，默认1 数据类型int(1)*/
	private Integer wavesSign;
	
	/**无理由退货期限 单位 天 数据类型int(5)*/
	private Integer returnDays;
	
	/**是否有效期管理:1 是，2-否 默认1 数据类型int(1)*/
	private Integer expSign;
	
	/**有效期月数 数据类型int(5)*/
	private Integer expDays;
	
	/**是否优质商品 :1是,2否 默认2 数据类型tinyint(4)*/
	private Integer qualityGoods;
	
	/**是否进口产品:1 是，2-否 默认2 数据类型int(1)*/
	private Integer importedSign;
	
	/**毛重 单位g 数据类型double(11,2)*/
	private Double weight;
	
	/**净重 单位g 数据类型double(11,2)*/
	private Double weightNet;
	
	/** 商品净数量 **/
	private Integer unitQuantity;
	
	/** 商品包装数量 */
	private Integer wrapQuantity;
	
	/**体积-宽度 数据类型int(11)*/
	private Integer volumeWidth;
	
	/**体积-长度 数据类型int(11)*/
	private Integer volumeLength;
	
	/**体积-高度 数据类型int(11)*/
	private Integer volumeHigh;
	
	/**品牌id 冗余字段 数据类型bigint(20)*/
	private Long brandId;
	
	/**品类三级code 冗余字段 数据类型varchar(50)*/
	private String categoryCode;
	
	/**单位id 冗余字段 数据类型bigint(20)*/
	private Long unitId;
	
	/**规格 数据类型varchar(255)*/
	private String specifications;
	
	/**箱规 数据类型varchar(255)*/
	private String cartonSpec;
	
	/**备注 数据类型varchar(255)*/
	private String remark;
	
	/**状态0-未上架 1-上架 2-作废 默认0 数据类型int(1)*/
	private Integer status;
	
	/**适用年龄Id：关联base里面的数据字典 数据类型bigint(20)*/
	private Long applyAgeId;
	
	/**海淘商品产地国家ID 数据类型bigint(20)*/
	private Long countryId;
	
	/**海淘商品配送方式 数据类型varchar(20)*/
	private String sendType;
	
	/**spu_name 冗余字段 数据类型varchar(255)*/
	private String spuName;
	
	/**产品库编号 数据类型varchar(20)*/
	private String btProCode;
	
	/**seagoor 最后更新时间 数据类型datetime*/
	private Date btLastUpdateTime;
	
	/**最后跟新页数 数据类型int(11)*/
	private Integer btLastUpdatePage;
	
	/**年龄段开始值 数据类型int(10)*/
	private Integer ageStartKey;
	
	/**年龄段结束的key 数据类型int(10)*/
	private Integer ageEndKey;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**创建人 数据类型varchar(32)*/
	private String createUser;
	
	/**修改时间 数据类型datetime*/
	private Date updateTime;
	
	/**修改人 数据类型varchar(32)*/
	private String updateUser;
	
	/**增值税率*/
	private Long addedValueRate;
	/**消费税率*/
	private Long exciseRate;
	/**关税率*/
	private Long customsRate;
	
	
	/** 规格组 id*/
	@Virtual
	private String specGroupIds;
	@Virtual
	private String onSaleStr;
	@Virtual
	private List<ItemDetailSpec> itemDetailSpecList;
	
	public Long getId(){
		return id;
	}
	public Long getItemId(){
		return itemId;
	}
	public String getPrdid(){
		return prdid;
	}
	public String getSpu(){
		return spu;
	}
	public String getItemTitle(){
		return itemTitle;
	}
	public String getMainTitle(){
		return mainTitle;
	}
	public String getStorageTitle(){
		return storageTitle;
	}
	public String getSubTitle(){
		return subTitle;
	}
	public String getBarcode(){
		return barcode;
	}
	public String getManufacturer(){
		return manufacturer;
	}
	public Integer getItemType(){
		return itemType;
	}
	public Double getBasicPrice(){
		return basicPrice;
	}
	public Long getTarrifRate(){
		return tarrifRate;
	}
	public Long getSaleRate(){
		return saleRate;
	}
	public Long getPurRate(){
		return purRate;
	}
	public Long getFreightTemplateId(){
		return freightTemplateId;
	}
	public Integer getWavesSign(){
		return wavesSign;
	}
	public Integer getReturnDays(){
		return returnDays;
	}
	public Integer getExpSign(){
		return expSign;
	}
	public Integer getExpDays(){
		return expDays;
	}
	public Integer getQualityGoods(){
		return qualityGoods;
	}
	public Integer getImportedSign(){
		return importedSign;
	}
	public Double getWeight(){
		return weight;
	}
	public Double getWeightNet(){
		return weightNet;
	}
	public Integer getVolumeWidth(){
		return volumeWidth;
	}
	public Integer getVolumeLength(){
		return volumeLength;
	}
	public Integer getVolumeHigh(){
		return volumeHigh;
	}
	public Long getBrandId(){
		return brandId;
	}
	public String getCategoryCode(){
		return categoryCode;
	}
	public Long getUnitId(){
		return unitId;
	}
	public String getSpecifications(){
		return specifications;
	}
	public String getCartonSpec(){
		return cartonSpec;
	}
	public String getRemark(){
		return remark;
	}
	public Integer getStatus(){
		return status;
	}
	public Long getApplyAgeId(){
		return applyAgeId;
	}
	public Long getCountryId(){
		return countryId;
	}
	public String getSendType(){
		return sendType;
	}
	public String getSpuName(){
		return spuName;
	}
	public String getBtProCode(){
		return btProCode;
	}
	public Date getBtLastUpdateTime(){
		return btLastUpdateTime;
	}
	public Integer getBtLastUpdatePage(){
		return btLastUpdatePage;
	}
	public Integer getAgeStartKey(){
		return ageStartKey;
	}
	public Integer getAgeEndKey(){
		return ageEndKey;
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
	public void setItemId(Long itemId){
		this.itemId=itemId;
	}
	public void setPrdid(String prdid){
		this.prdid=prdid;
	}
	public void setSpu(String spu){
		this.spu=spu;
	}
	public void setItemTitle(String itemTitle){
		this.itemTitle=itemTitle;
	}
	public void setMainTitle(String mainTitle){
		this.mainTitle=mainTitle;
	}
	public void setStorageTitle(String storageTitle){
		this.storageTitle=storageTitle;
	}
	public void setSubTitle(String subTitle){
		this.subTitle=subTitle;
	}
	public void setBarcode(String barcode){
		this.barcode=barcode;
	}
	public void setManufacturer(String manufacturer){
		this.manufacturer=manufacturer;
	}
	public void setItemType(Integer itemType){
		this.itemType=itemType;
	}
	public void setBasicPrice(Double basicPrice){
		this.basicPrice=basicPrice;
	}
	public void setTarrifRate(Long tarrifRate){
		this.tarrifRate=tarrifRate;
	}
	public void setSaleRate(Long saleRate){
		this.saleRate=saleRate;
	}
	public void setPurRate(Long purRate){
		this.purRate=purRate;
	}
	public void setFreightTemplateId(Long freightTemplateId){
		this.freightTemplateId=freightTemplateId;
	}
	public void setWavesSign(Integer wavesSign){
		this.wavesSign=wavesSign;
	}
	public void setReturnDays(Integer returnDays){
		this.returnDays=returnDays;
	}
	public void setExpSign(Integer expSign){
		this.expSign=expSign;
	}
	public void setExpDays(Integer expDays){
		this.expDays=expDays;
	}
	public void setQualityGoods(Integer qualityGoods){
		this.qualityGoods=qualityGoods;
	}
	public void setImportedSign(Integer importedSign){
		this.importedSign=importedSign;
	}
	public void setWeight(Double weight){
		this.weight=weight;
	}
	public void setWeightNet(Double weightNet){
		this.weightNet=weightNet;
	}
	public void setVolumeWidth(Integer volumeWidth){
		this.volumeWidth=volumeWidth;
	}
	public void setVolumeLength(Integer volumeLength){
		this.volumeLength=volumeLength;
	}
	public void setVolumeHigh(Integer volumeHigh){
		this.volumeHigh=volumeHigh;
	}
	public void setBrandId(Long brandId){
		this.brandId=brandId;
	}
	public void setCategoryCode(String categoryCode){
		this.categoryCode=categoryCode;
	}
	public void setUnitId(Long unitId){
		this.unitId=unitId;
	}
	public void setSpecifications(String specifications){
		this.specifications=specifications;
	}
	public void setCartonSpec(String cartonSpec){
		this.cartonSpec=cartonSpec;
	}
	public void setRemark(String remark){
		this.remark=remark;
	}
	public void setStatus(Integer status){
		this.status=status;
	}
	public void setApplyAgeId(Long applyAgeId){
		this.applyAgeId=applyAgeId;
	}
	public void setCountryId(Long countryId){
		this.countryId=countryId;
	}
	public void setSendType(String sendType){
		this.sendType=sendType;
	}
	public void setSpuName(String spuName){
		this.spuName=spuName;
	}
	public void setBtProCode(String btProCode){
		this.btProCode=btProCode;
	}
	public void setBtLastUpdateTime(Date btLastUpdateTime){
		this.btLastUpdateTime=btLastUpdateTime;
	}
	public void setBtLastUpdatePage(Integer btLastUpdatePage){
		this.btLastUpdatePage=btLastUpdatePage;
	}
	public void setAgeStartKey(Integer ageStartKey){
		this.ageStartKey=ageStartKey;
	}
	public void setAgeEndKey(Integer ageEndKey){
		this.ageEndKey=ageEndKey;
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
	public String getSpecGroupIds() {
		return specGroupIds;
	}
	public void setSpecGroupIds(String specGroupIds) {
		this.specGroupIds = specGroupIds;
	}
	public void setOnSaleStr(String onSaleStr) {
		this.onSaleStr = onSaleStr;
	}
	public String getOnSaleStr() {
		return onSaleStr;
	}
	public List<ItemDetailSpec> getItemDetailSpecList() {
		return itemDetailSpecList;
	}
	public void setItemDetailSpecList(List<ItemDetailSpec> itemDetailSpecList) {
		this.itemDetailSpecList = itemDetailSpecList;
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
	public Long getAddedValueRate() {
		if(null==addedValueRate){
			addedValueRate = 0l;
		}
		return addedValueRate;
	}
	public void setAddedValueRate(Long addedValueRate) {
		this.addedValueRate = addedValueRate;
	}
	public Long getExciseRate() {
		if(null==exciseRate){
			exciseRate = 0l;
		}
		return exciseRate;
	}
	public void setExciseRate(Long exciseRate) {
		this.exciseRate = exciseRate;
	}
	public Long getCustomsRate() {
		if(null==customsRate){
			customsRate = 0l;
		}
		return customsRate;
	}
	public void setCustomsRate(Long customsRate) {
		this.customsRate = customsRate;
	}
	public Integer getUnitQuantity() {
		return unitQuantity;
	}
	public void setUnitQuantity(Integer unitQuantity) {
		this.unitQuantity = unitQuantity;
	}
	public Integer getWrapQuantity() {
		return wrapQuantity;
	}
	public void setWrapQuantity(Integer wrapQuantity) {
		this.wrapQuantity = wrapQuantity;
	}
}
