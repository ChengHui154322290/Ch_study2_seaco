package com.tp.service.bse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.Constant;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.dao.bse.NationalIconDao;
import com.tp.model.bse.NationalIcon;
import com.tp.model.prd.ItemDetail;
import com.tp.service.BaseService;
import com.tp.service.bse.INationalIconService;
import com.tp.util.StringUtil;

@Service
public class NationalIconService extends BaseService<NationalIcon> implements INationalIconService {

	@Autowired
	private NationalIconDao nationalIconDao;
	
	@Override
	public BaseDao<NationalIcon> getDao() {
		return nationalIconDao;
	}
		
	@Override
	public List<NationalIcon> selectListByCountryIds(List<Long> Ids){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " country_id in ("+StringUtil.join(Ids, Constant.SPLIT_SIGN.COMMA)+")");
		return nationalIconDao.queryByParam(params);
	}

}
