package com.tp.service.ord;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.ord.CancelCustomsInfoDao;
import com.tp.model.ord.CancelCustomsInfo;
import com.tp.service.BaseService;
import com.tp.service.ord.ICancelCustomsInfoService;

@Service
public class CancelCustomsInfoService extends BaseService<CancelCustomsInfo> implements ICancelCustomsInfoService {

	@Autowired
	private CancelCustomsInfoDao cancelCustomsInfoDao;
	
	@Override
	public BaseDao<CancelCustomsInfo> getDao() {
		return cancelCustomsInfoDao;
	}

	@Override
	public CancelCustomsInfo selectByOrderCode(Long subCode) {
		Map<String, Object> params = new HashMap<>();
		params.put("orderCode", subCode);
		return queryUniqueByParams(params);
	}

}
