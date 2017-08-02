package com.tp.proxy.mmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.mmp.Goods;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.mmp.IGoodsService;
/**
 * 商品代理层
 * @author szy
 *
 */
@Service
public class GoodsProxy extends BaseProxy<Goods>{

	@Autowired
	private IGoodsService goodsService;

	@Override
	public IBaseService<Goods> getService() {
		return goodsService;
	}
}
