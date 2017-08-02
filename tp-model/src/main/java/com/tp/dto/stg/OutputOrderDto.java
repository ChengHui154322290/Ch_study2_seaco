package com.tp.dto.stg;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.tp.common.vo.StorageConstant.OutputOrderType;

/**
 * 出库订单
 * @author szy
 * 2015年1月22日 下午3:20:03
 *
 */
public class OutputOrderDto implements Serializable{
	private static final long serialVersionUID = 4097069388531076560L;


	/** 即子订单编号 必传 */
	private String orderCode;

	/** 配送方式  不传默认为申通 ExpressType */
	private String shipping;

	/** 销售渠道 */
	private String issuePartyId;

	/** 销售渠道名称 */
	private String issuePartyName;

	/** 客户昵称 */
	private String customerName;

	/** 付款方式 */
	private String payment;

	/** 网址 */
	private String website;

	/** 运费  必传*/
	private Double freight;

	/** 货到付款服务费  必传*/
	private Double serviceCharge;

	/** 收件人 必传*/
	private String name;

	/** 邮编  必传*/
	private String postCode;

	/** 固定电话  */
	private String phone;

	/** 手机号  必传*/
	private String mobile;

	/** 省 必传*/
	private String prov;

	/** 市  必传*/
	private String city;

	/** 区 */
	private String district;

	/** 地址  必传*/
	private String address;
	
	/** 订单实付总金额  必传*/
	private Double itemsValue;

	/** 卖家备注 */
	private String remark;

	/** 付款时间 */
	private Date payTime;

	/** 是否开票  默认为“无需开票” 需要开票 需开专票 OutputOrderConstant*/
	private String isCashsale;

	/** 订单优先级  使用OutputOrderConstant中常量OutputOrderConstant*/
	private String priority;

	/** 预期发货时间 */
	private Date expectedTime;

	/** 要求交货时间 */
	private Date requiredTime;
	
	/** 出库订单类型 */
	private OutputOrderType orderType;
	
	/**仓库id**/
	private Long warehouseId;
	
	/**仓库编号**/
	private String warehouseCode;
	
	/** 订单明细 */
	private List<OutputOrderDetailDto> orderDetailDtoList;

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getShipping() {
		return shipping;
	}

	public void setShipping(String shipping) {
		this.shipping = shipping;
	}

	public String getIssuePartyId() {
		return issuePartyId;
	}

	public void setIssuePartyId(String issuePartyId) {
		this.issuePartyId = issuePartyId;
	}

	public String getIssuePartyName() {
		return issuePartyName;
	}

	public void setIssuePartyName(String issuePartyName) {
		this.issuePartyName = issuePartyName;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getPayment() {
		return payment;
	}

	public void setPayment(String payment) {
		this.payment = payment;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public Double getFreight() {
		return freight;
	}

	public void setFreight(Double freight) {
		this.freight = freight;
	}

	public Double getServiceCharge() {
		return serviceCharge;
	}

	public void setServiceCharge(Double serviceCharge) {
		this.serviceCharge = serviceCharge;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getProv() {
		return prov;
	}

	public void setProv(String prov) {
		this.prov = prov;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Double getItemsValue() {
		return itemsValue;
	}

	public void setItemsValue(Double itemsValue) {
		this.itemsValue = itemsValue;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public String getIsCashsale() {
		return isCashsale;
	}

	public void setIsCashsale(String isCashsale) {
		this.isCashsale = isCashsale;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public Date getExpectedTime() {
		return expectedTime;
	}

	public void setExpectedTime(Date expectedTime) {
		this.expectedTime = expectedTime;
	}

	public Date getRequiredTime() {
		return requiredTime;
	}

	public void setRequiredTime(Date requiredTime) {
		this.requiredTime = requiredTime;
	}

	public List<OutputOrderDetailDto> getOrderDetailDtoList() {
		return orderDetailDtoList;
	}

	public void setOrderDetailDtoList(List<OutputOrderDetailDto> orderDetailDtoList) {
		this.orderDetailDtoList = orderDetailDtoList;
	}

	public OutputOrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(OutputOrderType orderType) {
		this.orderType = orderType;
	}

	public Long getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(Long warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}
}
