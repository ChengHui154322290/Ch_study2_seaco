package com.tp.service.bse;

import java.util.List;
import java.util.Map;

import com.tp.model.bse.CustomsUnitInfo;
import com.tp.service.IBaseService;
/**
  * @author szy 
  * 对接海关数据计量单位信息表接口
  */
public interface ICustomsUnitInfoService extends IBaseService<CustomsUnitInfo>{

	/**
	 *	根据系统的计量单位查询对应海关数据的计量单位信息
	 *	@return MAP<系统计量单位ID，海关计量单位信息>
	 */
	Map<Long, CustomsUnitInfo> queryCustomsUnitInfo(List<Long> unitIds);
}
