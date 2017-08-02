package com.tp.service.ord;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.common.vo.Constant;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.dao.ord.MemRealinfoDao;
import com.tp.model.ord.MemRealinfo;
import com.tp.service.BaseService;
import com.tp.service.ord.IMemRealinfoService;
import com.tp.util.StringUtil;

@Service
public class MemRealinfoService extends BaseService<MemRealinfo> implements IMemRealinfoService {

	@Autowired
	private MemRealinfoDao memRealinfoDao;
	
	@Override
	public BaseDao<MemRealinfo> getDao() {
		return memRealinfoDao;
	}
	@Override
	public MemRealinfo selectOneByOrderCode(Long orderCode) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("parentOrderCode", orderCode);
		return super.queryUniqueByParams(params);
	}
	@Override
	public List<MemRealinfo> selectListByOrderCodeList(List<Long> orderCodeList) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(),"parent_order_code in ("+StringUtil.join(orderCodeList, Constant.SPLIT_SIGN.COMMA)+")");
		return super.queryByParam(params);
	}
}
