package com.tp.dao.ord;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tp.common.dao.BaseDao;
import com.tp.model.ord.OrderPoint;

public interface OrderPointDao extends BaseDao<OrderPoint> {

	void batchInsert(@Param("orderPointList") List<OrderPoint> orderPointList);
}
