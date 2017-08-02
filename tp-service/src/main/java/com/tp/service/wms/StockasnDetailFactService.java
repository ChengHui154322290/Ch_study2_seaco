package com.tp.service.wms;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.wms.StockasnDetailFactDao;
import com.tp.model.wms.StockasnDetailFact;
import com.tp.service.BaseService;
import com.tp.service.wms.IStockasnDetailFactService;

@Service
public class StockasnDetailFactService extends BaseService<StockasnDetailFact> implements IStockasnDetailFactService {

	@Autowired
	private StockasnDetailFactDao stockasnDetailFactDao;
	
	@Override
	public BaseDao<StockasnDetailFact> getDao() {
		return stockasnDetailFactDao;
	}

	/**
	 * 批量保存
	 */
	@Override
	public void batchInsert(List<StockasnDetailFact> list) {
		stockasnDetailFactDao.batchInsert(list);
	}

}
