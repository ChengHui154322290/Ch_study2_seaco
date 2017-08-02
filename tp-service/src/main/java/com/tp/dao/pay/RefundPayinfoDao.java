package com.tp.dao.pay;

import com.tp.common.dao.BaseDao;
import com.tp.model.pay.RefundPayinfo;

public interface RefundPayinfoDao extends BaseDao<RefundPayinfo> {

	int updateDynamicByUnrefunded(RefundPayinfo refundPayinfo);
}
