package com.tp.dao.ord;

import java.util.List;

import com.tp.common.dao.BaseDao;
import com.tp.model.ord.RejectItem;

public interface RejectItemDao extends BaseDao<RejectItem> {

	List<RejectItem> queryListByRejectNos(List<Long> rejectNos);

}
