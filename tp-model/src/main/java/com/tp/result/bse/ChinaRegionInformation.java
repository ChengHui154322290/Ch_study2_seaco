package com.tp.result.bse;

import java.io.Serializable;
import java.util.List;

import com.tp.model.bse.DistrictInfo;

/**
 * 
 * <pre>
 * 中国的区域信息类
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 *          Exp $
 */
public class ChinaRegionInformation implements Serializable {

	private static final long serialVersionUID = 54075572407347186L;
	/**
	 * 区域名称
	 */
	private String regionName;
	/** 区域ID */
	private Long regionId;
	
	/**
	 * 该区域下所包含的一级目录信息
	 */
	private List<DistrictInfo> districtInfoList;

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public Long getRegionId() {
		return regionId;
	}

	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}

	public List<DistrictInfo> getDistrictInfoList() {
		return districtInfoList;
	}

	public void setDistrictInfoList(List<DistrictInfo> districtInfoList) {
		this.districtInfoList = districtInfoList;
	}

}
