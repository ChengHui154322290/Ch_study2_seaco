package com.tp.service.cmt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.cmt.ReviewImgDao;
import com.tp.model.cmt.ReviewImg;
import com.tp.service.BaseService;
import com.tp.service.cmt.IReviewImgService;

@Service
public class ReviewImgService extends BaseService<ReviewImg> implements IReviewImgService {

	@Autowired
	private ReviewImgDao reviewImgDao;
	
	@Override
	public BaseDao<ReviewImg> getDao() {
		return reviewImgDao;
	}

}
