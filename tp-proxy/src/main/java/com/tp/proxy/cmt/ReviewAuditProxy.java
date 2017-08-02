package com.tp.proxy.cmt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.cmt.ReviewAudit;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.cmt.IReviewAuditService;
/**
 * 评论审核日志表代理层
 * @author szy
 *
 */
@Service
public class ReviewAuditProxy extends BaseProxy<ReviewAudit>{

	@Autowired
	private IReviewAuditService reviewAuditService;

	@Override
	public IBaseService<ReviewAudit> getService() {
		return reviewAuditService;
	}
}
