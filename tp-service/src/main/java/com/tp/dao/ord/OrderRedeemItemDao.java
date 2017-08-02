package com.tp.dao.ord;

import java.util.List;

import com.tp.common.dao.BaseDao;
import com.tp.model.ord.OrderRedeemItem;
import com.tp.query.ord.RedeemItemQuery;
import com.tp.result.ord.OrderRedeemItemStatistics;

public interface OrderRedeemItemDao extends BaseDao<OrderRedeemItem> {
	
	public List<OrderRedeemItem>  queryOverdueOrderRedeemItem();

	OrderRedeemItemStatistics queryStatisticsByQuery(RedeemItemQuery redeemItemQuery);
}
