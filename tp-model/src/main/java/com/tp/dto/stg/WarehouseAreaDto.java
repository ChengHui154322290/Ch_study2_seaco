package com.tp.dto.stg;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * <pre>
 * 
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 */
public class WarehouseAreaDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3429796750285624562L;
	/**收货地址-大区*/
	private Long regionId;
	/**收货地址-省份*/
	private Long provinceId;
	/**收货地址-城市*/
	private Long cityId;
	/**收货地址-区县*/
	private Long countyId;
	/**收货地址-街道*/
	private Long streetId;
	/**仓库Id*/
	private List<Long> warehouseIds;
	/**
	 * Getter method for property <tt>provinceId</tt>.
	 * 
	 * @return property value of provinceId
	 */
	public Long getProvinceId() {
		return provinceId;
	}
	/**
	 * Setter method for property <tt>provinceId</tt>.
	 * 
	 * @param provinceId value to be assigned to property provinceId
	 */
	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}
	/**
	 * Getter method for property <tt>cityId</tt>.
	 * 
	 * @return property value of cityId
	 */
	public Long getCityId() {
		return cityId;
	}
	/**
	 * Setter method for property <tt>cityId</tt>.
	 * 
	 * @param cityId value to be assigned to property cityId
	 */
	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}
	/**
	 * Getter method for property <tt>countyId</tt>.
	 * 
	 * @return property value of countyId
	 */
	public Long getCountyId() {
		return countyId;
	}
	/**
	 * Setter method for property <tt>countyId</tt>.
	 * 
	 * @param countyId value to be assigned to property countyId
	 */
	public void setCountyId(Long countyId) {
		this.countyId = countyId;
	}
	/**
	 * Getter method for property <tt>streetId</tt>.
	 * 
	 * @return property value of streetId
	 */
	public Long getStreetId() {
		return streetId;
	}
	/**
	 * Setter method for property <tt>streetId</tt>.
	 * 
	 * @param streetId value to be assigned to property streetId
	 */
	public void setStreetId(Long streetId) {
		this.streetId = streetId;
	}
	/**
	 * Getter method for property <tt>warehouseIds</tt>.
	 * 
	 * @return property value of warehouseIds
	 */
	public List<Long> getWarehouseIds() {
		return warehouseIds;
	}
	/**
	 * Setter method for property <tt>warehouseIds</tt>.
	 * 
	 * @param warehouseIds value to be assigned to property warehouseIds
	 */
	public void setWarehouseIds(List<Long> warehouseIds) {
		this.warehouseIds = warehouseIds;
	}
	/**
	 * Getter method for property <tt>regionId</tt>.
	 * 
	 * @return property value of regionId
	 */
	public Long getRegionId() {
		return regionId;
	}
	/**
	 * Setter method for property <tt>regionId</tt>.
	 * 
	 * @param regionId value to be assigned to property regionId
	 */
	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}
}
