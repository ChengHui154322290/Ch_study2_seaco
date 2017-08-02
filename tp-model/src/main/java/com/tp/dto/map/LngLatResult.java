package com.tp.dto.map;

import java.io.Serializable;
import java.util.List;

/**
 * 经纬度结果
 * @author szy
 *
 */
public class LngLatResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7228052246127190902L;
	private Integer status;//结果状态值  值为0或1，0表示请求失败；1表示请求成功
	private Integer count;//返回结果数目  
	private String info;//状态说明 
	private List<GeocodesResult> geocodes;//地理编码信息列表 
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public List<GeocodesResult> getGeocodes() {
		return geocodes;
	}
	public void setGeocodes(List<GeocodesResult> geocodes) {
		this.geocodes = geocodes;
	}
}
