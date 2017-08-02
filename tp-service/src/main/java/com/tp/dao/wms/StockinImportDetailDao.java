package com.tp.dao.wms;

import java.util.List;

import com.tp.common.dao.BaseDao;
import com.tp.model.wms.StockinImportDetail;

public interface StockinImportDetailDao extends BaseDao<StockinImportDetail> {
	//批量插入导入入库的数据
	Integer batchInsert(List<StockinImportDetail> list);
}
