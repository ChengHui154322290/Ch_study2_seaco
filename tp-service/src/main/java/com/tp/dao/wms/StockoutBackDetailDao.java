package com.tp.dao.wms;

import java.util.List;

import com.tp.common.dao.BaseDao;
import com.tp.model.wms.StockoutBackDetail;

public interface StockoutBackDetailDao extends BaseDao<StockoutBackDetail> {

	/**
	 * 批量插入回执商品详情 
	 */
	void insertDetails(List<StockoutBackDetail> details);
	
}
