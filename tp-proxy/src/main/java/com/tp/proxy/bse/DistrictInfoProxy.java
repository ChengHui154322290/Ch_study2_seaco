package com.tp.proxy.bse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.BseConstant;
import com.tp.common.vo.Constant;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.model.bse.DistrictInfo;
import com.tp.proxy.BaseProxy;
import com.tp.result.bse.AreaTree;
import com.tp.service.IBaseService;
import com.tp.service.bse.IDistrictInfoService;
/**
 * 地区信息代理层
 * @author szy
 *
 */
@Service
public class DistrictInfoProxy extends BaseProxy<DistrictInfo>{

	@Autowired
	private IDistrictInfoService districtInfoService;

	@Override
	public IBaseService<DistrictInfo> getService() {
		return districtInfoService;
	}

	public Map<String,String>  getAllCountryAndAllProvince(){
		DistrictInfo districtInfo =new DistrictInfo();
	    List<DistrictInfo> result=new ArrayList<DistrictInfo>();
		districtInfo.setType(BseConstant.DISTRICT_LEVEL.CONTRY.getCode());
		List<DistrictInfo> listOfCountry = districtInfoService.queryByObject(districtInfo);
		result.addAll(listOfCountry);
		districtInfo.setType(BseConstant.DISTRICT_LEVEL.PROVINCE.getCode());
		List<DistrictInfo> listOfProvince = districtInfoService.queryByObject(districtInfo);
		result.addAll(listOfProvince);
		Map<String, String> resultMap  =new HashMap<String, String>();
		for(DistrictInfo infoDO :result){
			resultMap.put(infoDO.getId().toString(),infoDO.getName());
		}
		return resultMap;
		
	}

	/**
	 * 
	 * <pre>
	 * 封装所有大洲信息map
	 * </pre>
	 *
	 * @return
	 */
	public Map<String,String> inintContinentInformation(){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("type", 1);
		List<DistrictInfo> list = districtInfoService.queryByParam(params);
		Map<String, String> resultMap  =new HashMap<String, String>();
		for(DistrictInfo infoDO :list){
			resultMap.put(infoDO.getId().toString(),infoDO.getName());
		}
		return resultMap;
		
	}
	
	/**
	 * 根据country的name动态查询国家级的相关信息
	 * 
	 * @param name
	 * @return
	 * @throws ExceptionResultInfo
	 */
	public List<DistrictInfo> getDistrictInfosByCountryName(String name){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put(MYBATIS_SPECIAL_STRING.WHERE.name(), " name like concat('%','"+name+"','%')");
		return districtInfoService.queryByParam(params);

	}
	
private List<AreaTree> areaTreeList = new ArrayList<AreaTree>();
	
	/**
	 * 
	 * <pre>
	 * 	获取中国到县的行政区划
	 * </pre>
	 *
	 * @return
	 */
	public List<AreaTree> selectChinaRegions(){
		if(CollectionUtils.isEmpty(areaTreeList)){
			Map<String, Object> params = new HashMap<>();
			params.put("isDelete", Constant.DELECTED.NO);
			params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), "(id = 196 OR parent_id = 196 OR type = 4 OR type = 5 OR type = 6 OR type = 7)");
			List<DistrictInfo> districtInfos = districtInfoService.queryByParamNotEmpty(params); 
			for (DistrictInfo districtInfo : districtInfos) {
				AreaTree areaTree = new AreaTree();
				areaTree.setId(districtInfo.getId());
				areaTree.setpId(Long.valueOf(districtInfo.getParentId()));
				areaTree.setName(districtInfo.getName());
				areaTree.setType(districtInfo.getType());
				areaTreeList.add(areaTree);
			}
		}
		return areaTreeList;
	}

	
	/**
	 * 
	 * <pre>
	 * 	获取县区下面街道的行政区划
	 * </pre>
	 *
	 * @return
	 */
	public List<AreaTree> selectChinaStreets(Long areaId){
		Map<String, Object> params = new HashMap<>();
		params.put("isDelete", Constant.DELECTED.NO);
		params.put("parentId", areaId);
		List<DistrictInfo> districtInfos = districtInfoService.queryByParamNotEmpty(params);
		List<AreaTree> areaTrees = new ArrayList<>();
		for (DistrictInfo districtInfo : districtInfos) {
			AreaTree areaTree = new AreaTree();
			areaTree.setId(districtInfo.getId());
			areaTree.setpId(Long.valueOf(districtInfo.getParentId()));
			areaTree.setName(districtInfo.getName());
			areaTree.setType(districtInfo.getType());
			areaTrees.add(areaTree);
		}
		return areaTrees;
	}
	
	@SuppressWarnings("unchecked")
	public List<DistrictInfo> queryChildren(Long districtId){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("isDelete", Constant.DELECTED.NO);
		params.put("parentId", districtId);
		return districtInfoService.queryByParam(params);
	}

	@SuppressWarnings("unchecked")
	public List<DistrictInfo> queryProvinceList() {
		return districtInfoService.queryProvinceList();
	}
	
	public List<DistrictInfo> queryParents(Long districtId){
		DistrictInfo districtInfoDO = districtInfoService.queryById(districtId);
		if(districtInfoDO==null){
			return null;
		}
		List<DistrictInfo> provinceList = queryProvinceList();
		List<DistrictInfo> childrenList = new ArrayList<DistrictInfo>();
		Long parentId = districtInfoDO.getParentId();
		Long addressId = districtInfoDO.getId();
		boolean isProvince = Boolean.FALSE;
		do{
			if(!isProvince){
				List<DistrictInfo> children = queryChildren(parentId);
				for(DistrictInfo child:children){
					if(addressId==child.getId().longValue()){
						child.setSelected(Constant.SELECTED.YES);
					}
				}
				childrenList.addAll(children);
			}
			for(DistrictInfo province:provinceList){
				if(parentId==province.getId().longValue()){
					isProvince = Boolean.TRUE;
					province.setSelected(Constant.SELECTED.YES);
				}else province.setSelected(Constant.SELECTED.YES);
			}
			
			districtInfoDO = districtInfoService.queryById(parentId);
			parentId = districtInfoDO.getParentId();
			addressId = districtInfoDO.getId();
		}while(!isProvince);
		childrenList.addAll(provinceList);
		return childrenList;
	}
}
