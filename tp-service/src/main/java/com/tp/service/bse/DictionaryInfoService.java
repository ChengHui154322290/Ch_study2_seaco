package com.tp.service.bse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.Constant;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.dao.bse.DictionaryInfoDao;
import com.tp.model.bse.DictionaryInfo;
import com.tp.service.BaseService;
import com.tp.service.bse.IDictionaryInfoService;
import com.tp.util.StringUtil;

@Service
public class DictionaryInfoService extends BaseService<DictionaryInfo> implements IDictionaryInfoService {

	@Autowired
	private DictionaryInfoDao dictionaryInfoDao;
	
	@Override
	public BaseDao<DictionaryInfo> getDao() {
		return dictionaryInfoDao;
	}

	@Override
	public List<DictionaryInfo> selectListByIds(List<Long> ids) {
		if(CollectionUtils.isNotEmpty(ids)){
			Map<String,Object> params = new HashMap<String,Object>();
			params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " id in ("+StringUtil.join(ids,Constant.SPLIT_SIGN.COMMA)+")");
			return dictionaryInfoDao.queryByParam(params);
		}
		return null;
	}

}
