package com.tp.proxy.cmt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.cmt.ReviewReply;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.cmt.IReviewReplyService;
/**
 * 评论回复信息表代理层
 * @author szy
 *
 */
@Service
public class ReviewReplyProxy extends BaseProxy<ReviewReply>{

	@Autowired
	private IReviewReplyService reviewReplyService;

	@Override
	public IBaseService<ReviewReply> getService() {
		return reviewReplyService;
	}
}
