package com.tp.proxy.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.app.StatisticPublic;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.app.IStatisticPublicService;
/**
 * 统计相关 公共参数表代理层
 * @author szy
 *
 */
@Service
public class StatisticPublicProxy extends BaseProxy<StatisticPublic>{

	@Autowired
	private IStatisticPublicService statisticPublicService;

	@Override
	public IBaseService<StatisticPublic> getService() {
		return statisticPublicService;
	}
}
