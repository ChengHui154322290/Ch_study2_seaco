package com.tp.service.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.app.SwitchConfDao;
import com.tp.model.app.SwitchConf;
import com.tp.service.BaseService;
import com.tp.service.app.ISwitchConfService;

@Service
public class SwitchConfService extends BaseService<SwitchConf> implements ISwitchConfService {

	@Autowired
	private SwitchConfDao switchConfDao;
	
	@Override
	public BaseDao<SwitchConf> getDao() {
		return switchConfDao;
	}

}
