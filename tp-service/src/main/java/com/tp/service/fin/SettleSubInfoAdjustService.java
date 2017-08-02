package com.tp.service.fin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.fin.SettleSubInfoAdjustDao;
import com.tp.model.fin.SettleSubInfoAdjust;
import com.tp.service.BaseService;
import com.tp.service.fin.ISettleSubInfoAdjustService;

@Service
public class SettleSubInfoAdjustService extends BaseService<SettleSubInfoAdjust> implements ISettleSubInfoAdjustService {

	@Autowired
	private SettleSubInfoAdjustDao settleSubInfoAdjustDao;
	
	@Override
	public BaseDao<SettleSubInfoAdjust> getDao() {
		return settleSubInfoAdjustDao;
	}

}
