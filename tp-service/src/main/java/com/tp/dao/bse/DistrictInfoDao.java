package com.tp.dao.bse;

import java.util.List;

import com.tp.common.dao.BaseDao;
import com.tp.model.bse.DistrictInfo;
import com.tp.result.bse.AreaTreeDTO;
import com.tp.result.bse.CountryInformationResult;

public interface DistrictInfoDao extends BaseDao<DistrictInfo> {

	List<AreaTreeDTO> selectChinaRegions();

	List<AreaTreeDTO> selectChinaStreets(AreaTreeDTO areaTreeDTO);

	List<DistrictInfo> selecTAllProvinceInformation();

	List<CountryInformationResult> selectByLikeName(DistrictInfo districtInfo);

}
