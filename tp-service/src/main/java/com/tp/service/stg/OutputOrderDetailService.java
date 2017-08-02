package com.tp.service.stg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.stg.OutputOrderDetailDao;
import com.tp.model.stg.OutputOrderDetail;
import com.tp.service.BaseService;
import com.tp.service.stg.IOutputOrderDetailService;

@Service
public class OutputOrderDetailService extends BaseService<OutputOrderDetail> implements IOutputOrderDetailService {

	@Autowired
	private OutputOrderDetailDao outputOrderDetailDao;
	
	@Override
	public BaseDao<OutputOrderDetail> getDao() {
		return outputOrderDetailDao;
	}

}
