package com.tp.service.ord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.ord.OffsetLogDao;
import com.tp.model.ord.OffsetLog;
import com.tp.service.BaseService;
import com.tp.service.ord.IOffsetLogService;

@Service
public class OffsetLogService extends BaseService<OffsetLog> implements IOffsetLogService {

	@Autowired
	private OffsetLogDao offsetLogDao;
	
	@Override
	public BaseDao<OffsetLog> getDao() {
		return offsetLogDao;
	}

}
