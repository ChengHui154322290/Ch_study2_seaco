package com.tp.proxy.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.app.StatisticUser;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.app.IStatisticUserService;
/**
 * 在会员登录时投递，主要统计登录的会员数和登录结果代理层
 * @author szy
 *
 */
@Service
public class StatisticUserProxy extends BaseProxy<StatisticUser>{

	@Autowired
	private IStatisticUserService statisticUserService;

	@Override
	public IBaseService<StatisticUser> getService() {
		return statisticUserService;
	}
}
