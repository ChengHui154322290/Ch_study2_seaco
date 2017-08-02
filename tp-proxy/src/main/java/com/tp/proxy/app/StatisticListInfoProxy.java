package com.tp.proxy.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.app.StatisticListInfo;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.app.IStatisticListInfoService;
/**
 * 请求每个列表和详情页面文件时，后台返回结果（页面数据或者错误）时统计，主要用于统计每个页面的瞬间开销，请求成功失败的情况等信息代理层
 * @author szy
 *
 */
@Service
public class StatisticListInfoProxy extends BaseProxy<StatisticListInfo>{

	@Autowired
	private IStatisticListInfoService statisticListInfoService;

	@Override
	public IBaseService<StatisticListInfo> getService() {
		return statisticListInfoService;
	}
}
