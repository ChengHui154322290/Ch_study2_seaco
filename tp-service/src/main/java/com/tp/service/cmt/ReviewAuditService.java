package com.tp.service.cmt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.dao.cmt.ReviewAuditDao;
import com.tp.model.cmt.ReviewAudit;
import com.tp.service.BaseService;
import com.tp.service.cmt.IReviewAuditService;

@Service
public class ReviewAuditService extends BaseService<ReviewAudit> implements IReviewAuditService {

	@Autowired
	private ReviewAuditDao reviewAuditDao;
	
	@Override
	public BaseDao<ReviewAudit> getDao() {
		return reviewAuditDao;
	}

}
