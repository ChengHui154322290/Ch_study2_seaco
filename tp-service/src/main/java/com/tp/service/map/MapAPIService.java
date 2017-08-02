package com.tp.service.map;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tp.dto.map.DistanceRequest;
import com.tp.dto.map.DistancesResult;
import com.tp.dto.map.LngLatRequest;
import com.tp.dto.map.LngLatResult;
import com.tp.service.map.IMapAPIService;
import com.tp.util.HttpClientUtil;
import com.tp.util.JsonUtil;

@Service
public class MapAPIService implements IMapAPIService{
	
	private static final Logger logger = LoggerFactory.getLogger(MapAPIService.class);
	@Value("${lbs.amap.com.geocode.url}")
	private String geocodeUrl;
	@Value("${lbs.amap.com.distance.url}")
	private String distanceUrl;
	@Value("${lbs.amap.com.key}")
	private String key;
	/**
	 * 经纬度查询
	 * @param request
	 * @return
	 */
	public LngLatResult geocode(LngLatRequest request){
		String url= MessageFormat.format(geocodeUrl, key,encode(request.getAddress()),encode(request.getCity()));
		try {
			String result = HttpClientUtil.getData(url, "UTF-8");
			LngLatResult lngLat = (LngLatResult)JsonUtil.getFastJsonObject(result, LngLatResult.class);
			return lngLat;
		} catch (Exception e) {
			logger.error("经纬度查询出错：{},参数:{}", e,request);
		}
		return null;
	}
	
	/**
	 * 查询两点车程
	 * @param request
	 * @return
	 */
	public DistancesResult distance(DistanceRequest request){
		String url= MessageFormat.format(distanceUrl, key,encode(request.getOrigins()),encode(request.getDestination()));
		try {
			String result = HttpClientUtil.getData(url, "UTF-8");
			DistancesResult distances = (DistancesResult)JsonUtil.getFastJsonObject(result, DistancesResult.class);
			return distances;
		} catch (Exception e) {
			logger.error("查询两点车程出错：{},参数:{}", e,request);
		}
		return null;
	}
	
	private String encode(String str){
    	if(str==null)str="";
    	try {
			return URLEncoder.encode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return str;
		}
    }
}
