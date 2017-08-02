package com.tp.model.stg;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

import com.tp.common.annotation.Id;
import com.tp.common.annotation.Virtual;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 仓库信息表
  */
public class Warehouse extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1450403690117L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**供应商id 0 表示自营仓库 数据类型bigint(20)*/
	private Long spId;
	
	/**所在地id 数据类型bigint(20)*/
	private Long districtId;
	
	/**详细地址 数据类型varchar(50)*/
	private String address;
	/** 仓库经纬度*/
	private String lngLat;
	
	/**仓库名称 数据类型varchar(50)*/
	private String name;
	
	/**仓库编号 数据类型varchar(50)*/
	private String code;
	
	/**供应商名称 数据类型varchar(100)*/
	private String spName;
	
	/**邮编 数据类型varchar(10)*/
	private String zipCode;
	
	/**联系人 数据类型varchar(50)*/
	private String linkman;
	
	/**电话 数据类型varchar(20)*/
	private String phone;
	
	/**配送地区，多个地区id用逗号隔开，为0表示全国 数据类型text*/
	private String deliverAddr;
	
	/**记录创建时间 数据类型datetime*/
	private Date createTime;
	
	/**最后修改时间 数据类型datetime*/
	private Date modifyTime;
	
	/**1 普通(自营) 2平台 3 国内直发 4 保税区 5 境外直发  数据类型int(1)*/
	private Integer type;
	
	/**关联base的通关渠道Id 数据类型bigint(20)*/
	private Long bondedArea;
	
	/**电子邮箱 数据类型varchar(100)*/
	private String email;
	
	/**操作人id 数据类型bigint(20)*/
	private Long operatorUserId;
	
	/**操作人 数据类型varchar(50)*/
	private String operatorUserName;
	
	/** 主仓库相关信息（同一主仓库的商品，及时供应商不同，也不会拆单）：默认为0，不存在主仓库 */
	private String mainWarehouseName;
	private Long mainWarehouseId;
	private Long mainSpId;
	private String mainSpName;
	/** 主仓库类型：0不是主仓库（默认值）1主仓库（与物理仓库对应） */
	private Integer mainType; 
	
	/** 进口类型：0直邮进口1保税进口 */
	private Integer importType;
	
	/**推送标签*/
	private Integer putSign;
	
	/**对接WMS仓库名称仓库 数据类型varchar(32)*/
	private String wmsName;
	
	/**对接WMS仓库编码 数据类型varchar(32)*/
	private String wmsCode;
	
	/**进出口岸(关区) 数据类型varchar(32)*/
	private String ioSeaport;
	
	/**申报口岸(关区) 数据类型varchar(32)*/
	private String declSeaport;
	
	/**码头货场 数据类型varchar(32)*/
	private String customsField;
	
	/**快递企业 数据类型varchar(32)*/
	private String logistics;
	
	/**快递企业编码（报关参数） 数据类型varchar(32)*/
	private String logisticsCode;
	
	/**快递企业名称（报关参数） 数据类型varchar(32)*/
	private String logisticsName;
	
	/**货主名(仓库参数) 数据类型varchar(32)*/
	private String goodsOwner;
	
	/**账册编号(每个仓库只有一个) 数据类型varchar(32)*/
	private String accountBookNo;
	
	/**仓储企业名称(报关) 数据类型varchar(32)*/
	private String storageName;
	
	/**仓储企业代码(报关) 数据类型varchar(32)*/
	private String storageCode;
	
	/**报关类型 数据类型varchar(64)*/
	private String declareType;
	
	/**报关企业名称 数据类型varchar(32)*/
	private String declareCompanyName;
	
	/**报关企业编号 数据类型varchar(32)*/
	private String declareCompanyCode;
	
	/** 发货方式（报关参数） */
	private Integer postMode;
	
	/** 发件人(报关参数,可填写如：西客商城) */
	private String senderName;
	
	/** 发件人国别（报关参数） */
	private String senderCountryCode;
	
	/** 发件人城市（报关参数） */
	private String senderCity;
	
	/** 仓库申请单编号(仓储企业事先在辅助系统申请的分送集报申请单编号) */
	private String applicationFormNo;
	
	/** 运输类型 */
	private String trafMode;
	
	/** 贸易国（起运地） */
	private String tradeCountry;
	
	@Virtual
    /** 仓库联系人电话 */
    private String warehouseLinkmanTel;
	
	/**是否推送清关单*/
	@Virtual
	private String putCleanOrder="0";
	/**是否推送运单*/
	@Virtual
	private String putWaybill="0";
	/**是否推送订单*/
	@Virtual
	private String putOrder="0";
	/**是否推送支付单*/
	@Virtual
	private String putPayOrder="0";
	/**是否推送仓库*/
	@Virtual
	private String putStorage="0";
	/**推送标识字符*/
	@Virtual
	private transient StringBuffer putSignStr=new StringBuffer("00000");
	
	public boolean checkPutStorage(){
		return "1".equals(getPutStorage());
	}
	
	public boolean checkPutCleanOrder(){
		return "1".equals(getPutCleanOrder());
	}
	
	public boolean checkPutWaybill(){
		return "1".equals(getPutWaybill());
	}
	
	public boolean checkPutOrder(){
		return "1".equals(getPutOrder());
	}
	
	public boolean checkPutPayOrder(){
		return "1".equals(getPutPayOrder());
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getSpId() {
		return spId;
	}
	public void setSpId(Long spId) {
		this.spId = spId;
	}
	public Long getDistrictId() {
		return districtId;
	}
	public void setDistrictId(Long districtId) {
		this.districtId = districtId;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getSpName() {
		return spName;
	}
	public void setSpName(String spName) {
		this.spName = spName;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getLinkman() {
		return linkman;
	}
	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getDeliverAddr() {
		return deliverAddr;
	}
	public void setDeliverAddr(String deliverAddr) {
		this.deliverAddr = deliverAddr;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Long getBondedArea() {
		return bondedArea;
	}
	public void setBondedArea(Long bondedArea) {
		this.bondedArea = bondedArea;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Long getOperatorUserId() {
		return operatorUserId;
	}
	public void setOperatorUserId(Long operatorUserId) {
		this.operatorUserId = operatorUserId;
	}
	public String getOperatorUserName() {
		return operatorUserName;
	}
	public void setOperatorUserName(String operatorUserName) {
		this.operatorUserName = operatorUserName;
	}
	public Integer getPutSign() {
		return putSign;
	}
	public void setPutSign(Integer putSign) {
		this.putSign = putSign;
		if(null!=this.putSign && this.putSign>0){
			String putSignStrNew = Integer.toBinaryString(putSign);
			Integer length = putSignStr.length()-putSignStrNew.length();
			if(length<0){
				length=0;
			}
			putSignStr.replace(length, putSignStr.length(), putSignStrNew);
			putCleanOrder=putSignStr.substring(0, 1);
			putWaybill=putSignStr.substring(1, 2);
			putOrder=putSignStr.substring(2, 3);
			putPayOrder=putSignStr.substring(3, 4);
			putStorage=putSignStr.substring(4, 5);
		}
	}
	public String getWarehouseLinkmanTel() {
		return warehouseLinkmanTel;
	}
	public void setWarehouseLinkmanTel(String warehouseLinkmanTel) {
		this.warehouseLinkmanTel = warehouseLinkmanTel;
	}
	public String getPutCleanOrder() {
		return putCleanOrder;
	}
	public void setPutCleanOrder(String putCleanOrder) {
		if("1".equals(putCleanOrder)){
			this.putCleanOrder = "1";
			putSignStr.setCharAt(0, '1');
		}
		initPutSign();
	}
	public String getPutWaybill() {
		return putWaybill;
	}
	public void setPutWaybill(String putWaybill) {
		if("1".equals(putWaybill)){
			this.putWaybill = "1";
			putSignStr.setCharAt(1, '1');
		}
		initPutSign();
	}
	public String getPutOrder() {
		return putOrder;
	}
	public void setPutOrder(String putOrder) {
		if("1".equals(putOrder)){
			this.putOrder = "1";
			putSignStr.setCharAt(2, '1');
		}
		initPutSign();
	}
	public String getPutPayOrder() {
		return putPayOrder;
	}
	public void setPutPayOrder(String putPayOrder) {
		if("1".equals(putPayOrder)){
			this.putPayOrder = "1";
			putSignStr.setCharAt(3, '1');
		}
		initPutSign();
	}
	public String getPutStorage() {
		return putStorage;
	}
	public void setPutStorage(String putStorage) {
		if("1".equals(putStorage)){
			this.putStorage = "1";
			putSignStr.setCharAt(4, '1');
		}
		initPutSign();
	}
	
	private void initPutSign(){
		this.putSign = Integer.parseInt(putSignStr.toString(), 2);
	}
	
	public static List<Integer> getPutSignList(String putCleanOrder,String putWaybill,String putOrder,
			String putPayOrder,String putStorage){
		Integer max = Integer.parseInt("11111", 2);
		List<Integer> searchList = new ArrayList<Integer>(); 
		for(int i=0;i<=max;i++){
			searchList.add(i);
		}
		searchList.removeIf(new Predicate<Integer>(){
			public boolean test(Integer i) {
				String str = Integer.toBinaryString(i);
				str = "00000".replaceFirst("[0]{"+str.length()+"}$", str);
				if(putCleanOrder!=null && !str.matches(putCleanOrder+"[01]{4}$")){
					return Boolean.TRUE;
				}
				if(null!=putWaybill && !str.matches("^[01]"+putWaybill+"[01]{3}$")){
					return Boolean.TRUE;
				}
				if(null!=putOrder && !str.matches("^[01]{2}"+putOrder+"[01]{2}$")){
					return Boolean.TRUE;
				}
				if(null!=putPayOrder && !str.matches("^[01]{3}"+putPayOrder+"[01]$")){
					return Boolean.TRUE;
				}
				if(null!=putStorage && !str.matches("^[01]{4}"+putStorage+"$")){
					return Boolean.TRUE;
				}
				return false;
			}
		});
		if(searchList.size()==0){
			searchList = null;
		}
		return searchList;
	}
	public String getWmsName() {
		return wmsName;
	}
	public void setWmsName(String wmsName) {
		this.wmsName = wmsName;
	}
	public String getWmsCode() {
		return wmsCode;
	}
	public void setWmsCode(String wmsCode) {
		this.wmsCode = wmsCode;
	}
	public String getIoSeaport() {
		return ioSeaport;
	}
	public void setIoSeaport(String ioSeaport) {
		this.ioSeaport = ioSeaport;
	}
	public String getDeclSeaport() {
		return declSeaport;
	}
	public void setDeclSeaport(String declSeaport) {
		this.declSeaport = declSeaport;
	}
	public String getCustomsField() {
		return customsField;
	}
	public void setCustomsField(String customsField) {
		this.customsField = customsField;
	}
	public String getLogistics() {
		return logistics;
	}
	public void setLogistics(String logistics) {
		this.logistics = logistics;
	}
	public String getLogisticsCode() {
		return logisticsCode;
	}
	public void setLogisticsCode(String logisticsCode) {
		this.logisticsCode = logisticsCode;
	}
	public String getLogisticsName() {
		return logisticsName;
	}
	public void setLogisticsName(String logisticsName) {
		this.logisticsName = logisticsName;
	}
	public String getGoodsOwner() {
		return goodsOwner;
	}
	public void setGoodsOwner(String goodsOwner) {
		this.goodsOwner = goodsOwner;
	}
	public String getAccountBookNo() {
		return accountBookNo;
	}
	public void setAccountBookNo(String accountBookNo) {
		this.accountBookNo = accountBookNo;
	}
	public String getStorageName() {
		return storageName;
	}
	public void setStorageName(String storageName) {
		this.storageName = storageName;
	}
	public String getStorageCode() {
		return storageCode;
	}
	public void setStorageCode(String storageCode) {
		this.storageCode = storageCode;
	}
	public String getDeclareType() {
		return declareType;
	}
	public void setDeclareType(String declareType) {
		this.declareType = declareType;
	}
	public String getDeclareCompanyName() {
		return declareCompanyName;
	}
	public void setDeclareCompanyName(String declareCompanyName) {
		this.declareCompanyName = declareCompanyName;
	}
	public String getDeclareCompanyCode() {
		return declareCompanyCode;
	}
	public void setDeclareCompanyCode(String declareCompanyCode) {
		this.declareCompanyCode = declareCompanyCode;
	}
	public StringBuffer getPutSignStr() {
		return putSignStr;
	}
	public void setPutSignStr(StringBuffer putSignStr) {
		this.putSignStr = putSignStr;
	}
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	public String getSenderCountryCode() {
		return senderCountryCode;
	}
	public void setSenderCountryCode(String senderCountryCode) {
		this.senderCountryCode = senderCountryCode;
	}
	public Integer getPostMode() {
		return postMode;
	}
	public void setPostMode(Integer postMode) {
		this.postMode = postMode;
	}
	public String getApplicationFormNo() {
		return applicationFormNo;
	}
	public void setApplicationFormNo(String applicationFormNo) {
		this.applicationFormNo = applicationFormNo;
	}
	public String getSenderCity() {
		return senderCity;
	}
	public void setSenderCity(String senderCity) {
		this.senderCity = senderCity;
	}

	public Integer getImportType() {
		return importType;
	}

	public void setImportType(Integer importType) {
		this.importType = importType;
	}

	public Long getMainWarehouseId() {
		return mainWarehouseId;
	}

	public void setMainWarehouseId(Long mainWarehouseId) {
		this.mainWarehouseId = mainWarehouseId;
	}

	public Long getMainSpId() {
		return mainSpId;
	}

	public void setMainSpId(Long mainSpId) {
		this.mainSpId = mainSpId;
	}

	public String getMainSpName() {
		return mainSpName;
	}

	public void setMainSpName(String mainSpName) {
		this.mainSpName = mainSpName;
	}

	public String getMainWarehouseName() {
		return mainWarehouseName;
	}

	public void setMainWarehouseName(String mainWarehouseName) {
		this.mainWarehouseName = mainWarehouseName;
	}

	public Integer getMainType() {
		return mainType;
	}

	public void setMainType(Integer mainType) {
		this.mainType = mainType;
	}

	public String getLngLat() {
		return lngLat;
	}

	public void setLngLat(String lngLat) {
		this.lngLat = lngLat;
	}

	public String getTrafMode() {
		return trafMode;
	}

	public void setTrafMode(String trafMode) {
		this.trafMode = trafMode;
	}

	public String getTradeCountry() {
		return tradeCountry;
	}

	public void setTradeCountry(String tradeCountry) {
		this.tradeCountry = tradeCountry;
	}
}
