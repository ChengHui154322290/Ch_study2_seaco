package com.tp.model.ord;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author zhouguofeng
  * 订单导入明细
  */
public class OrderImportInfo extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1482303274647L;

	/**主键 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**order_import_log的主键  数据类型bigint(10)*/
	private Long logId;
	
	/**订单编号(11+yymmdd+LENGTH(0,id)) 数据类型bigint(20)*/
	private Long orderCode;
	
	/**渠道代码 数据类型varchar(16)*/
	private String channelCode;
	
	/**实付金额 数据类型double(10,2)*/
	private Double total;
	
	/**总金额 数据类型double(10,2)*/
	private Double itemTotal;
	
	/**用户身份号码 数据类型varchar(32)*/
	private String identityCard;
	
	/**用户真实姓名 数据类型varchar(20)*/
	private String trueName;
	
	/**收货地址-省份 数据类型bigint(11)*/
	private Long provinceId;
	
	/**省份-描述-用于展示 数据类型varchar(20)*/
	private String province;
	
	/**收货地址-城市 数据类型bigint(11)*/
	private Long cityId;
	
	/**城市-描述-用于展示 数据类型varchar(20)*/
	private String city;
	
	/**收货地址-区县 数据类型bigint(11)*/
	private Long countyId;
	
	/**区县-描述-用于展示 数据类型varchar(20)*/
	private String county;
	
	/**收货地址-街道 数据类型bigint(11)*/
	private Long streetId;
	
	/**街道地址-描述 数据类型varchar(50)*/
	private String street;
	
	/**收货地址-详细地址 数据类型varchar(200)*/
	private String address;
	
	/**收货人-电话 数据类型varchar(20)*/
	private String phone;
	
	/**收货人 数据类型varchar(20)*/
	private String name;
	
	/**下单时间 数据类型datetime*/
	private Date orderCreateTime;
	
	/**SKU编号 数据类型varchar(50)*/
	private String skuCode1;
	
	/**数量 数据类型int(11)*/
	private Integer quantity1;
	
	/**销售价 数据类型double(10,2)*/
	private Double salesPrice1;
	
	/**仓库ID 数据类型bigint(20)*/
	private Long warehouseId1;
	
	/**SKU编号 数据类型varchar(50)*/
	private String skuCode2;
	
	/**数量 数据类型int(11)*/
	private Integer quantity2;
	
	/**销售价 数据类型double(10,2)*/
	private Double salesPrice2;
	
	/**仓库ID 数据类型bigint(20)*/
	private Long warehouseId2;
	
	/**SKU编号 数据类型varchar(50)*/
	private String skuCode3;
	
	/**数量 数据类型int(11)*/
	private Integer quantity3;
	
	/**销售价 数据类型double(10,2)*/
	private Double salesPrice3;
	
	/**仓库ID 数据类型bigint(20)*/
	private Long warehouseId3;
	
	/**导入操作信息 数据类型text*/
	private String opMessage;
	
	/**导入的excel行号 数据类型bigint(8)*/
	private Long excelIndex;
	
	/**状态:1-为导入成功，2-为导入失败，默认1 数据类型int(1)*/
	private Integer status;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	
	public Long getId(){
		return id;
	}
	public Long getLogId(){
		return logId;
	}
	public Long getOrderCode(){
		return orderCode;
	}
	public String getChannelCode(){
		return channelCode;
	}
	public Double getTotal(){
		return total;
	}
	public Double getItemTotal(){
		return itemTotal;
	}
	public String getIdentityCard(){
		return identityCard;
	}
	public String getTrueName(){
		return trueName;
	}
	public Long getProvinceId(){
		return provinceId;
	}
	public String getProvince(){
		return province;
	}
	public Long getCityId(){
		return cityId;
	}
	public String getCity(){
		return city;
	}
	public Long getCountyId(){
		return countyId;
	}
	public String getCounty(){
		return county;
	}
	public Long getStreetId(){
		return streetId;
	}
	public String getStreet(){
		return street;
	}
	public String getAddress(){
		return address;
	}
	public String getPhone(){
		return phone;
	}
	public String getName(){
		return name;
	}
	public Date getOrderCreateTime(){
		return orderCreateTime;
	}
	public String getSkuCode1(){
		return skuCode1;
	}
	public Integer getQuantity1(){
		return quantity1;
	}
	public Double getSalesPrice1(){
		return salesPrice1;
	}
	public Long getWarehouseId1(){
		return warehouseId1;
	}
	public String getSkuCode2(){
		return skuCode2;
	}
	public Integer getQuantity2(){
		return quantity2;
	}
	public Double getSalesPrice2(){
		return salesPrice2;
	}
	public Long getWarehouseId2(){
		return warehouseId2;
	}
	public String getSkuCode3(){
		return skuCode3;
	}
	public Integer getQuantity3(){
		return quantity3;
	}
	public Double getSalesPrice3(){
		return salesPrice3;
	}
	public Long getWarehouseId3(){
		return warehouseId3;
	}
	public String getOpMessage(){
		return opMessage;
	}
	public Long getExcelIndex(){
		return excelIndex;
	}
	public Integer getStatus(){
		return status;
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
	public void setOrderCode(Long orderCode){
		this.orderCode=orderCode;
	}
	public void setChannelCode(String channelCode){
		this.channelCode=channelCode;
	}
	public void setTotal(Double total){
		this.total=total;
	}
	public void setItemTotal(Double itemTotal){
		this.itemTotal=itemTotal;
	}
	public void setIdentityCard(String identityCard){
		this.identityCard=identityCard;
	}
	public void setTrueName(String trueName){
		this.trueName=trueName;
	}
	public void setProvinceId(Long provinceId){
		this.provinceId=provinceId;
	}
	public void setProvince(String province){
		this.province=province;
	}
	public void setCityId(Long cityId){
		this.cityId=cityId;
	}
	public void setCity(String city){
		this.city=city;
	}
	public void setCountyId(Long countyId){
		this.countyId=countyId;
	}
	public void setCounty(String county){
		this.county=county;
	}
	public void setStreetId(Long streetId){
		this.streetId=streetId;
	}
	public void setStreet(String street){
		this.street=street;
	}
	public void setAddress(String address){
		this.address=address;
	}
	public void setPhone(String phone){
		this.phone=phone;
	}
	public void setName(String name){
		this.name=name;
	}
	public void setOrderCreateTime(Date orderCreateTime){
		this.orderCreateTime=orderCreateTime;
	}
	public void setSkuCode1(String skuCode1){
		this.skuCode1=skuCode1;
	}
	public void setQuantity1(Integer quantity1){
		this.quantity1=quantity1;
	}
	public void setSalesPrice1(Double salesPrice1){
		this.salesPrice1=salesPrice1;
	}
	public void setWarehouseId1(Long warehouseId1){
		this.warehouseId1=warehouseId1;
	}
	public void setSkuCode2(String skuCode2){
		this.skuCode2=skuCode2;
	}
	public void setQuantity2(Integer quantity2){
		this.quantity2=quantity2;
	}
	public void setSalesPrice2(Double salesPrice2){
		this.salesPrice2=salesPrice2;
	}
	public void setWarehouseId2(Long warehouseId2){
		this.warehouseId2=warehouseId2;
	}
	public void setSkuCode3(String skuCode3){
		this.skuCode3=skuCode3;
	}
	public void setQuantity3(Integer quantity3){
		this.quantity3=quantity3;
	}
	public void setSalesPrice3(Double salesPrice3){
		this.salesPrice3=salesPrice3;
	}
	public void setWarehouseId3(Long warehouseId3){
		this.warehouseId3=warehouseId3;
	}
	public void setOpMessage(String opMessage){
		this.opMessage=opMessage;
	}
	public void setExcelIndex(Long excelIndex){
		this.excelIndex=excelIndex;
	}
	public void setStatus(Integer status){
		this.status=status;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
}
