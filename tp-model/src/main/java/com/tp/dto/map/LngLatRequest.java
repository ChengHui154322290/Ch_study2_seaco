package com.tp.dto.map;

import java.io.Serializable;

/**
 * 经纬度查询
 * @author szy
 *
 */
public class LngLatRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1484481460318744L;
	/**地址*/
	private String address;
	/**城市*/
	private String city;
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
}
