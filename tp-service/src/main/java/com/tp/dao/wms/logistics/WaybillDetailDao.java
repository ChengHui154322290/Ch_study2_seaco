package com.tp.dao.wms.logistics;

import java.util.List;

import com.tp.common.dao.BaseDao;
import com.tp.model.wms.WaybillDetail;

public interface WaybillDetailDao extends BaseDao<WaybillDetail> {

	/**
	 * 批量插入运单详情
	 */
	void insertDetails(List<WaybillDetail> details);
	
}
