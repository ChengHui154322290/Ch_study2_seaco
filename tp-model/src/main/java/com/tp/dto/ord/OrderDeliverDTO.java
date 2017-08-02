/**
 * NewHeight.com Inc.
 * Copyright (c) 2008-2010 All Rights Reserved.
 */
package com.tp.dto.ord;

import java.io.Serializable;
import java.util.Date;

/**
 * <pre>
 * 订单发货实体
 * </pre>
 * 
 * @author szy
 * @version 0.0.1
 */
public class OrderDeliverDTO implements Serializable {
	private static final long serialVersionUID = -2929414186693628227L;

	/** 订单编号（子单） */
	private Long subOrderCode;

	/** 运单号,如果有多个订单号，则以半角逗号分隔 */
	private String packageNo;

	/** 物流方联系信息（快递员信息） */
	private String linkInfo;

	/** 发货时间 */
	private Date deliverlyTime;

	/** 物流公司ID */
	private String companyId;

	/** 物流公司名称 */
	private String companyName;

	/** 运费 */
	private Double freight;

	/** 货物总重量，单位为kg，精确到g */
	private Double weight;

	/** 创建人ID */
	private Long createUserId;

	/** 发货城市，到达市级即可，中文描述，如：江苏省苏州市 */
	private String startCity;

	/** 收货城市，到达市级即可，中文描述，如：江苏省南京市 */
	private String endCity;

	public Long getSubOrderCode() {
		return subOrderCode;
	}

	public void setSubOrderCode(Long subOrderCode) {
		this.subOrderCode = subOrderCode;
	}

	public String getPackageNo() {
		return packageNo;
	}

	public void setPackageNo(String packageNo) {
		this.packageNo = packageNo;
	}

	public Date getDeliverlyTime() {
		return deliverlyTime;
	}

	public void setDeliverlyTime(Date deliverlyTime) {
		this.deliverlyTime = deliverlyTime;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public String getLinkInfo() {
		return linkInfo;
	}

	public void setLinkInfo(String linkInfo) {
		this.linkInfo = linkInfo;
	}

	public Double getFreight() {
		return freight;
	}

	public void setFreight(Double freight) {
		this.freight = freight;
	}

	public Long getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}

	public String getStartCity() {
		return startCity;
	}

	public void setStartCity(String startCity) {
		this.startCity = startCity;
	}

	public String getEndCity() {
		return endCity;
	}

	public void setEndCity(String endCity) {
		this.endCity = endCity;
	}

}
