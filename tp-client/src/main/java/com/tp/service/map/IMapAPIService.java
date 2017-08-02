package com.tp.service.map;

import com.tp.dto.map.DistanceRequest;
import com.tp.dto.map.DistancesResult;
import com.tp.dto.map.LngLatRequest;
import com.tp.dto.map.LngLatResult;

public interface IMapAPIService {
	/**
	 * 经纬度查询
	 * @param request
	 * @return
	 */
	public LngLatResult geocode(LngLatRequest request);
	
	/**
	 * 查询两点车程
	 * @param request
	 * @return
	 */
	public DistancesResult distance(DistanceRequest request);
}
