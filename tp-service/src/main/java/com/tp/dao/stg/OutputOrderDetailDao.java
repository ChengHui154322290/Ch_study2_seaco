package com.tp.dao.stg;

import java.util.List;

import com.tp.common.dao.BaseDao;
import com.tp.model.stg.OutputOrderDetail;

public interface OutputOrderDetailDao extends BaseDao<OutputOrderDetail> {

	/**
	 * 批量插入
	 * @param detailDOs
	 */
	void insertBatch(List<OutputOrderDetail> detailDOs);
	
}
