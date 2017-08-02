package com.tp.service.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.app.StatisticCommentDao;
import com.tp.model.app.StatisticComment;
import com.tp.service.BaseService;
import com.tp.service.app.IStatisticCommentService;

@Service
public class StatisticCommentService extends BaseService<StatisticComment> implements IStatisticCommentService {

	@Autowired
	private StatisticCommentDao statisticCommentDao;
	
	@Override
	public BaseDao<StatisticComment> getDao() {
		return statisticCommentDao;
	}

}
