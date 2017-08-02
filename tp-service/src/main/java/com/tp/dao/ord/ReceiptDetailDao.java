package com.tp.dao.ord;

import com.tp.common.dao.BaseDao;
import com.tp.model.ord.ReceiptDetail;

public interface ReceiptDetailDao extends BaseDao<ReceiptDetail> {

	int updateBySubOrderCode(ReceiptDetail receipt);

}
