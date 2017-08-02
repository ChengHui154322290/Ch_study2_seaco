package com.tp.service.bse;

import java.util.List;
import java.util.Map;

import com.tp.model.bse.CustomsDistrictInfo;
import com.tp.service.IBaseService;
/**
  * @author szy 
  * 对接海关数据地区信息表接口
  */
public interface ICustomsDistrictInfoService extends IBaseService<CustomsDistrictInfo>{

	/**
	 *	根据系统的地区ID查询对应海关地区信息
	 *	@return MAP<系统地区ID, 海关数据地区信息>
	 */
	Map<Long, CustomsDistrictInfo> queryCustomsDistrictInfo(List<Long> districtIds);
}
