package com.tp.proxy.map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.dto.map.DistanceRequest;
import com.tp.dto.map.DistancesResult;
import com.tp.dto.map.LngLatRequest;
import com.tp.dto.map.LngLatResult;
import com.tp.service.map.IMapAPIService;

/**
 * 地图代理
 * @author szy
 *
 */
@Service
public class MapAPIProxy {
	
	@Autowired
	private IMapAPIService mapAPIService;
	
	/**
	 * 经纬度查询
	 * @param request
	 * @return
	 */
	public LngLatResult geocode(String address,String city){
		LngLatRequest request = new LngLatRequest();
		request.setAddress(address);
		request.setCity(city);
		return mapAPIService.geocode(request);
	}
	
	/**
	 * 查询两点车程
	 * @param request
	 * @return
	 */
	public DistancesResult distance(String origins,String destination){
		DistanceRequest request = new DistanceRequest();
		request.setOrigins(origins);
		request.setDestination(destination);
		return mapAPIService.distance(request);
	}
	
	/**
	 * 经纬度查询
	 * @param request
	 * @return
	 */
	public LngLatResult geocode(LngLatRequest request){
		return mapAPIService.geocode(request);
	}
	
	/**
	 * 查询两点车程
	 * @param request
	 * @return
	 */
	public DistancesResult distance(DistanceRequest request){
		return mapAPIService.distance(request);
	}
}
