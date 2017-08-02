package com.tp.model.ord;

import static com.tp.util.BigDecimalUtil.multiply;
import static com.tp.util.BigDecimalUtil.toPrice;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.common.vo.Constant;
import com.tp.dto.ord.SalePropertyDTO;
import com.tp.model.BaseDO;
import com.tp.util.BigDecimalUtil;
/**
  * @author szy
  * 订单行表
  */
public class OrderItem extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451468597512L;

	/**PK 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**订单ID 数据类型bigint(20)*/
	private Long parentOrderId;
	
	/**子订单ID 数据类型bigint(20)*/
	private Long orderId;
	
	/**会员id 数据类型bigint(20)*/
	private Long memberId;
	
	/**类型（1：商品行。2：赠品行） 数据类型tinyint(3) unsigned zerofill*/
	private Integer type;
	
	/** 数据类型bigint(20)*/
	private Long parentOrderCode;
	
	/** 数据类型bigint(20)*/
	private Long orderCode;
	
	/**商品ID 数据类型bigint(20)*/
	private Long spuId;
	
	/**商品编号 数据类型varchar(32)*/
	private String spuCode;
	
	/**商品名称 数据类型varchar(100)*/
	private String spuName;
	
	/**品牌ID 数据类型bigint(20)*/
	private Long brandId;
	
	/**品牌名称 数据类型varchar(50)*/
	private String brandName;
	
	/**SKU ID 数据类型bigint(20)*/
	private Long skuId;
	
	/**SKU编号 数据类型varchar(50)*/
	private String skuCode;
	
	/**SKU条形码 数据类型varchar(50)*/
	private String barCode;
	
	/**图片路径 数据类型varchar(255)*/
	private String img;
	
	/**商品版本 数据类型varchar(50)*/
	private String skuVersion;
	
	/**主题ID 数据类型bigint(20)*/
	private Long topicId;
	
	/** 主题是否预占库存标示：1预占库存0非预占库存 */
	private Integer topicInventoryFlag;
	
	public Integer getTopicInventoryFlag() {
		return topicInventoryFlag;
	}

	public void setTopicInventoryFlag(Integer topicInventoryFlag) {
		this.topicInventoryFlag = topicInventoryFlag;
	}

	/**供应商ID 数据类型bigint(20)*/
	private Long supplierId;
	
	/**供应商名称 数据类型varchar(50)*/
	private String supplierName;
	
	/**仓库ID 数据类型bigint(20)*/
	private Long warehouseId;
	
	/**入库批号 数据类型varchar(50)*/
	private String batchCode;
	
	/**销售类型 数据类型tinyint(3)*/
	private Integer salesType;
	
	/**销售属性 数据类型varchar(512)*/
	private String salesProperty;
	
	/**颜色 数据类型varchar(20)*/
	private String color;
	
	/**尺码 数据类型varchar(20)*/
	private String size;
	
	/**有效期-开始时间 数据类型datetime*/
	private Date startTime;
	
	/**有效期-结束时间 数据类型datetime*/
	private Date endTime;
	
	/**数量 数据类型int(11)*/
	private Integer quantity;
	
	/**吊牌价/市场价 数据类型double(10,2)*/
	private Double listPrice;
	
	/**销售价  数据类型double(10,2)*/
	private Double salesPrice;
	
	/**价格 数据类型double(10,2)*/
	private Double price;
	
	/**实付行小计 数据类型double(10,2)*/
	private Double subTotal;
	
	/**应付行小计 数据类型double(10,2)*/
	private Double originalSubTotal;
	
	/**原始税金 数据类型double(8,2)*/
	private Double origTaxFee;
	
	/**原始运费 数据类型double(8,2)*/
	private Double origFreight;
	
	/**行运费 数据类型double(10,2)*/
	private Double freight;
	
	/**商品重量(g) 数据类型double(10,2)*/
	private Double weight;
	
	/** 商品净重(g) 数据类型double(10,2) **/
	private Double weightNet;
	
	/**退款状态 数据类型tinyint(3)*/
	private Integer refundStatus;
	
	/**税率 数据类型double(10,2)*/
	private Double taxRate;
	
	/**行邮税号 数据类型varchar(11)*/
	private String parcelTaxId;
	
	/**下单IP地址 数据类型varchar(50)*/
	private String ip;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**修改时间 数据类型datetime*/
	private Date updateTime;
	
	/**货号 数据类型varchar(45)*/
	private String productCode;
	
	/**税费 数据类型double(10,2)*/
	private Double taxFee;
	
	/**退货限制天数 数据类型int(11)*/
	private Integer refundDays;
	
	/**单位 数据类型varchar(5)*/
	private String unit;
	
	/**佣金比率 数据类型double(10,2)*/
	private Double platformRate;
	
	/**结算状态 数据类型tinyint(2)*/
	private Integer settleStatus;
	
	/**优惠券金额 数据类型double(10,2)*/
	private Double orderCouponAmount;
	
	/**使用的积分 数据类型int(8)*/
	private Integer points;
	/**红包金额 数据类型double(10,2)*/
	private Double couponAmount;
	
	/**合同ID 数据类型bigint(14)*/
	private Long contractId;
	/**商品总金额 数据类型double(10,2)*/
	private Double itemAmount;
	/** 销售属性 */
	/**分销提佣比率 数据类型double(5,2)*/
	private Double commisionRate;
	
	/**增值税率*/
	private Double addedValueRate;
	/**消费税率*/
	private Double exciseRate;
	/**关税率*/
	private Double customsRate;
	
	//新增
	/**计量单位**/
	private Long unitId;
	
	/**原产地**/
	private Long countryId;
	
	/**多件装商品的净数量**/
	private Integer unitQuantity;
	
	/** 独立包装数 */
	private Integer wrapQuantity;
	
	@Virtual
	private Integer status;
	@Virtual
	private Long seaChannel;
	@Virtual
	private Integer storageType;
	@Virtual
	private Integer selected;
	/**退货次数*/
	@Virtual
	private Integer rejectCount;
	/**库存数量*/
	@Virtual
	private Integer stock;
	/**限购数量*/
	@Virtual
	private Integer limitCount;
	@Virtual
	private Boolean isSea=Boolean.FALSE;
	@Virtual
	private List<OrderPromotion> orderPromotionList = new ArrayList<OrderPromotion>();
	@Virtual
	private List<OrderPoint> orderPointList = new ArrayList<OrderPoint>();
	/**佣金*/
	@Virtual
	private Double commision;

	public Double getCommision() {
		return commision;
	}

	public void setCommision(Double commision) {
		this.commision = commision;
	}

	public boolean getSelectedBoolean() {
		return Constant.SELECTED.YES.equals(selected);
	}
	
	public Double getCommsionAmount(){
		if(commisionRate==null){
			return null;
		}
		return toPrice(new BigDecimal(itemAmount).add(new BigDecimal(couponAmount)).multiply(new BigDecimal(commisionRate)));
	}
	
	public List<SalePropertyDTO> getSalePropertyList() {
		if (StringUtils.isNoneBlank(getSalesProperty())) {
			try {
				return new ObjectMapper().readValue(getSalesProperty(), new TypeReference<List<SalePropertyDTO>>() {});
			} catch (Throwable e) {
			}
		}
		return new ArrayList<SalePropertyDTO>(0);
	}
	
	public Double getDiscount() {
		return getOriginalSubTotal() - getSubTotal();
	}
	
	/** 获取真实件数（多件装商品推送海关） */
	public Integer getRealQuantity(){
		return getUnitQuantity()*getQuantity();
	}
	
	/**
	 * 实付价格
	 * 
	 * @return
	 */
	public Double getRealPrice() {
		return new BigDecimal(getSubTotal()).divide(new BigDecimal(getQuantity()), 2, RoundingMode.HALF_UP).doubleValue();
	}
	public Long getId(){
		return id;
	}
	public Long getParentOrderId(){
		return parentOrderId;
	}
	public Long getOrderId(){
		return orderId;
	}
	public Long getMemberId(){
		return memberId;
	}
	public Integer getType(){
		return type;
	}
	public Long getParentOrderCode(){
		return parentOrderCode;
	}
	public Long getOrderCode(){
		return orderCode;
	}
	public Long getSpuId(){
		return spuId;
	}
	public String getSpuCode(){
		return spuCode;
	}
	public String getSpuName(){
		return spuName;
	}
	public Long getBrandId(){
		return brandId;
	}
	public String getBrandName(){
		return brandName;
	}
	public Long getSkuId(){
		return skuId;
	}
	public String getSkuCode(){
		return skuCode;
	}
	public String getBarCode(){
		return barCode;
	}
	public String getImg(){
		return img;
	}
	public String getSkuVersion(){
		return skuVersion;
	}
	public Long getTopicId(){
		return topicId;
	}
	public Long getSupplierId(){
		return supplierId;
	}
	public String getSupplierName(){
		return supplierName;
	}
	public Long getWarehouseId(){
		return warehouseId;
	}
	public String getBatchCode(){
		return batchCode;
	}
	public Integer getSalesType(){
		return salesType;
	}
	public String getSalesProperty(){
		return salesProperty;
	}
	public String getColor(){
		return color;
	}
	public String getSize(){
		return size;
	}
	public Date getStartTime(){
		return startTime;
	}
	public Date getEndTime(){
		return endTime;
	}
	public Integer getQuantity(){
		return quantity;
	}
	public Double getListPrice(){
		return listPrice;
	}
	public Double getPrice(){
		return price;
	}
	public Double getSubTotal(){
		if(null==subTotal){
			return Constant.ZERO;
		}
		return subTotal;
	}
	public Double getOriginalSubTotal(){
		if(null==originalSubTotal){
			return Constant.ZERO;
		}
		return originalSubTotal;
	}
	public Double getFreight(){
		if(null==freight){
			return Constant.ZERO;
		}
		return freight;
	}
	public Double getWeight(){
		return weight;
	}
	public Integer getRefundStatus(){
		return refundStatus;
	}
	public Double getTaxRate(){
		if(null==taxRate){
			return Constant.ZERO;
		}
		return taxRate;
	}
	public String getParcelTaxId(){
		return parcelTaxId;
	}
	public String getIp(){
		return ip;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Date getUpdateTime(){
		return updateTime;
	}
	public String getProductCode(){
		return productCode;
	}
	public Double getTaxFee(){
		if(null==taxFee){
			return Constant.ZERO;
		}
		return taxFee;
	}
	public Integer getRefundDays(){
		return refundDays;
	}
	public String getUnit(){
		return unit;
	}
	public Double getPlatformRate(){
		if(platformRate==null){
			platformRate=0D;
		}
		return platformRate;
	}
	public Integer getSettleStatus(){
		return settleStatus;
	}
	public Double getOrderCouponAmount(){
		if(orderCouponAmount==null){
			return Constant.ZERO;
		}
		return orderCouponAmount;
	}
	public Double getCouponAmount(){
		if(couponAmount==null){
			return Constant.ZERO;
		}
		return couponAmount;
	}
	public Long getContractId(){
		return contractId;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setParentOrderId(Long parentOrderId){
		this.parentOrderId=parentOrderId;
	}
	public void setOrderId(Long orderId){
		this.orderId=orderId;
	}
	public void setMemberId(Long memberId){
		this.memberId=memberId;
	}
	public void setType(Integer type){
		this.type=type;
	}
	public void setParentOrderCode(Long parentOrderCode){
		this.parentOrderCode=parentOrderCode;
	}
	public void setOrderCode(Long orderCode){
		this.orderCode=orderCode;
	}
	public void setSpuId(Long spuId){
		this.spuId=spuId;
	}
	public void setSpuCode(String spuCode){
		this.spuCode=spuCode;
	}
	public void setSpuName(String spuName){
		this.spuName=spuName;
	}
	public void setBrandId(Long brandId){
		this.brandId=brandId;
	}
	public void setBrandName(String brandName){
		this.brandName=brandName;
	}
	public void setSkuId(Long skuId){
		this.skuId=skuId;
	}
	public void setSkuCode(String skuCode){
		this.skuCode=skuCode;
	}
	public void setBarCode(String barCode){
		this.barCode=barCode;
	}
	public void setImg(String img){
		this.img=img;
	}
	public void setSkuVersion(String skuVersion){
		this.skuVersion=skuVersion;
	}
	public void setTopicId(Long topicId){
		this.topicId=topicId;
	}
	public void setSupplierId(Long supplierId){
		this.supplierId=supplierId;
	}
	public void setSupplierName(String supplierName){
		this.supplierName=supplierName;
	}
	public void setWarehouseId(Long warehouseId){
		this.warehouseId=warehouseId;
	}
	public void setBatchCode(String batchCode){
		this.batchCode=batchCode;
	}
	public void setSalesType(Integer salesType){
		this.salesType=salesType;
	}
	public void setSalesProperty(String salesProperty){
		this.salesProperty=salesProperty;
	}
	public void setColor(String color){
		this.color=color;
	}
	public void setSize(String size){
		this.size=size;
	}
	public void setStartTime(Date startTime){
		this.startTime=startTime;
	}
	public void setEndTime(Date endTime){
		this.endTime=endTime;
	}
	public void setQuantity(Integer quantity){
		this.quantity=quantity;
	}
	public void setListPrice(Double listPrice){
		this.listPrice=listPrice;
	}
	public void setPrice(Double price){
		this.price=price;
	}
	public void setSubTotal(Double subTotal){
		this.subTotal=subTotal;
	}
	public void setOriginalSubTotal(Double originalSubTotal){
		this.originalSubTotal=originalSubTotal;
	}
	public void setFreight(Double freight){
		this.freight=freight;
	}
	public void setWeight(Double weight){
		this.weight=weight;
	}
	public void setRefundStatus(Integer refundStatus){
		this.refundStatus=refundStatus;
	}
	public void setTaxRate(Double taxRate){
		this.taxRate=taxRate;
	}
	public void setParcelTaxId(String parcelTaxId){
		this.parcelTaxId=parcelTaxId;
	}
	public void setIp(String ip){
		this.ip=ip;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setUpdateTime(Date updateTime){
		this.updateTime=updateTime;
	}
	public void setProductCode(String productCode){
		this.productCode=productCode;
	}
	public void setTaxFee(Double taxFee){
		this.taxFee=taxFee;
	}
	public void setRefundDays(Integer refundDays){
		this.refundDays=refundDays;
	}
	public void setUnit(String unit){
		this.unit=unit;
	}
	public void setPlatformRate(Double platformRate){
		this.platformRate=platformRate;
	}
	public void setSettleStatus(Integer settleStatus){
		this.settleStatus=settleStatus;
	}
	public void setOrderCouponAmount(Double orderCouponAmount){
		this.orderCouponAmount=orderCouponAmount;
	}
	public void setCouponAmount(Double couponAmount){
		this.couponAmount=couponAmount;
	}
	public void setContractId(Long contractId){
		this.contractId=contractId;
	}

	public Long getSeaChannel() {
		return seaChannel;
	}

	public void setSeaChannel(Long seaChannel) {
		this.seaChannel = seaChannel;
	}

	public Integer getStorageType() {
		return storageType;
	}

	public void setStorageType(Integer storageType) {
		this.storageType = storageType;
	}

	public Integer getSelected() {
		return selected;
	}

	public void setSelected(Integer selected) {
		this.selected = selected;
	}

	public Integer getRejectCount() {
		return rejectCount;
	}

	public void setRejectCount(Integer rejectCount) {
		this.rejectCount = rejectCount;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public List<OrderPromotion> getOrderPromotionList() {
		return orderPromotionList;
	}

	public void setOrderPromotionList(List<OrderPromotion> orderPromotionList) {
		this.orderPromotionList = orderPromotionList;
	}

	public Double getItemAmount() {
		if(null==itemAmount){
			itemAmount = toPrice(multiply(price,quantity));
		}
		return itemAmount;
	}

	public Double getAddedValueRate() {
		if(this.addedValueRate == null) return Constant.ZERO;
		return addedValueRate;
	}

	public void setAddedValueRate(Double addedValueRate) {
		this.addedValueRate = addedValueRate;
	}

	public Double getExciseRate() {
		if(this.exciseRate == null) return Constant.ZERO;
		return exciseRate;
	}

	public void setExciseRate(Double exciseRate) {
		this.exciseRate = exciseRate;
	}

	public Double getCustomsRate() {
		return customsRate;
	}

	public void setCustomsRate(Double customsRate) {
		this.customsRate = customsRate;
	}

	public Integer getLimitCount() {
		return limitCount;
	}

	public void setLimitCount(Integer limitCount) {
		this.limitCount = limitCount;
	}

	public Boolean getIsSea() {
		return isSea;
	}

	public void setIsSea(Boolean isSea) {
		this.isSea = isSea;
	}

	public Double getSalesPrice() {
		if(salesPrice==null){
			return price;
		}
		return salesPrice;
	}

	public void setSalesPrice(Double salesPrice) {
		this.salesPrice = salesPrice;
	}

	public Double getCommisionRate() {
		return commisionRate;
	}

	public void setCommisionRate(Double commisionRate) {
		this.commisionRate = commisionRate;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public void setItemAmount(Double itemAmount) {
		this.itemAmount = itemAmount;
	}

	public Integer getPoints() {
		return points==null?0:points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public Long getUnitId() {
		return unitId;
	}

	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}
	
	public List<OrderPoint> getOrderPointList() {
		return orderPointList;
	}

	public void setOrderPointList(List<OrderPoint> orderPointList) {
		this.orderPointList = orderPointList;
	}
	public Integer getUnitQuantity() {
		return unitQuantity;
	}

	public void setUnitQuantity(Integer realUnitQuantity) {
		this.unitQuantity = realUnitQuantity;
	}

	public Double getWeightNet() {
		return weightNet;
	}

	public void setWeightNet(Double weightNet) {
		this.weightNet = weightNet;
	}
	
	public Long getCountryId() {
		return countryId;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}

	public Integer getWrapQuantity() {
		return wrapQuantity;
	}

	public void setWrapQuantity(Integer wrapQuantity) {
		this.wrapQuantity = wrapQuantity;
	}

	public Double getOrigItemAmount(){
		return BigDecimalUtil.multiply(salesPrice, quantity).doubleValue();
	}
	public Double getOrigTaxFee() {
		return origTaxFee;
	}

	public void setOrigTaxFee(Double origTaxFee) {
		this.origTaxFee = origTaxFee;
	}

	public Double getOrigFreight() {
		return origFreight;
	}

	public void setOrigFreight(Double origFreight) {
		this.origFreight = origFreight;
	}
}
