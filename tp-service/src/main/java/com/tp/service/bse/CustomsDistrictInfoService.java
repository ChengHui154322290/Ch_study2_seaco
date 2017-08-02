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
import com.tp.dao.bse.CustomsDistrictInfoDao;
import com.tp.dao.bse.CustomsDistrictLinkDao;
import com.tp.model.bse.CustomsDistrictInfo;
import com.tp.model.bse.CustomsDistrictLink;
import com.tp.service.BaseService;
import com.tp.service.bse.ICustomsDistrictInfoService;
import com.tp.util.StringUtil;

@Service
public class CustomsDistrictInfoService extends BaseService<CustomsDistrictInfo> implements ICustomsDistrictInfoService {

	@Autowired
	private CustomsDistrictInfoDao customsDistrictInfoDao;
	
	@Autowired
	private CustomsDistrictLinkDao customsDistrictLinkDao;
	
	@Override
	public BaseDao<CustomsDistrictInfo> getDao() {
		return customsDistrictInfoDao;
	}

	/**
	 *	根据海关类型和系统的地区ID查询对应海关地区信息
	 */
	public Map<Long, CustomsDistrictInfo> queryCustomsDistrictInfo(List<Long> districtIds){
		Map<Long, CustomsDistrictInfo> mapDid2CustomsDInfo = new HashMap<>();
		if (CollectionUtils.isEmpty(districtIds)) return mapDid2CustomsDInfo;
		
		Map<String, Object> params = new HashMap<>();
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), "district_id in(" + StringUtil.join(districtIds, Constant.SPLIT_SIGN.COMMA) + ")");
		List<CustomsDistrictLink> links = customsDistrictLinkDao.queryByParam(params);	
		if (CollectionUtils.isEmpty(links)) return mapDid2CustomsDInfo;
		
		List<Long> customsDistrictIds = new ArrayList<>();
		Map<Long, Long> mapCDid2Did = new HashMap<>();
		for (CustomsDistrictLink l : links) {
			customsDistrictIds.add(l.getCustomsDistrictId());
			mapCDid2Did.put(l.getCustomsDistrictId(), l.getDistrictId());
		}
		
		params.clear();
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), "id in(" + StringUtil.join(customsDistrictIds, Constant.SPLIT_SIGN.COMMA) + ")");
		List<CustomsDistrictInfo> infos = getDao().queryByParam(params);
		if (CollectionUtils.isEmpty(infos)) {
			return mapDid2CustomsDInfo;
		}
		
		for (CustomsDistrictInfo c : infos) {
			Long districtId = mapCDid2Did.get(c.getId());
			if (null != districtId) {
				mapDid2CustomsDInfo.put(districtId, c);
			}
		}
		return mapDid2CustomsDInfo;
	}
}
