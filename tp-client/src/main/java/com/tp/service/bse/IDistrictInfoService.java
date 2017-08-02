package com.tp.service.bse;

import java.util.List;

import com.tp.model.bse.DistrictInfo;
import com.tp.result.bse.AreaTreeDTO;
import com.tp.result.bse.ChinaRegionInformation;
import com.tp.result.bse.CountryInformationResult;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 地区信息接口
  */
public interface IDistrictInfoService extends IBaseService<DistrictInfo>{

	public DistrictInfo queryOwnPoliticsById(Long districtId);
	
	public List<CountryInformationResult> selectByLikeName(DistrictInfo districtInfo);
	
	public List<ChinaRegionInformation> chinaAllRegionInformation();
	
	public List<AreaTreeDTO> selectChinaRegions();
	
	public List<AreaTreeDTO> selectChinaStreets(AreaTreeDTO areaTreeDTO);
	
	public List<DistrictInfo> chinaAllProvinceInformation();
	
	public List<DistrictInfo> selectListByIds(List<Long> ids);
	
	public List<DistrictInfo> selectAllNationalDetail();

	public List<DistrictInfo> queryProvinceList();
}
