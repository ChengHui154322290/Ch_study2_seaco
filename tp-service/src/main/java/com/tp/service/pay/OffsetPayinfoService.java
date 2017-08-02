package com.tp.service.pay;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.pay.OffsetPayinfoDao;
import com.tp.model.pay.OffsetPayinfo;
import com.tp.service.BaseService;
import com.tp.service.pay.IOffsetPayinfoService;

@Service
public class OffsetPayinfoService extends BaseService<OffsetPayinfo> implements IOffsetPayinfoService {

	@Autowired
	private OffsetPayinfoDao offsetPayinfoDao;
	
	@Override
	public BaseDao<OffsetPayinfo> getDao() {
		return offsetPayinfoDao;
	}

}
