package com.tp.model.prd;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.Map;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.dto.prd.enums.ItemStatusEnum;
import com.tp.model.BaseDO;
import com.tp.util.BigDecimalUtil;
/**
  * @author szy
  * SPU对应SKU维度信息
  */
public class ItemSku extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450873698779L;

	/**主键 数据类型bigint(18)*/
	@Id
	private Long id;
	
	/**对应商品详情编码 数据类型bigint(18)*/
	private Long detailId;
	
	/**prdid+2位数流水码1 数据类型varchar(50)*/
	private String sku;
	
	/**商品ID 数据类型bigint(18)*/
	private Long itemId;
	
	/**冗余字段 商品名称 数据类型varchar(255)*/
	private String detailName;
	
	/**spu编码 冗余字段 数据类型varchar(50)*/
	private String spu;
	
	/**prdid编码 数据类型varchar(50)*/
	private String prdid;
	
	/**单位id 冗余字段 数据类型bigint(20)*/
	private Long unitId;
	
	/**品牌id 冗余字段 数据类型bigint(20)*/
	private Long brandId;
	
	/**商品类型 冗余字段 1-正常商品，2-服务商品，3-二手商品,4-报废商品 默认1 数据类型int(1)*/
	private Integer itemType;
	
	/**商品所属三级类别code 冗余字段 数据类型varchar(50)*/
	private String categoryCode;
	
	/**商品所属三级类别id 数据类型bigint(20)*/
	private Long categoryId;
	
	/**条形码 冗余字段 数据类型varchar(20)*/
	private String barcode;
	
	/**市场价 数据类型double(11,2)*/
	private Double basicPrice;
	
	/**促销价 数据类型double(11,2)*/
	private Double topicPrice;
	
	/**0-未上架 1-上架 2-作废 数据类型tinyint(4)*/
	private Integer status;
	
	/**销售类型 0-自营 1-平台 数据类型tinyint(4)*/
	private Integer saleType;
	
	/**供应商名称 数据类型varchar(100)*/
	private String spName;
	
	/**供应商商品编码 数据类型varchar(100)*/
	private String spCode;
	
	/**供应商id 数据类型bigint(20)*/
	private Long spId;
	
	/**商家库存int(10)*/
	private Integer supplierStock;
	
	/**排序 数据类型int(8)*/
	private Integer sort;
	
	/**spu_name 冗余字段 数据类型varchar(255)*/
	private String spuName;
	
	/**商品类型标示 1 国内 2海淘 冗余字段 数据类型tinyint(4)*/
	private Integer wavesSign;
	
	/**数据来源 数据类型varchar(10)*/
	private String dataSource;
	
	/** 数据类型varchar(32)*/
	private String createUser;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/** 数据类型varchar(32)*/
	private String updateUser;
	
	/**更新时间 数据类型datetime*/
	private Date updateTime;
	/**绑定层级***/
	private String bindLevel;
	
	/**主库商品ID**/
	private Long majorItemId;
	/**主库detailID**/
	private Long majorDetailId;
	/***品牌名称**/
	private String brandName;
	
	/***单位名称**/
	private String unitName;
	
	/**审核状态**/
	private String auditStatus;
	
	/**分销提佣比率 数据类型double(5,2)*/
	private Double commisionRate;
	
	/**分销提佣类型 数据类型int(2)*/
	private Integer commisionType;
	@Virtual
	private Double startRate;
	
	@Virtual
	private Double endRate;
	
	/**成本价**/
	private Double costPrice;
	
	public Double getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(Double costPrice) {
		this.costPrice = costPrice;
	}

	public Float getDiscount(){
		if(basicPrice!=null && topicPrice!=null){
			return BigDecimalUtil.toPrice(BigDecimalUtil.multiply(10, (BigDecimalUtil.divide(topicPrice, basicPrice,6)))).floatValue();
		}
		return null;
	}
	
	public String getStatusDesc(){
		return ItemStatusEnum.getByValue(status).getKey();
	} 
	
	public Long getId(){
		return id;
	}
	public Long getDetailId(){
		return detailId;
	}
	public String getSku(){
		return sku;
	}
	public Long getItemId(){
		return itemId;
	}
	public String getDetailName(){
		return detailName;
	}
	public String getSpu(){
		return spu;
	}
	public String getPrdid(){
		return prdid;
	}
	public Long getUnitId(){
		return unitId;
	}
	public Long getBrandId(){
		return brandId;
	}
	public Integer getItemType(){
		return itemType;
	}
	public String getCategoryCode(){
		return categoryCode;
	}
	public Long getCategoryId(){
		return categoryId;
	}
	public String getBarcode(){
		return barcode;
	}
	public Double getBasicPrice(){
		return basicPrice;
	}
	public Integer getStatus(){
		return status;
	}
	public Integer getSaleType(){
		return saleType;
	}
	public String getSpName(){
		return spName;
	}
	public String getSpCode(){
		return spCode;
	}
	public Long getSpId(){
		return spId;
	}
	public Integer getSort(){
		return sort;
	}
	public String getSpuName(){
		return spuName;
	}
	public Integer getWavesSign(){
		return wavesSign;
	}
	public String getDataSource(){
		return dataSource;
	}
	public String getCreateUser(){
		return createUser;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public String getUpdateUser(){
		return updateUser;
	}
	public Date getUpdateTime(){
		return updateTime;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setDetailId(Long detailId){
		this.detailId=detailId;
	}
	public void setSku(String sku){
		this.sku=sku;
	}
	public void setItemId(Long itemId){
		this.itemId=itemId;
	}
	public void setDetailName(String detailName){
		this.detailName=detailName;
	}
	public void setSpu(String spu){
		this.spu=spu;
	}
	public void setPrdid(String prdid){
		this.prdid=prdid;
	}
	public void setUnitId(Long unitId){
		this.unitId=unitId;
	}
	public void setBrandId(Long brandId){
		this.brandId=brandId;
	}
	public void setItemType(Integer itemType){
		this.itemType=itemType;
	}
	public void setCategoryCode(String categoryCode){
		this.categoryCode=categoryCode;
	}
	public void setCategoryId(Long categoryId){
		this.categoryId=categoryId;
	}
	public void setBarcode(String barcode){
		this.barcode=barcode;
	}
	public void setBasicPrice(Double basicPrice){
		this.basicPrice=basicPrice;
	}
	public void setStatus(Integer status){
		this.status=status;
	}
	public void setSaleType(Integer saleType){
		this.saleType=saleType;
	}
	public void setSpName(String spName){
		this.spName=spName;
	}
	public void setSpCode(String spCode){
		this.spCode=spCode;
	}
	public void setSpId(Long spId){
		this.spId=spId;
	}
	public void setSort(Integer sort){
		this.sort=sort;
	}
	public void setSpuName(String spuName){
		this.spuName=spuName;
	}
	public void setWavesSign(Integer wavesSign){
		this.wavesSign=wavesSign;
	}
	public void setDataSource(String dataSource){
		this.dataSource=dataSource;
	}
	public void setCreateUser(String createUser){
		this.createUser=createUser;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setUpdateUser(String updateUser){
		this.updateUser=updateUser;
	}
	public void setUpdateTime(Date updateTime){
		this.updateTime=updateTime;
	}
	public String getBindLevel() {
		return bindLevel;
	}
	public void setBindLevel(String bindLevel) {
		this.bindLevel = bindLevel;
	}
	public Long getMajorItemId() {
		return majorItemId;
	}
	public void setMajorItemId(Long majorItemId) {
		this.majorItemId = majorItemId;
	}
	public Long getMajorDetailId() {
		return majorDetailId;
	}
	public void setMajorDetailId(Long majorDetailId) {
		this.majorDetailId = majorDetailId;
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
	public String getAuditStatus() {
		return auditStatus;
	}
	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}
	public Double getCommisionRate() {
		return commisionRate;
	}
	public void setCommisionRate(Double commisionRate) {
		this.commisionRate = commisionRate;
	}

	public Double getStartRate() {
		return startRate;
	}

	public void setStartRate(Double startRate) {
		this.startRate = startRate;
	}

	public Double getEndRate() {
		return endRate;
	}

	public void setEndRate(Double endRate) {
		this.endRate = endRate;
	}
	
	public void remove(Map<String, Object> param) {
		Field[] fields = ItemSku.class.getDeclaredFields();
		for (Field f : fields) {
			Virtual v = f.getAnnotation(Virtual.class);
			if(null != v)
				param.remove(f.getName());
		}
	}

	public Double getTopicPrice() {
		return topicPrice;
	}

	public void setTopicPrice(Double topicPrice) {
		this.topicPrice = topicPrice;
	}

	public Integer getSupplierStock() {
		return supplierStock;
	}

	public void setSupplierStock(Integer supplierStock) {
		this.supplierStock = supplierStock;
	}

	public Integer getCommisionType() {
		return commisionType;
	}

	public void setCommisionType(Integer commisionType) {
		this.commisionType = commisionType;
	}
	
	
	/**商品编码（第三方推送）**/
	private String goodsCode;
	/**货号（第三方推送）**/
	private String articleCode;

	public String getGoodsCode() {
		return goodsCode;
	}

	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}

	public String getArticleCode() {
		return articleCode;
	}

	public void setArticleCode(String articleCode) {
		this.articleCode = articleCode;
	}
	
}
