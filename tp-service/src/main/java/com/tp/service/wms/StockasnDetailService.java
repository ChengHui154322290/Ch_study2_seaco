package com.tp.service.wms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.wms.StockasnDetailDao;
import com.tp.model.wms.StockasnDetail;
import com.tp.service.BaseService;
import com.tp.service.wms.IStockasnDetailService;

@Service
public class StockasnDetailService extends BaseService<StockasnDetail> implements IStockasnDetailService {

	@Autowired
	private StockasnDetailDao stockasnDetailDao;
	
	@Override
	public BaseDao<StockasnDetail> getDao() {
		return stockasnDetailDao;
	}

}
