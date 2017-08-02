package com.tp.proxy.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.app.StatisticTopRecommend;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.app.IStatisticTopRecommendService;
/**
 * 统计顶部推荐用户点击量代理层
 * @author szy
 *
 */
@Service
public class StatisticTopRecommendProxy extends BaseProxy<StatisticTopRecommend>{

	@Autowired
	private IStatisticTopRecommendService statisticTopRecommendService;

	@Override
	public IBaseService<StatisticTopRecommend> getService() {
		return statisticTopRecommendService;
	}
}
