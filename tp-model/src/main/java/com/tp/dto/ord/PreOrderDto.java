package com.tp.dto.ord;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tp.model.ord.OrderItem;
import com.tp.model.ord.SubOrder;

/**
 * 预订单,购物车分组(子订单)
 * @author szy
 *
 */
public class PreOrderDto extends SubOrder {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2498740475224423792L;

	/**发货仓、供应商名称*/
	private String deliveryStockName;
	/**满减分组,无满减KEY为空*/
	private Map<SimpleFullDiscountDTO,List<OrderItem>> fullDiscountMap = new HashMap<SimpleFullDiscountDTO,List<OrderItem>>();
	
	private String warnMessage;
	
	private Long freightTemplateId;
	
	private Boolean usedPointSign=Boolean.FALSE;
	
	private Integer subUsedPoint=0;
	
	private String lngLat;//仓库经纬度
	
	public String getDeliveryStockName() {
		return deliveryStockName;
	}
	public void setDeliveryStockName(String deliveryStockName) {
		this.deliveryStockName = deliveryStockName;
	}
	public Map<SimpleFullDiscountDTO, List<OrderItem>> getFullDiscountMap() {
		return fullDiscountMap;
	}
	public void setFullDiscountMap(Map<SimpleFullDiscountDTO, List<OrderItem>> fullDiscountMap) {
		this.fullDiscountMap = fullDiscountMap;
	}
	public Long getFreightTemplateId() {
		return freightTemplateId;
	}
	public void setFreightTemplateId(Long freightTemplateId) {
		this.freightTemplateId = freightTemplateId;
	}
	public String getWarnMessage() {
		return warnMessage;
	}
	public void setWarnMessage(String warnMessage) {
		this.warnMessage = warnMessage;
	}
	public Integer getSubUsedPoint() {
		return subUsedPoint;
	}
	public void setSubUsedPoint(Integer subUsedPoint) {
		this.subUsedPoint = subUsedPoint;
	}
	public Boolean getUsedPointSign() {
		return usedPointSign;
	}
	public void setUsedPointSign(Boolean usedPointSign) {
		this.usedPointSign = usedPointSign;
	}
	public String getLngLat() {
		return lngLat;
	}
	public void setLngLat(String lngLat) {
		this.lngLat = lngLat;
	}
}
