package com.tp.dao.wms;

import com.tp.common.dao.BaseDao;
import com.tp.model.wms.StockasnDetail;

import java.util.List;

public interface StockasnDetailDao extends BaseDao<StockasnDetail> {

    Integer batchInsert(List<StockasnDetail> list);


}
