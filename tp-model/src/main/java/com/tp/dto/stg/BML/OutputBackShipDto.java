package com.tp.dto.stg.BML;

import java.util.List;

/**
 * 
 * <pre>
 * 发运信息dto
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 */
public class OutputBackShipDto {
	/**
	 * 订单号
	 */

	private String orderNo;
	/**
	 * 运单号
	 */
	private String shipNo;
	/**
	 * 发运时间
	 */
	private String shipTime;
	/**
	 * 发送的物流公司ID
	 */
	private String carrierID;
	/**
	 * 发送的物流公司名称
	 */
	private String carrierName;
	/**
	 * 客户ID
	 */
	private String customerId;
	/**
	 * 对应的仓库ID号
	 */
	private String bgNo;
	/**
	 * 订单包裹重量
	 */
	private String weight;
	/**
	 * 箱型
	 */
	private String boxTpye;
	/**
	 * sku信息
	 */
	private List<OutPutBackShipSkuDto> send;

	public List<OutPutBackShipSkuDto> getSend() {
		return send;
	}

	public void setSend(List<OutPutBackShipSkuDto> send) {
		this.send = send;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getShipNo() {
		return shipNo;
	}

	public void setShipNo(String shipNo) {
		this.shipNo = shipNo;
	}

	public String getShipTime() {
		return shipTime;
	}

	public void setShipTime(String shipTime) {
		this.shipTime = shipTime;
	}

	public String getCarrierID() {
		return carrierID;
	}

	public void setCarrierID(String carrierID) {
		this.carrierID = carrierID;
	}

	public String getCarrierName() {
		return carrierName;
	}

	public void setCarrierName(String carrierName) {
		this.carrierName = carrierName;
	}


	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getBgNo() {
		return bgNo;
	}

	public void setBgNo(String bgNo) {
		this.bgNo = bgNo;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getBoxTpye() {
		return boxTpye;
	}

	public void setBoxTpye(String boxTpye) {
		this.boxTpye = boxTpye;
	}

}
