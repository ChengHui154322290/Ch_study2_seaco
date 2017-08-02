package com.tp.model.mmp;

import java.io.Serializable;

import com.tp.common.annotation.Id;
import com.tp.model.BaseDO;
/**
  * @author szy
  * 库位
  */
public class StockLocation extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1451440579109L;

	/**库位编号 数据类型char(32)*/
	@Id
	private String locationCode;
	
	/**库位名称 数据类型varchar(100)*/
	private String locationName;
	
	
	public String getLocationCode(){
		return locationCode;
	}
	public String getLocationName(){
		return locationName;
	}
	public void setLocationCode(String locationCode){
		this.locationCode=locationCode;
	}
	public void setLocationName(String locationName){
		this.locationName=locationName;
	}
}
