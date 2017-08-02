package com.tp.service.cmt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.cmt.ReviewTagsDao;
import com.tp.model.cmt.ReviewTags;
import com.tp.service.BaseService;
import com.tp.service.cmt.IReviewTagsService;

@Service
public class ReviewTagsService extends BaseService<ReviewTags> implements IReviewTagsService {

	@Autowired
	private ReviewTagsDao reviewTagsDao;
	
	@Override
	public BaseDao<ReviewTags> getDao() {
		return reviewTagsDao;
	}

}
