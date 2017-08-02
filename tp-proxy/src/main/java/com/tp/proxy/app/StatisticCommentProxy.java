package com.tp.proxy.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.app.StatisticComment;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.app.IStatisticCommentService;
/**
 * 代理层
 * @author szy
 *
 */
@Service
public class StatisticCommentProxy extends BaseProxy<StatisticComment>{

	@Autowired
	private IStatisticCommentService statisticCommentService;

	@Override
	public IBaseService<StatisticComment> getService() {
		return statisticCommentService;
	}
}
