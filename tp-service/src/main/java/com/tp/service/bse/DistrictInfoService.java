package com.tp.service.bse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.BseConstant;
import com.tp.common.vo.Constant;
import com.tp.dao.bse.DistrictInfoDao;
import com.tp.model.bse.DistrictInfo;
import com.tp.redis.util.JedisCacheUtil;
import com.tp.result.bse.AreaTreeDTO;
import com.tp.result.bse.ChinaRegionInformation;
import com.tp.result.bse.CountryInformationResult;
import com.tp.service.BaseService;
import com.tp.service.bse.IDistrictInfoService;

@Service
public class DistrictInfoService extends BaseService<DistrictInfo> implements IDistrictInfoService {

	@Autowired
	private DistrictInfoDao districtInfoDao;
	@Autowired
	private JedisCacheUtil jedisCacheUtil;
	
	@Override
	public BaseDao<DistrictInfo> getDao() {
		return districtInfoDao;
	}
	@Override
	public DistrictInfo queryOwnPoliticsById(Long districtId) {
		if(null==districtId){
			return null;
		}
		
		int count = 0;
		while(count<10){
			DistrictInfo districtInfoDO = queryById(districtId);
			if(null==districtInfoDO){
				return null;
			}
			if(districtInfoDO.getType()==BseConstant.DISTRICT_LEVEL.REGION.getCode()){
				return districtInfoDO;
			}
			districtId = districtInfoDO.getParentId();
			count++;
		}
		return null;
	}

	@Override
	public List<CountryInformationResult> selectByLikeName(DistrictInfo districtInfoDO) {
		return districtInfoDao.selectByLikeName(districtInfoDO);
	}
	
	@Override
	public List<ChinaRegionInformation> chinaAllRegionInformation() {
		@SuppressWarnings("unchecked")
		List<ChinaRegionInformation> chinaRegionInformations=(List<ChinaRegionInformation>)jedisCacheUtil.getCache("CHINA_ALL_REGION_INFO");
         if(CollectionUtils.isNotEmpty(chinaRegionInformations)){
        	 return chinaRegionInformations;
         }
		List<ChinaRegionInformation>  resultList =new ArrayList<ChinaRegionInformation>();
		DistrictInfo searchRegion=new DistrictInfo();
		searchRegion.setType(BseConstant.DISTRICT_LEVEL.REGION.getCode());
		DistrictInfo searchProvince=new DistrictInfo();
		searchProvince.setType(BseConstant.DISTRICT_LEVEL.PROVINCE.getCode());
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("type", BseConstant.DISTRICT_LEVEL.REGION.getCode());
		List<DistrictInfo> listOfRegion = districtInfoDao.queryByParam(params);
		params.put("type", BseConstant.DISTRICT_LEVEL.PROVINCE.getCode());
		List<DistrictInfo> listOfProvice = districtInfoDao.queryByParam(params);
		for (DistrictInfo districtInfoDO2 : listOfRegion) {
			ChinaRegionInformation  chinaRegionInformation =new ChinaRegionInformation();
			chinaRegionInformation.setRegionName(districtInfoDO2.getName());
			chinaRegionInformation.setRegionId(districtInfoDO2.getId());
			List<DistrictInfo> rusultOfProvinceList=new ArrayList<DistrictInfo>();
			for (DistrictInfo districtInfo : listOfProvice) {
			     if(districtInfoDO2.getId().equals(districtInfo.getParentId())){
			    	 rusultOfProvinceList.add(districtInfo);
			     }
			}
			chinaRegionInformation.setDistrictInfoList(rusultOfProvinceList);
			resultList.add(chinaRegionInformation);
		}
		 jedisCacheUtil.setCache("CHINA_ALL_REGION_INFO", resultList, Integer.MAX_VALUE);
		return resultList;
	}
	
	@Override
	public List<AreaTreeDTO> selectChinaRegions(){
		List<AreaTreeDTO> list = districtInfoDao.selectChinaRegions();
		return list;
	}
	
	@Override
	public List<AreaTreeDTO> selectChinaStreets(AreaTreeDTO areaTreeDTO){
		List<AreaTreeDTO> list = districtInfoDao.selectChinaStreets(areaTreeDTO);
		return list;
	}


	@Override
	public List<DistrictInfo> chinaAllProvinceInformation() {
		List<DistrictInfo> chinaAllProvinceInformations=(List<DistrictInfo>)jedisCacheUtil.getCache("CHINA_ALL_PROVINCE_INFO");
        if(CollectionUtils.isNotEmpty(chinaAllProvinceInformations)){
       	 return chinaAllProvinceInformations;
        }
		List<DistrictInfo>  resultList =districtInfoDao.selecTAllProvinceInformation();
		jedisCacheUtil.setCache("CHINA_ALL_PROVINCE_INFO", resultList, Integer.MAX_VALUE);
		return resultList;
	}


	@Override
	public List<DistrictInfo> selectListByIds(List<Long> ids)  {
		if(CollectionUtils.isEmpty(ids)){
			return new ArrayList<DistrictInfo>();
		}
		List<DistrictInfo> list =new ArrayList<DistrictInfo>();
		for (int i = 0; i < ids.size(); i++) {
			if(null==ids.get(i)){
				continue;
			}
			DistrictInfo districtInfoDO = this.queryById(ids.get(i));
			if(null !=districtInfoDO){
				list.add(districtInfoDO);
			}	
		}
		return list;
	}


	@Override
	public List<DistrictInfo> selectAllNationalDetail(){
		@SuppressWarnings("unchecked")
		List<DistrictInfo> allNationalDetail=(List<DistrictInfo>)jedisCacheUtil.getCache("ALL_NATIONAL_DETAIL");
        if(CollectionUtils.isNotEmpty(allNationalDetail)){
        	return allNationalDetail;
        }
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("type", BseConstant.DISTRICT_LEVEL.CONTRY.getCode());
        List<DistrictInfo>  resultList=districtInfoDao.queryByParam(params);
		jedisCacheUtil.setCache("ALL_NATIONAL_DETAIL", resultList, Integer.MAX_VALUE);
		return resultList;
	}
	@Override
	public List<DistrictInfo> queryProvinceList() {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("isDelete", Constant.DELECTED.NO);
		params.put("type", 4);
		return districtInfoDao.queryByParam(params);
	}
	
	@Override
	public List<DistrictInfo> queryByParam(Map<String,Object> params){
		Integer hashcode = params.hashCode();
		List<DistrictInfo> list=(List<DistrictInfo>)jedisCacheUtil.getCache("districtinfo:params:"+hashcode);
        if(null==list){
            list = super.queryByParam(params);
            jedisCacheUtil.setCache("districtinfo:params:"+hashcode, list);
        }
        return list;
	}
}
