package com.tp.m.ao.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.Constant;
import com.tp.m.base.MResultVO;
import com.tp.m.enums.MResultInfo;
import com.tp.m.to.system.CityTO;
import com.tp.m.to.system.ProvinceTO;
import com.tp.m.vo.system.ProvincesVO;
import com.tp.model.bse.DistrictInfo;
import com.tp.proxy.bse.DistrictInfoProxy;

/**
 * 地理位置
 * @author zhuss
 */
@Service
public class PositionAO {

	private static Logger log = LoggerFactory.getLogger(PositionAO.class);
	
	@Autowired
	private DistrictInfoProxy districtInfoProxy;
	/**
	 * 首页 - 获取省份列表
	 * @return
	 */
	public MResultVO<List<ProvincesVO>> getProvList(){
		try{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("type", 3);
			params.put("isDelete", Constant.DELECTED.NO);
			List<DistrictInfo> districtInfoList = districtInfoProxy.queryByParam(params).getData();
			List<ProvincesVO> pvlist = new ArrayList<>();
			if(CollectionUtils.isNotEmpty(districtInfoList)){
				for(DistrictInfo districtInfo:districtInfoList){
					List<DistrictInfo> children = districtInfoProxy.queryChildren(districtInfo.getId());
					List<ProvinceTO> childrenList = new ArrayList<>();
					if(CollectionUtils.isNotEmpty(children)){
						for(DistrictInfo child:children){
							childrenList.add(new ProvinceTO(child.getName(),child.getId().toString()));
						}
					}
					pvlist.add(new ProvincesVO(districtInfo.getName(),districtInfo.getId().toString(),childrenList));
				}
			}
			return new MResultVO<>(MResultInfo.SUCCESS,pvlist);
		}catch(Exception e){
			log.error("[API接口 - 获取省份列表 Exception] = {}", e);
			return new MResultVO<>(MResultInfo.CONN_ERROR);
		}
	} 
	
	/**
	 * 获取城市列表
	 * @param position
	 * @return
	 */
	public MResultVO<List<CityTO>> getCityList(Long id){
		try{
			List<DistrictInfo> districtInfoList = districtInfoProxy.queryChildren(id);
			List<CityTO> cylist = new ArrayList<>();
			if(CollectionUtils.isNotEmpty(districtInfoList)){
				for(DistrictInfo districtInfo:districtInfoList){
					cylist.add(new CityTO(districtInfo.getName(),districtInfo.getId().toString()));
				}
			}
			return new MResultVO<>(MResultInfo.SUCCESS,cylist);
		}catch(Exception e){
			log.error("[API接口 - 获取城市列表 Exception] = {}",e);
			return new MResultVO<>(MResultInfo.CONN_ERROR);
		}
	} 
}
