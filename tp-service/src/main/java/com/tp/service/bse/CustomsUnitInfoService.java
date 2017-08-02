package com.tp.service.bse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.Constant;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.dao.bse.CustomsUnitInfoDao;
import com.tp.dao.bse.CustomsUnitLinkDao;
import com.tp.model.bse.CustomsUnitInfo;
import com.tp.model.bse.CustomsUnitLink;
import com.tp.service.BaseService;
import com.tp.service.bse.ICustomsUnitInfoService;
import com.tp.util.StringUtil;

@Service
public class CustomsUnitInfoService extends BaseService<CustomsUnitInfo> implements ICustomsUnitInfoService {

	@Autowired
	private CustomsUnitInfoDao customsUnitInfoDao;
	
	@Autowired
	private CustomsUnitLinkDao customsUnitLinkDao;
	
	@Override
	public BaseDao<CustomsUnitInfo> getDao() {
		return customsUnitInfoDao;
	}

	public Map<Long, CustomsUnitInfo> queryCustomsUnitInfo(List<Long> unitIds){
		if (CollectionUtils.isEmpty(unitIds)) return new HashMap<>();
		Map<String, Object> params = new HashMap<>();
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), "unit_id in(" + StringUtil.join(unitIds, Constant.SPLIT_SIGN.COMMA) + ")");
		List<CustomsUnitLink> links = customsUnitLinkDao.queryByParam(params);
		if(CollectionUtils.isEmpty(links)) return new HashMap<>();
		Map<Long, Long> mapCustomsUnitId2UnitId = new HashMap<>();
		List<Long> customsUnitIds = new ArrayList<>();
		for (CustomsUnitLink l : links) {
			mapCustomsUnitId2UnitId.put(l.getCustomsUnitId(), l.getUnitId());
			customsUnitIds.add(l.getCustomsUnitId());
		}
		
		params.clear();
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), "id in(" + StringUtil.join(customsUnitIds, Constant.SPLIT_SIGN.COMMA) + ")");
		List<CustomsUnitInfo> infos = customsUnitInfoDao.queryByParam(params);
		if (CollectionUtils.isEmpty(infos)) {
			return new HashMap<>();
		}
		
		Map<Long, CustomsUnitInfo> mapUnitId2CustomsUnitInfo = new HashMap<>();
		for (CustomsUnitInfo c : infos) {
			Long unitId = mapCustomsUnitId2UnitId.get(c.getId());
			if (null != unitId) {
				mapUnitId2CustomsUnitInfo.put(unitId, c);
			}
		}
		return mapUnitId2CustomsUnitInfo;
	}
}
