package com.tp.service.stg;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.stg.OutputBackSkuDao;
import com.tp.model.stg.OutputBackSku;
import com.tp.service.BaseService;
import com.tp.service.stg.IOutputBackSkuService;

@Service
public class OutputBackSkuService extends BaseService<OutputBackSku> implements IOutputBackSkuService {

	@Autowired
	private OutputBackSkuDao outputBackSkuDao;
	
	@Override
	public BaseDao<OutputBackSku> getDao() {
		return outputBackSkuDao;
	}

	@Override
	public Long insertBatch(List<OutputBackSku> outputBackSkuList) {
		// TODO Auto-generated method stub
		return outputBackSkuDao.insertBatch(outputBackSkuList);
	}
}
