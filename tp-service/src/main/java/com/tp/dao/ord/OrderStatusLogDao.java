package com.tp.dao.ord;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tp.common.dao.BaseDao;
import com.tp.model.ord.OrderStatusLog;

public interface OrderStatusLogDao extends BaseDao<OrderStatusLog> {

	Integer batchInsert(@Param("orderStatusLogList")List<OrderStatusLog> orderStatusLogList);

}
