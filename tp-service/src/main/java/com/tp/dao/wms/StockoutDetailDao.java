package com.tp.dao.wms;

import java.util.List;

import com.tp.common.dao.BaseDao;
import com.tp.model.wms.StockoutDetail;

public interface StockoutDetailDao extends BaseDao<StockoutDetail> {

	/**
	 *	批量插入出库单商品 
	 *	@param details
	 *	@return
	 */
	void insertDetails(List<StockoutDetail> details);
}
