package com.tp.service.cmt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.cmt.ReviewTagsStatisDao;
import com.tp.model.cmt.ReviewTagsStatis;
import com.tp.service.BaseService;
import com.tp.service.cmt.IReviewTagsStatisService;

@Service
public class ReviewTagsStatisService extends BaseService<ReviewTagsStatis> implements IReviewTagsStatisService {

	@Autowired
	private ReviewTagsStatisDao reviewTagsStatisDao;
	
	@Override
	public BaseDao<ReviewTagsStatis> getDao() {
		return reviewTagsStatisDao;
	}

}
