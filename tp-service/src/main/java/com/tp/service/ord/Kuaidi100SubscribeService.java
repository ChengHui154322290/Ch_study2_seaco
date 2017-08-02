package com.tp.service.ord;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.ord.Kuaidi100SubscribeDao;
import com.tp.model.ord.Kuaidi100Subscribe;
import com.tp.service.BaseService;
import com.tp.service.ord.IKuaidi100SubscribeService;

@Service
public class Kuaidi100SubscribeService extends BaseService<Kuaidi100Subscribe> implements IKuaidi100SubscribeService {

	@Autowired
	private Kuaidi100SubscribeDao kuaidi100SubscribeDao;
	
	@Override
	public BaseDao<Kuaidi100Subscribe> getDao() {
		return kuaidi100SubscribeDao;
	}

	@Override
	public void batchInsert(List<Kuaidi100Subscribe> kuaidi100SubscribeDOList) {
		kuaidi100SubscribeDao.batchInsert(kuaidi100SubscribeDOList);
	}

}
