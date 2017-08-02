package com.tp.proxy.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.app.StatisticPay;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.app.IStatisticPayService;
/**
 * 代理层
 * @author szy
 *
 */
@Service
public class StatisticPayProxy extends BaseProxy<StatisticPay>{

	@Autowired
	private IStatisticPayService statisticPayService;

	@Override
	public IBaseService<StatisticPay> getService() {
		return statisticPayService;
	}
}
