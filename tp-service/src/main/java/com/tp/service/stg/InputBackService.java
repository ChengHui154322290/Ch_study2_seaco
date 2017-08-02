package com.tp.service.stg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.stg.InputBackDao;
import com.tp.model.stg.InputBack;
import com.tp.service.BaseService;
import com.tp.service.stg.IInputBackService;

@Service
public class InputBackService extends BaseService<InputBack> implements IInputBackService {

	@Autowired
	private InputBackDao inputBackDao;
	
	@Override
	public BaseDao<InputBack> getDao() {
		return inputBackDao;
	}

}
