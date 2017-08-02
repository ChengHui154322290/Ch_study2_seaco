package com.tp.dao.wms;

import java.util.List;

import com.tp.common.dao.BaseDao;
import com.tp.model.wms.StocksyncInfo;

public interface StocksyncInfoDao extends BaseDao<StocksyncInfo> {

	/**
	 *	批量插入
	 *	@param details
	 *	@return
	 */
	void insertDetails(List<StocksyncInfo> details);
	
}
