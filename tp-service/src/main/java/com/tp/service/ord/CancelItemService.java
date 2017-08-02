package com.tp.service.ord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.ord.CancelItemDao;
import com.tp.model.ord.CancelItem;
import com.tp.service.BaseService;
import com.tp.service.ord.ICancelItemService;

@Service
public class CancelItemService extends BaseService<CancelItem> implements ICancelItemService {

	@Autowired
	private CancelItemDao cancelItemDao;
	
	@Override
	public BaseDao<CancelItem> getDao() {
		return cancelItemDao;
	}

}
