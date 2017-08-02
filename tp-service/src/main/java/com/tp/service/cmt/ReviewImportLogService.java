package com.tp.service.cmt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.cmt.ReviewImportLogDao;
import com.tp.model.cmt.ReviewImportLog;
import com.tp.service.BaseService;
import com.tp.service.cmt.IReviewImportLogService;

@Service
public class ReviewImportLogService extends BaseService<ReviewImportLog> implements IReviewImportLogService {

	@Autowired
	private ReviewImportLogDao reviewImportLogDao;
	
	@Override
	public BaseDao<ReviewImportLog> getDao() {
		return reviewImportLogDao;
	}

}
