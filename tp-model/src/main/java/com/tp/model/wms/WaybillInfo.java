package com.tp.model.wms;

import java.io.Serializable;

import java.util.Date;

import com.tp.common.annotation.Id;
import com.tp.common.vo.wms.WmsWaybillConstant;
import com.tp.common.vo.wms.WmsWaybillConstant.WaybillType;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 运单报关及发运
  */
public class WaybillInfo extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1464849292316L;

	/** 数据类型bigint(20)*/
	@Id
	private Long id;
	
	/**快递企业编码 数据类型varchar(30)*/
	private String logisticsCode;
	
	/**快递企业名称 数据类型varchar(30)*/
	private String logisticsName;
	
	/**类型:0报关1发运 数据类型tinyint(1)*/
	private Integer type;
	
	/**订单编号 数据类型varchar(50)*/
	private String orderCode;
	
	/**运单号 数据类型varchar(20)*/
	private String waybillNo;
	
	/**主要商品名称：多种商品填写一种即可 数据类型varchar(300)*/
	private String mainGoodsName;
	
	/**包裹重量 数据类型varchar(50)*/
	private String grossWeight;
	
	/**包裹净重 数据类型varchar(50)*/
	private String netWeight;
	
	/**商品发货数量 数据类型varchar(50)*/
	private String packAmount;
	
	/**包裹价值 数据类型varchar(50)*/
	private String worth;
	
	/**邮费是否到付:0是1否 默认1 数据类型tinyint(1)*/
	private Integer isPostagePay;
	
	/**是否货到付款:0是1否 默认1 数据类型tinyint(1)*/
	private Integer isDeliveryPay;
	
	/**发货方省份 数据类型varchar(50)*/
	private String sendProvince;
	
	/**发货方城市 数据类型varchar(50)*/
	private String sendCity;
	
	/**发货方区域 数据类型varchar(50)*/
	private String sendArea;
	
	/**发货方地址 数据类型varchar(200)*/
	private String sendAddress;
	
	/**发货方粗略地址,例如：浙江杭州 数据类型varchar(100)*/
	private String sendRoughArea;
	
	/**发件人姓名 数据类型varchar(100)*/
	private String sendName;
	
	/**发件人电话 数据类型varchar(100)*/
	private String sendTel;
	
	/**发件人单位 数据类型varchar(100)*/
	private String sendCompany;
	
	/**收货人姓名 数据类型varchar(50)*/
	private String consignee;
	
	/**邮编 数据类型varchar(20)*/
	private String postCode;
	
	/**省名称 数据类型varchar(50)*/
	private String province;
	
	/**市名称 数据类型varchar(50)*/
	private String city;
	
	/**区名称 数据类型varchar(50)*/
	private String area;
	
	/**收件地址 数据类型varchar(500)*/
	private String address;
	
	/**移动电话 数据类型varchar(20)*/
	private String mobile;
	
	/**固定电话 数据类型varchar(20)*/
	private String tel;
	
	/**状态0成功1失败 数据类型tinyint(1)*/
	private Integer status;
	
	/**失败次数 数据类型int(11)*/
	private Integer failTimes;
	
	/**失败原因 数据类型varchar(500)*/
	private String errorMsg;
	
	/** 进口类型：0直邮1保税 */
	private Integer importType;
	
	/** 航班号 */
	private String voyageNo;
	
	/** 总提运单号 */
	private String deliveryNo;
	
	/**创建时间 数据类型datetime*/
	private Date createTime;
	
	/**更新时间 数据类型datetime*/
	private Date updateTime;
	
	/**备注 数据类型varchar(255)*/
	private String remark;
	
	public String getTypeStr(){
		return WaybillType.getDescByType(type);
	}
	
	public String getStatusStr(){
		return WmsWaybillConstant.PutStatus.getDescByStatus(status);
	}
	
	public String getIsPostagePayStr(){
		return isPostagePay == 0 ? "是":"否";
	}
	
	public String getIsDeliveryPayStr(){
		return isDeliveryPay == 0 ? "是":"否";
	}
	
	public Long getId(){
		return id;
	}
	public String getLogisticsCode(){
		return logisticsCode;
	}
	public String getLogisticsName(){
		return logisticsName;
	}
	public Integer getType(){
		return type;
	}
	public String getOrderCode(){
		return orderCode;
	}
	public String getWaybillNo(){
		return waybillNo;
	}
	public String getMainGoodsName(){
		return mainGoodsName;
	}
	public String getGrossWeight(){
		return grossWeight;
	}
	public String getNetWeight(){
		return netWeight;
	}
	public String getPackAmount(){
		return packAmount;
	}
	public String getWorth(){
		return worth;
	}
	public Integer getIsPostagePay(){
		return isPostagePay;
	}
	public Integer getIsDeliveryPay(){
		return isDeliveryPay;
	}
	public String getSendProvince(){
		return sendProvince;
	}
	public String getSendCity(){
		return sendCity;
	}
	public String getSendArea(){
		return sendArea;
	}
	public String getSendAddress(){
		return sendAddress;
	}
	public String getSendRoughArea(){
		return sendRoughArea;
	}
	public String getSendName(){
		return sendName;
	}
	public String getSendTel(){
		return sendTel;
	}
	public String getSendCompany(){
		return sendCompany;
	}
	public String getConsignee(){
		return consignee;
	}
	public String getPostCode(){
		return postCode;
	}
	public String getProvince(){
		return province;
	}
	public String getCity(){
		return city;
	}
	public String getArea(){
		return area;
	}
	public String getAddress(){
		return address;
	}
	public String getMobile(){
		return mobile;
	}
	public String getTel(){
		return tel;
	}
	public Integer getStatus(){
		return status;
	}
	public Integer getFailTimes(){
		return failTimes;
	}
	public String getErrorMsg(){
		return errorMsg;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public Date getUpdateTime(){
		return updateTime;
	}
	public String getRemark(){
		return remark;
	}
	public void setId(Long id){
		this.id=id;
	}
	public void setLogisticsCode(String logisticsCode){
		this.logisticsCode=logisticsCode;
	}
	public void setLogisticsName(String logisticsName){
		this.logisticsName=logisticsName;
	}
	public void setType(Integer type){
		this.type=type;
	}
	public void setOrderCode(String orderCode){
		this.orderCode=orderCode;
	}
	public void setWaybillNo(String waybillNo){
		this.waybillNo=waybillNo;
	}
	public void setMainGoodsName(String mainGoodsName){
		this.mainGoodsName=mainGoodsName;
	}
	public void setGrossWeight(String grossWeight){
		this.grossWeight=grossWeight;
	}
	public void setNetWeight(String netWeight){
		this.netWeight=netWeight;
	}
	public void setPackAmount(String packAmount){
		this.packAmount=packAmount;
	}
	public void setWorth(String worth){
		this.worth=worth;
	}
	public void setIsPostagePay(Integer isPostagePay){
		this.isPostagePay=isPostagePay;
	}
	public void setIsDeliveryPay(Integer isDeliveryPay){
		this.isDeliveryPay=isDeliveryPay;
	}
	public void setSendProvince(String sendProvince){
		this.sendProvince=sendProvince;
	}
	public void setSendCity(String sendCity){
		this.sendCity=sendCity;
	}
	public void setSendArea(String sendArea){
		this.sendArea=sendArea;
	}
	public void setSendAddress(String sendAddress){
		this.sendAddress=sendAddress;
	}
	public void setSendRoughArea(String sendRoughArea){
		this.sendRoughArea=sendRoughArea;
	}
	public void setSendName(String sendName){
		this.sendName=sendName;
	}
	public void setSendTel(String sendTel){
		this.sendTel=sendTel;
	}
	public void setSendCompany(String sendCompany){
		this.sendCompany=sendCompany;
	}
	public void setConsignee(String consignee){
		this.consignee=consignee;
	}
	public void setPostCode(String postCode){
		this.postCode=postCode;
	}
	public void setProvince(String province){
		this.province=province;
	}
	public void setCity(String city){
		this.city=city;
	}
	public void setArea(String area){
		this.area=area;
	}
	public void setAddress(String address){
		this.address=address;
	}
	public void setMobile(String mobile){
		this.mobile=mobile;
	}
	public void setTel(String tel){
		this.tel=tel;
	}
	public void setStatus(Integer status){
		this.status=status;
	}
	public void setFailTimes(Integer failTimes){
		this.failTimes=failTimes;
	}
	public void setErrorMsg(String errorMsg){
		this.errorMsg=errorMsg;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public void setUpdateTime(Date updateTime){
		this.updateTime=updateTime;
	}
	public void setRemark(String remark){
		this.remark=remark;
	}

	public Integer getImportType() {
		return importType;
	}

	public void setImportType(Integer importType) {
		this.importType = importType;
	}

	public String getVoyageNo() {
		return voyageNo;
	}

	public void setVoyageNo(String voyageNo) {
		this.voyageNo = voyageNo;
	}

	public String getDeliveryNo() {
		return deliveryNo;
	}

	public void setDeliveryNo(String deliveryNo) {
		this.deliveryNo = deliveryNo;
	}
}
