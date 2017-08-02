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
import com.tp.dao.bse.ClearanceChannelsDao;
import com.tp.model.bse.ClearanceChannels;
import com.tp.service.BaseService;
import com.tp.service.bse.IClearanceChannelsService;
import com.tp.util.StringUtil;

@Service
public class ClearanceChannelsService extends BaseService<ClearanceChannels> implements IClearanceChannelsService {

	@Autowired
	private ClearanceChannelsDao clearanceChannelsDao;
	
	@Override
	public BaseDao<ClearanceChannels> getDao() {
		return clearanceChannelsDao;
	}

	@Override
	public List<ClearanceChannels> getAllClearanceChannelsByStatus(Integer status) {
		if(1!=status && 0!=status){
			status=null;
		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("status", status);
		return super.queryByParamNotEmpty(params);
	}

	@Override
	public List<ClearanceChannels> getClearanceChannelsListByIds(List<Long> ids) {
		if (CollectionUtils.isEmpty(ids)) {
			return new ArrayList<ClearanceChannels>();
		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " id in ("+StringUtil.join(ids, Constant.SPLIT_SIGN.COMMA)+")");
		return super.queryByParam(params);
	}

}
