package com.tp.service.stg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.stg.InputBackSkuDao;
import com.tp.model.stg.InputBackSku;
import com.tp.service.BaseService;
import com.tp.service.stg.IInputBackSkuService;

@Service
public class InputBackSkuService extends BaseService<InputBackSku> implements IInputBackSkuService {

	@Autowired
	private InputBackSkuDao inputBackSkuDao;
	
	@Override
	public BaseDao<InputBackSku> getDao() {
		return inputBackSkuDao;
	}

}
