package com.tp.dao.ord;

import java.util.List;

import com.tp.common.dao.BaseDao;
import com.tp.model.ord.FisherDeliveryLog;

public interface FisherDeliveryLogDao extends BaseDao<FisherDeliveryLog> {

	void batchInsert(List<FisherDeliveryLog> fisherDeliveryLogList);

}
