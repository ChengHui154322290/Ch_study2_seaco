package com.tp.proxy.cmt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.cmt.ReviewTags;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.cmt.IReviewTagsService;
/**
 * 评论标签信息代理层
 * @author szy
 *
 */
@Service
public class ReviewTagsProxy extends BaseProxy<ReviewTags>{

	@Autowired
	private IReviewTagsService reviewTagsService;

	@Override
	public IBaseService<ReviewTags> getService() {
		return reviewTagsService;
	}
}
