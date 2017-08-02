package com.tp.service.cmt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.cmt.ReviewImportListDao;
import com.tp.model.cmt.ReviewImportList;
import com.tp.service.BaseService;
import com.tp.service.cmt.IReviewImportListService;

@Service
public class ReviewImportListService extends BaseService<ReviewImportList> implements IReviewImportListService {

	@Autowired
	private ReviewImportListDao reviewImportListDao;
	
	@Override
	public BaseDao<ReviewImportList> getDao() {
		return reviewImportListDao;
	}

}
