package com.tp.dao.wms;

import com.tp.common.dao.BaseDao;
import com.tp.model.wms.StockasnDetailFact;

import java.util.List;

public interface StockasnDetailFactDao extends BaseDao<StockasnDetailFact> {

    Integer batchInsert(List<StockasnDetailFact> list);

}
