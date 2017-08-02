package com.tp.proxy.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.app.StatisticPoster;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.app.IStatisticPosterService;
/**
 * APP启动展示启动海报后投递，用于统计启动 和 保证展现量数据代理层
 * @author szy
 *
 */
@Service
public class StatisticPosterProxy extends BaseProxy<StatisticPoster>{

	@Autowired
	private IStatisticPosterService statisticPosterService;

	@Override
	public IBaseService<StatisticPoster> getService() {
		return statisticPosterService;
	}
}
