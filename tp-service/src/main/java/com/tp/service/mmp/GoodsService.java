package com.tp.service.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.mmp.GoodsDao;
import com.tp.model.mmp.Goods;
import com.tp.service.BaseService;
import com.tp.service.mmp.IGoodsService;

@Service
public class GoodsService extends BaseService<Goods> implements IGoodsService {

	@Autowired
	private GoodsDao goodsDao;
	
	@Override
	public BaseDao<Goods> getDao() {
		return goodsDao;
	}

}
