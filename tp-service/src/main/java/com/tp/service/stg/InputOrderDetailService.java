package com.tp.service.stg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.stg.InputOrderDetailDao;
import com.tp.model.stg.InputOrderDetail;
import com.tp.service.BaseService;
import com.tp.service.stg.IInputOrderDetailService;

@Service
public class InputOrderDetailService extends BaseService<InputOrderDetail> implements IInputOrderDetailService {

	@Autowired
	private InputOrderDetailDao inputOrderDetailDao;
	
	@Override
	public BaseDao<InputOrderDetail> getDao() {
		return inputOrderDetailDao;
	}

}
