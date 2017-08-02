package com.tp.service.wms;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.wms.StockinImportDetailDao;
import com.tp.model.wms.StockinImportDetail;
import com.tp.service.BaseService;
import com.tp.service.wms.IStockinImportDetailService;

@Service
public class StockinImportDetailService extends BaseService<StockinImportDetail> implements IStockinImportDetailService {

	@Autowired
	private StockinImportDetailDao stockinImportDetailDao;
	
	@Override
	public BaseDao<StockinImportDetail> getDao() {
		return stockinImportDetailDao;
	}

	@Override
	public void batchInsert(List<StockinImportDetail> list) {
		stockinImportDetailDao.batchInsert(list);
	}
}
