package com.tp.proxy.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.app.StatisticShare;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.app.IStatisticShareService;
/**
 * 统计分享的各项数据。 代理层
 * @author szy
 *
 */
@Service
public class StatisticShareProxy extends BaseProxy<StatisticShare>{

	@Autowired
	private IStatisticShareService statisticShareService;

	@Override
	public IBaseService<StatisticShare> getService() {
		return statisticShareService;
	}
}
