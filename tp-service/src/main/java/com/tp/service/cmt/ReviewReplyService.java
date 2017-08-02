package com.tp.service.cmt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.cmt.ReviewReplyDao;
import com.tp.model.cmt.ReviewReply;
import com.tp.service.BaseService;
import com.tp.service.cmt.IReviewReplyService;

@Service
public class ReviewReplyService extends BaseService<ReviewReply> implements IReviewReplyService {

	@Autowired
	private ReviewReplyDao reviewReplyDao;
	
	@Override
	public BaseDao<ReviewReply> getDao() {
		return reviewReplyDao;
	}

}
