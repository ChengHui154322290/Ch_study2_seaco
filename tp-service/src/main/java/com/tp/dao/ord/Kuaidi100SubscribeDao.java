package com.tp.dao.ord;

import java.util.List;

import com.tp.common.dao.BaseDao;
import com.tp.model.ord.Kuaidi100Subscribe;

public interface Kuaidi100SubscribeDao extends BaseDao<Kuaidi100Subscribe> {

	Long batchInsert(List<Kuaidi100Subscribe> kuaidi100SubscribeDOList);

}
