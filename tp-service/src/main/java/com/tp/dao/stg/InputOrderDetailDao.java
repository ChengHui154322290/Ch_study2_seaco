package com.tp.dao.stg;

import java.util.List;

import com.tp.common.dao.BaseDao;
import com.tp.model.stg.InputOrderDetail;

public interface InputOrderDetailDao extends BaseDao<InputOrderDetail> {
	/**
	 * 批量插入
	 * @param detailDOs
	 */
	void batchInsert(List<InputOrderDetail> detailObjs);
}
