package com.tp.service.usr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.Constant;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.dao.usr.SysMenuLimitDao;
import com.tp.model.usr.SysMenuLimit;
import com.tp.service.BaseService;
import com.tp.service.usr.ISysMenuLimitService;
import com.tp.util.StringUtil;

@Service
public class SysMenuLimitService extends BaseService<SysMenuLimit> implements ISysMenuLimitService {

	@Autowired
	private SysMenuLimitDao sysMenuLimitDao;
	
	@Override
	public BaseDao<SysMenuLimit> getDao() {
		return sysMenuLimitDao;
	}
	@Override
	public List<SysMenuLimit> selectByIds(List<Long> ids){
		if(null == ids || ids.isEmpty()) return new ArrayList<SysMenuLimit>();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " id in ("+StringUtil.join(ids, Constant.SPLIT_SIGN.COMMA)+")");
		return sysMenuLimitDao.queryByParam(params);
	}
}
