package com.tp.service.ord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.ord.RejectItemDao;
import com.tp.model.ord.RejectItem;
import com.tp.service.BaseService;
import com.tp.service.ord.IRejectItemService;

@Service
public class RejectItemService extends BaseService<RejectItem> implements IRejectItemService {

	@Autowired
	private RejectItemDao rejectItemDao;
	
	@Override
	public BaseDao<RejectItem> getDao() {
		return rejectItemDao;
	}

}
