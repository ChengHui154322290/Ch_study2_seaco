package com.tp.dao.ord;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tp.common.dao.BaseDao;
import com.tp.model.ord.OrderPromotion;

public interface OrderPromotionDao extends BaseDao<OrderPromotion> {

	void batchInsert(@Param("orderPromotionList")List<OrderPromotion> orderPromotionList);

}
